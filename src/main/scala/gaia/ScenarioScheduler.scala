package gaia

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import gaia.AppState.ScenarioState
import gaia.MainApp.system
import gaia.apps.core.Api._
import gaia.apps.healthchecker.HealthChecker

import scala.concurrent.duration._
import scala.util.Random


/**
  * Schedule scenario every 1 minute with random starting time
  */
object ScenarioScheduler {
  def props(): Props = Props(new ScenarioScheduler)

  case class AddScenario(id: String, config: String)
}

class ScenarioScheduler extends Actor with ActorLogging{

  import ScenarioScheduler._
  import context.dispatcher

  val healthChecker: ActorRef = system.actorOf(HealthChecker.props())

  override def receive: Receive = {
    case AddScenario(id, config) =>
      scheduleScenario(id, config)
    case JobSucceeded(id) =>
      AppState.scenarioStateChanged(id, ScenarioState.Success)
    case JobFailed(id, err) =>
      AppState.scenarioStateChanged(id, ScenarioState.Failed, err)
  }

  private def scheduleScenario(id: String, config: String): Unit = {
    val initialDelay = Random.nextInt(60)
    context.system.scheduler.schedule(initialDelay.seconds, 1.minutes)(healthChecker ! RunJob(id, config))
  }

}
