package info.acidflow.homer

import info.acidflow.homer.modules.music.deezer.{DeezerSnipsModule, DeezerSnipsModuleConfigFactory}
import info.acidflow.homer.modules.timers.{TimerModuleConfigFactory, TimersSnipsModule}
import info.acidflow.homer.modules.weather.{WeatherOwmModuleConfigFactory, WeatherOwmSnipsModule}

import scala.io.StdIn


object Main {

  def main(args: Array[String]): Unit = {

    val timerModule = new TimersSnipsModule(
      TimerModuleConfigFactory.fromResource("modules/conf/timers.local.properties")
    )

    val weatherModule = new WeatherOwmSnipsModule(
      WeatherOwmModuleConfigFactory.fromResource("modules/conf/weather_owm.local.properties")
    )

    val deezerModule = new DeezerSnipsModule(
      DeezerSnipsModuleConfigFactory.fromResource("modules/conf/music_deezer.local.properties")
    )

    val modules = List(deezerModule, weatherModule, timerModule)
    modules.foreach(m => m.start())

    StdIn.readLine()
    
  }
}
