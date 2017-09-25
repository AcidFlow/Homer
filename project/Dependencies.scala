import sbt._


object Dependencies {

  val resolverRepos = Seq(
    "paho mqtt " at "https://repo.eclipse.org/content/repositories/paho-releases/"
  )


  object LibVersion {

    val pahoMqtt = "1.0.2"
    val jackson = "2.9.1"
    val jacksonScala = "2.9.0"
    val macWire = "2.3.0"
    val scalaLogging = "3.7.2"
    val logBack = "1.2.3"
    val retrofit = "2.3.0"

    // Tests
    val scalaTest = "3.0.1"
  }


  object Libraries {

    val mqtt = "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % LibVersion.pahoMqtt
    val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core" % LibVersion.jackson
    val jacksonDataBind = "com.fasterxml.jackson.core" % "jackson-databind" % LibVersion.jackson
    val jacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % LibVersion.jacksonScala
    val macWire = "com.softwaremill.macwire" %% "macros" % LibVersion.macWire
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % LibVersion.scalaLogging
    val logBack = "ch.qos.logback" % "logback-classic" % LibVersion.logBack
    val retrofit = "com.squareup.retrofit2" % "retrofit" % LibVersion.retrofit
    val retrofitJackson = "com.squareup.retrofit2" % "converter-jackson" % LibVersion.retrofit


    // Test
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  }


}
