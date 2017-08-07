package com.wincom.dcim.sharded

import akka.actor.Props
import akka.cluster.sharding.ShardRegion
import akka.event.Logging
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.wincom.dcim.rest.Settings
import com.wincom.dcim.sharded.SignalActor._

object SignalActor {
  def props(signalId: Int) = Props(new SignalActor)

  def name(signalId: Int) = signalId.toString()

  val numberOfShards = 100

  trait Command {
    def signalId: Int
  }

  trait Event {
    def signalId: Int
  }

  final case class Signal(signalId: Int, driverId: Int, name: String)

  final case class CreateSignalCmd(signalId: Int, driverId: Int, name: String) extends Command

  final case class RenameSignalCmd(signalId: Int, newName: String) extends Command

  final case class SelectDriverCmd(signalId: Int, driverId: Int) extends Command

  final case class SaveSnapshotCmd(signalId: Int) extends Command

  final case class UpdateValueCmd(signalId: Int, value: AnyVal) extends Command

  final case class CreateSignalEvt(signalId: Int, driverId: Int, name: String) extends Event

  final case class RenameSignalEvt(signalId: Int, newName: String) extends Event

  final case class SelectDriverEvt(signalId: Int, driverId: Int) extends Event

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.signalId.toString, cmd)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.signalId % numberOfShards).toString()
  }
}

class SignalActor extends PersistentActor {
  override def persistenceId: String = s"${self.path.name}"

  var signalId: Option[Int] = None
  var driverId: Option[Int] = None
  var signalName: Option[String] = None
  var signalValue: AnyVal = 0

  val log = Logging(context.system.eventStream, "sharded-fsus")

  context.setReceiveTimeout(Settings(context.system).passivateTimeout)

  def receiveRecover = {
    case evt: CreateSignalEvt =>
      updateState(evt)
    case evt: RenameSignalEvt =>
      updateState(evt)
    case evt: SelectDriverEvt =>
      updateState(evt)
    case SnapshotOffer(_, Signal(signalId, driverId, signalName)) =>
      this.signalId = Some(signalId)
      this.driverId = Some(driverId)
      this.signalName = Some(signalName)
    case x => log.info("RECOVER: {} {}", this, x)
  }

  def receiveCommand = {
    case CreateSignalCmd(signalId, driverId, signalName) =>
      persist(CreateSignalEvt(signalId, driverId, signalName))(updateState)
    case RenameSignalCmd(signalId, newName) =>
      persist(RenameSignalEvt(signalId, newName))(updateState)
    case SelectDriverCmd(signalId, driverId) =>
      persist(SelectDriverEvt(signalId, driverId))(updateState)
    case SaveSnapshotCmd =>
      if (signalId.isDefined && driverId.isDefined && signalName.isDefined) {
        saveSnapshot(Signal(signalId.get, driverId.get, signalName.get))
      }
    case x => log.info("COMMAND: {} {}", this, x)
  }

  private def updateState: (Event => Unit) = {
    case CreateSignalEvt(signalId, driverId, signalName) =>
      if (this.signalId.isEmpty) {
        this.signalId = Some(signalId)
        this.driverId = Some(driverId)
        this.signalName = Some(signalName)
      }
    case RenameSignalEvt(_, newName) =>
      this.signalName = Some(newName)
  }
}
