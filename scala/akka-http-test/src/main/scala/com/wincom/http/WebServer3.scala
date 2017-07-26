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


object WebServer3 {
  final case class Bid(userId: String, offer: Int)
  final case object GetBids
  final case class Bids(bids: List[Bid])

  implicit val bidFormat = jsonFormat2(Bid)
  implicit val bidsFormat = jsonFormat1(Bids)

//  class Auction extends Actor with ActorLogging {
//    var bids = List.empty[Bid]
//    for (x <- 0 until 10) bids = bids :+ Bid("wangdy", 1000 + x)
//    def receive = {
//      case bid @ Bid(userId, offer) =>
//        bids = bids :+ bid
//        log.info(s"Bid complete: $userId, $offer")
//      case GetBids => sender() ! Bids(bids)
//      case _ => log.info("Invalid message")
//    }
//  }

  def main(args: Array[String]) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    //val auction = system.actorOf(Props[Auction], "auction")

    val route: Route =
      path("auction") {
//        put {
//          parameter("bid".as[Int], "user") { (bid, user) =>
//            auction ! Bid(user, bid)
//            complete((StatusCodes.Accepted, "bid placed"))
//          }
//        } ~
//          get {
//            implicit val timeout: Timeout = 5.seconds
//            val bids: Future[Bids] = (auction ? GetBids).mapTo[Bids]
//            complete(bids)
//          } ~
          post {
            entity(as[Bid]) { bid =>
              complete(bid)
            }
          }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println("Server started on http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}