//
// Created by ether on 2018/12/12.
//

#ifndef BAMBOO_VIDEOPLAYER_H
#define BAMBOO_VIDEOPLAYER_H

#include "../util/BlockQueue.h"
#include "../util/PlayerCallJava.h"
#include "../util/EGLUtil.h"
#include "../util/GLUtil.h"
#include "../audio/AudioPlayer.h"

#define CODEC_HARDWARE 0
#define CODEC_SOFTWARE 1

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavutil/time.h>
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
};

#define  null NULL

class VideoPlayer {
public:
    EGLUtil *eglUtil;
    GLUtil *glUtil;
    int videoIndex = -1;
    AVCodecContext *pCodecCtx = null;
    AVCodecParameters *parameters = null;
    BlockQueue *blockQueue = NULL;
    PlayerCallJava *playerCallJava = NULL;
    PlayerStatus *playerStatus = null;
    AVRational time_base;
    pthread_t startThread;
    ANativeWindow *window;
    const char *vertexCode;
    const char *fragCode;
    pthread_mutex_t codecMutex;
    GLfloat *vertexArray;
    VideoPlayer(PlayerCallJava *playerCallJava, PlayerStatus *playerStatus,
                const char *vertexCode, const char *fragCode, ANativeWindow *window, int w,
                int h);

    void play();

    void release();

    void initPlay(AVFrame *frame);

    ~VideoPlayer();

    void initVertex();

    void initLocation();

    int w;
    int h;
    AudioPlayer *audioPlayer;

    double getFrameDiffTime(AVFrame *frame, AVPacket *packet);

    double defaultDelayTime = 0;

    double getDelayTime(double diff);

    double clock;
    int codecType;
    AVBSFContext *pBsfContext = null;
private:
    GLfloat *textureArray;

    GLuint *textureArr;
    GLint aPosition, aColor;
    GLint aTextureCoordinates;
    GLint uTexY, uTexU, uTexV;
    double delayTime;

    void draw(AVFrame *pFrame);

};


#endif //BAMBOO_VIDEOPLAYER_H
