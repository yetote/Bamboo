//
// Created by ether on 2018/12/12.
//

#include "EGLUtil.h"

void EGLUtil::eglCreate(ANativeWindow *window) {
    EGLint ret;
    eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == null) {
        ret = eglGetError();
        LOGE("获取eglDisplay失败，错误码%d", ret);
        return;
    }
    if (!eglInitialize(eglDisplay, 0, 0)) {
        ret = eglGetError();
        LOGE("初始化eglDisplay失败，错误码%d", ret);
        return;
    }
    int eglConfigAttr[] = {
            EGL_BUFFER_SIZE, 32,
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
            EGL_NONE
    };
    int *numConfig = new int[1];
    EGLConfig *eglConfigArr = new EGLConfig[1];
    if (!eglChooseConfig(eglDisplay,
                         eglConfigAttr,
                         eglConfigArr,
                         sizeof(numConfig) / sizeof(numConfig[0]),
                         numConfig)) {
        ret = eglGetError();
        LOGE("egl配置失败,错误码%d", ret);
        delete[] numConfig;
        delete[] eglConfigArr;
        return;
    }
    eglConfig = eglConfigArr[0];
    delete[] numConfig;
    delete[] eglConfigArr;
    int eglContextAttr[] = {
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
    };
    eglContext = eglCreateContext(eglDisplay, eglConfig, null, eglContextAttr);
    if (eglContext == null) {
        ret = eglGetError();
        LOGE("创建eglContext失败,错误码%d", ret);
        return;
    }
    int *surfaceAttr = new int[1]{EGL_NONE};
    eglSurface = eglCreateWindowSurface(eglDisplay, eglConfig, window, surfaceAttr);
    LOGE("window%p", window);
    LOGE("egldisplay%p", eglDisplay);
    LOGE("eglConfig%p", eglConfig);
    LOGE("surfaceAttr%p", surfaceAttr);
    if (eglSurface == null) {
        ret = eglGetError();
        LOGE("创建eglSurface失败,错误码%d", ret);
        delete[] surfaceAttr;
        return;
    }
    delete[] surfaceAttr;
}

EGLUtil::EGLUtil() {
    eglContext = null;
    eglConfig = null;
    eglSurface = null;
    eglDisplay = null;
}

void EGLUtil::eglRelease() {

    if (eglContext != null) {
        eglDestroyContext(eglDisplay, eglContext);
        eglContext = null;
    }
    if (eglSurface != null) {
        eglDestroySurface(eglDisplay, eglSurface);
        eglSurface = null;
    }
    if (eglConfig != null) {
        eglConfig = null;
    }
    if (!eglTerminate(eglDisplay)) {
        int ret = eglGetError();
        LOGE("终止egl与display连接失败,错误码%d", ret);
    }
    eglDisplay = null;
    LOGE("egl释放完成");
}

EGLUtil::~EGLUtil() {

}
