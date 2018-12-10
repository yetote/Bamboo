//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_DECODE_H
#define BAMBOO_DECODE_H


#include "../util/PlayerStatuts.h"
#include "../util/PlayerCallJava.h"
#include "../audio/AudioPlayer.h"
#include <pthread.h>

extern "C" {
#include <libavformat/avformat.h>
#include <libavformat/avformat.h>
};
#define null NULL

class Decode {
public:
    PlayerStatus *playstatus = null;
    pthread_t decodeThread;

    Decode(PlayerStatus *playStatus, PlayerCallJava *callJava, const char *url);

    void prepared();

    void pause();

    void resume();

    void start();

    void seek(int64_t secs);

    void release();

    void ffmpegDecodeThread();


    ~Decode();


private:
    int duration = 0;
    bool isExit = false;

    const char *url = null;
    pthread_mutex_t initMutex;
    pthread_mutex_t seekMutex;
    PlayerCallJava *callJava = null;

    AVFormatContext *pFmtCtx = null;
    AVStream *pStream = null;
    AudioPlayer *audio = null;
};


#endif //BAMBOO_DECODE_H
