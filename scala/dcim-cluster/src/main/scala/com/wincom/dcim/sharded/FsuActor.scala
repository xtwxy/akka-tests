package com.wincom.dcim.sharded

import akka.actor.{ Props, ReceiveTimeout }
import akka.event.Logging
import akka.persistence.PersistentActor
import akka.persistence.SnapshotOffer
import akka.cluster.sharding.ShardRegion
import com.wincom.dcim.rest.Settings
import com.wincom.dcim.sharded.FsuActor._
import java.util.{ Calendar, Date }
import org.joda.time.DateTime

object FsuActor {
  def props(fsuId: Int) = Props(new FsuActor)
  def name(fsuId: Int) = fsuId.toString()

  case class Fsu(id: Int, name: String)
  case class Fsus(fsus: List[Fsu])

  trait Command {
    def fsuId: Int
  }

  final case class CreateFsu(fsuId: Int, name: String) extends Command
  final case class Ping(fsuId: Int) extends Command
  final case class Pong(fsuId: Int, ts: DateTime) extends Command
  final case class StopFsu(fsuId: Int) extends Command

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.fsuId.toString, cmd)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.fsuId % 100).toString()
  }
}

class FsuActor extends PersistentActor {
  override def persistenceId: String = s"${self.path.name}"

  var fsuId: Int = 0
  var fsuName: String = null
  var isDirty: Boolean = true

  val log = Logging(context.system.eventStream, "sharded-fsus")

  context.setReceiveTimeout(Settings(context.system).passivateTimeout)

  def receiveRecover = {
    case cmd: CreateFsu =>
      updateState(cmd)
    case SnapshotOffer(_, Fsu(fsuId, name)) =>
      this.fsuId = fsuId
      this.fsuName = name
    case x => log.info("RECOVER: {} {}", this, x)
  }

  def receiveCommand = {
    case cmd: CreateFsu =>
      persist(cmd)(updateState)
      this.isDirty = true
    case x: ReceiveTimeout =>
      log.info("COMMAND: {} {}", this, x)
      if (isDirty) {
        saveSnapshot(Fsu(fsuId, fsuName))
        isDirty = false
      }
    case cmd @ Ping(id) =>
      log.info("COMMAND: {} {}", this, cmd)
    case StopFsu => context.stop(self)
    case x => log.info("COMMAND: {} {}", this, x)
  }

  private def updateState: (Command => Unit) = {
    case CreateFsu(fsuId, name) =>
      log.info("UPDATE: persistenceId = {} fsuId = {} name = {}", persistenceId, fsuId, name)
      if (this.fsuId == 0) {
        this.fsuId = fsuId
        this.fsuName = name
        log.info("UPDATE APPLIED: persistenceId = {} fsuId = {} name = {}", persistenceId, fsuId, name)
      } else {
        log.info("UPDATE IGNORED: using existing persistenceId = {} fsuId = {} name = {}", persistenceId, this.fsuId, this.fsuName)
      }
  }
}