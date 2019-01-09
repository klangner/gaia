package gaia

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import gaia.AppState.Scenario
import gaia.AppState.ScenarioState.ScenarioState
import gaia.twirl.TwirlMarshaller._


object Frontend {

  case class ScenarioInfo(name: String, state: ScenarioState)

  val route: Route = path("refresh") {
    get {
      MainApp.reloadScenarios()
      redirect("/", StatusCodes.SeeOther)
    }
  } ~ pathEndOrSingleSlash {
    get {
      val scenarios = AppState.getScenarios.map(mapScenarios)
      complete(html.index.render(scenarios))
    }
  }

  private def mapScenarios(scenario: Scenario): ScenarioInfo = {
    ScenarioInfo(scenario.id, AppState.getScenarioState(scenario.id))
  }

}
