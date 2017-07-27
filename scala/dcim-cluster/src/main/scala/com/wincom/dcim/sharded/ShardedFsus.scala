package com.wincom.dcim.sharded

import akka.actor.Actor
import akka.actor.Props
import akka.cluster.sharding.ClusterSharding
import akka.cluster.sharding.ClusterShardingSettings
import akka.event.Logging

object ShardedFsus {
  def props = Props(new ShardedFsus)
  def name = "sharded-fsus"
}
class ShardedFsus extends Actor {

  ClusterSharding(context.system).start(
    ShardedFsu.shardName,
    ShardedFsu.props,
    ClusterShardingSettings(context.system),
    ShardedFsu.extractEntityId,
    ShardedFsu.extractShardId)

  val log = Logging(context.system.eventStream, "sharded-fsus")
  def shardedFsu = {
    ClusterSharding(context.system).shardRegion(ShardedFsu.shardName)
  }
  def receive = {
    case x => {
      log.info("{}: forwarding message '{}' to {}", this, x, shardedFsu)
      shardedFsu forward(x)
    }
  }
}