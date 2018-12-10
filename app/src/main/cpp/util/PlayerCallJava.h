//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_PLAYERCALLJAVA_H
#define BAMBOO_PLAYERCALLJAVA_H


#include <jni.h>
#include "LogUtil.h"

#define MAIN_THREAD 0
#define CHILD_THREAD 1

class PlayerCallJava {
public:
    JavaVM *javaVM;
    JNIEnv *jniEnv;
    jobject jobj;
    jmethodID prepared_jMid;
    jmethodID load_jMid;
    jmethodID timeInfo_jMid;
    jmethodID error_jMid;

    PlayerCallJava(JavaVM *jvm, JNIEnv *env, jobject obj);

    ~PlayerCallJava();

    void onCallPrepared(int type);

    void onCallLoad(int type, bool load);

    void onCallTimeInfo(int type, int currentTime, int totalTime);

    void onCallError(int type, int code, char *msg);
};


#endif //BAMBOO_PLAYERCALLJAVA_H
