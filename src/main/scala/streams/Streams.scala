package streams

import java.nio.file.StandardOpenOption

import scala.concurrent.Future

object Streams extends App{

  import akka.stream._
  import akka.stream.scaladsl._

  import akka.{ NotUsed, Done }
  import akka.actor.ActorSystem
  import akka.util.ByteString
  import scala.concurrent._
  import scala.concurrent.duration._
  import java.nio.file.Paths


  implicit val system = ActorSystem("streams")
  val materializer = ActorMaterializer()

  // type of elements and aux value type
  val source: Source[Int, NotUsed] = Source(1 to 100)

  source.runForeach(i => println(i))(materializer)

}
