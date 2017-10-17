package info.acidflow.homer.modules.music.deezer

import info.acidflow.homer.Constants
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.music.deezer.api.DeezerApi
import info.acidflow.homer.modules.{SnipsModule, SnipsTTS, TTSAware}


class DeezerSnipsModule(val moduleConf: DeezerSnipsModuleConfig = DeezerSnipsModuleConfigFactory.fromResource())
  extends SnipsModule
    with SnipsTTS
    with TTSAware {

  private val deezerIntentHandler = new DeezerIntentHandler(new DeezerApi(moduleConf.apiBaseUrl, moduleConf.userToken))

  override def getIntentSubscriptions: Seq[String] = {
    Seq(
      "PlayMusicCreativeWork", "StopMusicCreativeWork", "Pause", "ResumeMusicCreativeWork", "PreviousTrack", "NextTrack"
    ).map(n => Constants.Mqtt.INTENT_REGISTER_PREFIX + n)
  }

  override def handleIntent(nluResult: NluResult): Unit = {
    if (!DeezerPlayer.isReady) {
      say("Deezer player is not yet ready. Please try again in few seconds.")
      return
    }

    if ("PlayMusicCreativeWork".equals(nluResult.intent.intentName)) {
      deezerIntentHandler.play(nluResult)
    } else if ("StopMusicCreativeWork".equals(nluResult.intent.intentName)) {
      deezerIntentHandler.stop()
    } else if ("Pause".equals(nluResult.intent.intentName)) {

    } else if ("NextTrack".equals(nluResult.intent.intentName)) {
      deezerIntentHandler.next()
    } else {
      throw new IllegalArgumentException("Unknown weather forecast intent")
    }
  }

  override def handleSayFinished(): Unit = {
    DeezerPlayer.resume()
  }

  override def handleSayStart(): Unit = {
    DeezerPlayer.pause()
  }

  override def startModule(): Unit = {
    DeezerPlayer.init(
      moduleConf.appId, moduleConf.appName, moduleConf.appVersion, moduleConf.userCachePath,
      moduleConf.userToken
    )
    super.startModule()
  }

}
