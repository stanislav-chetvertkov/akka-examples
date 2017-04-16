package event_bus

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object HierarchyExample extends App{

  case class DownstreamMessage(message: String)

  class Publisher extends Actor {

    override def preStart(): Unit = {
      context.system.eventStream.publish(DownstreamMessage("test"))
    }

    def receive: Receive = {
      case _ =>
    }
  }

  class Subscriber extends Actor with ActorLogging{

    override def preStart: Unit = {
      context.actorOf(Props[SubscriberChild])
      context.system.eventStream.subscribe(context.self, classOf[DownstreamMessage])
    }

    def receive: Receive = {
      case DownstreamMessage(message) =>
        log.info(s"got message: $message")
    }
  }

  class SubscriberChild extends Actor with ActorLogging {
    override def preStart: Unit = {
      context.system.eventStream.subscribe(context.self, classOf[DownstreamMessage])
    }

    def receive: Receive = {
      case DownstreamMessage(message) =>
        log.info(s"child got message: $message")
    }
  }

  val system = ActorSystem("eventbus-example")

  //reverse the initialization order and subscriber won't get the message
  val subscriber = system.actorOf(Props[Subscriber])
  Thread.sleep(1000)
  val publisher = system.actorOf(Props[Publisher])

}
