package info.acidflow.homer.modules.weather.model

import com.fasterxml.jackson.annotation.JsonProperty


case class OwmForecast(
  coord: OwmCoordinates,
  weather: List[OwmWeather],
  base: String, main: OwmMain,
  visibility: Int,
  wind: OwmWind,
  clouds: OwmClouds,
  dt: Long,
  sys: OwmSys,
  id: Int,
  name: String,
  cod: Int)


case class OwmCoordinates(lon: Double, lat: Double)


case class OwmWeather(id: Int, main: String, description: String, icon: String)


case class OwmMain(temp: Double, pressure: Int, humidity: Int, temp_min: Double, temp_max: Double)


case class OwmWind(speed: Double, deg: Double)


case class OwmClouds(all: Int)


case class OwmSys(@JsonProperty("type") t: Int, id: Int, message: Double, country: String, sunrise: Long, sunset: Long)
