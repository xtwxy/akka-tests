package test

import anorm._
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder

final case class Music(id: Long, name: String, composer: String)

object Main extends App {
  val config = ConfigFactory.load("application.conf")
  val app = new GuiceApplicationBuilder().configure(Configuration(config)).build()
  val database = app.injector.instanceOf[Database]

  val musicParser = Macro.parser[Music]("id", "name", "composer")
  val sql: SqlQuery = SQL("SELECT id, name, composer from music")

  database.withConnection { implicit c =>
    val result: List[Music] = sql.as(musicParser.*)
    print(s"result: $result")
  }
}
