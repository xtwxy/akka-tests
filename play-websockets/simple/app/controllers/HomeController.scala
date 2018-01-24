package controllers

import javax.inject._

import play.api._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import akka.actor.ActorSystem
import akka.stream.Materializer
import models.MyWebSocketActor

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {
  private val logger = Logger(getClass)
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      logger.info("ws connection accepted.")
      MyWebSocketActor.props(out)
    }
  }
}
