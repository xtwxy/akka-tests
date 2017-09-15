package publish.rest

import spray.json._
import publish.message._

trait PublishMarshaling extends DefaultJsonProtocol {
  implicit val dataVoFormat = jsonFormat2(DataVo.apply)
}

object PublishMarshaling extends PublishMarshaling