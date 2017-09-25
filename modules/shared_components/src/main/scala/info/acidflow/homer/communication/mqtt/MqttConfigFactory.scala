package info.acidflow.homer.communication.mqtt

import java.util.Properties

import scala.io.Source


object MqttConfigFactory {

  def fromResource(resPath: String = "global/conf/mqtt.properties"): MqttClientConfig = {
    val props = new Properties()
    props.load(Source.fromResource(resPath).reader())

    MqttClientConfig(
      props.getProperty("protocol"),
      props.getProperty("host"),
      props.getProperty("port").toInt
    )
  }

}
