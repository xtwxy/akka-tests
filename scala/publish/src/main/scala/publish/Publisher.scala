package publish

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator._
import akka.persistence.{PersistentActor, SnapshotOffer}
import publish.Publisher._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps
import publish.Messages.Command
import publish.Messages.Event

object Publisher {
  def props = Props(new Publisher)

  def name = queueName

  val queueName = "publish"
}

class Publisher extends PersistentActor with ActorLogging {
  var counter: Option[Int] = None
  var data: Option[String] = None

  val mediator = DistributedPubSub(context.system).mediator

  implicit def executionContext: ExecutionContext = context.dispatcher

  context.setReceiveTimeout(10 seconds)

  override def persistenceId: String = name


  override def receiveRecover: Receive = {
    case evt: Event =>
      updateState(evt)
    case SnapshotOffer(_, Event(c, d)) =>
      counter = Some(c)
      data = Some(d)
    case x =>
      log.warning("RECOVER: {}", x)
  }

  override def receiveCommand: Receive = {
    case Command(i: Int, s: String) =>
      persist(Event(i, s))(updateState)
    case ReceiveTimeout =>
      mediator ! Publish(queueName, Command(0, this.getClass.getSimpleName))
    case x =>
      log.warning("COMMAND: {}", x)
  }

  private def updateState: Event => Unit = {
    case Event(c, d) =>
      counter = Some(c + counter.getOrElse(0))
      data = Some(d)
      log.info(String.format("%s, %s, %s", sender.toString, c.toString, d.toString))
      if("deadLetters" != sender().path.name) {
        sender() ! Command(c + 1, d)
      }
    case x =>
      log.warning("EVENT: {}", x)
  }
}
