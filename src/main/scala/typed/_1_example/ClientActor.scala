package typed._1_example

import akka.actor.{Actor, Props}

class ClientActor extends Actor {
    override def receive: Receive = {
      case _ =>
    }
}

object ClientActor {
  def props: Props = Props(classOf[ClientActor])
}
