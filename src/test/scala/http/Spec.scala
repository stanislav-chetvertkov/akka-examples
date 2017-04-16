package http

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class Spec extends WordSpec with Matchers with ScalatestRouteTest {

  "Application Route" should {

    "handle the request" in {

      Post("/stop") ~> Demo.apiRoute() ~> check {
      }
    }
  }

}
