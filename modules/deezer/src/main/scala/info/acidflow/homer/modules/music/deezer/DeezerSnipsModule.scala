package info.acidflow.homer.modules.music.deezer

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.NluResult
import info.acidflow.homer.modules.music.deezer.api.DeezerApi
import info.acidflow.homer.modules.{SnipsClient, SnipsClientModule, SnipsClientTTSListener}


class DeezerSnipsModule(val snipsClient: SnipsClient, val moduleConf: DeezerSnipsModuleConfig)
  extends SnipsClientModule
    with SnipsClientTTSListener
    with LazyLogging {

  private val deezerIntentHandler = new DeezerIntentHandler(
    snipsClient, new DeezerApi(moduleConf.apiBaseUrl, moduleConf.userToken)
  )

  override def start(): Unit = {
    DeezerPlayer.init(
      moduleConf.appId, moduleConf.appName, moduleConf.appVersion, moduleConf.userCachePath,
      moduleConf.userToken
    )
    snipsClient.registerForTTS(this)
    Seq(
      "PlayMusicCreativeWork", "StopMusicCreativeWork", "Pause", "ResumeMusicCreativeWork", "PreviousTrack", "NextTrack"
    ).foreach(i => snipsClient.registerForIntent(i, this))
  }

  override def stop(): Unit = {
    DeezerPlayer.stop()
    DeezerPlayer.release()
  }

  override def handleIntent(nluResult: NluResult): Unit = {
    if (!DeezerPlayer.isReady) {
      snipsClient.say("Deezer player is not yet ready. Please try again in few seconds.")
    } else {
      deezerIntentHandler.handle(nluResult)
    }
  }

  override def onSayFinished(): Unit = {
    DeezerPlayer.resume()
  }

  override def onSayStart(): Unit = {
    DeezerPlayer.pause()
  }


}
