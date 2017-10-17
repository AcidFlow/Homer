package info.acidflow.homer.modules.music.deezer

import com.typesafe.config.ConfigFactory


object DeezerSnipsModuleConfigFactory {

  def fromResource(res: String = "modules/conf/music_deezer.conf"): DeezerSnipsModuleConfig = {
    val conf = ConfigFactory.load(res)
    DeezerSnipsModuleConfig(
      conf.getString("music.deezer.app.id"),
      conf.getString("music.deezer.app.secret"),
      conf.getString("music.deezer.app.name"),
      conf.getString("music.deezer.app.version"),
      conf.getString("music.deezer.cache.path"),
      conf.getString("music.deezer.user.token"),
      conf.getString("music.deezer.api.base_url")
    )
  }
}
