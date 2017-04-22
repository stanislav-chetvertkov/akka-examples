package routing.remote

import akka.actor.Actor

object Remote {

  case class TestMessage(message: String)

  class Processor extends Actor {
    override def receive: Receive = {
      case m =>
        println(self.path.toString + " got message:" + m)
    }
  }


}
