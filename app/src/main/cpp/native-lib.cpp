#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_bamboo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_util_PlayerView_createEGLContext(JNIEnv *env, jobject instance) {

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_bamboo_util_PlayerView_destroyEGLContext(JNIEnv *env, jobject instance) {

}