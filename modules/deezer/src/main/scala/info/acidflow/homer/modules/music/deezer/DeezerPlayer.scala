package info.acidflow.homer.modules.music.deezer

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.modules.music.deezer.api.model.Track
import info.acidflow.homer.modules.music.deezer.jni.DeezerPlayerNative

import scala.collection.mutable


object DeezerPlayer extends LazyLogging {


  private val deezerFlowPrefix = "dzradio:///user-"
  private val deezerRadioPrefix = "dzradio:///radio-"
  private val trackList = mutable.ListBuffer.empty[Track]
  private var _isReady = false
  private var _playMode = MediaTypePlaying.NONE

  def init(appId: String, productId: String, productBuild: String, cachePath: String, accessToken: String): Unit = {
    DeezerPlayerNative.init(appId, productId, productBuild, cachePath, accessToken)
  }

  def playFlow(userId: String): Unit = {
    if (!_isReady) return

    _playMode = MediaTypePlaying.FLOW
    trackList.clear()
    DeezerPlayerNative.load(s"$deezerFlowPrefix$userId")
  }

  def playRadio(radioId: String): Unit = {
    if (!_isReady) return

    _playMode = MediaTypePlaying.RADIO
    trackList.clear()
    DeezerPlayerNative.load(s"$deezerRadioPrefix$radioId")
  }

  def playTrackList(list: List[Track]): Unit = {
    _playMode = MediaTypePlaying.TRACKLIST
    trackList.clear()
    trackList ++= list
    playNaturalNext()
  }

  def playNext(): Unit = {
    if (!_isReady) return

    if (!requireNextLoad()) {
      DeezerPlayerNative.next()
    } else {
      playNaturalNext()
    }
  }

  def playNaturalNext(): Unit = {
    if (!_isReady) return

    if (trackList.isEmpty) {
      logger.info("No more track to play")
      return
    }

    val t = trackList.remove(0)
    DeezerPlayerNative.load(t.deezerUri)
  }

  private def requireNextLoad(): Boolean = {
    _playMode == MediaTypePlaying.TRACKLIST
  }

  def playPrevious(): Unit = {
    if (!_isReady) return

    DeezerPlayerNative.previous()
  }

  def pause(): Unit = {
    if (!_isReady) return

    DeezerPlayerNative.pause()
  }

  def resume(): Unit = {
    if (!_isReady) return
    DeezerPlayerNative.resume()
  }

  def play(): Unit = {
    if (!_isReady) return

    DeezerPlayerNative.play()
  }

  def stop(): Unit = {
    if (!_isReady) return

    DeezerPlayerNative.stop()
  }

  def ready(): Unit = {
    logger.info("Deezer player is ready!")
    _isReady = true
  }

  def isReady: Boolean = _isReady


  object MediaTypePlaying extends Enumeration {

    type MediaTypePlaying = Value
    val NONE, RADIO, FLOW, TRACKLIST = Value
  }
}
