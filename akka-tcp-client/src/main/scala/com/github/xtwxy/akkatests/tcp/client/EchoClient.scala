package com.github.xtwxy.akkatests.tcp.client

import java.net._
import java.nio.charset.Charset

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

  val initialRequest = """GET / HTTP/1.1
                         |Host: localhost:8080
                         |User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0
                         |Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
                         |Accept-Language: en-US,en;q=0.5
                         |Accept-Encoding: gzip, deflate
                         |Connection: keep-alive
                         |Upgrade-Insecure-Requests: 1
                         |
                         |""".stripMargin

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) ⇒
      log.error("connect failed")
      stop()

    case c@Connected(remote, local) ⇒
      log.info(s"remote host: ${remote.toString}, local host: ${local.toString}")
      val connection = sender()
      connection ! Register(self)
      connection ! Write(ByteString(initialRequest.getBytes(Charset.forName("UTF-8"))))
      context become {
        case data: ByteString ⇒
          log.error("case data: ByteString")
          connection ! Write(data)
        case CommandFailed(w: Write) ⇒
          // O/S buffer was full
          log.error("write failed")
        case Received(data) ⇒
          log.info(new String(data.toArray))
          connection ! Write(data)
        case "close" ⇒
          connection ! Close
        case _: ConnectionClosed ⇒
          log.info("connection closed")
          stop()
      }
  }

  private def stop(): Unit = {
    context stop self
    context.system.terminate()
  }
}
