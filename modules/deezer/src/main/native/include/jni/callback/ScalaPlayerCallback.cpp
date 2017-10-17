#include <cstdio>

#include "ScalaPlayerCallback.hpp"
#include "../JNIUtils.hpp"
#include "../../logger.hpp"

ScalaPlayerCallback::ScalaPlayerCallback() {

}

void ScalaPlayerCallback::scala_on_player_event(int event) {

    JNIEnv * env = JNIUtils::attachThread();

    if(_scala_player_callback_class == NULL) {
        _scala_player_callback_class = env->FindClass("info/acidflow/homer/modules/music/deezer/jni/callback/DeezerPlayerCallback");
        if(_scala_player_callback_class == NULL ){
            log_e("DeezerPlayerCallback not found!!!\n");
            return;
        }
    }
    if(_scala_player_callback_on_event_method == NULL){
        _scala_player_callback_on_event_method = env->GetStaticMethodID(_scala_player_callback_class, "onPlayerEvent", "(I)V");
        if(_scala_player_callback_on_event_method == NULL ){
            log_e("onPlayerEvent() not found!!!\n");
            return;
        }
    }
    env->CallStaticVoidMethod(_scala_player_callback_class, _scala_player_callback_on_event_method, event);
    JNIUtils::checkAndClearException(env);
    JNIUtils::detachThread();
}