package subscribe

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator._
import publish.Publisher.queueName
import publish.message._
import subscribe.Subscriber._

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by wangxy on 17-8-29.
  */
object Subscriber {
  val id: String = "subscriber"
  val name: String = id
  val ttl: Int = 3
}

class Subscriber extends Actor with ActorLogging {
  var senders: mutable.Set[ActorRef] = mutable.Set()
  val mediator = DistributedPubSub(context.system).mediator

  mediator ! Subscribe(queueName, self)
  context.setReceiveTimeout(10 seconds)

  override def receive: Receive = {
    case ReceiveTimeout =>
      senders.foreach(s => {
        val c = DataCommand(id, Some(name), 0, "Hey, anybody there?")
        log.info(String.format("%s, %s", s.toString, c))
        s ! c
      })
    case DataCommand(_, n, c: Int, d: String) =>
      log.info(String.format("%s, %s, %s", n.getOrElse("[UNIDENTIFIED]"), c.toString, d.toString))
      if (c < ttl) {
        sender ! DataCommand(id, Some(name), c + 1, d)
      } else {
        sender ! DataResponse(Some(name), c + 1, d, ResponseType.SUCCESS)
      }
      senders += sender
  }
}
