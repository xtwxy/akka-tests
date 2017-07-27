package com.wincom.dcim.sharded

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging

object FsuActor {
  def props(fsuId: Int) = Props(new FsuActor)
  def name(fsuId: Int) = fsuId.toString()

  trait Command {
    def fsuId: Int
  }
  
  final case class CreateFsu(fsuId: Int, name: String) extends Command
}

class FsuActor extends Actor {
  def fsuId = self.path.name.toLong

  val log = Logging(context.system.eventStream, "sharded-fsus")
  def receive = {
    case x => {
      log.info("RECV: {} {}", this, x)
    }
  }
}