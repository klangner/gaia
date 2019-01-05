package gaia

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import gaia.MainApp.system
import gaia.apps.core.Api.{Failure, RunJob, Success}
import gaia.apps.healthchecker.HealthChecker


object ScenarioScheduler {
  def props(): Props = Props(new ScenarioScheduler)

  case class AddScenario(id: String, config: String)
}

class ScenarioScheduler extends Actor with ActorLogging{

  import ScenarioScheduler._

  val healthChecker: ActorRef = system.actorOf(HealthChecker.props())

  override def receive: Receive = {
    case AddScenario(id, config) =>
      healthChecker ! RunJob(config)

    case Success =>
      println("Job succeeded")
    case Failure(err) =>
      println("Error running job")
      println(err)
  }
}
