package info.acidflow.homer.modules

import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.communication.mqtt.MqttConfigFactory
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttClient, MqttMessage}


trait TTSAware extends LazyLogging {

  private[this] val ttsAwareMqttConfig = MqttConfigFactory.fromResource("global/conf/mqtt.local.properties")
  private[this] val ttsAwareMqttClient = new MqttClient(
    ttsAwareMqttConfig.getUri.toString, generateClientId(),
    new MqttDefaultFilePersistence(ttsAwareMqttConfig.persistenceDir)
  )
  private[this] val ttsAwareMqttCallback = new TTSAwareMqttCallback()
  private[this] val ttsAwareSubscriptions = Seq("hermes/tts/say", "hermes/tts/sayFinished")

  startTTSAwareness()

  def handleSayFinished()

  def handleSayStart()

  final def startTTSAwareness(): Unit = {
    ttsAwareMqttClient.setCallback(ttsAwareMqttCallback)
    ttsAwareMqttClient.connect()
    ttsAwareMqttClient.subscribe(ttsAwareSubscriptions.toArray)
  }


  class TTSAwareMqttCallback extends MqttCallback {

    final override def connectionLost(cause: Throwable): Unit = {
      logger.error("MQTT connection lost", cause)
      logger.info("Trying to reconnect MQTT client")
      startTTSAwareness()
    }

    final override def messageArrived(topic: String, message: MqttMessage): Unit = {
      if (topic.equals("hermes/tts/say")) {
        handleSayStart()
      } else if (topic.equals("hermes/tts/sayFinished")) {
        handleSayFinished()
      }
    }

    final override def deliveryComplete(token: IMqttDeliveryToken): Unit = {
    }

  }


}
