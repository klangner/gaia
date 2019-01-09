package gaia

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, Props}
import gaia.AppState.ScenarioState
import gaia.MainApp.system
import gaia.apps.core.Api._
import gaia.apps.healthchecker.HealthChecker

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.Random


/**
  * Schedule scenario every 1 minute with random starting time
  */
object ScenarioScheduler {
  def props(): Props = Props(new ScenarioScheduler)

  case class AddScenario(id: String, config: String)
  case object RemoveAllScenarios
}

class ScenarioScheduler extends Actor with ActorLogging{

  import ScenarioScheduler._
  import context.dispatcher

  val healthChecker: ActorRef = system.actorOf(HealthChecker.props())
  var runningScenarios: mutable.Set[Cancellable] = mutable.HashSet[Cancellable]()

  override def receive: Receive = {
    case AddScenario(id, config) =>
      scheduleScenario(id, config)
    case JobSucceeded(id) =>
      AppState.scenarioStateChanged(id, ScenarioState.Success)
    case JobFailed(id, err) =>
      AppState.scenarioStateChanged(id, ScenarioState.Failed, err)
    case RemoveAllScenarios =>
      stopAllScenarios()
  }

  private def scheduleScenario(id: String, config: String): Unit = {
    val initialDelay = Random.nextInt(60)
    val cancellable = context.system.scheduler.schedule(initialDelay.seconds, 1.minutes)(runJob(id, config))
    runningScenarios += cancellable
  }

  private def runJob(id: String, config: String): Unit = {
    healthChecker ! RunJob(id, config)
  }

  private def stopAllScenarios(): Unit = {
    runningScenarios.foreach(_.cancel())
    runningScenarios.clear()
  }
}
