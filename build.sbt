import sbt.Keys.libraryDependencies


lazy val homer = project.in(file("."))
  .settings(
    BuildSettings.buildSettings,
    name := "Homer-Assistant",
    version := "0.1",
    description := "Homer modules for snips NLU"
  ).settings(mainClass in assembly := Some("info.acidflow.homer.Main"))
  .dependsOn(shared_components, weather_owm, timers, deezer)

lazy val shared_components = (project in file("modules/shared_components"))
  .settings(
    BuildSettings.buildSettings,
    name := "shared_components",
    description := "Shared components for homer assistant.",
    libraryDependencies ++= Seq(
      Dependencies.Libraries.jacksonCore,
      Dependencies.Libraries.jacksonDataBind,
      Dependencies.Libraries.jacksonScala,
      Dependencies.Libraries.mqtt,
      Dependencies.Libraries.scalaLogging,
      Dependencies.Libraries.logBack,

      Dependencies.Libraries.scalaTest
    )
  ).settings(assemblyJarName in assembly := "shared_components.jar")

lazy val weather_owm = (project in file("modules/weather_owm"))
  .settings(
    BuildSettings.buildSettings,
    name := "weather_owm",
    description := "Weather module using Open weather map.",
    libraryDependencies ++= Seq(
      Dependencies.Libraries.retrofit,
      Dependencies.Libraries.retrofitJackson
    )
  )
  .settings(assemblyJarName in assembly := "weather_owm.jar")
  .dependsOn(shared_components)

lazy val timers = (project in file("modules/timers"))
  .settings(
    BuildSettings.buildSettings,
    name := "timers",
    description := "Timers module."
  )
  .settings(assemblyJarName in assembly := "timers.jar")
  .dependsOn(shared_components)



lazy val deezer = (project in file("modules/deezer"))
  .settings(
    BuildSettings.buildSettings,
    name := "deezer",
    description := "Deezer module.",
    libraryDependencies ++= Seq(
      Dependencies.Libraries.retrofit,
      Dependencies.Libraries.retrofitJackson
    )
  )
  .settings(target in javah := sourceDirectory.value / "main" /"native" / "include" / "jni")
  .settings(assemblyJarName in assembly := "deezer.jar")
  .dependsOn(shared_components)
