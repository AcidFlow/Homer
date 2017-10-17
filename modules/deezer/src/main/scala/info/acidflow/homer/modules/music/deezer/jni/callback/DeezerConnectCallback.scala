package info.acidflow.homer.modules.music.deezer.jni.callback

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.modules.music.deezer.DeezerPlayer


object DeezerConnectCallback extends LazyLogging {

  def onConnectEvent(event: Int): Unit = {
    logger.info("Scala connect callback called with event : {}", ConnectEvent.values.toArray.apply(event))
    val eventScala = ConnectEvent.values.toArray.apply(event)
    eventScala match {
      case ConnectEvent.DZ_CONNECT_EVENT_USER_LOGIN_OK => DeezerPlayer.ready()
      case _ =>
    }
  }


  object ConnectEvent extends Enumeration {

    type PlayerEvent = Value

    val

    /** < Connect event has not been set yet, not a valid value. */
    DZ_CONNECT_EVENT_UNKNOWN,

    /** < User logged in, and credentials from offline store are loaded. */
    DZ_CONNECT_EVENT_USER_OFFLINE_AVAILABLE,


    /** < (Not available) dz_connect_login_with_email() ok, and access_token is available */
    DZ_CONNECT_EVENT_USER_ACCESS_TOKEN_OK,

    /** < (Not available) dz_connect_login_with_email() failed */
    DZ_CONNECT_EVENT_USER_ACCESS_TOKEN_FAILED,


    /** < Login with access_token ok, infos from user available. */
    DZ_CONNECT_EVENT_USER_LOGIN_OK,

    /** < Login with access_token failed because of network condition. */
    DZ_CONNECT_EVENT_USER_LOGIN_FAIL_NETWORK_ERROR,

    /** < Login with access_token failed because of bad credentials. */
    DZ_CONNECT_EVENT_USER_LOGIN_FAIL_BAD_CREDENTIALS,

    /** < Login with access_token failed because of other problem. */
    DZ_CONNECT_EVENT_USER_LOGIN_FAIL_USER_INFO,

    /** < Login with access_token failed because we are in forced offline mode. */
    DZ_CONNECT_EVENT_USER_LOGIN_FAIL_OFFLINE_MODE,


    /** < User options have just changed. */
    DZ_CONNECT_EVENT_USER_NEW_OPTIONS,


    /** < A new advertisement needs to be displayed. */
    DZ_CONNECT_EVENT_ADVERTISEMENT_START,

    /** < An advertisement needs to be stopped. */
    DZ_CONNECT_EVENT_ADVERTISEMENT_STOP

    = Value
  }


}
