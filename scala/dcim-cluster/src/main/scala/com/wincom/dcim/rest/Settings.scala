package com.wincom.dcim.rest

import scala.concurrent.duration.Duration
import com.typesafe.config.Config
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.ExtensionKey

object Settings extends ExtensionKey[Settings]

class Settings(config: Config) extends Extension {
  def this(system: ExtendedActorSystem) = this(system.settings.config)

  val passivateTimeout = Duration(config.getString("passivate-timeout"))

  object http {
    val host = config.getString("http.host")
    val port = config.getInt("http.port")
  }
}