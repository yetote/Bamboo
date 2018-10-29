//
// Created by ether on 2018/10/29.
//

#ifndef BAMBOO_PLAYERVIEW_H
#define BAMBOO_PLAYERVIEW_H


#include "../util/BlockQueue.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include "GLUtil.h"
#include <android/log.h>

extern "C" {
#include "libavutil/frame.h"
};

class PlayerView {
public:
    GLfloat *vertex;
    GLuint program;
    GLuint *textureArr;


    void play(BlockQueue<AVFrame *> &blackQueue, const char *vertexCode, const char *fragCode);

    void initLocation(const char *vertexCode, const char *fragCode);

private:
    void initVertex();

};


#endif //BAMBOO_PLAYERVIEW_H
