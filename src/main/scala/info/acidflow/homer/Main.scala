package info.acidflow.homer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import info.acidflow.homer.communication.mqtt.MqttConfigFactory
import info.acidflow.homer.modules.SnipsClient
import info.acidflow.homer.modules.music.deezer.{DeezerSnipsModule, DeezerSnipsModuleConfigFactory}
import info.acidflow.homer.modules.timers.{TimerModuleConfigFactory, TimersSnipsModule}
import info.acidflow.homer.modules.weather.{WeatherOwmModuleConfigFactory, WeatherOwmSnipsModule}
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence

import scala.io.StdIn


object Main {

  def main(args: Array[String]): Unit = {

    val objectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
    val mqttClientConfig = MqttConfigFactory.fromResource("global/conf/mqtt.local.conf")
    val mqttClient = new MqttClient(
      mqttClientConfig.getUri.toString, generateClientId(),
      new MqttDefaultFilePersistence(mqttClientConfig.persistenceDir)
    )

    val snipsClient = new SnipsClient(mqttClient, objectMapper)

    snipsClient.start()

    val modules = Seq(
      new DeezerSnipsModule(
        snipsClient, DeezerSnipsModuleConfigFactory.fromResource("modules/conf/music_deezer.local.conf")
      ),
      new WeatherOwmSnipsModule(
        snipsClient, WeatherOwmModuleConfigFactory.fromResource("modules/conf/weather_owm.local.conf")
      ),
      new TimersSnipsModule(
        snipsClient, TimerModuleConfigFactory.fromResource("modules/conf/timers.local.conf")
      )
    )

    modules.foreach(m => m.start())

    StdIn.readLine()

    modules.foreach(m => m.stop())

    snipsClient.stop()

    System.exit(0)
  }
}
