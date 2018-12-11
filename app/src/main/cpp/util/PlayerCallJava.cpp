//
// Created by ether on 2018/12/7.
//

#include "PlayerCallJava.h"


#define  null NULL

PlayerCallJava::PlayerCallJava(JavaVM *jvm, JNIEnv *env, jobject obj) {
    this->jniEnv = env;
    this->javaVM = jvm;
    this->jobj = env->NewGlobalRef(obj);
    jclass jlz = jniEnv->GetObjectClass(jobj);
    if (!jlz) {
        LOGE("获取jclass失败");
        return;
    }
    prepared_jMid = jniEnv->GetMethodID(jlz, "onPreparedCall", "()V");
    load_jMid = jniEnv->GetMethodID(jlz, "onCallLoad", "(Z)V");
    timeInfo_jMid = jniEnv->GetMethodID(jlz, "onTimeInfoCall", "(II)V");
    complete_jMid = jniEnv->GetMethodID(jlz, "onCallComplete", "()V");
//    error_jMid = jniEnv->GetMethodID(jlz, "onCallError", "(ILjava/lang/String;)V");
}

void PlayerCallJava::onCallPrepared(int type) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jobj, prepared_jMid);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return;
        }
        jniEnv1->CallVoidMethod(jobj, prepared_jMid);
        javaVM->DetachCurrentThread();
    }
}

PlayerCallJava::~PlayerCallJava() {

}

void PlayerCallJava::onCallLoad(int type, bool load) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jobj, load_jMid, load);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return;
        }
        jniEnv1->CallVoidMethod(jobj, load_jMid, load);
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onCallTimeInfo(int type, int currentTime, int totalTime) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jobj, timeInfo_jMid, currentTime, totalTime);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return;
        }
        jniEnv1->CallVoidMethod(jobj, timeInfo_jMid, currentTime, totalTime);
        javaVM->DetachCurrentThread();
    }
}

void PlayerCallJava::onCallError(int type, int code, char *msg) {
    jstring jmsg = jniEnv->NewStringUTF(msg);
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jobj, error_jMid, code, jmsg);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return;
        }
        jniEnv1->CallVoidMethod(jobj, error_jMid, code, jmsg);
        javaVM->DetachCurrentThread();
    }
    jniEnv->DeleteLocalRef(jmsg);
}

void PlayerCallJava::onCallComplete(int type) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jobj, complete_jMid);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return;
        }
        jniEnv1->CallVoidMethod(jobj, complete_jMid);
        javaVM->DetachCurrentThread();
    }
}

