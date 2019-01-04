package healthchecker

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn


// domain model
final case class JobDescription(endpoints: Seq[String])
final case class JobResult(status: String)

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jobDescriptionFormat: RootJsonFormat[JobDescription] = jsonFormat1(JobDescription)
  implicit val jobResultFormat: RootJsonFormat[JobResult] = jsonFormat1(JobResult)
}


object MainApp extends JsonSupport {

  val host = "0.0.0.0"
  val port = 8080

  implicit val system: ActorSystem = ActorSystem("health-checker")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  val route: Route = path("run") {
    post {
      entity(as[JobDescription]) { job =>
        val reqs: Seq[Future[Boolean]] = job.endpoints.map(isAccessable)
        val result: Future[Boolean] = Future.sequence(reqs).map(xs => xs.reduce(_ && _))
        val jobResult = result.map( r => JobResult(if (r) "ok" else "failed"))
        complete(jobResult)
      }
    }
  }

  /**
    * Check if given endpoint is accessible (returns HTTP 200)
    */
  private def isAccessable(endpoint: String): Future[Boolean] = {
    Http().singleRequest(HttpRequest(uri = endpoint))
      .map { res =>
        res.discardEntityBytes()
        res.status.isSuccess()
      }.recover {
        case _ => false
      }
  }

  def main(args: Array[String]): Unit = {
    val bindingFuture = Http().bindAndHandle(route, host, port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
