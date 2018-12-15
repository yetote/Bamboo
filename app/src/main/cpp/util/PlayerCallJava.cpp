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
    supportHardWare_jMid = jniEnv->GetMethodID(jlz, "onCallIsSupportHardwareCodec",
                                               "(Ljava/lang/String;)Z");
    initCodec_jMid = jniEnv->GetMethodID(jlz, "initMediaCodec", "(Ljava/lang/String;II[B[B)V");
    decode_jMid = jniEnv->GetMethodID(jlz, "decode", "(I[B)V");
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

bool PlayerCallJava::onCallSupportHardwareCodec(int type, const char *ffmpegName) {
    bool isSupport = false;
    if (type == MAIN_THREAD) {
        jstring name = jniEnv->NewStringUTF(ffmpegName);
        isSupport = jniEnv->CallBooleanMethod(jobj, supportHardWare_jMid, name);
        jniEnv->DeleteLocalRef(name);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv1;
        if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
            LOGE("子线程回调java方法失败");
            return isSupport;
        }
        jstring name = jniEnv1->NewStringUTF(ffmpegName);
        isSupport = jniEnv1->CallBooleanMethod(jobj, supportHardWare_jMid, name);
        jniEnv1->DeleteLocalRef(name);
        javaVM->DetachCurrentThread();
    }
    return isSupport;
}

void
PlayerCallJava::onCallInitCodec(char *mime, int w, int h, int csd0size, int csd1size, uint8_t *csd0,
                                uint8_t *csd1) {
    JNIEnv *jniEnv1;
    if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
        LOGE("子线程回调java方法失败");
    }
    jstring type = jniEnv1->NewStringUTF(mime);
    jbyteArray csd0Arr = jniEnv1->NewByteArray(csd0size);
    jniEnv1->SetByteArrayRegion(csd0Arr, 0, csd0size, reinterpret_cast<const jbyte *>(csd0));
    jbyteArray csd1Arr = jniEnv1->NewByteArray(csd1size);
    jniEnv1->SetByteArrayRegion(csd1Arr, 0, csd1size, reinterpret_cast<const jbyte *>(csd1));
    jniEnv1->CallVoidMethod(jobj, initCodec_jMid, type, w, h, csd0Arr, csd1Arr);

    jniEnv1->DeleteLocalRef(csd0Arr);
    jniEnv1->DeleteLocalRef(csd1Arr);
    jniEnv1->DeleteLocalRef(type);
    javaVM->DetachCurrentThread();
}

void PlayerCallJava::onCallDecode(int dataSize, uint8_t *data) {
    JNIEnv *jniEnv1;
    if (javaVM->AttachCurrentThread(&jniEnv1, 0) != JNI_OK) {
        LOGE("子线程回调java方法失败");
    }
    jbyteArray dataArr = jniEnv1->NewByteArray(dataSize);
    jniEnv1->SetByteArrayRegion(dataArr, 0, dataSize, reinterpret_cast<const jbyte *>(data));
    jniEnv1->CallVoidMethod(jobj, decode_jMid, dataSize,dataArr);

    jniEnv1->DeleteLocalRef(dataArr);
    javaVM->DetachCurrentThread();
}

