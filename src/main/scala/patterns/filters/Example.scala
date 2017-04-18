package patterns.filters

import akka.actor.{Actor, ActorRef}

class Example extends App {

  case class MyMessage(number: Int)

  trait Filter[T] {
    self: Actor =>

    def next: ActorRef

    def filter(input: T): Boolean

    override def receive: Receive = {
      case m: T if filter(m) => next ! m
    }
  }

  class FilterA(val next: ActorRef) extends Actor with Filter[MyMessage] {
    override def filter(input: MyMessage): Boolean = input.number > 100
  }

  class FilterB(val next: ActorRef) extends Actor with Filter[MyMessage] {
    override def filter(input: MyMessage): Boolean = input.number < 200
  }

}
