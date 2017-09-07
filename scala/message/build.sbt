import com.trueaccord.scalapb.compiler.Version.scalapbVersion

name := "message"
organization := "wangxy"
version := "1.0.0"

scalaVersion := "2.12.3"

lazy val akkaVersion = "2.5.3"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

libraryDependencies ++= Seq(
  "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.2",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)


