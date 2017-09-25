package info.acidflow.homer.modules.weather.network

import info.acidflow.homer.modules.weather.model.OwmForecast
import retrofit2.Call
import retrofit2.http.{GET, Query}

trait OwmService {

  @GET("/data/2.5/weather")
  def getForecastForCity(@Query("APPID") apiKey : String, @Query("units") units : String, @Query("q") city : String) : Call[OwmForecast]

  @GET("/data/2.5/weather")
  def getForecastForCityId(@Query("APPID") apiKey : String, @Query("units") units : String, @Query("id") cityId : String) : Call[OwmForecast]

}
