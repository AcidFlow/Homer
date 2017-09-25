#include <deezer-connect.h>

#ifndef DEEZERCONNECTCALLBACK_HPP
#define DEEZERCONNECTCALLBACK_HPP

class DeezerConnectCallback {

    public:
        static void on_connect_event_cb(dz_connect_handle handle, dz_connect_event_handle event, void* delegate);

};

#endif // DEEZERCONNECTCALLBACK_HPP
