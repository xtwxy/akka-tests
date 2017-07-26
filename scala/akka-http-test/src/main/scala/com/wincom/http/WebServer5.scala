package com.wincom.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn

import scala.concurrent.Future

object WebServer5 {
  final case class Fsu(name: String, id: Long)
  final case class Fsus(items: List[Fsu])
  
  implicit val itemFormat = jsonFormat2(Fsu)
  implicit val orderFormat = jsonFormat1(Fsus)
  
   def main(args: Array[String]) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route =
      get {
        pathPrefix("fsu" / LongNumber) { id =>
          complete(id.toString())
        }
      } ~
        post {
          path("create-fsu") {
            entity(as[Fsu]) { fsu => 
              complete(fsu)
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap { _.unbind() }
      .onComplete { _ => system.terminate() }
  }
 
}