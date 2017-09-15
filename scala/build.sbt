import sbtassembly.MergeStrategy

lazy val akkaVersion = "2.5.3"
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "wangxy",
      scalaVersion := "2.12.3",
      version      := "1.0.0"
    )),
    name := "scala",
    libraryDependencies ++= Seq(
      "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.2",
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  )
  .aggregate(
    rest,
    message,
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

lazy val rest = (project in file("rest")).dependsOn(message)
lazy val message = (project in file("message"))
lazy val publish = (project in file("publish"))
    .dependsOn(message)
lazy val subscribe = (project in file("subscribe")).dependsOn(message, publish)
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

