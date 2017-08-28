package scheduler

import akka.actor._
import akka.event.Logging
import scheduler.Scheduler.EvalTimeoutCmd

import scala.concurrent.ExecutionContext
import scala.language.postfixOps
import scala.concurrent.duration._

object Scheduler {
  def props(id: String) = Props(new Scheduler(id))
  def name(id: String) = s"$id"

  sealed trait Command {
    def id: String
  }

  final case class EvalTimeoutCmd(id: String) extends Command
}


class Scheduler(val id: String) extends Actor {
  val log = Logging(context.system.eventStream, getClass.getName)

  var counter = 0

  implicit def executionContext: ExecutionContext = context.dispatcher

  val task = context.system.scheduler.schedule(
      0 milliseconds,
      1000 milliseconds,
      self,
      EvalTimeoutCmd(id)
    )

  override def receive = {
    case EvalTimeoutCmd(_) =>
      log.info("EvalTimeoutCmd: {}", counter)
      counter += 1
    case x => log.info("*IGNORED*: {}", x)
  }
}
