package info.acidflow.homer.modules.music.deezer

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.modules.music.deezer.api.DeezerApi
import info.acidflow.homer.modules.music.deezer.jni.DeezerPlayerNative

import scala.io.{Source, StdIn}
import scala.reflect.io.Directory
import scala.util.{Failure, Success}


object MainTmp extends App with LazyLogging {
  val cache = Directory.makeTemp("deezer")
  println(s"Cache dir : ${cache.toAbsolute.toString()}")
  val conf = DeezerSnipsModuleConfigFactory.fromResource("modules/conf/music_deezer.local.properties")
  DeezerPlayerNative.init(conf.appId, conf.appName, conf.appVersion, cache.toAbsolute.toString(), conf.userToken)
  val deezerApi = new DeezerApi("https://api.deezer.com", conf.userToken)
  var c : String = StdIn.readLine()
  while(c != "Q" ){
    c match {
      case "+" => DeezerPlayerNative.next()
      case "-" => DeezerPlayerNative.previous()
      case "P" => DeezerPlayerNative.play()
      case "S" => DeezerPlayerNative.stop()
      case "p" => DeezerPlayerNative.pause()
      case "r" => DeezerPlayerNative.resume()
      case "l" =>
        val media = StdIn.readLine()
        DeezerPlayerNative.load(media)
      case "f" =>
        import scala.concurrent.ExecutionContext.Implicits.global
        deezerApi.getMyUser().onComplete(t => t match {
        case Success(u) =>
          logger.info("Loading : {}", s"dzradio:///user-${u.id}")
          DeezerPlayerNative.load(s"dzradio:///user-${u.id}")
        case Failure(e) => logger.error("Error while query user", e)
      })
      case "radio" =>
        val style = StdIn.readLine()
        import scala.concurrent.ExecutionContext.Implicits.global
        deezerApi.getRadios().onComplete(t => t match {
          case Success(radios) =>
            val styleMatch = radios.filter(r => r.title.toLowerCase.contains(style))
            if (styleMatch.nonEmpty){
              logger.info("Loading : {}", s"dzradio:///radio-${styleMatch.head.id}")
              DeezerPlayerNative.load(s"dzradio:///radio-${styleMatch.head.id}")
            } else {
              logger.error(s"No radio found for style $style")
            }
          case Failure(e) => logger.error("Error while getting radios", e)
        })
      case _ =>
    }
    c = StdIn.readLine()
  }
  DeezerPlayerNative.stop()
  DeezerPlayerNative.release()
  System.exit(0)
}
