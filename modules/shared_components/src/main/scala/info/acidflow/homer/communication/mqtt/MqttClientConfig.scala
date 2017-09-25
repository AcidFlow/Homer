package info.acidflow.homer.communication.mqtt

import java.net.URI


case class MqttClientConfig(protocol: String, host: String, port: Int, persistenceDir : String) {

  def getUri: URI = {
    new URI(protocol, null, host, port, null, null, null)
  }

}