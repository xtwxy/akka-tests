package publish.message

trait Command {
  def id: String
  def user: Option[String]
}
