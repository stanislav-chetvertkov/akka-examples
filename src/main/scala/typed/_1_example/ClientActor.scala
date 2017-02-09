package typed._1_example

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import typed._1_example.TaskRunnerActor.TaskCompleted
import typed._1_example.TaskSupervisorActor.{SubmitTask, Task, TaskSubmitted}

class ClientActor(supervisorActorRef: ActorRef) extends Actor with ActorLogging{

  override def preStart(): Unit = {
    supervisorActorRef ! SubmitTask(Task("test"))
  }

  override def receive: Receive = {
    case TaskCompleted(id, result) =>
      log.info(s"Task Completed id:$id, result:$result")

    case TaskSubmitted(id) =>
      log.info(s"Task $id was submitted")

  }
}

object ClientActor {
  def props(supervisorActorRef: ActorRef): Props = Props(classOf[ClientActor], supervisorActorRef)
}
