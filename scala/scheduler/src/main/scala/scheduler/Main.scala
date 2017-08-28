package scheduler

import akka.actor.ActorSystem

object Main extends App {
  val system = ActorSystem.create("The-System")
  val scheduler = system.actorOf(Scheduler.props("scheduler-1"), Scheduler.name("scheduler-1"))
}
