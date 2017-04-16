package event_bus.helper

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import event_bus.Example.DownstreamMessage


object Example extends App{

  import akka.actor.{Actor, ActorLogging}

  import scala.reflect.ClassTag

  trait CommandsSubscriber[T] {
    this: Actor with ActorLogging =>

    val ttag: ClassTag[T]

    def processEvent(event: T): Unit

    override def preStart(): Unit = {
      log.info("subscribing " + self.path.name + " to event: " + ttag.runtimeClass.getSimpleName)
      context.system.eventStream.subscribe(context.self, ttag.runtimeClass.asInstanceOf[Class[T]])
    }

  }

  case class DownstreamMessage(message: String)

  class Publisher extends Actor {

    override def preStart(): Unit = {
      context.system.eventStream.publish(DownstreamMessage("test"))
    }

    def receive: Receive = {
      case _ =>
    }
  }

  class Subscriber extends Actor with ActorLogging with CommandsSubscriber[DownstreamMessage]{

    override implicit val ttag: ClassTag[DownstreamMessage] = ClassTag(classOf[DownstreamMessage])

    override def processEvent(event: DownstreamMessage): Unit = log.info(s"got message: $event")

    override def receive: Receive = {
      case m: DownstreamMessage => processEvent(m)
    }

  }


  val system = ActorSystem("eventbus-example")

  //reverse the initialization order and subscriber won't get the message
  val subscriber = system.actorOf(Props[Subscriber])
  Thread.sleep(1000)
  val publisher = system.actorOf(Props[Publisher])

}
