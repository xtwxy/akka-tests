package subscribe

import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe

import scala.concurrent.ExecutionContext

/**
  * Created by wangxy on 17-8-29.
  */
class Subscriber extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator
  val executionContext: ExecutionContext = context.dispatcher

  mediator ! Subscribe("publish", self)

  override def receive: Receive = {
    case x => log.info("RCV: {}", x)
  }
}
