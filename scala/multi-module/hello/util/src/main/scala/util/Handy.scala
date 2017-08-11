package util

/**
  * Created by wangxy on 17-8-11.
  */
trait Handy {
  def id: Int
}

case class Utility(id: Int, name: String) extends Handy
