package com.github.xtwxy.akkatests.tcp.client

import java.net._

import akka.actor._
import akka.io._
import akka.util._

object EchoClient {
  def props(remote: InetSocketAddress) =
    Props(classOf[EchoClient], remote)
}

class EchoClient(remote: InetSocketAddress) extends Actor with ActorLogging {

  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) ⇒
      log.error("connect failed")
      context stop self
      context.system.terminate()

    case c@Connected(remote, local) ⇒
      log.info(s"remote host: ${remote.toString}, local host: ${local.toString}")
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString ⇒
          connection ! Write(data)
        case CommandFailed(w: Write) ⇒
          // O/S buffer was full
          log.error("write failed")
        case Received(data) ⇒
          log.info(data.toString())
        case "close" ⇒
          connection ! Close
        case _: ConnectionClosed ⇒
          log.info("connection closed")
          context stop self
      }
  }
}
