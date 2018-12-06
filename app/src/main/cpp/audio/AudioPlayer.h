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

    void initQueue();
    void initSwrCtx(AVSampleFormat inSampleFmt, int inSampleRate, uint64_t inSampleChannel);

    SLAndroidSimpleBufferQueueItf bufferQueueItf;

private:
    SLObjectItf objectItf, outMaxObjItf, playerObjItf;
    SLEngineItf engineItf;
    SLPlayItf playItf;
    SLEnvironmentalReverbItf environmentalReverbItf;
    SLEffectSendItf effectSendItf;
    SLVolumeItf volumeItf;

    void start();


    void prepare();

};


#endif //BAMBOO_AUDIOPLAYER_H
