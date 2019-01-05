package gaia.apps.healthchecker

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import gaia.apps.core.Api._

import scala.concurrent.Future

/**
  * Checks system health by calling list of HTTP endpoints
  */
object HealthChecker {
  def props(): Props = Props(new HealthChecker())
}

class HealthChecker extends Actor with ActorLogging {

  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  private val http = Http(context.system)

  def receive: Receive = {
    case RunJob(id, config) =>
      println(s"Run job $id with config:\n" + config)
      runJob(id, config, sender)
  }

  private def runJob(jobId: String, config: String, sender: ActorRef): Unit = {
    val endpoints = config.split('\n')
    val reqs: Seq[Future[Either[String, Unit]]] = endpoints.map(checkHealth)
    Future.sequence(reqs)
      .map(_.reduce(joinResults))
      .foreach {
        case Left(err) => sender ! JobFailed(jobId, err)
        case Right(_) => sender ! JobSucceeded(jobId)
      }
  }

  private def checkHealth(endpoint: String): Future[Either[String, Unit]] = {
    implicit val system: ActorSystem = context.system
    http.singleRequest(HttpRequest(uri = endpoint))
      .map { res =>
        res.discardEntityBytes()
        Right()
      }.recover {
      case e => Left(e.getMessage)
    }
  }

  private def joinResults(r1: Either[String, Unit], r2: Either[String, Unit]): Either[String, Unit] = {
    if (r1.isLeft) r1 else r2
  }
}
