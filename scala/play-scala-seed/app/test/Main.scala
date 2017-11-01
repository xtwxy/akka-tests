package test

import anorm._
import anorm.SqlParser._
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.api.data.FormError
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.libs.functional.syntax._
import se.radley.plugin.enumeration.form._
import play.api.data.format._

trait MusicType {
  type EnumType = MusicType

  def value: Int
  def name: String

  def isClassical: Boolean = false

  def isJazz: Boolean = false

  def isPopular: Boolean = false
}

case object MusicType {

  case object CLASSICAL extends MusicType {
    val value = 0
    val name = "CLASSICAL"

    override def isClassical: Boolean = true
  }

  case object JAZZ extends MusicType {
    val value = 1
    val name = "JAZZ"

    override def isJazz: Boolean = true
  }

  case object POPULAR extends MusicType {
    val value = 2
    val name = "POPULAR"

    override def isPopular: Boolean = true
  }

  case class Unrecognized(val value: Int) extends MusicType {
    val name = "Unrecognized"
  }

  def fromName(name: String): Option[MusicType] = values.find(_.name == name)
  def values: Seq[MusicType] = Seq(CLASSICAL, JAZZ, POPULAR)

  def fromValue(value: Int): MusicType = value match {
    case 0 => CLASSICAL
    case 1 => JAZZ
    case 2 => POPULAR
    case __other => Unrecognized(__other)
  }

}

final case class Music(id: Long, name: String, composer: String, musicType: MusicType)

object Music {
  val parser: RowParser[Music] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("composer") ~
      get[Int]("type") map {
      case id ~ name ~ composer ~ musicType => Music(id, name, composer, MusicType.fromValue(musicType))
    }
  }

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
    val result: List[Music] = sql.as(Music.parser.*)
    print(s"${Json.toJson(result)}")
  }
}
