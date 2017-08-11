package hello

import core.BigCore

object Hello extends Greeting with App {
  println(greeting)
  val core = new BigCore()
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
