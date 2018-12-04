#include <jni.h>
#include <string>
#include "video/PlayerView.h"
#include "audio/AudioPlayer.h"
#include <thread>
#include "decode/Decode.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>

using namespace std;
Decode decode;
PlayerView playerView;
AudioPlayer audioPlayer;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_play(JNIEnv *env, jobject instance, jstring path_,
                                               jstring outpath_,
                                               jstring vertexCode_, jstring fragCode_,
                                               jobject surface, jint w, jint h) {
    const char *path = env->GetStringUTFChars(path_, 0);
    const char *outPath = env->GetStringUTFChars(outpath_, 0);
    const char *vertexCode = env->GetStringUTFChars(vertexCode_, 0);
    const char *fragCode = env->GetStringUTFChars(fragCode_, 0);

    ANativeWindow *window = ANativeWindow_fromSurface(env, surface);

    std::thread decodeThread(&Decode::decode, decode, path, DECODE_AUDIO, &playerView,
                             &audioPlayer,outPath);
//    std::thread playThread(&PlayerView::play, playerView, vertexCode,
//                           fragCode, window, w, h);
    std::thread audioThread(&AudioPlayer::playAudio, &audioPlayer, outPath);
//    AudioPlayer::playAudio(path);
    decodeThread.join();
//    playThread.join();
    audioThread.join();

    env->ReleaseStringUTFChars(path_, path);
    env->ReleaseStringUTFChars(outpath_, outPath);
    env->ReleaseStringUTFChars(vertexCode_, vertexCode);
    env->ReleaseStringUTFChars(fragCode_, fragCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_configEGLContext(JNIEnv *env, jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_myview_PlayerView_destroyEGLContext(JNIEnv *env, jobject instance) {

    // TODO

}