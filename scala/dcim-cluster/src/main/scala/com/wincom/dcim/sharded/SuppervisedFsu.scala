package com.wincom.dcim.sharded

import akka.actor.Props
import akka.actor.ReceiveTimeout
import akka.actor.actorRef2Scala
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate
import com.wincom.dcim.rest.Settings
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.actor.DeathPactException
import akka.actor.Actor
import akka.actor.ActorInitializationException
  

class SuppervisedFsu extends Actor {
  val fsuActor = context.actorOf(Props[FsuActor], s"${self.path.name}")

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException     ⇒ SupervisorStrategy.Restart
    case _: ActorInitializationException ⇒ SupervisorStrategy.Restart
    case _: DeathPactException           ⇒ SupervisorStrategy.Restart
    case _: Exception                    ⇒ SupervisorStrategy.Restart
  }

  def receive = {
    case msg ⇒ fsuActor forward msg
  }
}