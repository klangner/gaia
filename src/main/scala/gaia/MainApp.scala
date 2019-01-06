package gaia

import java.io.File

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import gaia.Models.{Scenario, ScenarioState}
import gaia.ScenarioScheduler.AddScenario
import gaia.twirl.TwirlMarshaller._

import scala.concurrent.ExecutionContext
import scala.io.{Source, StdIn}


object MainApp {

  val host = "0.0.0.0"
  val port = 8080

  implicit val system: ActorSystem = ActorSystem("health-checker")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  val scheduler: ActorRef = system.actorOf(ScenarioScheduler.props(), "scheduler")

  var scenarios: Seq[Scenario] = Seq()


  val route: Route = pathEndOrSingleSlash {
    get {
      complete(html.index.render(scenarios))
    }
  } ~ pathPrefix("static") {
    getFromDirectory("./static")
  }

  def main(args: Array[String]): Unit = {
    val scenarioData: Seq[(String, String)] = loadScenarios()

    scenarios = scenarioData.map(d => Scenario(d._1, ScenarioState.Waiting))

    scenarioData.foreach{ scenario =>
      scheduler ! AddScenario(scenario._1, scenario._2)
    }
    runServer()
  }

  def loadScenarios(): Seq[(String, String)] = {
    val ext = ".scenario"
    val d = new File("./scenarios")
    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter(_.getName.endsWith(ext))
        .map{ file =>
          (file.getName.stripSuffix(ext), Source.fromFile(file).getLines.mkString("\n"))
        }
    } else {
      List[(String, String)]()
    }
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
