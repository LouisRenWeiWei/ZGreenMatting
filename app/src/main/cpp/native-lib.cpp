#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_zgreenmatting_utils_JniUtil_stringFromJNI(JNIEnv *env, jobject instance) {

    // TODO

    const char *returnValue;
    return env->NewStringUTF(returnValue);
}
