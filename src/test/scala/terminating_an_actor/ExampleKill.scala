package terminating_an_actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Kill, Props}
import akka.actor.Actor.Receive


object ExampleKill extends App {

  class Victim extends Actor with ActorLogging{
    override def receive: Receive = {
      case message => log.info(s"got message: $message")
    }
  }

  val actorSystem = ActorSystem("termination-example")

  val victimRef = actorSystem.actorOf(Props[Victim])

  for (i <- 1 to 10){
    victimRef ! i
    if (i == 5) {
      // actor will throw akka.actor.ActorKilledException
      // all of the messages after this won't be processed
      victimRef ! Kill
    }
  }

}
