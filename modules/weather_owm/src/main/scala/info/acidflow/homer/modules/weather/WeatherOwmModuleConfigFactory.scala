package info.acidflow.homer.modules.weather

import com.typesafe.config.ConfigFactory


object WeatherOwmModuleConfigFactory {

  def fromResource(res: String = "modules/conf/weather_owm.conf"): WeatherOwmModuleConfig = {
    val conf = ConfigFactory.load(res)

    WeatherOwmModuleConfig(
      conf.getString("weather.owm.api.base_url"),
      conf.getString("weather.owm.api.key"),
      conf.getString("weather.owm.defaults.units"),
      conf.getString("weather.owm.defaults.location")
    )
  }

}
