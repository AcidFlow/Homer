package info.acidflow.homer.modules.weather

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.Constants
import info.acidflow.homer.model.{NluResult, SlotValueCustom}
import info.acidflow.homer.modules.{SnipsModule, SnipsTTS}
import info.acidflow.homer.modules.weather.network.OwmApi

import scala.util.{Failure, Success}


class WeatherOwmSnipsModule(
  val config: WeatherOwmModuleConfig = WeatherOwmModuleConfigFactory.fromResource()) extends SnipsModule
  with LazyLogging
  with SnipsTTS {

  private lazy val api = new OwmApi(config.owmBaseUrl, config.owmApiKey, config.owmUnits)

  override def getSubscriptions: Seq[String] = {
    Seq(config.intentSearchWeatherForecast)
      .map(n => Constants.Mqtt.INTENT_REGISTER_PREFIX + n)
  }

  override def handleMessage(nluResult: NluResult): Unit = {
    if (config.intentSearchWeatherForecast.equals(nluResult.intent.intentName)) {
      searchForecast(nluResult)
    } else {
      throw new IllegalArgumentException("Unknown weather forecast intent")
    }
  }

  private def searchForecast(nluResult: NluResult): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = nluResult.extractSlotMap().get("weatherForecastLocality")
      .map(s => api.getLocalityForecast(s.value.asInstanceOf[SlotValueCustom].value))
      .getOrElse(api.getForecastForId(config.owmDefaultCityId))

    future.onComplete(
      t => {
        t match {
          case Success(v) => {
            say(s"The weather forecast for ${v.name}, ${v.weather.head.description}, ${v.main.temp} degrees")
            logger.info(v.toString)
          }

          case Failure(e) => {
            logger.error("Error while getting forecast", e)
          }
        }
      }
    )
  }



}
