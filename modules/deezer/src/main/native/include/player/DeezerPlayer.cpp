#include <cstdlib>
#include <cstdio>
#include <cstring>

#include "DeezerPlayer.hpp"
#include "callback/DeezerPlayerCallback.hpp"
#include "callback/DeezerConnectCallback.hpp"
#include "../logger.hpp"

DeezerPlayer::DeezerPlayer(dz_connect_configuration conf) : _config(conf), _app_ctxt() {
    _config.connect_event_cb = DeezerConnectCallback::on_connect_event_cb;
}

int DeezerPlayer::initialize(const char* access_token) {
    log_i("<-- Deezer native SDK Version : %s\n", dz_connect_get_build_id());
    log_i("--> Application ID:    %s\n", _config.app_id);
    log_i("--> Product ID:        %s\n", _config.product_id);
    log_i("--> Product BUILD ID:  %s\n", _config.product_build_id);
    log_i("--> User Profile Path: %s\n", _config.user_profile_path);

    _app_ctxt.dzconnect = dz_connect_new(&_config);
    if (_app_ctxt.dzconnect == NULL) {
          log_i("dzconnect null\n");
          return -1;
    }

    log_i("Device ID: %s\n", dz_connect_get_device_id(_app_ctxt.dzconnect));

    dz_error_t dzerr = DZ_ERROR_NO_ERROR;
//    dzerr = dz_connect_debug_log_disable(_app_ctxt.dzconnect);
//    if (dzerr != DZ_ERROR_NO_ERROR) {
//        log_i("dz_connect_debug_log_disable error\n");
//        return -1;
//    }

    dzerr = dz_connect_activate(_app_ctxt.dzconnect, &_app_ctxt);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_connect_activate error\n");
       return -1;
    }

    _app_ctxt.activation_count++;

    dz_connect_cache_path_set(_app_ctxt.dzconnect, NULL, NULL, _config.user_profile_path);

    _app_ctxt.dzplayer = dz_player_new(_app_ctxt.dzconnect);
    if (_app_ctxt.dzplayer == NULL) {
       log_i("dzplayer null\n");
       return -1;
    }

    dzerr = dz_player_activate(_app_ctxt.dzplayer, &_app_ctxt);
    if (dzerr != DZ_ERROR_NO_ERROR) {
        log_i("dz_player_activate error\n");
        return -1;
    }
    _app_ctxt.activation_count++;

     dzerr = dz_player_set_event_cb(_app_ctxt.dzplayer, DeezerPlayerCallback::on_player_event);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_player_set_event_cb error\n");
       return -1;
    }

    dzerr = dz_player_set_output_volume(_app_ctxt.dzplayer, NULL, NULL, 80);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_player_set_output_volume error\n");
       return -1;
    }

    dzerr = dz_player_set_crossfading_duration(_app_ctxt.dzplayer, NULL, NULL, 3000);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_player_set_crossfading_duration error\n");
       return -1;
    }

    _app_ctxt.repeat_mode = DZ_QUEUELIST_REPEAT_MODE_OFF;
    _app_ctxt.is_shuffle_mode = false;

    dzerr = dz_connect_set_access_token(_app_ctxt.dzconnect,NULL, NULL, access_token);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_connect_set_access_token error\n");
       return -1;
    }

    /* Calling dz_connect_offline_mode(FALSE) is mandatory to force the login */
    dzerr = dz_connect_offline_mode(_app_ctxt.dzconnect, NULL, NULL, false);
    if (dzerr != DZ_ERROR_NO_ERROR) {
       log_i("dz_connect_offline_mode error\n");
       return -1;
    }
    return 0;
}

void DeezerPlayer::load(const char * media_uri) {
    if (_app_ctxt.sz_content_url) {
        free(_app_ctxt.sz_content_url);
    }

    _app_ctxt.sz_content_url = strdup(media_uri);

    log_i("CHANGE => %s\n", _app_ctxt.sz_content_url);
    dz_player_load(_app_ctxt.dzplayer, NULL, NULL, _app_ctxt.sz_content_url);
}

void DeezerPlayer::play() const {
    log_i("PLAY track n° %d of => %s\n", _app_ctxt.nb_track_played, _app_ctxt.sz_content_url);
    dz_player_play(_app_ctxt.dzplayer, NULL, NULL, DZ_PLAYER_PLAY_CMD_START_TRACKLIST, DZ_INDEX_IN_QUEUELIST_CURRENT);
}

void DeezerPlayer::stop() const {
    log_i("STOP => %s\n", _app_ctxt.sz_content_url);
    dz_player_stop(_app_ctxt.dzplayer, NULL, NULL);
}

void DeezerPlayer::next() const {
    log_i("NEXT => %s\n", _app_ctxt.sz_content_url);
    dz_player_play(_app_ctxt.dzplayer, NULL, NULL, DZ_PLAYER_PLAY_CMD_START_TRACKLIST, DZ_INDEX_IN_QUEUELIST_NEXT);
}

void DeezerPlayer::previous() const {
    log_i("PREVIOUS => %s\n", _app_ctxt.sz_content_url);
    dz_player_play(_app_ctxt.dzplayer, NULL, NULL, DZ_PLAYER_PLAY_CMD_START_TRACKLIST, DZ_INDEX_IN_QUEUELIST_PREVIOUS);
}

void DeezerPlayer::pause() const {
    log_i("PAUSE track n° %d of => %s\n", _app_ctxt.nb_track_played, _app_ctxt.sz_content_url);
    dz_player_pause(_app_ctxt.dzplayer, NULL, NULL);
}

void DeezerPlayer::resume() const {
    log_i("RESUME track n° %d of => %s\n", _app_ctxt.nb_track_played, _app_ctxt.sz_content_url);
    dz_player_resume(_app_ctxt.dzplayer, NULL, NULL);
}

int DeezerPlayer::change_volume(int percent) const {
    dz_error_t dzerr = dz_player_set_output_volume(_app_ctxt.dzplayer, NULL, NULL, percent);
    if (dzerr != DZ_ERROR_NO_ERROR) {
        log_i("dz_player_set_output_volume error\n");
        return -1;
    }
    return 0;
}

void DeezerPlayer::release() {
    log_i("SHUTDOWN (1/2) - dzplayer = %p\n", _app_ctxt.dzplayer);
    if (_app_ctxt.dzplayer){
        dz_player_deactivate(_app_ctxt.dzplayer, NULL, NULL);
    }

    log_i("SHUTDOWN (2/2) - dzconnect = %p\n", _app_ctxt.dzconnect);
    if (_app_ctxt.dzconnect){
        dz_connect_deactivate(_app_ctxt.dzconnect, NULL, NULL);
    }

    if (_app_ctxt.dzplayer) {
        log_i("-- FREE PLAYER @ %p --\n", _app_ctxt.dzplayer);
        dz_object_release((dz_object_handle) _app_ctxt.dzplayer);
        _app_ctxt.dzplayer = NULL;
    }

    if (_app_ctxt.dzconnect) {
        log_i("-- FREE CONNECT @ %p --\n",_app_ctxt.dzconnect);
        dz_object_release((dz_object_handle) _app_ctxt.dzconnect);
        _app_ctxt.dzconnect = NULL;
    }

    //XXX Should we free the configuration?

    log_i("-- shutdowned --\n");

}
