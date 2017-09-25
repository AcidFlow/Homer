package info.acidflow.homer.sound

import java.io.BufferedInputStream
import javax.sound.sampled.{AudioSystem, Clip, FloatControl, Line}

import scala.concurrent.{ExecutionContext, Future}

class SoundPlayer {

  def playSoundResource(resourcePath: String)(implicit ec : ExecutionContext) = Future {
    val resAsStream = Thread.currentThread().getContextClassLoader.getResourceAsStream(resourcePath)
    val audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(resAsStream))
    val clip = AudioSystem.getLine(new Line.Info(classOf[Clip])).asInstanceOf[Clip]
    clip.open(audioInputStream)
    val floatGainControl = clip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
    floatGainControl.setValue(0) //reduce volume by x decibels (like -10f or -20f)
    clip.loop(3)
  }

}
