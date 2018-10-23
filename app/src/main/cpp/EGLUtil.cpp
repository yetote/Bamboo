//
// Created by ether on 2018/10/20.
//

#include "EGLUtil.h"


#define null NULL
#define LOG_TAG "eglUtil"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

void EGLUtil::createCtx() {
    this->eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY) {
        LOGE("无法获取eglDisplay");
        return;
    }
    if (!eglInitialize(eglDisplay, null, null)) {
        LOGE("egl初始化失败");
        return;
    }
    int eglConfigAttr[] = {
            EGL_BUFFER_SIZE, 32,
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_RENDER_BUFFER, EGL_OPENGL_ES2_BIT,
            EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
            EGL_NONE
    };
    auto *numConfig = new int[1];
    auto *eglConfigArr = new EGLConfig[1];
    if (!eglChooseConfig(eglDisplay, eglConfigAttr, eglConfigArr,
                         sizeof(&numConfig) / sizeof(numConfig[0]), numConfig)) {
        LOGE("egl配置失败");
        return;
    }
    this->eglConfig = eglConfigArr[0];
    delete[] eglConfigArr;
    int eglContextAttr[] = {
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
    };
    this->eglContext = eglCreateContext(eglDisplay, eglConfig, null, eglContextAttr);
    if (eglContext == null) {
        LOGE("创建eglContext失败");
        return;
    }

}

void EGLUtil::destroyCtx() {
    eglDestroyContext(eglDisplay, eglContext);
    eglContext = null;
    eglDisplay = null;
}
