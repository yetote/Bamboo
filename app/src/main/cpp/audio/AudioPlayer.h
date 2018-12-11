//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_AUDIOPLAYER_H
#define BAMBOO_AUDIOPLAYER_H

#include "../util/PlayerStatuts.h"
#include "../util/PlayerCallJava.h"
#include "../util/BlockQueue.h"
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

#define success SL_RESULT_SUCCESS
#define sl_true SL_BOOLEAN_TRUE
#define false SL_BOOLEAN_FALSE
extern "C" {
#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
};

#define null NULL

class AudioPlayer {
public:
    int streamIndex;
    AVCodecParameters *codecParameters = NULL;
    AVCodecContext *pCodecCtx = NULL;
    BlockQueue *blockQueue;
    pthread_t playThread;
    PlayerStatus *playstatus = NULL;
    AVPacket *avPacket = NULL;
    AVFrame *avFrame = NULL;
    int sampleRate;
    int duration;
    AVRational time_base;
    double now_time = 0;
    double clock = 0;
    double last_time = 0;
    SLAndroidSimpleBufferQueueItf bufferQueueItf;

    void release();

    void play();

    int resampleAudio();

    uint8_t *buffer;
    int dataSize;
    PlayerCallJava *playerCallJava;

    AudioPlayer(PlayerStatus *playstatus, int sampleRate, PlayerCallJava *playerCallJava);

    ~AudioPlayer();

    void initOpenSLES();

    void pause();

    void resume();

    SLuint32 getCurrentSamplRate(int sampleRate);


private:
    SLObjectItf engineObj, mixObj, playerObj;
    SLEngineItf engineItf = null;
    SLPlayItf playItf = null;
    SLOutputMixItf mixItf;
    SLEnvironmentalReverbItf environmentalReverbItf = null;
    SLEnvironmentalReverbSettings reverbSettings;

    void stop();};


#endif //BAMBOO_AUDIOPLAYER_H
