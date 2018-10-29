#include <jni.h>
#include <string>
#include "video/PlayerView.h"
#include <thread>
#include "decode/Decode.h"

using namespace std;
PlayerView playerView;
Decode decode;
BlockQueue<AVFrame *> blockQueue;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_util_PlayerView_play(JNIEnv *env, jobject instance, jstring path_,
                                             jstring vertexCode_, jstring fragCode_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    const char *vertexCode = env->GetStringUTFChars(vertexCode_, 0);
    const char *fragCode = env->GetStringUTFChars(fragCode_, 0);

    std::thread decodeThread(&Decode::decode, decode, path, DECODE_VIDEO, ref(blockQueue));
    std::thread playThread(&PlayerView::play, playerView,ref(blockQueue),vertexCode,fragCode);

    decodeThread.join();
    playThread.join();

    env->ReleaseStringUTFChars(path_, path);
    env->ReleaseStringUTFChars(vertexCode_, vertexCode);
    env->ReleaseStringUTFChars(fragCode_, fragCode);
}