package subscribe

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator._
import publish.Messages.Command
import publish.Publisher._

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by wangxy on 17-8-29.
  */
class Subscriber extends Actor with ActorLogging {
  var senders: mutable.Set[ActorRef] = mutable.Set()
  val mediator = DistributedPubSub(context.system).mediator
  val executionContext: ExecutionContext = context.dispatcher

  mediator ! Subscribe(queueName, self)
  context.setReceiveTimeout(1 seconds)
  override def receive: Receive = {
    case ReceiveTimeout =>
      senders.foreach(s => {
        val c = Command(0, "Hey, anybody there?")
        log.info(String.format("%s, %s", s.toString, c))
        s ! c
      })
    case Command(i: Int, s: String) =>
      log.info(String.format("%s, %s, %s", sender.toString, i.toString, s.toString))
      senders += sender
  }
}
