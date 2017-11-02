package test

import anorm.SqlParser.get
import anorm.{RowParser, ~}
import apuex.music.{MusicType, MusicVo}

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
