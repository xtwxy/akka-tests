package publish

import akka.actor.{Actor, ActorLogging, ReceiveTimeout}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

class Publisher extends Actor with ActorLogging {

  val mediator = DistributedPubSub(context.system).mediator

  implicit def executionContext: ExecutionContext = context.dispatcher
/*
  val task = context.system.scheduler.schedule(
    0 milliseconds,
    1000 milliseconds,
    self,
    "publish"
  )
*/
  context.setReceiveTimeout(5 seconds)

  def receive = {
    case x: String =>
      mediator ! Publish("publish", this.getClass.getSimpleName)
    case ReceiveTimeout =>
      mediator ! Publish("publish", this.getClass.getSimpleName)
    case _ =>
  }
}
