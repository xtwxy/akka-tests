package com.wincom.dcim.rest

import akka.http.scaladsl.server.Route
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http.ServerBinding
import scala.concurrent.Future
import com.typesafe.config.Config
import akka.util.Timeout
import akka.event.Logging
import akka.actor.ActorRef


trait WebServer extends RequestTimeout {
  def startService(shoppers: ActorRef)(implicit system: ActorSystem) = {
    val config = system.settings.config
    val settings = Settings(system)
    val host = settings.http.host
    val port = settings.http.port

    implicit val ec = system.dispatcher

    val api = new FsuService(shoppers, system, requestTimeout(config)).routes
 
    implicit val materializer = ActorMaterializer()
    val bindingFuture: Future[ServerBinding] =
      Http().bindAndHandle(api, host, port)
   
    val log =  Logging(system.eventStream, "fsus")
    bindingFuture.map { serverBinding =>
      log.info(s"Shoppers API bound to ${serverBinding.localAddress} ")
    }.onFailure { 
      case ex: Exception =>
        log.error(ex, "Failed to bind to {}:{}!", host, port)
        system.terminate()
    }
  }

}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
