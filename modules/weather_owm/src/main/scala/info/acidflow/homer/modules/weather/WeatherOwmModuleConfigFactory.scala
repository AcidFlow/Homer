package info.acidflow.homer.modules.weather

import java.util.Properties

import scala.io.Source

object WeatherOwmModuleConfigFactory {

  def fromResource(res : String = "modules/conf/weather_owm.properties") : WeatherOwmModuleConfig = {
    val props = new Properties()
    props.load(Source.fromResource(res).reader)

    WeatherOwmModuleConfig(
      props.getProperty("intent.search_weather_forecast"),
      props.getProperty("owm.base_url"),
      props.getProperty("owm.api_key"),
      props.getProperty("owm.units"),
      props.getProperty("owm.default.location")
    )
  }

}
