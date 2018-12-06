//
// Created by ether on 2018/10/23.
//

#include "Decode.h"

#define null NULL
#define LOG_TAG "decode"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define MAX_AUDIO_FRAME_SIZE 44100*4
struct audioParams_ {
    AudioPlayer *audioPlayer;
    int index;
    AVCodecContext *codecContext;
    AVFormatContext *formatContext;
    int SUCCESS_CODE;
};
pthread_t audioThread;

void *start(void *params) {
    int ret;
    int i = 0;
    AVPacket *pPacket = static_cast<AVPacket *>(av_malloc(sizeof(AVPacket)));
    audioParams_ *params1 = static_cast<audioParams_ *>(params);
    LOGE("index为%d", params1->SUCCESS_CODE);
    while (av_read_frame(params1->formatContext, pPacket) >= 0) {
        if (pPacket->stream_index == params1->index) {
            ret = avcodec_send_packet(params1->codecContext, pPacket);
            while (ret >= 0) {
                AVFrame *pFrame = av_frame_alloc();
                ret = avcodec_receive_frame(params1->codecContext, pFrame);
                if (ret == AVERROR(EAGAIN)) {
                    LOGE("%s", "读取解码数据失败");
                    av_frame_free(&pFrame);
                    break;
                } else if (ret == AVERROR_EOF) {
                    LOGE("%s", "解码完成");
//                    fclose(outFile);
                    av_frame_free(&pFrame);
                    return null;
                } else if (ret < 0) {
                    LOGE("%s", "解码出错");
                    av_frame_free(&pFrame);
                    break;
                }
                params1->audioPlayer->pushData(pFrame,
                                               params1->codecContext->channels,
                                               params1->codecContext->frame_size,
                                               params1->codecContext->sample_fmt);
                av_frame_free(&pFrame);
                usleep(3000);
            }
        }
    }
    pthread_exit(&audioThread);
}

void Decode::decode(const char *path, DECODE_TYPE decode_type, PlayerView *playerView,
                    AudioPlayer *audioPlayer) {

    av_register_all();
    pFmtCtx = avformat_alloc_context();
    if (avformat_open_input(&pFmtCtx, path, null, null) != 0) {
        LOGE("打开文件失败");
        return;
    }
    if (avformat_find_stream_info(pFmtCtx, null) < 0) {
        LOGE("获取文件信息失败");
        return;
    }
    index = -1;
    for (int i = 0; i < pFmtCtx->nb_streams; ++i) {
        switch (decode_type) {
            case DECODE_AUDIO:
                findIndex(DECODE_AUDIO);
                break;
            case DECODE_VIDEO:
                findIndex(DECODE_VIDEO);
                break;
            case DECODE_UNKNOWN:
                LOGE("未知类型");
                return;
            default:
                LOGE("暂不支持其他类型数据");
                return;
        }
    }
    if (index == -1) {
        LOGE("未找到对应的流信息");
        return;
    }
    pStream = pFmtCtx->streams[index];
    pCodec = avcodec_find_decoder(pStream->codecpar->codec_id);
    if (pCodec == null) {
        LOGE("未找到对应的解码器");
        return;
    }
    pCodecCtx = avcodec_alloc_context3(pCodec);
    avcodec_parameters_to_context(pCodecCtx, pStream->codecpar);
    if (avcodec_open2(pCodecCtx, pCodec, null) < 0) {
        LOGE("无法打开解码器");
        return;
    }


    if (decode_type == DECODE_VIDEO) {
        video(playerView);
    } else {
        audio(audioPlayer);
    }
}

void Decode::findIndex(DECODE_TYPE type) {
    AVMediaType mediaType;
    if (type == DECODE_VIDEO) {
        mediaType = AVMEDIA_TYPE_VIDEO;
    } else {
        mediaType = AVMEDIA_TYPE_AUDIO;
    }
    for (int i = 0; i < pFmtCtx->nb_streams; ++i) {
        if (pFmtCtx->streams[i]->codecpar->codec_type == mediaType) {
            index = i;
        }
    }
}

void Decode::audio(AudioPlayer *pPlayer) {
//    FILE *file = fopen(path, "wb+");

    enum AVSampleFormat inSampleFmt = pCodecCtx->sample_fmt;

    int inSampleRate = pCodecCtx->sample_rate;

    uint64_t inSampleChannel = pCodecCtx->channel_layout;


    pPlayer->initSwrCtx(inSampleFmt, inSampleRate, inSampleChannel);
    pPlayer->initQueue();
    audioParams_ *audioParams = new audioParams_;
    audioParams->audioPlayer = pPlayer;
    audioParams->index = index;
    LOGE("INDEX为%d", index);
    audioParams->SUCCESS_CODE = 1972;
    audioParams->formatContext = pFmtCtx;
    audioParams->codecContext = pCodecCtx;
//                int outBufferSize = av_samples_get_buffer_size(pFrame->linesize,
//                                                               pCodecCtx->channels,
//                                                               pCodecCtx->frame_size,
//                                                               pCodecCtx->sample_fmt,
//                                                               1);
//                int rst = swr_convert(swrContext,
//                                      &outBuffer,
//                                      outBufferSize,
//                                      const_cast<const uint8_t **>(reinterpret_cast<uint8_t **>(pFrame->data)),
//                                      pFrame->nb_samples);
    //todo 将解码后数据填充进queue中
//                fwrite(outBuffer, 1, outBufferSize, file);
    pthread_create(&audioThread, null, start, (void *) audioParams);
}


void Decode::video(PlayerView *pView) {
//    pView->initQueue();
//    int df = 0;
//    uint8_t *outputBuffer = static_cast<uint8_t *>(av_malloc(
//            av_image_get_buffer_size(AV_PIX_FMT_YUV420P, pCodecCtx->width, pCodecCtx->height, 1)));
//    av_image_fill_arrays(pFrame->data, pFrame->linesize, outputBuffer, AV_PIX_FMT_YUV420P,
//                         pCodecCtx->width, pCodecCtx->height, 1);
//    struct SwsContext *swsCtx = sws_getContext(
//            pCodecCtx->width,
//            pCodecCtx->height,
//            pCodecCtx->pix_fmt,
//            pCodecCtx->width,
//            pCodecCtx->height,
//            AV_PIX_FMT_YUV420P,
//            SWS_BICUBIC,
//            null,
//            null,
//            null
//    );
//    int ret;
//    while (av_read_frame(pFmtCtx, pPacket) >= 0) {
//        if (pPacket->stream_index == index) {
//            ret = avcodec_send_packet(pCodecCtx, pPacket);
//            while (ret >= 0) {
//                ret = avcodec_receive_frame(pCodecCtx, pFrame);
//                if (ret == AVERROR(EAGAIN)) {
//                    LOGE("%s", "读取解码数据失败");
//                    break;
//                } else if (ret == AVERROR_EOF) {
//                    LOGE("%s", "解码完成");
//                    return;
//                } else if (ret < 0) {
//                    LOGE("%s", "解码出错");
//                    break;
//                }
//                sws_scale(swsCtx,
//                          pFrame->data,
//                          pFrame->linesize,
//                          0,
//                          pCodecCtx->height,
//                          pFrame->data,
//                          pFrame->linesize);
//                df++;
//                LOGE("解码了%d帧", df);
//                pFrame->width = pCodecCtx->width;
//                pFrame->height = pCodecCtx->height;
//                pView->pushData(pFrame);
//                usleep(46000);
//            }
//        }
//    }
}

void Decode::destroy() {
    avformat_close_input(&pFmtCtx);
    avcodec_free_context(&pCodecCtx);
    avformat_free_context(pFmtCtx);
}




