package persistence

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import persistence.Demo.Command.{Add, PrintResult}
import persistence.Demo.Event.{Added, WithDrawn}

object Demo extends App{

  sealed trait Command
  object Command {
    case class Add(value: Double) extends Command
    case class WithDraw(value: Double) extends Command
    case object PrintResult extends Command
  }

  sealed trait Event
  object Event {
    case class Added(value: Double) extends Event
    case class WithDrawn(value: Double) extends Event
  }

  case class BankAccount(value: Double)

  class BankAccountActor extends PersistentActor with ActorLogging {
    override def persistenceId: String = "test-id"

    override def receiveRecover: Receive = {
      case event: Event => updateState(event)
      case RecoveryCompleted => log.info("bank account recovery completed")
    }

    override def receiveCommand: Receive = {
      case Add(value) => persist(Added(value))(updateState)
      case WithDrawn(value) => persist(WithDrawn(value))(updateState)
      case PrintResult => sender() ! state
    }


    var state = BankAccount(0)

    def updateState(event: Event): Unit = event match {
      case Added(value) => state = BankAccount(state.value + value)
      case WithDrawn(value) => state = BankAccount(state.value - value)
    }

  }

}
