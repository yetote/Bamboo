#include <jni.h>
#include <string>
#include <thread>
#include "util/PlayerCallJava.h"
#include "decode/Decode.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>

#define  null NULL
PlayerCallJava *playerCallJava = null;
PlayerStatus *playerStatus;
Decode *decode = null;
JavaVM *javaVM;
bool nExit = true;
pthread_t startThread;

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *version) {
    jint result = -1;
    javaVM = jvm;
    JNIEnv *env;
    if (jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }
    return JNI_VERSION_1_6;
}


void *start(void *data) {
    Decode *decode1 = static_cast<Decode *>(data);
    decode1->start();
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegStart(JNIEnv *env, jobject instance) {

    if (decode != null) {
        pthread_create(&startThread, null, start, decode);
//        decode->start();
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegStop(JNIEnv *env, jobject instance) {

    if (!nExit) {
        return;
    }

    jclass jlz = env->GetObjectClass(instance);
    jmethodID next_jMid = env->GetMethodID(jlz, "onCallPlayNext", "()V");
    nExit = false;
    if (decode != null) {
        decode->release();
        pthread_join(startThread, null);
        delete decode;
        decode = null;
    }
    if (playerCallJava != null) {
        delete (playerCallJava);
        playerCallJava = null;
    }
    if (playerStatus != null) {
        delete (playerStatus);
        playerStatus = null;
    }
    nExit = true;
    env->CallVoidMethod(instance, next_jMid);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegSeek(JNIEnv *env, jobject instance, jint secs) {

    if (decode != null) {
        decode->seek(secs);
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegPause(JNIEnv *env, jobject instance) {

    if (decode != null) {
        decode->pause();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegResume(JNIEnv *env, jobject instance) {
    if (decode != null) {
        decode->resume();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegSetVolume(JNIEnv *env, jobject instance,
                                                          jint percent) {

    if (decode != null) {
        decode->setVolume(percent);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegVideoStart(JNIEnv *env, jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegPrepared(JNIEnv *env, jobject instance,
                                                         jstring source_, jstring vertexCode_,
                                                         jstring fragCode_, jobject surface, jint w,
                                                         jint h) {
    const char *source = env->GetStringUTFChars(source_, 0);
    const char *vertexCode = env->GetStringUTFChars(vertexCode_, 0);
    const char *fragCode = env->GetStringUTFChars(fragCode_, 0);


    if (decode == null) {
        if (playerCallJava == null) {
            playerCallJava = new PlayerCallJava(javaVM, env, instance);
        }
        playerStatus = new PlayerStatus();
        ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
        decode = new Decode(playerStatus, playerCallJava, source, vertexCode, fragCode, window, w,
                            h);
    }
    decode->prepared();

//    env->ReleaseStringUTFChars(source_, source);
//    env->ReleaseStringUTFChars(vertexCode_, vertexCode);
//    env->ReleaseStringUTFChars(fragCode_, fragCode);
}