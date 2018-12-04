//
// Created by ether on 2018/11/1.
//

#ifndef BAMBOO_AUDIOPLAYER_H
#define BAMBOO_AUDIOPLAYER_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <android/log.h>
#include "../util/BlockQueue.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavutil/frame.h>
#include <libswresample/swresample.h>
#include <libavformat/avformat.h>
};

class AudioPlayer {
public:
    AVCodecContext *pCodecCtx;



    void playAudio(const char *path);

    void pushData(AVFrame *frame, int channels, int frameSize, AVSampleFormat sampleFormat);

    AudioPlayer();

    int getData(uint8_t *&buffer);

    void initSwrCtx(AVSampleFormat inSampleFmt, int inSampleRate, uint64_t inSampleChannel);

    int getData(uint8_t *&buffer, const char *path);

private:
    SLObjectItf objectItf, outMaxObjItf, playerObjItf;
    SLEngineItf engineItf;
    SLPlayItf playItf;
    SLAndroidSimpleBufferQueueItf bufferQueueItf;
    SLEnvironmentalReverbItf environmentalReverbItf;
    SLEffectSendItf effectSendItf;
    SLVolumeItf volumeItf;
    popResult popResult;

    void start();


    void prepare();

};


#endif //BAMBOO_AUDIOPLAYER_H
