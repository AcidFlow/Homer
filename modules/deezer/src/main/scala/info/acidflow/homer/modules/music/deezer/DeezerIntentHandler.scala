package info.acidflow.homer.modules.music.deezer

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.SnipsClient
import info.acidflow.homer.modules.music.deezer.api.DeezerApi

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.{Failure, Success}


class DeezerIntentHandler(snipsClient: SnipsClient, deezerApi: DeezerApi) extends LazyLogging {

  private val slotArtistName = "musicCreativeWorkArtistName"
  private val slotCreativeWorkName = "musicCreativeWorkName"

  private val apiExecutor = ExecutionContext.global

  def handle(nluResult: NluResult): Unit = {
    if ("PlayMusicCreativeWork".equals(nluResult.intent.intentName)) {
      play(nluResult)
    } else if ("StopMusicCreativeWork".equals(nluResult.intent.intentName)) {
      stop()
    } else if ("Pause".equals(nluResult.intent.intentName)) {
      pause()
    } else if ("NextTrack".equals(nluResult.intent.intentName)) {
      next()
    } else {
      throw new IllegalArgumentException("Unknown deezer intent")
    }
  }

  def play(nluResult: NluResult): Unit = {

    if (isArtist(nluResult)) {
      nluResult.extractSlotMap().get(slotArtistName) match {
        case Some(slot) => playTopArtist(slot.rawValue)
        case None => logger.error("No artist slot")
      }
    } else {
      nluResult.extractSlotMap().get(slotCreativeWorkName) match {
        case Some(slot) =>
          val value = slot.rawValue
          if (isDeezerFlow(value)) {
            playDeezerFlow()
          } else if (isDeezerRadio(value)) {
            playDeezerRadio(value)
          }
        case None => logger.error("No creative work name slot")
      }
    }
  }

  private def playTopArtist(artist: String): Unit = {
    implicit val ec: ExecutionContextExecutor = apiExecutor
    deezerApi.searchArtist(artist)
      .flatMap(a => deezerApi.getTopForArtist(a.id))
      .onComplete {
        case Success(t) => DeezerPlayer.playTrackList(t)
        case Failure(t) => logger.error("Error", t)
      }
  }

  private def playDeezerRadio(genre: String): Unit = {
    implicit val ec: ExecutionContextExecutor = apiExecutor
    val removedRadio = genre.replace("radio", "").trim
    deezerApi.getRadios()
      .onComplete {
        case Success(radios) =>
          val genre = radios.filter(r => r.title.toLowerCase.contains(removedRadio))
          if (genre.nonEmpty) {
            DeezerPlayer.playRadio(genre.head.id)
          } else {
            snipsClient.say(s"No radio found for genre $removedRadio")
          }
        case Failure(e) =>
          logger.error("Error while getting radios", e)
      }
  }

  private def playDeezerFlow(): Unit = {
    implicit val ec: ExecutionContextExecutor = apiExecutor
    deezerApi.getMyUser()
      .onComplete {
        case Success(user) => DeezerPlayer.playFlow(user.id)
        case Failure(e) => logger.error("Error while getting my user", e)
      }
  }

  private def isDeezerFlow(str: String): Boolean = {
    str.contains("flow")
  }

  private def isDeezerRadio(str: String): Boolean = {
    str.contains("radio")
  }

  private def isArtist(nluResult: NluResult): Boolean = {
    val slots = nluResult.extractSlotMap()
    slots.contains(slotArtistName)
  }

  def stop(): Unit = {
    DeezerPlayer.stop()
  }

  def pause(): Unit = {
    DeezerPlayer.pause()
  }

  def next(): Unit = {
    DeezerPlayer.playNext()
  }

  def resume(): Unit = {
    DeezerPlayer.resume()
  }


}
