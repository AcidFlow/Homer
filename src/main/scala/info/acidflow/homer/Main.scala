package info.acidflow.homer

import info.acidflow.homer.modules.timers.{TimerModuleConfigFactory, TimersSnipsModule}
import info.acidflow.homer.modules.weather.{WeatherOwmModuleConfigFactory, WeatherOwmSnipsModule}

object Main {

  def main(args: Array[String]): Unit = {

    val timerModule = new TimersSnipsModule(
      TimerModuleConfigFactory.fromResource("modules/conf/timers.local.properties")
    )

    val weatherModule = new WeatherOwmSnipsModule(
      WeatherOwmModuleConfigFactory.fromResource("modules/conf/weather_owm.local.properties")
    )

    val modules = List(timerModule, weatherModule)
    modules.foreach(m => m.start())
  }
}