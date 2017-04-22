package supervision

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.duration._

object SupervisionDemo extends App {

  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._
  import scala.concurrent.duration._

  // Restart - tries to restart the SAME actor

  class Parent extends Actor {

    override val supervisorStrategy: OneForOneStrategy =
      OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1.minute) {
        // tries to restart if there are more than maxNrOfRetries failures within a withinTimeRange it stops
        // will be restarting constantly if I decrease withinTimeRange to something less than 6 seconds
        case _: MyException              => Restart
        case _: NullPointerException     => Restart
        case _: IllegalArgumentException => Stop
        case _: Exception                => Escalate

      }

    // Stop - would just stop the actor



    override def preStart(): Unit = {
      context.actorOf(Props[Child], "child")
    }

    override def receive: Receive = {
      case _ =>
    }
  }

  class Child extends Actor {
    implicit val ec = system.dispatcher
    context.system.scheduler.scheduleOnce(2.seconds,self, CauseFailure)

    override def receive: Receive = {
      case CauseFailure => throw MyException("failure")
    }
  }

  case object CauseFailure
  case class MyException(message: String) extends Exception


  val system = ActorSystem("test")
  system.actorOf(Props[Parent], "parent")

}
