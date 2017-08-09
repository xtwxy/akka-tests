package shape

import java.util.Map

trait Factory {
  def name: String
  def create(params: Map[String, String]): Option[String]
}

class TestFactory extends Factory {
  override def name = "Test"
  override def create(params: Map[String, String]): Option[String] = {
    if(!params.isEmpty) Some(params.size.toString) else None
  }
}