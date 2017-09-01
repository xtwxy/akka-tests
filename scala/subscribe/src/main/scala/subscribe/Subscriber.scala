package subscribe

import akka.actor.{Actor, ActorRef, ActorLogging}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe

import scala.collection.mutable
import scala.concurrent.ExecutionContext

/**
  * Created by wangxy on 17-8-29.
  */
class Subscriber extends Actor with ActorLogging {

  var senders: mutable.Set[ActorRef] = mutable.Set()
  val mediator = DistributedPubSub(context.system).mediator
  val executionContext: ExecutionContext = context.dispatcher

  mediator ! Subscribe("publish", self)

  override def receive: Receive = {
    case x => 
      log.info("RCV: {}", x)
      senders += sender
      senders.foreach(s => s ! "Hi, my friend!")
  }
}
