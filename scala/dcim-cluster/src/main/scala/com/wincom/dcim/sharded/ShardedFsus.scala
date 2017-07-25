package com.wincom.dcim.sharded

import akka.actor.Actor
import akka.actor.Props
import akka.cluster.sharding.ClusterSharding
import akka.cluster.sharding.ClusterShardingSettings

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

  def receive = {
    case _ => {}
  }
}