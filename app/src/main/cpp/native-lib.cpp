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