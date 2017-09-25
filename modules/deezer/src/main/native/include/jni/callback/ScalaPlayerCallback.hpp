#include <jni.h>

#ifndef SCALAPLAYERCALLBACK_HPP
#define SCALAPLAYERCALLBACK_HPP

class ScalaPlayerCallback {

    jclass _scala_player_callback_class = NULL;
    jmethodID _scala_player_callback_on_event_method = NULL;

    public:
        ScalaPlayerCallback();
        void scala_on_player_event(int event);
};

#endif // SCALAPLAYERCALLBACK_HPP
