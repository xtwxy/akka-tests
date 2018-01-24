lazy val akkaVersion = "2.5.6"

resolvers ++= Seq(
  "Atlassian Releases" at "https://maven.atlassian.com/public/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  Resolver.sonatypeRepo("snapshots")
 )

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.xtwxy.play.websockets",
      scalaVersion := "2.12.3",
      version      := "1.0.0"
    )),
    name := "play-websockets",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"      %% "akka-testkit"    % akkaVersion     % Test,
      "org.scalatest"          %% "scalatest"       % "3.0.1"         % Test
    )
  )
  .aggregate(
    simple
  )

lazy val simple = (project in file("simple")).enablePlugins(PlayScala)

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
