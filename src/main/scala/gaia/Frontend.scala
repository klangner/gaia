package gaia

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import gaia.AppState.Scenario
import gaia.AppState.ScenarioState.ScenarioState
import gaia.twirl.TwirlMarshaller._


object Frontend {

  case class ScenarioInfo(id: String, name: String, config: String, log: String, state: ScenarioState)

  val route: Route = path("refresh") {
    get {
      MainApp.reloadScenarios()
      redirect("/", StatusCodes.SeeOther)
    }
  } ~ path("scenario" / Remaining) { id =>
    get {
      getScenario(id) match {
        case Some(s) => complete(html.scenario.render(s))
        case None => reject()
      }
    }
  } ~ pathEndOrSingleSlash {
    get {
      val scenarios = AppState.getScenarios.map(mapScenarios)
      complete(html.index.render(scenarios))
    }
  }

  private def mapScenarios(scenario: Scenario): ScenarioInfo = {
    ScenarioInfo(scenario.id, scenario.id, "", "", AppState.getScenarioState(scenario.id))
  }

  private def getScenario(id: String): Option[ScenarioInfo] = {
    AppState.getScenario(id).map { s =>
        ScenarioInfo(s.id, s.id, s.config, AppState.getLog(s.id).getOrElse(""),
          AppState.getScenarioState(s.id))
      }
  }

}
