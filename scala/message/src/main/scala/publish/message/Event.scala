package publish.message

trait Event {
  def user: Option[String]
}
