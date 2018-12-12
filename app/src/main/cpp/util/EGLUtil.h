//
// Created by ether on 2018/12/12.
//

#ifndef BAMBOO_EGLUTIL_H
#define BAMBOO_EGLUTIL_H

#include <EGL/egl.h>
#include "LogUtil.h"

#define null NULL

class EGLUtil {
public:

    void eglCreate(ANativeWindow *window);

    void eglRelease();

    EGLUtil();

    ~EGLUtil();

    EGLDisplay eglDisplay;
    EGLContext eglContext;
    EGLSurface eglSurface;
private:
    EGLConfig eglConfig;
};


#endif //BAMBOO_EGLUTIL_H
