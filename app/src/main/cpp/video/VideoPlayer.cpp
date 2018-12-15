//
// Created by ether on 2018/12/12.
//



#include "VideoPlayer.h"

VideoPlayer::VideoPlayer(PlayerCallJava *playerCallJava, PlayerStatus *playerStatus,
                         const char *vertexCode, const char *fragCode, ANativeWindow *window, int w,
                         int h) {
    this->playerStatus = playerStatus;
    this->playerCallJava = playerCallJava;
    this->w = w;
    this->h = h;
    this->window = window;
    this->vertexCode = vertexCode;
    this->fragCode = fragCode;
    blockQueue = new BlockQueue(playerStatus);
    audioPlayer = null;
    eglUtil = null;
    glUtil = null;
    delayTime = 0;
    pthread_mutex_init(&codecMutex, null);
    codecType = CODEC_SOFTWARE;
}

VideoPlayer::~VideoPlayer() {
    pthread_mutex_destroy(&codecMutex);
}

void *videoStart(void *data) {
    VideoPlayer *videoPlayer = static_cast<VideoPlayer *>(data);
    if (videoPlayer->eglUtil == null) {
        videoPlayer->eglUtil = new EGLUtil();
    }
    if (videoPlayer->glUtil == null) {
        videoPlayer->glUtil = new GLUtil();
    }
    videoPlayer->eglUtil->eglCreate(videoPlayer->window);
    eglMakeCurrent(videoPlayer->eglUtil->eglDisplay, videoPlayer->eglUtil->eglSurface,
                   videoPlayer->eglUtil->eglSurface,
                   videoPlayer->eglUtil->eglContext);
    videoPlayer->initVertex();
    float changeH =
            (float) videoPlayer->pCodecCtx->height / (float) videoPlayer->pCodecCtx->width *
            (float) videoPlayer->w /
            (float) videoPlayer->h;
    for (int i = 0; i < 12; ++i) {
        videoPlayer->vertexArray[++i] *= changeH;
    }
    videoPlayer->initLocation();
    glViewport(0, 0, videoPlayer->w, videoPlayer->h);
    videoPlayer->glUtil->createProgram(videoPlayer->vertexCode, videoPlayer->fragCode);
    videoPlayer->glUtil->createTexture();

    while (videoPlayer->playerStatus != null && !videoPlayer->playerStatus->isExit) {
        if (videoPlayer->playerStatus->isSeek) {
            av_usleep(1000 * 100);
            continue;
        }
        if (videoPlayer->playerStatus->isPause) {
            av_usleep(1000 * 100);
            continue;
        }
        if (videoPlayer->blockQueue->getQueueSize() == 0) {
            if (!videoPlayer->playerStatus->isLoad) {
                videoPlayer->playerStatus->isLoad = true;
                videoPlayer->playerCallJava->onCallLoad(CHILD_THREAD, true);
            }
            av_usleep(1000 * 100);
            continue;
        } else {
            if (videoPlayer->playerStatus->isLoad) {
                videoPlayer->playerStatus->isLoad = false;
                videoPlayer->playerCallJava->onCallLoad(CHILD_THREAD, false);
            }
        }

        AVPacket *packet = av_packet_alloc();
        if (videoPlayer->blockQueue->getAvpacket(packet) != 0) {
            av_packet_free(&packet);
            av_free(packet);
            packet = null;
            continue;
        }
        LOGE("获取视频Packet成功");


        if (videoPlayer->codecType == CODEC_HARDWARE) {

            if (av_bsf_send_packet(videoPlayer->pBsfContext, packet) != 0) {
                av_packet_free(&packet);
                av_free(packet);
                packet = null;
                continue;
            }
            while (av_bsf_receive_packet(videoPlayer->pBsfContext, packet) == 0) {
                LOGE("使用硬解码");

                videoPlayer->playerCallJava->onCallDecode(packet->size, packet->data);
                double diff = videoPlayer->getFrameDiffTime(null, packet);
                av_usleep(videoPlayer->getDelayTime(diff) * 1000000);
                av_packet_free(&packet);
                av_free(packet);
                continue;
            }
            packet = null;

        } else {
            pthread_mutex_lock(&videoPlayer->codecMutex);
            if (avcodec_send_packet(videoPlayer->pCodecCtx, packet) != 0) {
                av_packet_free(&packet);
                av_free(packet);
                packet = null;
                pthread_mutex_unlock(&videoPlayer->codecMutex);
                continue;
            }
            LOGE("使用软解码");

            AVFrame *frame = av_frame_alloc();
            if (avcodec_receive_frame(videoPlayer->pCodecCtx, frame) != 0) {

                av_frame_free(&frame);
                av_free(frame);
                av_packet_free(&packet);
                av_free(packet);
                packet = null;
                frame = null;
                pthread_mutex_unlock(&videoPlayer->codecMutex);
                continue;
            }


            if (frame->format == AV_PIX_FMT_YUV420P) {
                double diff = videoPlayer->getFrameDiffTime(frame, null);
                av_usleep(videoPlayer->getDelayTime(diff) * 1000000);
                LOGE("获取frame成功,diff=%f", videoPlayer->getDelayTime(diff * 1000000));
                videoPlayer->initPlay(frame);
            } else {
                AVFrame *pFrame420P = av_frame_alloc();
                int num = av_image_get_buffer_size(AV_PIX_FMT_YUV420P,
                                                   videoPlayer->pCodecCtx->width,
                                                   videoPlayer->pCodecCtx->height, 1);
                uint8_t *buffer = static_cast<uint8_t *>(av_malloc(num * sizeof(uint8_t)));

                av_image_fill_arrays(pFrame420P->data,
                                     pFrame420P->linesize,
                                     buffer,
                                     AV_PIX_FMT_YUV420P,
                                     videoPlayer->pCodecCtx->width,
                                     videoPlayer->pCodecCtx->height,
                                     1);
                SwsContext *swsContext = sws_getContext(videoPlayer->pCodecCtx->width,
                                                        videoPlayer->pCodecCtx->height,
                                                        videoPlayer->pCodecCtx->pix_fmt,
                                                        videoPlayer->pCodecCtx->width,
                                                        videoPlayer->pCodecCtx->height,
                                                        AV_PIX_FMT_YUV420P,
                                                        SWS_BICUBIC, null, null, null
                );
                if (!swsContext) {
                    av_frame_free(&pFrame420P);
                    av_free(pFrame420P);
                    av_free(buffer);
                    pFrame420P = null;
                    pthread_mutex_unlock(&videoPlayer->codecMutex);
                    continue;
                }
                sws_scale(swsContext,
                          frame->data,
                          frame->linesize,
                          0,
                          frame->height,
                          pFrame420P->data,
                          pFrame420P->linesize);
                double diff = videoPlayer->getFrameDiffTime(pFrame420P, null);

                av_usleep(videoPlayer->getDelayTime(diff) * 1000000);
                LOGE("获取frame成功,diff=%f", videoPlayer->getDelayTime(diff * 1000000));
                videoPlayer->initPlay(pFrame420P);
                av_frame_free(&pFrame420P);
                av_free(pFrame420P);
                av_free(buffer);
                pFrame420P = null;
                sws_freeContext(swsContext);
            }

            av_frame_free(&frame);
            av_free(frame);
            av_packet_free(&packet);
            av_free(packet);
            packet = null;
            frame = null;
            pthread_mutex_unlock(&videoPlayer->codecMutex);
        }
    }
    pthread_exit(&videoPlayer->startThread);
}

void VideoPlayer::play() {
    pthread_create(&startThread, null, videoStart, this);
}

void VideoPlayer::release() {
    if (eglUtil != null) {
        eglUtil->eglRelease();\
        delete eglUtil;
        eglUtil = null;
    }
    if (glUtil != null) {
        glUtil->destroy();
        delete glUtil;
        glUtil = null;
    }
    if (blockQueue != null) {
        delete blockQueue;
        blockQueue = null;
    }
    if (pBsfContext != null) {
        av_bsf_free(&pBsfContext);
        pBsfContext = null;
    }
    if (pCodecCtx != null) {
        pthread_mutex_lock(&codecMutex);
        avcodec_close(pCodecCtx);
        avcodec_free_context(&pCodecCtx);
        pCodecCtx = null;
        pthread_mutex_unlock(&codecMutex);
    }
    if (playerCallJava != null) {
        playerCallJava = null;
    }
    if (playerStatus != null) {
        playerStatus = null;
    }
}

void VideoPlayer::initPlay(AVFrame *frame) {

    draw(frame);
}

void VideoPlayer::initVertex() {
    //@//    @formatter:on
    vertexArray = new GLfloat[12]{
            1.0f, 1.0f,
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f,
    };
    textureArray = new GLfloat[12]{
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };
    //    @formatter:on
}

void VideoPlayer::initLocation() {
    if (glUtil == null) {
        LOGE("glutil为空");
        return;
    }
    glUtil->createProgram(vertexCode, fragCode);
    textureArr = glUtil->createTexture();
//    LOGE("%d", textureArr[0]);
    aPosition = glGetAttribLocation(glUtil->program, "a_Position");
//    aColor = glGetAttribLocation(glUtil->program, "a_Color");
    aTextureCoordinates = glGetAttribLocation(glUtil->program, "a_TextureCoordinates");
    uTexY = glGetUniformLocation(glUtil->program, "u_TexY");
    uTexU = glGetUniformLocation(glUtil->program, "u_TexU");
    uTexV = glGetUniformLocation(glUtil->program, "u_TexV");
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
}

void VideoPlayer::draw(AVFrame *pFrame) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glUseProgram(glUtil->program);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureArr[0]);
    glTexImage2D(GL_TEXTURE_2D,
                 0,
                 GL_LUMINANCE,
                 pFrame->width,
                 pFrame->height,
                 0,
                 GL_LUMINANCE,
                 GL_UNSIGNED_BYTE,
                 pFrame->data[0]);
    glUniform1i(uTexY, 0);
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, textureArr[1]);
    glTexImage2D(GL_TEXTURE_2D,
                 0,
                 GL_LUMINANCE,
                 pFrame->width / 2,
                 pFrame->height / 2,
                 0,
                 GL_LUMINANCE,
                 GL_UNSIGNED_BYTE,
                 pFrame->data[1]);
    glUniform1i(uTexU, 1);
    glActiveTexture(GL_TEXTURE2);
    glBindTexture(GL_TEXTURE_2D, textureArr[2]);
    glTexImage2D(GL_TEXTURE_2D,
                 0,
                 GL_LUMINANCE,
                 pFrame->width / 2,
                 pFrame->height / 2, 0,
                 GL_LUMINANCE,
                 GL_UNSIGNED_BYTE,
                 pFrame->data[2]);
    glUniform1i(uTexV, 2);
    glVertexAttribPointer(aPosition, 2, GL_FLOAT, GL_FALSE, 0, vertexArray);
    glEnableVertexAttribArray(aPosition);
    glVertexAttribPointer(aTextureCoordinates, 2, GL_FLOAT, GL_FALSE, 0, textureArray);
    glEnableVertexAttribArray(aTextureCoordinates);
    glDrawArrays(GL_TRIANGLES, 0, 6);
    eglSwapBuffers(eglUtil->eglDisplay, eglUtil->eglSurface);
}

double VideoPlayer::getFrameDiffTime(AVFrame *frame, AVPacket *packet) {
    double pts = 0;
    if (frame != null) {
        pts = av_frame_get_best_effort_timestamp(frame);
    } else if (packet != null) {
        pts = packet->pts;
    }
    if (pts == AV_NOPTS_VALUE) {
        pts = 0;
    }
    pts *= av_q2d(time_base);
    if (pts > 0) {
        clock = pts;
    }

    double diff = audioPlayer->clock - clock;


    return diff;
}

double VideoPlayer::getDelayTime(double diff) {

    if (diff > 0.003) {
        delayTime = delayTime * 2 / 3;
        if (delayTime < defaultDelayTime / 2) {
            delayTime = defaultDelayTime * 2 / 3;
        } else if (delayTime > defaultDelayTime * 2) {
            delayTime = defaultDelayTime * 2;
        }
    } else if (diff < -0.003) {
        delayTime = delayTime * 3 / 2;
        if (delayTime < defaultDelayTime / 2) {
            delayTime = defaultDelayTime * 2 / 3;
        } else if (delayTime > defaultDelayTime * 2) {
            delayTime = defaultDelayTime * 2;
        }
    } else if (diff == 0.003) {

    }

    if (diff >= 0.5) {
        delayTime = 0;
    } else if (diff <= -0.5) {
        delayTime = defaultDelayTime * 2;
    }

    if (fabs(diff) >= 10) {
        delayTime = defaultDelayTime;
    }

    return delayTime;
}
