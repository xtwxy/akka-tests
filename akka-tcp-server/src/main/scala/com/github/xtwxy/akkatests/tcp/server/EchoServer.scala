package com.github.xtwxy.akkatests.tcp.server

import java.net._
import java.nio.charset.Charset

import akka.actor._
import akka.io._
import akka.util._

object EchoServer {
  def props(host: InetSocketAddress) =
    Props(classOf[EchoServer], host)
}

class EchoServer(host: InetSocketAddress) extends Actor with ActorLogging {

  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Bind(self, host)

  def receive = {
    case b @ Bound(localAddress) ⇒
      log.info(s"bound to: ${localAddress}")
      context.parent ! b

    case CommandFailed(_: Bind) ⇒ context stop self

    case c @ Connected(remote, local) ⇒
      log.info(s"remote: ${remote}, local: ${local}")
      val handler = context.actorOf(Props[SimplisticHandler])
      val connection = sender()
      connection ! Register(handler)
  }
}
