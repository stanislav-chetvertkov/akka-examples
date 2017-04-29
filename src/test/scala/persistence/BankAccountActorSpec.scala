package persistence

import akka.actor._
import akka.testkit._
import org.scalatest._

class BankAccountActorSpec extends PersistenceSpec(ActorSystem("test")) with PersistenceCleanup {

}
