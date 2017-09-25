// SBT
import sbt._
import Keys._


object BuildSettings {

  // Basic settings for our app
  lazy val basicSettings = Seq(
    organization          :=  "info.acidflow",
    scalaVersion          :=  "2.12.3",
    javacOptions          :=  javaCompilerOptions,
    resolvers             ++= Dependencies.resolverRepos
  )

  lazy val javaCompilerOptions = Seq(
    "-source", "1.8",
    "-target", "1.8"
  )

  lazy val buildSettings = basicSettings
}
