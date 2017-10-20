package info.acidflow.homer.modules.timers


import java.util.concurrent.{Future, ScheduledThreadPoolExecutor, TimeUnit}

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.{NluResult, SlotValueCustom, SlotValueDuration}
import info.acidflow.homer.modules.timers.async.{TimerFinishedListener, TimerRunnable}
import info.acidflow.homer.modules.{SnipsClient, SnipsClientModule}
import info.acidflow.homer.sound.SoundPlayer

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


class TimersSnipsModule(
  private val snipsClient: SnipsClient,
  private val conf: TimerModuleConfig = TimerModuleConfigFactory.fromResource(),
  private val soundPlayer: SoundPlayer = new SoundPlayer,
  private val executor: ScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4),
  private val timerMap: mutable.Map[String, Future[_]] = mutable.Map[String, Future[_]]())
  extends SnipsClientModule
    with TimerFinishedListener
    with LazyLogging {


  override def start(): Unit = {
    Seq(conf.intentStartTimer, conf.intentStopTimer).foreach(i => snipsClient.registerForIntent(i, this))
    executor.setRemoveOnCancelPolicy(true)
  }


  override def stop(): Unit = {
    executor.shutdownNow()
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
    timerMap.remove(slotTimerName) match {
      case Some(f) => if (f.cancel(false)) logger.debug("Timer {} stopped", slotTimerName)
      case None => logger.debug("No timer with key {}", slotTimerName)
    }
  }

  override def onTimerFinished(timerName: String): Unit = {
    timerMap -= timerName
    logger.debug("Time is up for : {}", timerName)

    implicit val ec: ExecutionContext = ExecutionContext.global
    soundPlayer.playSoundResource(conf.soundAlarm).onComplete {
      case Success(_) =>
        snipsClient.say(s"Timer for $timerName is over!")

      case Failure(e) =>
        logger.error("Error when playing audio file", e)
        snipsClient.say(s"Timer for $timerName is over!")
    }
  }
}
