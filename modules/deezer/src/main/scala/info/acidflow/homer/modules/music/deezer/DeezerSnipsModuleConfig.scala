package info.acidflow.homer.modules.music.deezer

case class DeezerSnipsModuleConfig(
  appId: String,
  appSecret: String,
  appName: String,
  appVersion: String,
  userCachePath: String,
  userToken: String,
  apiBaseUrl : String)
