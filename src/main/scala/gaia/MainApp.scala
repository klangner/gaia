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

  val route: Route = path("/") {
    get {
      complete("ok")
    }
  }

  def main(args: Array[String]): Unit = {
    scheduler ! AddScenario("1", "https://google.com\nhttps://google.com")
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
