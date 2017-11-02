package com.wincom.dcim.util

import anorm._
import apuex.music.{MusicType, MusicVo}
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._

object Main extends App {
  val config = ConfigFactory.load("application.conf")
  val app = new GuiceApplicationBuilder().configure(Configuration(config)).build()
  val database = app.injector.instanceOf[Database]

  implicit val musicTypeFormat = EnumFormat.format(MusicType)
  implicit val musicFormat = Json.format[MusicVo]

  val sql: SqlQuery = SQL("SELECT id, name, composer, type from music")

  database.withConnection { implicit c =>
    val result: List[MusicVo] = sql.as(ResultSetParser.music.*)
    print(s"${Json.toJson(result)}")
  }
}
