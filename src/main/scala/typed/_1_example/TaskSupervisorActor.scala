package typed._1_example

import java.util.UUID

import akka.actor.{Actor, ActorRef, PoisonPill, Props, Terminated}
import TaskSupervisorActor._
import typed._1_example.TaskRunnerActor.KillTask

class TaskSupervisorActor extends Actor{

  var runners: Map[String, ActorRef] = Map.empty

  override def receive: Receive = {
    case SubmitTask(task) =>
      val id = UUID.randomUUID().toString
      val replyTo = sender()
      val runner = context.actorOf(TaskRunnerActor.props(id, task, replyTo))
      runners += id -> runner

      replyTo ! SubmitTaskResponse(runner)


    case GetTaskStatus(id) =>
      val replyTo = sender()
      runners.get(id) match {
        case Some(ref) => replyTo ! GetStatusResponse(id, "running")
        case _ => replyTo ! GetStatusResponse(id, "not found/killed")
      }

    case Terminated(ref) =>
      runners = runners.filter{case (_, r) => r != ref}
  }

}

object TaskSupervisorActor {

  case class Task(payload: String)

  case class SubmitTask(task: Task)

  case class SubmitTaskResponse(handler: ActorRef)

  case class CancelTask(id: String)

  case class GetTaskStatus(id: String)

  case class GetStatusResponse(id: String, status: String)

  case object ListTasks

  def props(): Props  = Props(classOf[TaskSupervisorActor])
}