package com.wincom.dcim.sharded

import akka.actor.Props
import akka.actor.ReceiveTimeout
import akka.actor.actorRef2Scala
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate
import com.wincom.dcim.rest.Settings

object ShardedFsu {
  def props = Props(new ShardedFsu)
  def name(fsuId: Long) = fsuId.toString()

  case object StopFsu

  val shardName: String = "fsus"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: FsuActor.Command => (cmd.fsuId.toString, cmd)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: FsuActor.Command => (cmd.fsuId % 100).toString()
  }
}

class ShardedFsu extends FsuActor {
  context.setReceiveTimeout(Settings(context.system).passivateTimeout)

  override def unhandled(msg: Any) = msg match {
    case ReceiveTimeout =>
      context.parent ! Passivate(stopMessage = ShardedFsu.StopFsu)
    case ShardedFsu.StopFsu => context.stop(self)
  }
}