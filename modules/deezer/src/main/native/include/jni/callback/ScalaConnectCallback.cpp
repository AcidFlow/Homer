#include <cstdio>

#include "ScalaConnectCallback.hpp"
#include "../JNIUtils.hpp"
#include "../../logger.hpp"

ScalaConnectCallback::ScalaConnectCallback() {

}

void ScalaConnectCallback::scala_on_connect_event(int event) {

    JNIEnv * env = JNIUtils::attachThread();

    if(_scala_connect_callback_class == NULL) {
        _scala_connect_callback_class = env->FindClass("info/acidflow/homer/modules/music/deezer/jni/callback/DeezerConnectCallback");
        if(_scala_connect_callback_class == NULL ){
            log_e("DeezerConnectCallback not found!!!\n");
            return;
        }
    }
    if(_scala_connect_callback_on_event_method == NULL){
        _scala_connect_callback_on_event_method = env->GetStaticMethodID(_scala_connect_callback_class, "onConnectEvent", "(I)V");
        if(_scala_connect_callback_on_event_method == NULL ){
            log_e("onConnectEvent() not found!!!\n");
            return;
        }
    }
    env->CallStaticVoidMethod(_scala_connect_callback_class, _scala_connect_callback_on_event_method, event);
    JNIUtils::checkAndClearException(env);
    JNIUtils::detachThread();
}