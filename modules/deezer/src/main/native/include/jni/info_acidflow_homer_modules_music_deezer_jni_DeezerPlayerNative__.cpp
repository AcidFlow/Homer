#include <cstring>

#include "info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative__.hpp"
#include "JNIUtils.hpp"
#include "../player/DeezerPlayer.hpp"

// TODO Refactoring of the app context to an object?!
static DeezerPlayer* sPlayer = NULL;


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_init
  (JNIEnv * env, jobject jobj, jstring japp_id, jstring jproduct_id, jstring jproduct_build, jstring jcache_path,
    jstring jaccess_token) {

    struct dz_connect_configuration config;
    memset(&config, 0, sizeof(struct dz_connect_configuration));

    config.app_id            = strdup(JNIUtils::extractJniString(env, japp_id).c_str());      // SET YOUR APPLICATION ID
    config.product_id        = strdup(JNIUtils::extractJniString(env, jproduct_id).c_str());    // SET YOUR APPLICATION NAME
    config.product_build_id  = strdup(JNIUtils::extractJniString(env, jproduct_build).c_str()); // SET YOUR APPLICATION VERSION
    config.user_profile_path = strdup(JNIUtils::extractJniString(env, jcache_path).c_str());          // SET THE USER CACHE PATH
    config.connect_event_cb  = NULL;

    std::string access_token = JNIUtils::extractJniString(env, jaccess_token);
    sPlayer = new DeezerPlayer(config);
    sPlayer->initialize(access_token.c_str());

}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_load
  (JNIEnv * env, jobject jobj, jstring jmedia_uri){
    std::string media_uri = JNIUtils::extractJniString(env, jmedia_uri);
    sPlayer->load(media_uri.c_str());
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_play
  (JNIEnv * env, jobject jobj){
    sPlayer->play();
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_stop
  (JNIEnv * env, jobject jobj){
    sPlayer->stop();
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_pause
  (JNIEnv * env, jobject jobj){
    sPlayer->pause();
}

JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_resume
  (JNIEnv * env, jobject jobj){
    sPlayer->resume();
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_next
  (JNIEnv * env, jobject jobj){
    sPlayer->next();
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_previous
  (JNIEnv * env, jobject jobj){
    sPlayer->previous();
}


JNIEXPORT void JNICALL Java_info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative_00024_release
  (JNIEnv * env, jobject jobj){
    sPlayer->release();

    delete sPlayer;
    sPlayer = NULL;
}
