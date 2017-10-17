package info.acidflow.homer.modules.music.deezer.jni.callback

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.modules.music.deezer.DeezerPlayer


object DeezerPlayerCallback extends LazyLogging {


  def onPlayerEvent(event: Int): Unit = {
    val eventScala = PlayerEvent.values.toArray.apply(event)
    logger.info("Scala player callback called with event : {}", eventScala)
    eventScala match {
      case PlayerEvent.DZ_PLAYER_EVENT_QUEUELIST_LOADED => DeezerPlayer.play()
      case PlayerEvent.DZ_PLAYER_EVENT_QUEUELIST_NEED_NATURAL_NEXT => DeezerPlayer.playNaturalNext()
      case _ =>
    }

  }


  object PlayerEvent extends Enumeration {

    type PlayerEvent = Value

    val

    /** Player event has not been set yet, not a valid value. */
    DZ_PLAYER_EVENT_UNKNOWN,

    // Data access related event.
    /** Another deezer player session was created elsewhere, the player has entered pause mode. */
    DZ_PLAYER_EVENT_LIMITATION_FORCED_PAUSE,

    // Track selection related event.
    /** Content has been loaded. */
    DZ_PLAYER_EVENT_QUEUELIST_LOADED,

    /** You don't have the right to play this content: track, album or playlist */
    DZ_PLAYER_EVENT_QUEUELIST_NO_RIGHT,

    /** You're offline, the track is not available. */
    DZ_PLAYER_EVENT_QUEUELIST_TRACK_NOT_AVAILABLE_OFFLINE,

    /** You have right to play it, but you should render an ads first :
      * - Use dz_player_event_get_advertisement_infos_json().
      *  - Play an ad with dz_player_play_audioads().
      *  - Wait for #DZ_PLAYER_EVENT_RENDER_TRACK_END.
      *  - Use dz_player_play() with previous track or DZ_PLAYER_PLAY_CMD_RESUMED_AFTER_ADS (to be done even on mixes for now).
      */
    DZ_PLAYER_EVENT_QUEUELIST_TRACK_RIGHTS_AFTER_AUDIOADS,

    /** < You're on a mix, and you had no right to do skip. */
    DZ_PLAYER_EVENT_QUEUELIST_SKIP_NO_RIGHT,

    /** < A track is selected among the ones available on the server, and will be fetched and read. */
    DZ_PLAYER_EVENT_QUEUELIST_TRACK_SELECTED,

    /** < We need a new natural_next action. */
    DZ_PLAYER_EVENT_QUEUELIST_NEED_NATURAL_NEXT,

    // Data loading related event.
    /** < Data is ready to be introduced into audio output (first data after a play). */
    DZ_PLAYER_EVENT_MEDIASTREAM_DATA_READY,

    /** < Data is ready to be introduced into audio output (first data after a seek). */
    DZ_PLAYER_EVENT_MEDIASTREAM_DATA_READY_AFTER_SEEK,

    // Play (audio rendering on output) related event.
    /** < Error, track is unable to play. */
    DZ_PLAYER_EVENT_RENDER_TRACK_START_FAILURE,

    /** < A track has started to play. */
    DZ_PLAYER_EVENT_RENDER_TRACK_START,

    /** < A track has stopped because the stream has ended. */
    DZ_PLAYER_EVENT_RENDER_TRACK_END,

    /** < Currently on paused. */
    DZ_PLAYER_EVENT_RENDER_TRACK_PAUSED,

    /** < Waiting for new data on seek. */
    DZ_PLAYER_EVENT_RENDER_TRACK_SEEKING,

    /** < Underflow happened whilst playing a track. */
    DZ_PLAYER_EVENT_RENDER_TRACK_UNDERFLOW,

    /** < Player resumed play after a underflow or a pause. */
    DZ_PLAYER_EVENT_RENDER_TRACK_RESUMED,

    /** < Player stopped playing a track. */
    DZ_PLAYER_EVENT_RENDER_TRACK_REMOVED
    = Value
  }


}
