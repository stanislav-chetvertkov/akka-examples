package routing

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.routing.{BalancingPool, FromConfig}
import com.typesafe.config.ConfigFactory
import routing.remote.Remote.Processor

object RoutingDemo extends App{


  /**
    * This router distributes the messages to the idle routees.
    * It does this internally differently than the other routers.
    * It uses one mailbox for all the routees.
    * The router is able to do this by using a special dispatcher for the routees.
    * This is also the reason that only the pool version is available.
    */

  val system = ActorSystem("routing", ConfigFactory.parseString(
    """
      |akka.actor.deployment {
      |  /poolRouter {
      |    router = balancing-pool
      |    nr-of-instances = 5
      |}}
    """.stripMargin))

  // Round Robin

  // creating a router using the cofiguration

  val router = system.actorOf(
    FromConfig.props(Props(classOf[Processor])), "poolRouter" //name matches to the one in the configuration
  )

  for (i <- 1 to 10) router ! i


  // creating in code

  val router2 = system.actorOf(
    BalancingPool(5).props(Props(classOf[Processor])), "poolRouter2"
  )

  for (i <- 1 to 10) router2 ! i

  // if send Kill or PoisonPill router will terminate

  router2 ! PoisonPill

  Thread.sleep(1000)

  router2 ! "test"

}
