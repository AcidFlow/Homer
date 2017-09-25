package info.acidflow.homer.modules.timers.async

import com.typesafe.scalalogging.LazyLogging


class TimerRunnable(
  private[this] val name: String,
  private[this] val timerFinishedListener: TimerFinishedListener)
  extends Runnable
    with LazyLogging {

  override def run(): Unit = {
    timerFinishedListener.onTimerFinished(name)
  }

}
