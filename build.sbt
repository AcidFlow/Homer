import sbt.Keys.libraryDependencies

lazy val homer = project.in(file("."))
  .settings(
    BuildSettings.buildSettings,
    name := "snips-assistant",
    version := "0.1",
    description := "Modules for snips"
  ).settings(mainClass in assembly := Some("info.acidflow.snips.assistant.Main"))
  .aggregate(shared_components, weather_owm, timers)
  .dependsOn(shared_components, weather_owm, timers)

lazy val shared_components = (project in file("modules/shared_components"))
  .settings(
    BuildSettings.buildSettings,
    name := "shared_components",
    description := "Shared components for snips assistant.",
    libraryDependencies ++= Seq(
      Dependencies.Libraries.jacksonCore,
      Dependencies.Libraries.jacksonDataBind,
      Dependencies.Libraries.jacksonScala,
      Dependencies.Libraries.mqtt,
      Dependencies.Libraries.scalaLogging,
      Dependencies.Libraries.logBack,
      Dependencies.Libraries.retrofit,
      Dependencies.Libraries.retrofitJackson,

      Dependencies.Libraries.scalaTest
    )
  ).settings(assemblyJarName in assembly := "shared_components.jar")

lazy val weather_owm = (project in file("modules/weather_owm"))
  .settings(
    BuildSettings.buildSettings,
    name := "weather_owm",
    description := "Weather module using Open weather map."
  )
  .settings(assemblyJarName in assembly := "weather_owm.jar")
  .aggregate(shared_components)
  .dependsOn(shared_components)

lazy val timers = (project in file("modules/timers"))
  .settings(
    BuildSettings.buildSettings,
    name := "timers",
    description := "Timers module."
  )
  .settings(assemblyJarName in assembly := "timers.jar")
  .aggregate(shared_components)
  .dependsOn(shared_components)


