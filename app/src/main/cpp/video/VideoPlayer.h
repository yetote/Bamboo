//
// Created by ether on 2018/12/12.
//

#ifndef BAMBOO_VIDEOPLAYER_H
#define BAMBOO_VIDEOPLAYER_H

#include "../util/BlockQueue.h"
#include "../util/PlayerCallJava.h"
#include "../util/EGLUtil.h"
#include "../util/GLUtil.h"

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
private:
    GLfloat *textureArray;
    GLfloat *vertexArray;
    GLuint *textureArr;
    GLint aPosition, aColor;
    GLint aTextureCoordinates;
    GLint uTexY, uTexU, uTexV;

    void draw(AVFrame *pFrame);
};


#endif //BAMBOO_VIDEOPLAYER_H
