//
// Created by ether on 2018/11/1.
//


#include <cstdlib>
#include "AudioPlayer.h"

#define null NULL
#define success SL_RESULT_SUCCESS
#define true SL_BOOLEAN_TRUE
#define false SL_BOOLEAN_FALSE
#define LOG_TAG "audioPlayer"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
uint8_t *outBuffer;

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
    const SLboolean mreq[1] = {true};
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
    result = (*playerObjItf)->GetInterface(playerObjItf, SL_IID_BUFFERQUEUE, &bufferQueue);
    if (result != success) {
        LOGE("队列接口获取失败");
        return;
    }
}
