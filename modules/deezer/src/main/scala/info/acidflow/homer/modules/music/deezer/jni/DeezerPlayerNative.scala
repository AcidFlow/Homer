package info.acidflow.homer.modules.music.deezer.jni

import ch.jodersky.jni.nativeLoader


@nativeLoader("deezer")
@nativeLoader("deezerplayer0")
object DeezerPlayerNative {

  @native def init(appId: String, productId: String, productBuild: String, cachePath: String, accessToken: String): Unit

  @native def load(mediaUri: String): Unit

  @native def play(): Unit

  @native def stop(): Unit

  @native def pause(): Unit

  @native def resume(): Unit

  @native def next(): Unit

  @native def previous(): Unit

  @native def changeVolume(percent: Int): Int

  @native def release(): Unit

}
