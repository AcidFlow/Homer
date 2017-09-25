package info.acidflow.homer.modules.timers.async

trait TimerFinishedListener {
  def onTimerFinished(timerName : String)
}
