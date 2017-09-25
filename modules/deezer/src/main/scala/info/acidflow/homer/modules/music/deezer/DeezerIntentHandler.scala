package info.acidflow.homer.modules.music.deezer

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.SnipsTTS
import info.acidflow.homer.modules.music.deezer.api.DeezerApi
import info.acidflow.homer.modules.music.deezer.jni.DeezerPlayerNative

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


class DeezerIntentHandler(deezerApi: DeezerApi) extends SnipsTTS with LazyLogging {

  private def isDeezerFlow(str: String): Boolean = {
    str.contains("flow")
  }

  private def isDeezerRadio(str: String): Boolean = {
    str.contains("radio")
  }

  def play(nluResult: NluResult): Unit = {

    nluResult.extractSlotMap().get("musicCreativeWorkName").foreach(
      slot => {
        val value = slot.rawValue
        if (isDeezerFlow(value)) {
          implicit val ec = ExecutionContext.global
          deezerApi.getMyUser().onComplete {
            case Success(user) =>
              DeezerPlayerNative.load(s"dzradio:///user-${user.id}")
            case Failure(e) =>
              logger.error("Error while getting my user", e)
          }
        } else if( isDeezerRadio(value) ){
          val removedRadio = value.replace("radio", "").trim

          implicit val ec = ExecutionContext.global
          deezerApi.getRadios().onComplete {
            case Success(radios) =>
              val genre = radios.filter(r => r.title.toLowerCase.contains(removedRadio))
              if (genre.nonEmpty){
                DeezerPlayerNative.load(s"dzradio:///radio-${genre.head.id}")
              }else{
                say(s"No radio found for genre $removedRadio")
              }
            case Failure(e) =>
              logger.error("Error while getting radios", e)
          }(ExecutionContext.global)
        }
      }
    )
  }

  def stop(): Unit = {
    DeezerPlayerNative.stop()
  }

  def next(): Unit = {
    DeezerPlayerNative.next()
  }


}
