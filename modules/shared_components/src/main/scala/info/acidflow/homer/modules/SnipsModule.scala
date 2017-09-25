package info.acidflow.homer.modules

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.Constants
import info.acidflow.homer.communication.mqtt.MqttConfigFactory
import info.acidflow.homer.model.{NluResult, SnipsSayText}
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttClient, MqttMessage}


trait SnipsModule extends MqttCallback with LazyLogging {

  private[this] val objectMapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private[this] val mqttConfig = MqttConfigFactory.fromResource("global/conf/mqtt.local.properties")
  private[this] val mqttClient = new MqttClient(
    mqttConfig.getUri.toString, generateClientId(), new MqttDefaultFilePersistence(mqttConfig.persistenceDir)
  )

  def getSubscriptions: Seq[String]

  def handleMessage(nluResult: NluResult)

  private def init(): Unit = {
    mqttClient.setCallback(this)
    mqttClient.connect()
  }

  def start(): Unit = {
    init()
    mqttClient.subscribe(getSubscriptions.toArray)
  }

  final override def connectionLost(cause: Throwable): Unit = {
    logger.error("MQTT connection lost", cause)
    logger.info("Trying to reconnect MQTT client")
    start()
  }

  final override def messageArrived(topic: String, message: MqttMessage): Unit = {
    logger.debug("Message arrived : {}", StandardCharsets.UTF_8.decode(ByteBuffer.wrap(message.getPayload)))
    val result = objectMapper.readValue(message.getPayload, classOf[NluResult])
    handleMessage(result)
  }

  final override def deliveryComplete(token: IMqttDeliveryToken): Unit = {

  }


}
