package typed._1_example

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import typed._1_example.TaskRunnerActor.TaskCompleted
import typed._1_example.TaskSupervisorActor.Task


class TaskRunnerActor(id: String, task: Task, replyTo: ActorRef) extends Actor {

  override def preStart(): Unit = {
    replyTo ! TaskCompleted(id, task.payload.toUpperCase)
  }

  override def receive: Receive = {
    case _ =>
  }
}

object TaskRunnerActor {

  case class TaskCompleted(id: String, result: String)

  case object KillTask

  case class TaskAborted()

  def props(id: String, task: Task, replyTo: ActorRef) = Props(classOf[TaskRunnerActor], id, task, replyTo)
}
