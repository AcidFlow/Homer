#ifndef SCALAPLAYERCALLBACK_HPP
#define SCALAPLAYERCALLBACK_HPP

#include <jni.h>


class ScalaPlayerCallback {

    jclass _scala_player_callback_class = nullptr;
    jmethodID _scala_player_callback_on_event_method = nullptr;

    public:
        ScalaPlayerCallback();
        void scala_on_player_event(int event);
};

#endif // SCALAPLAYERCALLBACK_HPP
