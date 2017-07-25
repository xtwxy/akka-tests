package com.wincom.dcim.sharded

import akka.actor.Actor
import akka.actor.Props

object Fsu {
  def props(fsuId: Long) = Props(new Fsu)
  def name(fsuId: Long) = fsuId.toString()

  trait Command {
    def fsuId: Long
  }
}

class Fsu extends Actor {
  def fsuId = self.path.name.toLong

  def receive = {
    case _ => {}
  }
}