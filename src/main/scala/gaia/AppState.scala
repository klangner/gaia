package gaia

import java.io.File

import gaia.AppState.ScenarioState.ScenarioState

import scala.collection.mutable
import scala.io.Source

/**
  * Global mutable application state
  */
object AppState {

  object ScenarioState extends Enumeration {
    type ScenarioState = Value
    val Waiting, Success, Failed = Value
  }

  case class Scenario(id: String, config: String)

  private var scenarios: Seq[Scenario] = Seq()
  private val scenarioState: mutable.Map[String, ScenarioState] = new mutable.HashMap[String, ScenarioState]()

  /**
    * Load scenarios from the given location and replace the current ones
    */
  def loadScenarios(path: String): Unit = {
    val ext = ".scenario"
    val directory = new File(path)
    scenarios = if (directory.exists && directory.isDirectory) {
      directory.listFiles
        .filter(_.getName.endsWith(ext))
        .map { file =>
          val id = file.getName.stripSuffix(ext)
          val config = Source.fromFile(file).getLines.mkString("\n")
          Scenario(id, config)
        }
        .sortBy(_.id)
    } else {
      Seq[Scenario]()
    }
  }

  def getScenarios: List[Scenario] = {
    scenarios.toList
  }

  def getScenarioState(id: String): ScenarioState = scenarioState.getOrElse(id, ScenarioState.Waiting)

  def scenarioStateChanged(id: String, newState: ScenarioState, log: String = ""): Unit = {
    scenarioState.put(id, newState)
  }
}
