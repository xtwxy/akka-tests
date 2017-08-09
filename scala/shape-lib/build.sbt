import sbtassembly.MergeStrategy

lazy val root = (project in file(".")).
  settings(
    name := "shape-lib",
    organization := "shape-lib",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

enablePlugins(JavaAppPackaging)


parallelExecution in Test := false

fork := true

libraryDependencies ++= {
  val akkaVersion = "2.5.1"
  Seq(
    "com.wincom.dcim.driver"     %  "driver"                              % "1.0-SNAPSHOT",
    "org.scalatest"             %%  "scalatest"                           % "3.0.0"       % "test"
  )
}
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.rename
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("shape.Main")
assemblyJarName in assembly := "shape-lib.jar"


