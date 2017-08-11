package hello

import sbt.Keys.{organization, scalaVersion, version}
import sbt._


object Dependencies {
  val commonSettings = List(
    organization := "wangxy",
    scalaVersion := "2.12.3",
    version      := "1.0.0"
  )

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"


}

