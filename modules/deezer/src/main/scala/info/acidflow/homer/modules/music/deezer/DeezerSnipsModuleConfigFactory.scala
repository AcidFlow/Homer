package info.acidflow.homer.modules.music.deezer

import java.util.Properties

import scala.io.Source


object DeezerSnipsModuleConfigFactory {

  def fromResource(res: String = "modules/conf/music_deezer.properties"): DeezerSnipsModuleConfig = {
    val props = new Properties()
    props.load(Source.fromResource(res).reader)

    DeezerSnipsModuleConfig(
      props.getProperty("music.deezer.app_id"),
      props.getProperty("music.deezer.app_secret"),
      props.getProperty("music.deezer.app_name"),
      props.getProperty("music.deezer.app_version"),
      props.getProperty("music.deezer.path.cache"),
      props.getProperty("music.deezer.usr.token"),
      props.getProperty("music.deezer.api.base_url")
    )
  }
}
