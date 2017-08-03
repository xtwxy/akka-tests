package com.wincom.dcim.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

import scala.concurrent.Future
import spray.json.DefaultJsonProtocol
import scala.util.Try
import akka.util.Timeout
import akka.actor._
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import com.wincom.dcim.sharded.FsuActor
import com.wincom.dcim.sharded.FsuActor._



class FsuService(val fsuShard: ActorRef, val system: ActorSystem, val requestTimeout: Timeout) extends FsuRoutes {
  val executionContext = system.dispatcher
}

trait FsuRoutes extends DefaultJsonProtocol {
  implicit val fsuFormat = jsonFormat2(Fsu)
  implicit val fsusFormat = jsonFormat1(Fsus)

  def fsuIdSegment = Segment.flatMap(id => Try(id.toInt).toOption)

  def fsuShard: ActorRef
  implicit def requestTimeout: Timeout
  implicit def executionContext: ExecutionContext


  def routes: Route =
    create ~ update

  def create = {
    path("fsu") {
      post {
        pathEnd {
          entity(as[Fsu]) { fsu =>
            onSuccess(fsuShard.ask(CreateFsu(fsu.id, fsu.name)).mapTo[Command]) {
              case _ => complete(OK)
            }
          }
        }
      }
    }
  }
  
  def update =
    pathPrefix("fsu" / fsuIdSegment /) { fsuId =>
      get {
        pathEnd {
          fsuShard ! CreateFsu(fsuId, "Wangxy")
          complete(OK)
        }
      } ~
      put {
        complete(OK)
      } ~
      delete {
        complete(OK)
      }
    }
}