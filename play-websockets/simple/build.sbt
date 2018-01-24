name := """simple"""
organization := "com.github.xtwxy.play.websockets"

version := "1.0-SNAPSHOT"

lazy val simple = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "org.webjars"                 %% "webjars-play"         % "2.6.3",
  "org.webjars.bower"	           % "socket.io" 	          % "2.0.3",
  "org.scalatestplus.play"      %% "scalatestplus-play"   % "3.1.2"       % Test
)

routesGenerator := InjectedRoutesGenerator

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.xtwxy.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.xtwxy.binders._"
