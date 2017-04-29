package locating_actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem, Props}

object Demo extends App{

  class Counter extends Actor{
    var count = 0
    override def receive: Receive = {
      case _: Any =>
        count += 1
        println(s"count $count")
    }
  }

  val system = ActorSystem("actor-paths")
  val counter = system.actorOf(Props[Counter])

  system.actorSelection("counter")


}
