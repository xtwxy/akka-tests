package publish.message

import akka.actor._
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator._
import akka.persistence.{PersistentActor, SnapshotOffer}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps
import publish.message.Publisher._
object Publisher {
  val queueName = "publish"

  def props = Props(new Publisher)

  def name = queueName
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
    case SnapshotOffer(_, DataEvent(name, c, d)) =>
      counter = Some(c)
      data = Some(d)
    case x =>
      log.warning("RECOVER: {}", x)
  }

  override def receiveCommand: Receive = {
    case DataResponse(u, c, d) =>
      log.info(String.format("end relay: %s, %s, %s", u.getOrElse("[UNIDENTIFIED"), c.toString, d.toString))
      persist(DataEvent(u, c, d))(updateState)
    case DataCommand(_, u, c, d) =>
      log.info(String.format("relayed: %s, %s, %s", u.getOrElse("[UNIDENTIFIED]"), c.toString, d.toString))
      persist(DataEvent(u, c, d))(updateState)
    case ReceiveTimeout =>
      mediator ! Publish(queueName, DataCommand(persistenceId, Some(name), 0, "Anyone there?"))
    case x =>
      log.warning("COMMAND: {}", x)
  }

  private def updateState: Event => Unit = {
    case DataEvent(_, c, d) =>
      counter = Some(c + counter.getOrElse(0))
      data = Some(d)
      if("deadLetters" != sender().path.name) {
        sender() ! DataCommand(persistenceId, Some(name), c + 1, d)
      }
    case x =>
      log.warning("EVENT: {}", x)
  }
}
