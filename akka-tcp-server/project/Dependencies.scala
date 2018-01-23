import sbt._

object Dependencies {
  val scalatestVersion = "3.0.4"
  val akkaVersion = "2.5.6"
  lazy val akkaActor = "com.typesafe.akka"         %%  "akka-actor"                          % akkaVersion
  lazy val akkaSlf4j = "com.typesafe.akka"         %%  "akka-slf4j"                          % akkaVersion
  lazy val scalaTest = "org.scalatest"             %%  "scalatest"                           % scalatestVersion
}
