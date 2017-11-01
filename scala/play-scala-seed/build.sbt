name := """play-scala-seed"""
organization := "wangxy"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++=
  Seq(
    guice,
    jdbc,
    "com.typesafe.play" %% "anorm" % "2.5.3",
    "com.typesafe.play" %% "play-slick" % "3.0.2",
    "mysql" % "mysql-connector-java" % "6.0.6",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "wangxy.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "wangxy.binders._"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case PathList("play", "reference-overrides.conf") => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("play.core.server.ProdServerStart")
assemblyJarName in assembly := "play-scala-seed.jar"
