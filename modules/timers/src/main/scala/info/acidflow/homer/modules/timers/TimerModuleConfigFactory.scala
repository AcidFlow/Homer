package info.acidflow.homer.modules.timers

import com.typesafe.config.ConfigFactory


object TimerModuleConfigFactory {

  def fromResource(res: String = "conf/modules/timers.conf"): TimerModuleConfig = {
    val conf = ConfigFactory.load(res)

    TimerModuleConfig(
      conf.getString("timers.intents.start_timer"),
      conf.getString("timers.intents.stop_timer"),
      conf.getString("timers.sound.alarm")
    )
  }

}
