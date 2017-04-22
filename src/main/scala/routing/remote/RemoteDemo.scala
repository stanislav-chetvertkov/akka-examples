package routing.remote

import akka.actor.{ActorSystem, Address, AddressFromURIString, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory
import routing.remote.Remote.Processor

object RemoteDemo extends App{

  val config = ConfigFactory.parseString(
    """
      |akka {
      |  remote {
      |    enabled-transports = ["akka.remote.netty.tcp"]
      |    netty.port = 2551
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

object RemoteSystem extends App {

  val config = ConfigFactory.parseString(
    """
      |akka {
      |  remote {
      |    enabled-transports = ["akka.remote.netty.tcp"]
      |    netty.port = 2552
      |  }
      |}
    """.stripMargin)

  val remoteSystem = ActorSystem("RemoteTestSystem", config)

  remoteSystem
}
