package info.acidflow.homer.modules.weather

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.model.{NluResult, SlotValueCustom}
import info.acidflow.homer.modules.weather.network.OwmApi
import info.acidflow.homer.modules.{SnipsClient, SnipsClientModule}

import scala.util.{Failure, Success}


class WeatherOwmSnipsModule(val snipsClient: SnipsClient, config: WeatherOwmModuleConfig)
  extends SnipsClientModule
    with LazyLogging {

  private val owmApi = new OwmApi(config.owmBaseUrl, config.owmApiKey, config.owmUnits)

  override def start(): Unit = {
    snipsClient.registerForIntent("SearchWeatherForecast", this)
  }

  override def handleIntent(nluResult: NluResult): Unit = {
    nluResult.intent.intentName match {
      case "SearchWeatherForecast" => searchForecast(nluResult)
      case _ => throw new IllegalArgumentException("Unknown weather forecast intent")
    }
  }

  private def searchForecast(nluResult: NluResult): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = nluResult.extractSlotMap().get("weatherForecastLocality")
      .map(s => owmApi.getLocalityForecast(s.value.asInstanceOf[SlotValueCustom].value))
      .getOrElse(owmApi.getForecastForId(config.owmDefaultCityId))

    future.onComplete {
      case Success(v) =>
        logger.info(v.toString)
        snipsClient.say(s"The weather forecast for ${v.name}, ${v.weather.head.description}, ${v.main.temp} degrees")

      case Failure(e) =>
        logger.error("Error while getting forecast", e)
    }
  }


}
