package info.acidflow.homer.modules.timers


import java.util.concurrent.{Executors, Future, ScheduledThreadPoolExecutor, TimeUnit}

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.Constants
import info.acidflow.homer.model.{NluResult, SlotValueCustom, SlotValueDuration}
import info.acidflow.homer.modules.timers.async.{TimerFinishedListener, TimerRunnable}
import info.acidflow.homer.modules.{SnipsModule, SnipsTTS}
import info.acidflow.homer.sound.SoundPlayer

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


class TimersSnipsModule(
  private[this] val conf: TimerModuleConfig = TimerModuleConfigFactory.fromResource(),
  private[this] val soundPlayer: SoundPlayer = new SoundPlayer,
  private[this] val executor: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4),
  private[this] var timerMap: Map[String, Future[_]] = Map[String, Future[_]]())
  extends SnipsModule
    with SnipsTTS
    with TimerFinishedListener
    with LazyLogging {

  override def getIntentSubscriptions: Seq[String] = {
    Seq(conf.intentStartTimer, conf.intentStopTimer)
      .map(s => Constants.Mqtt.INTENT_REGISTER_PREFIX + s)
  }

  override def handleIntent(nluResult: NluResult): Unit = {
    logger.debug("Received message : {}", nluResult)

    if (conf.intentStartTimer.equals(nluResult.intent.intentName)) {
      startTimer(nluResult)
    } else if (conf.intentStopTimer.equals(nluResult.intent.intentName)) {
      stopTimer(nluResult)
    } else {
      throw new IllegalArgumentException("Unknown timer intent")
    }
  }

  private def startTimer(nluResult: NluResult): Unit = {
    val slotTimerDuration = nluResult.extractSlotMap()("duration").value.asInstanceOf[SlotValueDuration]
    val slotTimerName = nluResult.extractSlotMap()("timerName").value.asInstanceOf[SlotValueCustom].value


    val timerDurationMs = (Duration(slotTimerDuration.days, TimeUnit.DAYS) +
      Duration(slotTimerDuration.hours, TimeUnit.HOURS) +
      Duration(slotTimerDuration.minutes, TimeUnit.MINUTES) +
      Duration(slotTimerDuration.seconds, TimeUnit.SECONDS)
      ).toMillis

    timerMap += (slotTimerName -> executor
      .schedule(new TimerRunnable(slotTimerName, this), timerDurationMs, TimeUnit.MILLISECONDS))
    logger.debug("Started timer: {}, delay : {} ms", slotTimerName, timerDurationMs)
  }

  private def stopTimer(nluResult: NluResult): Unit = {
    val slotTimerName = nluResult.extractSlotMap()("timerName").value.asInstanceOf[SlotValueCustom].value
    timerMap.get(slotTimerName)
      .map(f => f.cancel(true))
      .foreach(b => if (b) say(s"Timer $slotTimerName canceled"))

    timerMap -= slotTimerName
    logger.debug("Stopped timer: {}", slotTimerName)
  }

  override def onTimerFinished(timerName: String): Unit = {
    timerMap -= timerName
    logger.debug("Time is up for : {}", timerName)

    implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
    soundPlayer.playSoundResource(conf.soundAlarm).onComplete {
      case Success(_) => {
        say(s"Timer for $timerName is over!")
      }
      case Failure(e) => {

        logger.error("Error when playing audio file", e)
        say(s"Timer for $timerName is over!")
      }
    }
  }
}
