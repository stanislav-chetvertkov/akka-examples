package routing.supervision

import akka.actor.{ActorSystem, Props, SupervisorStrategy}
import akka.routing.RoundRobinPool

object Demo extends App {

  val system = ActorSystem("test")

  val myStrategy = SupervisorStrategy.defaultStrategy
//  val router = system.actorOf(
//      RoundRobinPool(5,supervisorStrategy = myStrategy).props(Props[TestSuper]),
//    "roundrobinRouter"
//  )

}
