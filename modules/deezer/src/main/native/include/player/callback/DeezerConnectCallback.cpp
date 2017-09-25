#include "DeezerConnectCallback.hpp"
#include "../../jni/callback/ScalaConnectCallback.hpp"

static ScalaConnectCallback scalaCallback;

void DeezerConnectCallback::on_connect_event_cb(dz_connect_handle handle, dz_connect_event_handle event, void* delegate) {
    dz_connect_event_t type = dz_connect_event_get_type(event);
    scalaCallback.scala_on_connect_event(type);
}

