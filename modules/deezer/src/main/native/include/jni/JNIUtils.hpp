#include <jni.h>
#include <string>

#ifndef _JNIUtils_
#define _JNIUtils_

class JNIUtils {

    public:
        static JavaVM* g_vm;

    public:
        static JNIEnv* getJNIEnv();
        static std::string extractJniString(JNIEnv * env, jstring jstr);

};

#endif