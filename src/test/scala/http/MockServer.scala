package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

/**
  * Mock Akka HTTP Server, will handle requests which is provided with handle request
  */
object MockServer {

  /**
    * Instructs to launch new instance of the Mock server and serve mocked requests
    * @param system ActorSystem, used to initialize mocks
    * @param response mocked response
    * @param httpRequest request to which mock server must react
    * @return Future containing bind event
    */
  def handleRequest(response: HttpResponse, httpRequest: HttpRequest)(implicit system: ActorSystem): Future[Http.ServerBinding] = {
    implicit val materializer = ActorMaterializer()

    val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
      Http().bind(interface = "localhost", port = 0)

    val requestPath = httpRequest.uri.path.toString()

    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(httpRequest.method, Uri.Path(`requestPath`), _, _, _) =>
        response
      case _: HttpRequest =>
        HttpResponse(404, entity = "Unknown resource!")
    }

    serverSource.to(Sink.foreach { connection =>
      println("Mock Server accepted new connection from " + connection.remoteAddress)
      connection handleWithSyncHandler requestHandler
    }).run()
  }
}