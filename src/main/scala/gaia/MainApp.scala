package gaia

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import gaia.ScenarioScheduler.AddScenario

import scala.concurrent.ExecutionContext
import scala.io.StdIn


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
    AppState.loadScenarios()

    AppState.getScenarios.foreach{ scenario =>
      scheduler ! AddScenario(scenario.id, scenario.config)
    }
    runServer()
  }

  private def runServer(): Unit = {
    val bindingFuture = Http().bindAndHandle(route, host, port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
