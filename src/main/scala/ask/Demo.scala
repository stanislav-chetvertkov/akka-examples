package ask

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.concurrent.duration._


object Demo extends App{

  implicit val timeout = Timeout(3.seconds)

  case class Message(msg: String)
  case class Enrich(message: Message)
  case class EnrichedMessage(msg: String, result: Int)

  import akka.pattern.ask

  class DemoActor(enrichmentRef: ActorRef) extends Actor with ActorLogging{
    override def receive: Receive = {
      case msg: Message =>
        log.info(s"got message $msg")
        val result: Future[EnrichedMessage] = (enrichmentRef ? Enrich(msg)).mapTo[EnrichedMessage]
        result.foreach( r =>log.info(s"enriched message $r"))
    }
  }

  class EnrichmentActor extends Actor {
    var counter = 0
    override def receive: Receive = {
      case Enrich(msg) =>
        Thread.sleep(1000)
        sender() ! EnrichedMessage(msg.msg, counter)
        counter += 1
    }
  }


  val system = ActorSystem("test-ask")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val enrichment = system.actorOf(Props(classOf[EnrichmentActor]))
  val demo = system.actorOf(Props(classOf[DemoActor], enrichment))

  for (i <- 1 to 10){
    demo ! Message(i.toString)
  }

}
