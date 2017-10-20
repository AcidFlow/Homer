package info.acidflow.homer.modules

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.scalalogging.LazyLogging
import info.acidflow.homer.Constants
import info.acidflow.homer.model.{NluResult, SnipsSayText}
import org.eclipse.paho.client.mqttv3.{IMqttDeliveryToken, MqttCallback, MqttClient, MqttMessage}

import scala.collection.mutable


class SnipsClient(private val _mqttClient: MqttClient, private val _objectMapper: ObjectMapper)
  extends LazyLogging
    with MqttCallback {

  private val _intentListeners = new mutable.HashMap[String, List[SnipsClientModule]]
  private val _ttsListeners = new mutable.ArrayBuffer[SnipsClientTTSListener]

  final override def connectionLost(cause: Throwable): Unit = {
    logger.error("MQTT connection lost", cause)
    logger.info("Trying to reconnect MQTT client")
  }

  final override def messageArrived(topic: String, message: MqttMessage): Unit = {
    if (isTTSMessage(topic)) {
      topic match {
        case Constants.Mqtt.TTS_SAY => _ttsListeners.foreach(l => l.onSayStart())
        case Constants.Mqtt.TTS_SAY_FINISHED => _ttsListeners.foreach(l => l.onSayFinished())
        case _ => ???
      }
    } else if (isIntentMessage(topic)) {
      logger.debug("Intent arrived : {}", StandardCharsets.UTF_8.decode(ByteBuffer.wrap(message.getPayload)))
      val result = _objectMapper.readValue(message.getPayload, classOf[NluResult])
      val intentName = result.intent.intentName
      _intentListeners.getOrElse(intentName, List.empty).foreach(m => m.handleIntent(result))
    }
  }

  private def isIntentMessage(topic: String) = {
    topic.startsWith(Constants.Mqtt.INTENT)
  }

  private def isTTSMessage(topic: String) = {
    topic.startsWith(Constants.Mqtt.TTS)
  }

  final override def deliveryComplete(token: IMqttDeliveryToken): Unit = {
  }

  final def start(): Unit = {
    _mqttClient.setCallback(this)
    _mqttClient.connect()
    _mqttClient.subscribe(Constants.Mqtt.TTS_SAY)
    _mqttClient.subscribe(Constants.Mqtt.TTS_SAY_FINISHED)
  }

  final def stop(): Unit = {
    unsubscribeIntent()
    _mqttClient.setCallback(null)
    _mqttClient.disconnect()
    _mqttClient.close()
  }

  final def registerForIntent(intentName: String, module: SnipsClientModule): Unit = {
    val currentList = _intentListeners.getOrElse(intentName, List.empty)
    _intentListeners += (intentName -> (module :: currentList))
    subscribeIntent()
  }

  final def unregisterForIntent(intentName: String, module: SnipsClientModule): Unit = {
    val newList = _intentListeners.getOrElse(intentName, List.empty).filterNot(m => m == module)

    if (newList.isEmpty) {
      _intentListeners.remove(intentName)
    } else {
      _intentListeners += (intentName -> newList)
    }
    unsubscribeIntent()
    subscribeIntent()
  }

  private def subscribeIntent(): Unit = {
    _mqttClient.subscribe(_intentListeners.keySet.map(t => Constants.Mqtt.INTENT_REGISTER_PREFIX + t).toArray)
  }

  private def unsubscribeIntent(): Unit = {
    _mqttClient.unsubscribe(_intentListeners.keySet.map(t => Constants.Mqtt.INTENT_REGISTER_PREFIX + t).toArray)
  }

  final def registerForTTS(module: SnipsClientTTSListener): Unit = {
    _ttsListeners += module
  }

  final def unregisterForTTS(module: SnipsClientTTSListener): Unit = {
    _ttsListeners -= module
  }

  final def say(text: String): Unit = {
    logger.debug("Sending TTS input : {}", text)
    _mqttClient.publish(
      Constants.Mqtt.TTS_SAY,
      new MqttMessage(_objectMapper.writeValueAsBytes(SnipsSayText(text)))
    )
  }

}
