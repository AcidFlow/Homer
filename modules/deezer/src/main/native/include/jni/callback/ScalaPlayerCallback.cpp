#include <cstdio>

#include "ScalaPlayerCallback.hpp"
#include "../JNIUtils.hpp"

ScalaPlayerCallback::ScalaPlayerCallback() {

}

void ScalaPlayerCallback::scala_on_player_event(int event) {

    JNIEnv * env = JNIUtils::getJNIEnv();

    if(_scala_player_callback_class == NULL) {
        _scala_player_callback_class = env->FindClass("info/acidflow/homer/modules/music/deezer/jni/callback/DeezerPlayerCallback");
        if(_scala_player_callback_class == NULL ){
            printf("DeezerPlayerCallback not found!!!\n");
            return;
        }
    }
    if(_scala_player_callback_on_event_method == NULL){
        _scala_player_callback_on_event_method = env->GetStaticMethodID(_scala_player_callback_class, "onPlayerEvent", "(I)V");
        if(_scala_player_callback_on_event_method == NULL ){
            printf("onPlayerEvent() not found!!!\n");
            return;
        }
    }
    env->CallStaticVoidMethod(_scala_player_callback_class, _scala_player_callback_on_event_method, event);
}