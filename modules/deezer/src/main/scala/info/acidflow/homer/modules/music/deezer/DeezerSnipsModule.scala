package info.acidflow.homer.modules.music.deezer

import info.acidflow.homer.Constants
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.music.deezer.api.DeezerApi
import info.acidflow.homer.modules.music.deezer.jni.DeezerPlayerNative
import info.acidflow.homer.modules.music.deezer.jni.callback.DeezerConnectCallback
import info.acidflow.homer.modules.{SnipsModule, SnipsTTS}


class DeezerSnipsModule(
  val moduleConf: DeezerSnipsModuleConfig = DeezerSnipsModuleConfigFactory.fromResource()) extends SnipsModule
  with SnipsTTS {

  private var isPlayerReady = false
  private val deezerIntentHandler = new DeezerIntentHandler(new DeezerApi(moduleConf.apiBaseUrl, moduleConf.userToken))
  DeezerConnectCallback.deezerSnipsModule = this

  override def getSubscriptions: Seq[String] = {
    Seq(
      "PlayMusicCreativeWork", "StopMusicCreativeWork", "Pause", "ResumeMusicCreativeWork", "PreviousTrack", "NextTrack"
    )
      .map(n => Constants.Mqtt.INTENT_REGISTER_PREFIX + n)
  }

  override def handleMessage(nluResult: NluResult): Unit = {
    if (!isPlayerReady) {
      say("Deezer player is not yet ready. Please try again in few seconds.")
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


  override def start(): Unit = {
    DeezerPlayerNative
      .init(moduleConf.appId, moduleConf.appName, moduleConf.appVersion, moduleConf.userCachePath, moduleConf.userToken)
    super.start()
  }

  def playerReady(): Unit = {
    logger.info("Deezer player is ready!")
    isPlayerReady = true
  }

}
