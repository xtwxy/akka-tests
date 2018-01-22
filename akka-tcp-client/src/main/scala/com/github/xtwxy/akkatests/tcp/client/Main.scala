package com.github.xtwxy.akkatests.tcp.client

import java.net.InetSocketAddress

import akka.actor._

object Main extends App {
  if(args.length < 2) {
    // too few arguments.
    print("too few arguments.")
  } else {
    val sys = ActorSystem("client")
    val client = sys.actorOf(EchoClient.props(new InetSocketAddress(args(0), args(1).toInt)))
  }

}
