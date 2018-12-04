//
// Created by ether on 2018/11/1.
//


#include <cstdlib>
#include "AudioPlayer.h"
#include <unistd.h>

#define null NULL
#define success SL_RESULT_SUCCESS
#define sl_true SL_BOOLEAN_TRUE
#define false SL_BOOLEAN_FALSE
#define LOG_TAG "audioPlayer"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
uint8_t *outBuffer;
AVFrame *pFrame;
SwrContext *swrContext;
BlockQueue<AVFrame *> blockQueue;
int frameSize;
int channels;
enum AVSampleFormat sampleFormat;
int outChannelNum;
#define MAX_AUDIO_FRAME_SIZE 44100*2

void playerCallBack(SLAndroidSimpleBufferQueueItf bf, void *context) {
    AudioPlayer *audioPlayer = static_cast<AudioPlayer *>(context);
    int size = audioPlayer->getData(outBuffer, "");
    LOGE("bufferSize的大小%d", size);
    if (outBuffer != null && size > 0) {
        SLresult result = (*bf)->Enqueue(bf, outBuffer, size);
    } else {
        playerCallBack(bf, context);
    }
}

void AudioPlayer::prepare() {
    SLresult result;
    result = slCreateEngine(&objectItf, 0, null, 0, null, null);
    if (result != success) {
        LOGE("创建引擎失败");
        return;
    }
    result = (*objectItf)->Realize(objectItf, false);
    if (result != success) {
        LOGE("引擎对象实现失败");
        return;
    }
    result = (*objectItf)->GetInterface(objectItf, SL_IID_ENGINE, &engineItf);
    if (result != success) {
        LOGE("获取引擎接口失败");
        return;
    }

    const SLInterfaceID ids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean req[1] = {false};

    result = (*engineItf)->CreateOutputMix(engineItf, &outMaxObjItf, 1, ids, req);
    if (result != success) {
        LOGE("创建混音器失败");
        return;
    }
    result = (*outMaxObjItf)->Realize(outMaxObjItf, false);
    if (result != success) {
        LOGE("混音器实现失败");
        return;
    }
    result = (*outMaxObjItf)->GetInterface(outMaxObjItf, SL_IID_ENVIRONMENTALREVERB,
                                           &environmentalReverbItf);
    if (result != success) {
        LOGE("获取环境混音器接口失败,请检查MODIFY_AUDIO_SETTINGS权限是否开启");
        return;
    }
    outBuffer = static_cast<uint8_t *>(malloc(44100 * 2 * 2));
    SLDataLocator_AndroidSimpleBufferQueue bufferQueue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
                                                          2};
    SLDataFormat_PCM fmt = {
            SL_DATAFORMAT_PCM,
            2,
            SL_SAMPLINGRATE_44_1,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
            SL_BYTEORDER_LITTLEENDIAN
    };
    SLDataSource audioSrc = {&bufferQueue, &fmt};
    SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outMaxObjItf};
    SLDataSink audioSink = {&outputMix, null};

    const SLInterfaceID mids[1] = {SL_IID_BUFFERQUEUE};
    const SLboolean mreq[1] = {sl_true};
    result = (*engineItf)->CreateAudioPlayer(engineItf, &playerObjItf, &audioSrc, &audioSink, 1,
                                             mids, mreq);
    if (result != success) {
        LOGE("创建播放器失败");
        return;
    }

    result = (*playerObjItf)->Realize(playerObjItf, false);
    if (result != success) {
        LOGE("播放器实现失败");
        return;
    }

    result = (*playerObjItf)->GetInterface(playerObjItf, SL_IID_PLAY, &playItf);
    if (result != success) {
        LOGE("播放器接口获取失败");
        return;
    }
    result = (*playerObjItf)->GetInterface(playerObjItf, SL_IID_BUFFERQUEUE, &bufferQueueItf);
    if (result != success) {
        LOGE("队列接口获取失败");
        return;
    }
    result = (*bufferQueueItf)->RegisterCallback(bufferQueueItf, playerCallBack, this);
    if (result != success) {
        LOGE("回调接口失败");
        return;
    }
    effectSendItf = null;
}

void AudioPlayer::start() {
    SLresult result = (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_PLAYING);
    if (result != success) {
        LOGE("设置播放状态失败");
        return;
    }
    playerCallBack(bufferQueueItf, this);
}


void AudioPlayer::playAudio(const char *path) {
    prepare();
    start();
//    uint8_t *outBuffer = static_cast<uint8_t *>(av_malloc(MAX_AUDIO_FRAME_SIZE));
//    getData(outBuffer, path);
}

int AudioPlayer::getData(uint8_t *&buffer, const char *path) {
//    FILE *file = fopen(path, "wb+");

    while (true) {
        popResult = blockQueue.pop(pFrame);
        if (popResult == POP_STOP) break;
        if (popResult == POP_UNEXPECTED) continue;
        int bufferSize = av_samples_get_buffer_size(pFrame->linesize,
                                                    channels,
                                                    frameSize,
                                                    sampleFormat,
                                                    1);
        int rst = swr_convert(swrContext,
                              &buffer,
                              bufferSize,
                              (const uint8_t **) (pFrame->data),
                              pFrame->nb_samples);
//        int rst = swr_convert(swrContext,
//                              &buffer,
//                              MAX_AUDIO_FRAME_SIZE,
//                              (const uint8_t **) (pFrame->data),
//                              pFrame->nb_samples);
//        int bufferSize = av_samples_get_buffer_size(NULL, outChannelNum,
//                                                       pFrame->nb_samples,
//                                                       AV_SAMPLE_FMT_S16, 1);
        LOGE("%d", bufferSize);
//        fwrite(buffer, 1, bufferSize, file);
        return bufferSize;
    }
    return 0;
}

AudioPlayer::AudioPlayer() {

    LOGE("初始化");
}

void AudioPlayer::pushData(AVFrame *frame, int channelsParams,
                           int frameSizeParams,
                           AVSampleFormat sampleFormatParams) {
    blockQueue.push(frame);
    channels = channelsParams;
    frameSize = frameSizeParams;
    sampleFormat = sampleFormatParams;
    LOGE("queue大小%ld", blockQueue.getSize());
}

void
AudioPlayer::initSwrCtx(AVSampleFormat inSampleFmt, int inSampleRate, uint64_t inSampleChannel) {
    swrContext = swr_alloc();
    enum AVSampleFormat outSampleFmt = AV_SAMPLE_FMT_S16;

    int outSampleRate = 44100;

    uint64_t outSampleChannel = AV_CH_LAYOUT_STEREO;

    swr_alloc_set_opts(swrContext,
                       outSampleChannel,
                       outSampleFmt,
                       outSampleRate,
                       inSampleChannel,
                       inSampleFmt,
                       inSampleRate,
                       0,
                       null);
    swr_init(swrContext);
    outChannelNum = av_get_channel_layout_nb_channels(outSampleChannel);
}