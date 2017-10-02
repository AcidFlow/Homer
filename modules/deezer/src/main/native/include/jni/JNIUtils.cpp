#include "JNIUtils.hpp"

// Static initialization
JavaVM* JNIUtils::g_vm = nullptr;

jint JNI_OnLoad(JavaVM* vm, void *reserved){
    jint result = -1;
    void* venv = NULL;
    JNIEnv* env = NULL;
    if (vm->GetEnv(&venv, JNI_VERSION_1_8) != JNI_OK) {
        return -1;
    }

    JNIUtils::g_vm = vm;

    return JNI_VERSION_1_8;
}

JNIEnv* JNIUtils::getJNIEnv() {
    JNIEnv* env;
    JNIUtils::g_vm->AttachCurrentThread((void **)&env, nullptr);
    return env;
}


std::string JNIUtils::extractJniString(JNIEnv* env, jstring jstr) {
    const char *str = env->GetStringUTFChars(jstr, 0);
    std::string dup = std::string(str);
    env->ReleaseStringUTFChars(jstr, str);
    return dup;
}
