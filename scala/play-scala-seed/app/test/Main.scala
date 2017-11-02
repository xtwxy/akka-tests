package test

import anorm.SqlParser._
import anorm._
import apuex.music.{MusicVo, MusicType}
import com.trueaccord.scalapb.{GeneratedEnum, GeneratedEnumCompanion}
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._


object ResultSetParser {
  val music: RowParser[MusicVo] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("composer") ~
      get[Int]("type") map {
      case id ~ name ~ composer ~ musicType => MusicVo.apply(id, Some(name), Some(composer), Some(MusicType.fromValue(musicType)))
    }
  }

}

object EnumFormat {
  def format[E<: GeneratedEnum](companion: GeneratedEnumCompanion[E]): Format[E] = new Format[E] {
    override def writes(o: E) = JsString(o.name)

    override def reads(json: JsValue) = {
      json match {
        case JsString(name) => JsSuccess(companion.fromName(name).get)
        case _ => JsError(json.toString())
      }
    }
  }
}

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
