package routing.remote

import akka.actor.{ActorSystem, Address, AddressFromURIString, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory
import routing.remote.Remote.Processor

object RemoteDemo extends App {

  val config = ConfigFactory.parseString(
    """
      |akka {
      |  actor {
      |    provider = remote
      |  }
      |  remote {
      |    enabled-transports = ["akka.remote.netty.tcp"]
      |    netty.tcp {
      |      hostname = "127.0.0.1"
      |      port = 2551
      |    }
      |  }
      |}
    """.stripMargin)

  val system = ActorSystem("client", config)

  val addresses = Seq(
    Address("akka.tcp", "RemoteTestSystem", "127.0.0.1", 2552),
    AddressFromURIString("akka.tcp://RemoteTestSystem@127.0.0.1:2552")
  )

  val remoteRouter = system.actorOf(
    RemoteRouterConfig(RoundRobinPool(5), addresses).props(
      Props(classOf[Processor])), "poolRouter-code")

  for (i <- 1 to 10) remoteRouter ! i

}

// Processes the messages
object RemoteSystem extends App {

  val config = ConfigFactory.parseString(
    """
      |akka {
      |  actor {
      |    provider = remote
      |  }
      |  remote {
      |    enabled-transports = ["akka.remote.netty.tcp"]
      |    netty.tcp {
      |      hostname = "127.0.0.1"
      |      port = 2552
      |    }
      |  }
      |}
    """.stripMargin)

  val remoteSystem = ActorSystem("RemoteTestSystem", config)

}

// Akka has two ways of using remoting:
// Lookup : used to look up an actor on a remote node with actorSelection(path)
// Creation : used to create an actor on a remote node with actorOf(Props(...), actorName)
