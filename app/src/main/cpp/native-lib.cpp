#include <jni.h>
#include <string>
#include <thread>
#include "util/PlayerCallJava.h"
#include "decode/Decode.h"

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

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_ffmpegPrepared(JNIEnv *env, jobject instance,
                                                         jstring source_) {
    const char *source = env->GetStringUTFChars(source_, 0);

    if (decode == null) {
        if (playerCallJava == null) {
            playerCallJava = new PlayerCallJava(javaVM, env, instance);
        }
        playerStatus = new PlayerStatus();
        decode = new Decode(playerStatus, playerCallJava, source);
    }
    decode->prepared();
//    env->ReleaseStringUTFChars(source_, source);
}

void *start(void *data) {
    Decode *decode1 = static_cast<Decode *>(data);
    decode1->start();
    pthread_exit(&startThread);
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

    jclass  jlz=env->GetObjectClass(instance);
    jmethodID next_jMid=env->GetMethodID(jlz,"onCallPlayNext","()V");

    if (decode != null) {
        decode->release();
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
    env->CallVoidMethod(instance,next_jMid);
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