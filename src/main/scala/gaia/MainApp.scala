package gaia

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import gaia.ScenarioScheduler.{AddScenario, RemoveAllScenarios}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}


object MainApp {

  val host = "0.0.0.0"
  val port = 8080

  implicit val system: ActorSystem = ActorSystem("health-checker")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  val scheduler: ActorRef = system.actorOf(ScenarioScheduler.props(), "scheduler")


  val route: Route =
    Frontend.route ~ pathPrefix("static") {
      getFromDirectory("./static")
    }

  def main(args: Array[String]): Unit = {
    reloadScenarios()
    runServer()
  }

  def reloadScenarios() = {
    AppState.loadScenarios()

    scheduler ! RemoveAllScenarios
    AppState.getScenarios.foreach { scenario =>
      scheduler ! AddScenario(scenario.id, scenario.config)
    }
  }

  private def runServer(): Unit = {
    println(s"Server online at http://localhost:$port/")
    Await.result(Http().bindAndHandle(route, host, port), Duration.Inf)
  }
}
