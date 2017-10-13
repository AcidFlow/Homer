package info.acidflow.homer.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.Constants
import info.acidflow.homer.communication.mqtt.MqttConfigFactory
import info.acidflow.homer.model.SnipsSayText
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttMessage}


trait SnipsTTS extends LazyLogging {

  private[this] val ttsObjectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private[this] val ttsMqttConfig = MqttConfigFactory.fromResource("global/conf/mqtt.local.properties")
  private[this] val ttsMqttClient = new MqttClient(
    ttsMqttConfig.getUri.toString, generateClientId(), new MqttDefaultFilePersistence(ttsMqttConfig.persistenceDir)
  )
  ttsMqttClient.connect()

  final def say(text: String): Unit = {
    logger.debug("Sending TTS input : {}", text)
    ttsMqttClient
      .publish(Constants.Mqtt.TTS_SAY, new MqttMessage(ttsObjectMapper.writeValueAsBytes(SnipsSayText(text))))
  }

}
