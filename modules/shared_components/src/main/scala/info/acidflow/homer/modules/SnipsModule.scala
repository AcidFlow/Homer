package info.acidflow.homer.modules

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.communication.mqtt.MqttConfigFactory
import info.acidflow.homer.model.NluResult
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttClient, MqttMessage}


trait SnipsModule extends LazyLogging {

  private[this] val intentObjectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private[this] val intentMqttConfig = MqttConfigFactory.fromResource("global/conf/mqtt.local.properties")
  private[this] val intentMqttClient = new MqttClient(
    intentMqttConfig.getUri.toString, generateClientId(),
    new MqttDefaultFilePersistence(intentMqttConfig.persistenceDir)
  )
  private[this] val intentMqttCallback = new IntentMqttCallback()

  def getIntentSubscriptions: Seq[String]

  def handleIntent(nluResult: NluResult)

  def handleSayFinished(): Unit = {}

  def handleSayStart(): Unit = {}

  def startModule(): Unit = {
    initIntentClient()
    intentMqttClient.subscribe(getIntentSubscriptions.toArray)
  }

  private def initIntentClient(): Unit = {
    intentMqttClient.setCallback(intentMqttCallback)
    intentMqttClient.connect()
  }


  class IntentMqttCallback extends MqttCallback {

    final override def connectionLost(cause: Throwable): Unit = {
      logger.error("MQTT connection lost", cause)
      logger.info("Trying to reconnect MQTT client")
    }

    final override def messageArrived(topic: String, message: MqttMessage): Unit = {
      logger.debug("Message arrived : {}", StandardCharsets.UTF_8.decode(ByteBuffer.wrap(message.getPayload)))
      val result = intentObjectMapper.readValue(message.getPayload, classOf[NluResult])
      handleIntent(result)
    }

    final override def deliveryComplete(token: IMqttDeliveryToken): Unit = {
    }

  }


}