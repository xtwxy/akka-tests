import Dependencies._
import sbtassembly.MergeStrategy

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "wangxy",
      scalaVersion := "2.12.3",
      version      := "1.0.0"
    )),
    name := "scala",
    libraryDependencies += scalaTest % Test
  )
  .aggregate(
    publish,
    subscribe,
    scheduler,
    websocket
  )

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)
scriptClasspath +="../conf"

parallelExecution in Test := false

fork := true

lazy val publish = (project in file("publish"))
lazy val subscribe = (project in file("subscribe")).dependsOn(publish)
lazy val scheduler = (project in file("scheduler"))
lazy val websocket = (project in file("websocket"))

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.rename
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("wangxy.Main")
assemblyJarName in assembly := "scala.jar"

