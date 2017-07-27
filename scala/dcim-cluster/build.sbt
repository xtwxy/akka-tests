import sbtassembly.MergeStrategy

lazy val root = (project in file(".")).
  settings(
    name := "dcim-cluster",
    organization := "com.wincom",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

enablePlugins(JavaAppPackaging)
enablePlugins(JavaServerAppPackaging)
scriptClasspath +="../conf"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Sonatype snapshots"  at "http://oss.sonatype.org/content/repositories/snapshots/")

parallelExecution in Test := false

fork := true

libraryDependencies ++= {
  val akkaVersion = "2.5.1"
  Seq(
    "com.typesafe.akka"         %%  "akka-actor"                          % akkaVersion,

    "com.typesafe.akka"         %%  "akka-persistence"                    % akkaVersion,
    "com.typesafe.akka"         %%  "akka-persistence-query-experimental" % "2.4.19",
    "org.iq80.leveldb"          %  "leveldb"                              % "0.7",
    "org.fusesource.leveldbjni" %  "leveldbjni-all"                       % "1.8",

    "com.typesafe.akka"         %%  "akka-cluster"                        % akkaVersion,
    "com.typesafe.akka"         %%  "akka-cluster-tools"                  % akkaVersion,
    "com.typesafe.akka"         %%  "akka-cluster-sharding"               % akkaVersion,

    "com.typesafe.akka"         %%  "akka-http-core"                      % "2.4.11",
    "com.typesafe.akka"         %%  "akka-http-experimental"              % "2.4.11",
    "com.typesafe.akka"         %%  "akka-http-spray-json-experimental"   % "2.4.11",

    "com.typesafe.akka"         %%  "akka-persistence-cassandra"          % "0.54",
    "com.typesafe.akka"         %%  "akka-persistence-cassandra-launcher" % "0.54" % Test,

    "org.scalatest"             %%  "scalatest"                           % "3.0.0"       % "test",

    "com.typesafe.akka"         %%  "akka-testkit"                        % akkaVersion   % "test",
    "com.typesafe.akka"         %%  "akka-multi-node-testkit"             % akkaVersion   % "test",

    "com.typesafe.akka"         %%  "akka-slf4j"                          % akkaVersion,

    "commons-io"                %  "commons-io"                           % "2.4",
    "ch.qos.logback"            %  "logback-classic"                      % "1.1.2"
  )
}
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.rename
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

val defaultMergeStrategy: String => MergeStrategy = {
  case x if Assembly.isConfigFile(x) =>
    MergeStrategy.concat
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
    MergeStrategy.rename
  case PathList("META-INF", xs @ _*) =>
    (xs map {_.toLowerCase}) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) | ("io.netty.versions.properties" :: Nil) =>
        MergeStrategy.discard
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.deduplicate
    }
  case _ => MergeStrategy.deduplicate
}

mainClass in assembly := Some("com.wincom.dcim.sharded.Main")
assemblyJarName in assembly := "dcim-cluster.jar"


