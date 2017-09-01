import sbtassembly.MergeStrategy

name := "subscribe"
organization := "wangxy"
version := "1.0.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

libraryDependencies ++= Seq(
  "wangxy"            %% "publish" % version.value,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.rename
  case PathList("application.conf") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("subscribe.Main")
assemblyJarName in assembly := "subscribe.jar"

