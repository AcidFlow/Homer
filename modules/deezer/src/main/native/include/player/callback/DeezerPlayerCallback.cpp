#include "DeezerPlayerCallback.hpp"
#include "../../jni/callback/ScalaPlayerCallback.hpp"

static ScalaPlayerCallback scalaCallback;

void DeezerPlayerCallback::on_player_event(dz_player_handle handle, dz_player_event_handle event, void* supervisor){;
    dz_player_event_t type = dz_player_event_get_type(event);
    scalaCallback.scala_on_player_event(type);
}
