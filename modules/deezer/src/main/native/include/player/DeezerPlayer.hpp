#include <deezer-connect.h>
#include <deezer-player.h>

#ifndef DEEZERPLAYER_HPP
#define DEEZERPLAYER_HPP

typedef struct {
    int                   nb_track_played;
    bool                  is_playing;
    char*                 sz_content_url;
    int                   activation_count;
    dz_connect_handle     dzconnect;
    dz_player_handle      dzplayer;
    dz_queuelist_repeat_mode_t repeat_mode;
    bool                  is_shuffle_mode;
} app_context;

class DeezerPlayer {

    private:
        dz_connect_configuration _config;
        app_context _app_ctxt;

    public:
        DeezerPlayer(dz_connect_configuration conf);
        int initialize(const char* access_token);
        void load(const char * media_uri);
        void play();
        void stop();
        void pause();
        void resume();
        void next();
        void previous();
        int change_volume(int percent);
        void release();
};

#endif // DEEZERPLAYER_HPP
