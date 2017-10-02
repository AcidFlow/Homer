#ifndef SCALACONNECTCALLBACK_HPP
#define SCALACONNECTCALLBACK_HPP

#include <jni.h>


class ScalaConnectCallback {

    jclass _scala_connect_callback_class = nullptr;
    jmethodID _scala_connect_callback_on_event_method = nullptr;

    public:
        ScalaConnectCallback();
        void scala_on_connect_event(int event);
};

#endif // SCALACONNECTCALLBACK_HPP
