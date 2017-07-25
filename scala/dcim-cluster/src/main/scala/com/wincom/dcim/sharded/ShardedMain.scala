package com.wincom.dcim.sharded

import akka.actor.ActorSystem

object SharedMain extends App {
  implicit val system = ActorSystem("dcim")

  val fsus = system.actorOf(ShardedFsus.props, ShardedFsus.name)
}