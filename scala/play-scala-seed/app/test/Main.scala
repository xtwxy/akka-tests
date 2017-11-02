package test

import anorm.SqlParser._
import anorm._
import apuex.music.{Music, MusicType}
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._


object MusicUtil {
  val parser: RowParser[Music] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("composer") ~
      get[Int]("type") map {
      case id ~ name ~ composer ~ musicType => Music.apply(id, Some(name), Some(composer), Some(MusicType.fromValue(musicType)))
    }
  }

}

object EnumFormat {

}

object Main extends App {
  val config = ConfigFactory.load("application.conf")
  val app = new GuiceApplicationBuilder().configure(Configuration(config)).build()
  val database = app.injector.instanceOf[Database]

  implicit val musicTypeFormat: Format[MusicType] = new Format[MusicType] {
    override def writes(o: MusicType) = JsString(o.name)

    override def reads(json: JsValue) = {
      json match {
        case JsString(name) => JsSuccess(MusicType.fromName(name).get)
        case _ => JsError(json.toString())
      }
    }
  }

  implicit val musicFormat = Json.format[Music]

  val sql: SqlQuery = SQL("SELECT id, name, composer, type from music")

  database.withConnection { implicit c =>
    val result: List[Music] = sql.as(MusicUtil.parser.*)
    print(s"${Json.toJson(result)}")
  }
}
