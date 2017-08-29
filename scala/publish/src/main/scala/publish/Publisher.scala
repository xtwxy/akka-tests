package publish

import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class Publisher extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  implicit def executionContext: ExecutionContext = context.dispatcher

  val task = context.system.scheduler.schedule(
    0 milliseconds,
    1000 milliseconds,
    self,
    "publish"
  )

  def receive = {
    case x =>
      mediator ! Publish("publish", String.format("%s, %s", x.toString, System.currentTimeMillis().toString))
  }
}
