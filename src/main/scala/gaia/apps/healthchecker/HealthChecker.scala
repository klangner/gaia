package gaia.apps.healthchecker

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import gaia.apps.core.Api.{Failure, JobResult, RunJob, Success}

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
    case RunJob(config) =>
      println("Run job with config " + config)
      runJob(config, sender)
  }

  private def runJob(config: String, sender: ActorRef): Unit = {
    val endpoints = config.split('\n')
    val reqs: Seq[Future[JobResult]] = endpoints.map(checkHealth)
    Future.sequence(reqs)
      .map(_.reduce(joinJobResults))
      .foreach(res => sender ! res)
  }

  private def checkHealth(endpoint: String): Future[JobResult] = {
    implicit val system: ActorSystem = context.system
    http.singleRequest(HttpRequest(uri = endpoint))
      .map { res =>
        res.discardEntityBytes()
        Success
      }.recover {
      case e => Failure(e.getMessage)
    }
  }

  private def joinJobResults(r1: JobResult, r2: JobResult): JobResult = {
    if (r1 != Success) r1 else r2
  }
}
