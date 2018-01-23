package com.github.xtwxy.akkatests.tcp.server

import java.net.InetSocketAddress

import akka.actor._

object Main extends App {
  if(args.length < 2) {
    // too few arguments.
    print("too few arguments.")
  } else {
    val sys = ActorSystem("client")
    val client = sys.actorOf(EchoServer.props(new InetSocketAddress(args(0), args(1).toInt)))
  }

}
