package info.acidflow.homer

import info.acidflow.homer.modules.music.deezer.{DeezerSnipsModule, DeezerSnipsModuleConfigFactory}
import info.acidflow.homer.modules.timers.{TimerModuleConfigFactory, TimersSnipsModule}
import info.acidflow.homer.modules.weather.{WeatherOwmModuleConfigFactory, WeatherOwmSnipsModule}


object Main {

  def main(args: Array[String]): Unit = {

    val timerModule = new TimersSnipsModule(
      TimerModuleConfigFactory.fromResource("modules/conf/timers.local.conf")
    )

    val weatherModule = new WeatherOwmSnipsModule(
      WeatherOwmModuleConfigFactory.fromResource("modules/conf/weather_owm.local.conf")
    )

    val deezerModule = new DeezerSnipsModule(
      DeezerSnipsModuleConfigFactory.fromResource("modules/conf/music_deezer.local.conf")
    )

    val modules = List(weatherModule, timerModule, deezerModule)

    modules.foreach(m => m.startModule())
  }
}
