//
// Created by ether on 2018/10/29.
//

#include "PlayerView.h"

#define LOG_TAG "PlayerView"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define null NULL

GLUtil *glUtil;

void PlayerView::initVertex() {
//    @formatter:off
    vertex = new GLfloat[24]{
//             x,     y,    s,    w
            1.0f, 1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,

            -1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f
    };
//    @formatter:on

    delete[] vertex;
}

void PlayerView::initLocation(const char *vertexCode, const char *fragCode) {
    glUtil = new GLUtil;
    glUtil->createProgram(vertexCode, fragCode);
    textureArr = glUtil->createTexture();
    LOGE("%d", textureArr[0]);
}

void
PlayerView::play(BlockQueue<AVFrame *> &blackQueue, const char *vertexCode, const char *fragCode) {
    initVertex();
    initLocation(vertexCode, fragCode);
}
