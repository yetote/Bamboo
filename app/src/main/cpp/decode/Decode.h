//
// Created by ether on 2018/10/23.
//

#ifndef BAMBOO_DECODE_H
#define BAMBOO_DECODE_H


#include <android/log.h>
#include <cstdint>
#include <unistd.h>
#include "../util/BlockQueue.h"
#include "../audio/AudioPlayer.h"
#include "../video/PlayerView.h"

extern "C" {
#include <libavutil/frame.h>
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
};
enum DECODE_TYPE {
    DECODE_VIDEO, DECODE_AUDIO, DECODE_UNKNOWN
};

class Decode {
public:
    void decode(const char *path, DECODE_TYPE decode_type, PlayerView *playerView,
                AudioPlayer *audioPlayer);

    void destroy();

private:
    AVCodecContext *pCodecCtx;
    AVFormatContext *pFmtCtx;
    int index;
    AVStream *pStream;
    AVCodec *pCodec;

    void findIndex(DECODE_TYPE type);


    void video(PlayerView *pView);

    void audio(AudioPlayer *pPlayer);

};


#endif //BAMBOO_DECODE_H
