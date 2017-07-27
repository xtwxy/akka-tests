package com.wincom.dcim.sharded

import akka.actor.{Props, ReceiveTimeout}
import akka.event.Logging
import akka.persistence.PersistentActor
import com.wincom.dcim.sharded.FsuActor.CreateFsu

object FsuActor {
  def props(fsuId: Int) = Props(new FsuActor)
  def name(fsuId: Int) = fsuId.toString()

  trait Command {
    def fsuId: Int
  }
  
  final case class CreateFsu(fsuId: Int, name: String) extends Command
  final case class Snapshot(fsuId: Int, name: String) extends Command
}

class FsuActor extends PersistentActor {
  override def persistenceId: String = s"${self.path.name}"

  var fsuId: Int = 0
  var fsuName: String = null
  var isDirty: Boolean = false
  
  val log = Logging(context.system.eventStream, "sharded-fsus")
  
  def receiveRecover = {
    case FsuActor.Snapshot(fsuId, name) =>
      this.fsuId = fsuId
      this.fsuName = name
    case x => log.info("RECOVER: {} {}", this, x)
  }

  def receiveCommand = {
    case cmd : FsuActor.CreateFsu =>
      persist(cmd)(updateState)
    case x : ReceiveTimeout =>
      if(isDirty) {
        log.info("COMMAND: {} {}", this, x)
        saveSnapshot(FsuActor.Snapshot(fsuId, fsuName))
        isDirty = false
      }
    case x => log.info("COMMAND: {} {}", this, x)
  }
  
  private def updateState: (FsuActor.Command => Unit) = {
    case CreateFsu(fsuId, name) => 
      log.info("UPDATE: persistenceId = {} fsuId = {} name = {}", persistenceId, fsuId, name)
      this.fsuId = fsuId
      this.fsuName = name
      this.isDirty = true
  }
}