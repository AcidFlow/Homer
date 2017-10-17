#ifndef _JNIUtils_
#define _JNIUtils_

#include <jni.h>
#include <string>

class JNIUtils {

    public:
        static JavaVM* g_vm;

    public:
        static JNIEnv* attachThread();
        static void detachThread();
        static void checkAndClearException(JNIEnv * env);
        static std::string extractJniString(JNIEnv * env, jstring jstr);

};

#endif