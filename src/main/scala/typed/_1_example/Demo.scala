package typed._1_example

import akka.actor.ActorSystem

object Demo extends App{

  implicit val system = ActorSystem("demo")


  val taskSupervisorRef = system.actorOf(TaskSupervisorActor.props())
  val client = system.actorOf(ClientActor.props(taskSupervisorRef))


}
