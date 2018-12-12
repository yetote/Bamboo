//
// Created by ether on 2018/12/7.
//


#include "Decode.h"
#include <unistd.h>


Decode::Decode(PlayerStatus *playStatus, PlayerCallJava *callJava, const char *url,
               const char *vertexCode, const char *fragCode, ANativeWindow *window, int w, int h) {
    this->callJava = callJava;
    this->url = url;
    this->playstatus = playStatus;
    this->window = window;
    this->vertexCode = vertexCode;
    this->fragCode = fragCode;
    this->w = w;
    this->h = h;
    pthread_mutex_init(&initMutex, null);
    pthread_mutex_init(&seekMutex, null);
}

Decode::~Decode() {
    pthread_mutex_destroy(&initMutex);
    pthread_mutex_destroy(&seekMutex);
}

void *decode(void *data) {
    Decode *fFmpegDecode = static_cast<Decode *>(data);

    fFmpegDecode->ffmpegDecodeThread();

    pthread_exit(&fFmpegDecode->decodeThread);
}

void Decode::prepared() {
    pthread_create(&decodeThread, null, decode, this);
}

int avformat_callback(void *ctx) {
    Decode *fFmpegDecode = static_cast<Decode *>(ctx);
    if (fFmpegDecode->playstatus->isExit) {
        return AVERROR_EOF;
    }
    return 0;
}

void Decode::ffmpegDecodeThread() {
    pthread_mutex_lock(&initMutex);
    av_register_all();
    avformat_network_init();

    pFmtCtx = avformat_alloc_context();
    pFmtCtx->interrupt_callback.callback = avformat_callback;
    pFmtCtx->interrupt_callback.opaque = this;
    if (avformat_open_input(&pFmtCtx, url, null, null) != 0) {
        LOGE("打开文件失败");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return;
    }
    if (avformat_find_stream_info(pFmtCtx, null) < 0) {
        LOGE("获取文件信息失败");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return;
    }
    for (int i = 0; i < pFmtCtx->nb_streams; ++i) {
        if (pFmtCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            if (audio == null) {
                audio = new AudioPlayer(playstatus, pFmtCtx->streams[i]->codecpar->sample_rate,
                                        callJava);
            }
            audio->streamIndex = i;
            audio->codecParameters = pFmtCtx->streams[i]->codecpar;
            audio->duration = pFmtCtx->duration / AV_TIME_BASE;
            audio->time_base = pFmtCtx->streams[i]->time_base;
            duration = audio->duration;

        } else if (pFmtCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            if (video == null) {
                video = new VideoPlayer(callJava, playstatus, vertexCode, fragCode, window, w, h);
            }
            video->videoIndex = i;
            video->parameters = pFmtCtx->streams[i]->codecpar;
            video->time_base = pFmtCtx->streams[i]->time_base;
        }
    }
    if (audio->streamIndex == -1) {
        LOGE("未找到对应的流信息");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return;
    }

    if (audio != null) {
        int ret = getCodecId(audio->codecParameters, &audio->pCodecCtx);
        if (ret == -1) {
            LOGE("寻找音频解码器失败");
            return;
        }
    }
    if (video != null) {
        int ret = getCodecId(video->parameters, &video->pCodecCtx);
        if (ret == -1) {
            LOGE("寻找视频解码器失败");
            return;
        }
    }

    callJava->onCallPrepared(CHILD_THREAD);
    pthread_mutex_unlock(&initMutex);
}

void Decode::start() {
    if (audio == null) {
        LOGE("audio is null");
        return;
    }
    if (video == null) {
        LOGE("video is null");
        return;
    }
    audio->play();
    video->play();
    while (playstatus != NULL && !playstatus->isExit) {

        if (playstatus->isSeek) {
            av_usleep(1000 * 100);
            continue;
        }

        if (audio->blockQueue->getQueueSize() > 40) {
            av_usleep(1000 * 100);
            continue;
        }

        AVPacket *avPacket = av_packet_alloc();
        pthread_mutex_lock(&seekMutex);
        int ret = av_read_frame(pFmtCtx, avPacket);
        pthread_mutex_unlock(&seekMutex);
        if (ret == 0) {
            isReadFrameFinish = false;
            if (avPacket->stream_index == audio->streamIndex) {
                audio->blockQueue->putAvpacket(avPacket);
            } else if (avPacket->stream_index == video->videoIndex) {
                video->blockQueue->putAvpacket(avPacket);
            } else {
                av_packet_free(&avPacket);
                av_free(avPacket);
            }
        } else {
            isReadFrameFinish = true;
            av_packet_free(&avPacket);
            av_free(avPacket);
            while (playstatus != null && !playstatus->isExit) {
                if (audio->blockQueue->getQueueSize() > 0) {
                    av_usleep(1000 * 100);
                    continue;
                } else {
                    playstatus->isExit = true;
                    break;
                }
            }
        }
    }
    if (callJava != null) {
        callJava->onCallComplete(CHILD_THREAD);
    }
    isExit = true;
    LOGE("解码完成");


}

void Decode::pause() {
    if (audio != null) {
        audio->pause();
    }
}

void Decode::resume() {
    if (audio != null) {
        audio->resume();
    }
}

void Decode::release() {
    playstatus->isExit = true;
    pthread_mutex_lock(&initMutex);
    int sleepCount = 0;
    while (!isExit) {
        if (sleepCount > 1000) {
            isExit = true;
        }
        LOGE("ffmpeg准备退出%d", sleepCount);
        sleepCount++;
        av_usleep(1000 * 10);
    }
    if (audio != null) {
        audio->release();
        delete audio;
        audio = null;
    }
    if (video != null) {
        video->release();
        delete video;
        video = null;
    }
    if (pFmtCtx != null) {
        avformat_close_input(&pFmtCtx);
        avformat_free_context(pFmtCtx);
        pFmtCtx = null;
    }
    if (playstatus != null) {
        playstatus = null;
    }
    if (callJava != null) {
        callJava = null;
    }
    pthread_mutex_unlock(&initMutex);
}

void Decode::seek(int64_t secs) {
    if (duration <= 0) {
        return;
    }
    if (secs >= 0 && secs <= duration) {
        if (audio != null) {
            playstatus->isSeek = true;
            audio->blockQueue->clearPacket();
            audio->clock = 0;
            audio->last_time = 0;
            pthread_mutex_lock(&seekMutex);
            int64_t rel = secs * AV_TIME_BASE;
            avformat_seek_file(pFmtCtx, -1, INT64_MIN, rel, INT64_MAX, 0);
            pthread_mutex_unlock(&seekMutex);
            playstatus->isSeek = false;
        }
    }
}

void Decode::setVolume(int percent) {
    if (audio != null) {
        audio->setVolume(percent);
    }
}

int Decode::getCodecId(AVCodecParameters *parameters, AVCodecContext **codecContext) {
    AVCodec *pCodec = avcodec_find_decoder(parameters->codec_id);
    if (pCodec == null) {
        LOGE("未找到对应的解码器");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return -1;
    }
    *codecContext = avcodec_alloc_context3(pCodec);
    if (avcodec_parameters_to_context(*codecContext, parameters) < 0) {
        LOGE("赋值解码器上下文失败");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return -1;
    }
    if (avcodec_open2(*codecContext, pCodec, null) != 0) {
        LOGE("无法打开解码器");
        isExit = true;
        pthread_mutex_unlock(&initMutex);
        return -1;
    }
    return 0;
}
