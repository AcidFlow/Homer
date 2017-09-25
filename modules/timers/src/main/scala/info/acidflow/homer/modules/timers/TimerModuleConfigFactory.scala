package info.acidflow.homer.modules.timers

import java.util.Properties

import scala.io.Source


object TimerModuleConfigFactory {

  def fromResource(res: String = "conf/modules/timers.properties"): TimerModuleConfig = {
    val props = new Properties()
    props.load(Source.fromResource(res).reader)

    TimerModuleConfig(
      props.getProperty("intent.start_timer"),
      props.getProperty("intent.stop_timer"),
      props.getProperty("sound.alarm")
    )
  }

}
