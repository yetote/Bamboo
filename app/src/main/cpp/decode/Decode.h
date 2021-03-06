//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_DECODE_H
#define BAMBOO_DECODE_H


#include "../util/PlayerStatuts.h"
#include "../util/PlayerCallJava.h"
#include "../audio/AudioPlayer.h"
#include "../video/VideoPlayer.h"
#include <pthread.h>

extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/time.h>
};
#define null NULL

class Decode {
public:
    PlayerStatus *playstatus = null;
    pthread_t decodeThread;

    Decode(PlayerStatus *playStatus, PlayerCallJava *callJava, const char *url,
           const char *vertexCode, const char *fragCode, ANativeWindow *window, int w, int h);

    bool isSupportHardWareCodec = false;

    void prepared();

    void pause();

    void resume();

    void start();

    void seek(int64_t secs);

    void release();

    void ffmpegDecodeThread();

    void setVolume(int percent);

    ~Decode();


private:
    int duration = 0;
    bool isExit = false;

    const char *url = null;
    pthread_mutex_t initMutex;
    pthread_mutex_t seekMutex;
    PlayerCallJava *callJava = null;
    const AVBitStreamFilter *pStreamFilter = null;
    AVFormatContext *pFmtCtx = null;
    AVStream *pStream = null;
    bool isReadFrameFinish = false;
    AudioPlayer *audio = null;
    VideoPlayer *video = null;
    ANativeWindow *window;
    const char *vertexCode;
    const char *fragCode;
    int w;
    int h;


    int getCodecId(AVCodecParameters *parameters, AVCodecContext **codecContext);
};


#endif //BAMBOO_DECODE_H
