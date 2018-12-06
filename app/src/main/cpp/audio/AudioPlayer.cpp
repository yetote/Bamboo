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
#define MAX_AUDIO_FRAME_SIZE 44100*2
uint8_t *outBuffer;
AVFrame *pFrame;
SwrContext *swrContext;
BlockQueue<AVFrame *> blockQueue;
int frameSize;
int channels;
enum AVSampleFormat sampleFormat;
int outChannelNum;
pthread_t playThread;
struct pathParams_ {
    const char *path;
};

void playerCallBack(SLAndroidSimpleBufferQueueItf bf, void *context) {
//    AudioPlayer *audioPlayer = static_cast<AudioPlayer *>(context);
//    int size = audioPlayer->getData(outBuffer, "");
//    LOGE("bufferSize的大小%d", size);
//    SLresult result = (*audioPlayer->bufferQueueItf)->Enqueue(bf, outBuffer, size);
}

void AudioPlayer::prepare() {
    SLresult result;
    result = slCreateEngine(&objectItf, 0, 0, 0, 0, 0);
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
    result = (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_PLAYING);
    if (result != success) {
        LOGE("设置播放状态失败");
        return;
    }
    playerCallBack(bufferQueueItf, this);
}

void AudioPlayer::start() {

}

void *getData(void *params) {
    uint8_t *outBuffer = static_cast<uint8_t *>(av_malloc(MAX_AUDIO_FRAME_SIZE));
    pathParams_ *params1 = static_cast<pathParams_ *>(params);
    FILE *file = fopen(params1->path, "wb+");
    pFrame = av_frame_alloc();
    int i = 0;
    while (true) {
        blockQueue.pop(pFrame);
        int rst = swr_convert(swrContext,
                              &outBuffer,
                              pFrame->nb_samples,
                              (const uint8_t **) (pFrame->data),
                              pFrame->nb_samples);
        int out_channels = av_get_channel_layout_nb_channels(pFrame->channel_layout);
        int bufferSize = rst * out_channels * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);
        fwrite(outBuffer, 1, bufferSize, file);
        usleep(3000);
//        return bufferSize;
    }
    pthread_exit(&playThread);
}

void AudioPlayer::playAudio(const char *path) {
    pathParams_ *params = new pathParams_;
    params->path = path;
    pthread_create(&playThread, null, getData, (void *) params);
//    getData(outBuffer, path);
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

void AudioPlayer::initQueue() {
    blockQueue.init();
}
