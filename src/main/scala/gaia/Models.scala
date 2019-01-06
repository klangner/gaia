package gaia

import gaia.Models.ScenarioState.ScenarioState

object Models {

  object ScenarioState extends Enumeration {
    type ScenarioState = Value
    val Waiting, Success, Failed = Value
  }

  case class Scenario(id: String, state: ScenarioState)

}
