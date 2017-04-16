package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

object Demo extends App{


  def apiRoute(): Route = {

    val route: Route = {
      path("stop") {
        post {
          complete {
            "ok"
          }
        }
      }
    }

    route
  }

  implicit val system = ActorSystem("http-test")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val mt: ActorMaterializer = ActorMaterializer()

  val port = 8181

  val binding: ServerBinding =
    Await.result(Http().bindAndHandle(Demo.apiRoute(), "localhost", port), 5.seconds)

}
