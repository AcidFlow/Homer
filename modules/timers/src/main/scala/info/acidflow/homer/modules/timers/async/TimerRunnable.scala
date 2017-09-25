package info.acidflow.homer.modules.timers.async

import com.typesafe.scalalogging.LazyLogging

class TimerRunnable(val name: String, timerFinishedListener: TimerFinishedListener)
  extends Runnable with LazyLogging {

  override def run(): Unit = {
    timerFinishedListener.onTimerFinished(name)
  }
}
