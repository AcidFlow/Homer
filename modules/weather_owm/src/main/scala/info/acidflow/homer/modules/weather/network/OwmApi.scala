package info.acidflow.homer.modules.weather.network

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import info.acidflow.homer.modules.weather.model.OwmForecast
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

import scala.concurrent.{ExecutionContext, Future}


class OwmApi(private[this] var baseUrl: String, private[this] val apiKey: String, private[this] val units: String) {

  private[this] val service = new Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper().registerModule(DefaultScalaModule)))
    .build()
    .create(classOf[OwmService])

  def getLocalityForecast(city: String)(implicit ec: ExecutionContext): Future[OwmForecast] = {
    Future {
      service.getForecastForCity(apiKey, units, city).execute().body()
    }
  }

  def getForecastForId(cityId: String)(implicit ec: ExecutionContext): Future[OwmForecast] = {
    Future {
      service.getForecastForCityId(apiKey, units, cityId).execute().body()
    }
  }

}
