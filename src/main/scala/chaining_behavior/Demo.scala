package chaining_behavior

import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive

object Demo extends App{

  case object Publish
  case class SomeMessage(message: String)

  trait Receiving extends Actor{
    var receivers: Receive = Actor.emptyBehavior
    def receiver(next: Receive) { receivers = receivers orElse next }
    def receive: Receive = receivers // Actor.receive definition
  }

  trait PubSubActor extends Receiving {
    receiver {
      case Publish => /* I'm the first to handle messages */
        println("pubsub handled")
    }
  }

  class MyActor extends PubSubActor with Receiving {
    receiver {
      case SomeMessage(m) =>
        println("my actor handled")
    }
  }


  val system = ActorSystem("system")

  val ref = system.actorOf(Props(classOf[MyActor]))

  ref ! Publish
  ref ! SomeMessage("test")



}
