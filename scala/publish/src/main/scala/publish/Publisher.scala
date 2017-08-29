package publish

import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.DistributedPubSub

import scala.language.postfixOps
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

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
        log.info("publish: msg={}, q={}", System.currentTimeMillis(), x)
  }
}
