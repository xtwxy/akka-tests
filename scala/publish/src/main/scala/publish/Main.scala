package publish

import akka.actor.{ActorSystem, Props}

/**
  * Created by wangxy on 17-8-29.
  */
object Main extends App {
  val system = ActorSystem("system")
  val publish = system.actorOf(Props(new Publisher), "publish")
}
