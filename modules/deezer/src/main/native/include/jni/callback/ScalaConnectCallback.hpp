#include <jni.h>

#ifndef SCALACONNECTCALLBACK_HPP
#define SCALACONNECTCALLBACK_HPP

class ScalaConnectCallback {

    jclass _scala_connect_callback_class = NULL;
    jmethodID _scala_connect_callback_on_event_method = NULL;

    public:
        ScalaConnectCallback();
        void scala_on_connect_event(int event);
};

#endif // SCALACONNECTCALLBACK_HPP
