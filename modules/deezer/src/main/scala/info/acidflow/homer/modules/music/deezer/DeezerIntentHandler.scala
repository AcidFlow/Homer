package info.acidflow.homer.modules.music.deezer

import java.util.concurrent.{ExecutorService, Executors}

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.SnipsTTS
import info.acidflow.homer.modules.music.deezer.api.DeezerApi

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.util.{Failure, Success}


class DeezerIntentHandler(deezerApi: DeezerApi) extends SnipsTTS with LazyLogging {

  private val slotArtistName = "musicCreativeWorkArtistName"
  private val slotCreativeWorkName = "musicCreativeWorkName"

  private val apiThreadPool: ExecutorService = Executors.newFixedThreadPool(4)
  private val apiExecutor = ExecutionContext.fromExecutor(apiThreadPool)

  def play(nluResult: NluResult): Unit = {

    if (isArtist(nluResult)) {
      nluResult.extractSlotMap().get(slotArtistName).foreach(
        slot => {
          implicit val ec: ExecutionContextExecutor = apiExecutor
          val artist = slot.rawValue

          deezerApi.searchArtist(artist)
            .flatMap(a => deezerApi.getTopForArtist(a.id))
            .onComplete {
              case Success(t) => DeezerPlayer.playTrackList(t)
              case Failure(t) => logger.error("Error", t)
            }
        }
      )
    } else {
      nluResult.extractSlotMap().get(slotCreativeWorkName).foreach(
        slot => {
          val value = slot.rawValue
          if (isDeezerFlow(value)) {
            implicit val ec: ExecutionContextExecutor = apiExecutor
            deezerApi.getMyUser()
              .onComplete {
                case Success(user) => DeezerPlayer.playFlow(user.id)
                case Failure(e) => logger.error("Error while getting my user", e)
              }
          } else if (isDeezerRadio(value)) {
            val removedRadio = value.replace("radio", "").trim

            implicit val ec: ExecutionContextExecutor = apiExecutor
            deezerApi.getRadios()
              .onComplete {
                case Success(radios) =>
                  val genre = radios.filter(r => r.title.toLowerCase.contains(removedRadio))
                  if (genre.nonEmpty) {
                    DeezerPlayer.playRadio(genre.head.id)
                  } else {
                    say(s"No radio found for genre $removedRadio")
                  }
                case Failure(e) =>
                  logger.error("Error while getting radios", e)
              }
          }
        }
      )
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

  def next(): Unit = {
    DeezerPlayer.playNext()
  }


}
