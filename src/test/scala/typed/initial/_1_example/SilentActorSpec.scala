package typed_example

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import org.scalatest.{MustMatchers, WordSpecLike}
import typed.initial._1_example.TaskSupervisorActor

class SilentActor01Test extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with MustMatchers {

  "A Silent Actor" must {
    "change state when it receives a message, single threaded" in {

      val probe = TestProbe()
      val supervisorRef = system.actorOf(TaskSupervisorActor.props())




      supervisorRef
    }

  }
}
