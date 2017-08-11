import hello.Dependencies._


lazy val root = (project in file(".")).
  settings(
    inThisBuild(commonSettings),
    name := "hello",
    libraryDependencies += scalaTest % Test
  ).aggregate(cluster, core, util)

lazy val cluster = Project(id = "cluster", base = file("cluster")).dependsOn(core)
lazy val core = Project(id = "core", base = file("core")).dependsOn(util)
lazy val util = Project(id = "util", base = file("util"))