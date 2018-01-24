package models

import akka.actor._

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case msg: String =>
      log.info(s"A message is received: ${msg}")
      out ! ("I received your message: " + msg)
    case x =>
      log.warning(x.toString)
  }
}
