package terminating_an_actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object ExampleContextStop extends App {

  class Victim extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(s"got message: $message")
    }
  }

  val config = ConfigFactory.parseString("akka.actor.default-dispatcher.throughput=1")

  val actorSystem = ActorSystem("termination-example", config)

  val victimRef = actorSystem.actorOf(Props[Victim])

  for (i <- 1 to 10) {
    victimRef ! i
    if (i == 5) {
      actorSystem.stop(victimRef)
    }
  }


}
