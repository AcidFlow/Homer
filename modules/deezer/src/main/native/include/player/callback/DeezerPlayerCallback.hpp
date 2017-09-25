#include <deezer-player.h>

#ifndef DEEZERPLAYERCALLBACK_HPP
#define DEEZERPLAYERCALLBACK_HPP

class DeezerPlayerCallback {

    public:
        static void on_player_event(dz_player_handle handle, dz_player_event_handle event, void* supervisor);

};

#endif // DEEZERPLAYERCALLBACK_HPP
