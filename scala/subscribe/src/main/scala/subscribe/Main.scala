package subscribe

import akka.actor.{ActorSystem, Props}

/**
  * Created by wangxy on 17-8-29.
  */
object Main extends App {
  val system = ActorSystem("system")
  val subscribe = system.actorOf(Props(new Subscriber), "subscribe")
}
