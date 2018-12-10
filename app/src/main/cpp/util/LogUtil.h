//
// Created by ether on 2018/12/7.
//

#ifndef FFMPEGDEMO_LOGUTIL_H
#define FFMPEGDEMO_LOGUTIL_H

#endif //FFMPEGDEMO_LOGUTIL_H

#include <android/log.h>

#define LOG_TAG "blockQueue"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)