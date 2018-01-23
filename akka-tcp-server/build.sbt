import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.xtwxy.akkatests",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "tcp-server",
    mainClass in assembly := Some("com.github.xtwxy.akkatests.tcp.server.Main"),
    assemblyJarName in assembly := "akka-tcp-server.jar",
    libraryDependencies ++= {
        Seq(
            akkaActor,
            akkaSlf4j,
            scalaTest     % Test
        )
    }
  )
