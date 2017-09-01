package subscribe

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator._

import scala.collection.mutable
import scala.concurrent.ExecutionContext

/**
  * Created by wangxy on 17-8-29.
  */
class Subscriber extends Actor with ActorLogging {
  val queueName = "publish"
  var senders: mutable.Set[ActorRef] = mutable.Set()
  val mediator = DistributedPubSub(context.system).mediator
  val executionContext: ExecutionContext = context.dispatcher

  mediator ! Subscribe(queueName, self)

  override def receive: Receive = {
    case (i: Int, s: String) => 
      senders += sender
      log.info(String.format("%s, %s, %s", sender.toString, i.toString, s.toString))
      if(i < 2) {
        senders.foreach(s => s ! (i + 1, "Hi, my friend!"))
        mediator ! Publish(queueName, (i + 1, "Hi, my friend!"))
      }
  }
}
