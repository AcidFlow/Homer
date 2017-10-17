package info.acidflow.homer.communication.mqtt

import com.typesafe.config.ConfigFactory


object MqttConfigFactory {

  def fromResource(resPath: String = "global/conf/mqtt.conf"): MqttClientConfig = {
    val conf = ConfigFactory.load(resPath)
    MqttClientConfig(
      conf.getString("mqtt.protocol"),
      conf.getString("mqtt.host"),
      conf.getInt("mqtt.port"),
      conf.getString("mqtt.persistence")
    )
  }

}
