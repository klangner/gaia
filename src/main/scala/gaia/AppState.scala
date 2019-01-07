package gaia

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}
import com.typesafe.config.{Config, ConfigFactory}
import gaia.AppState.ScenarioState.ScenarioState

import scala.collection.JavaConverters._
import scala.collection.mutable


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
    * Load scenarios from database
    */
  def loadScenarios(): Unit = {
    val config: Config = ConfigFactory.defaultApplication().resolve()
    val tableName = config.getString("scenarios.table")
    val accessKey = config.getString("aws.accessKey")
    val secretKey = config.getString("aws.secretKey")
    val awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
    val client: AmazonDynamoDB = AmazonDynamoDBClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .build()
    val dynamoDB = new DynamoDB(client)
    val table = dynamoDB.getTable(tableName)

    val query: QuerySpec = new QuerySpec()
      .withKeyConditionExpression("environment = :v_env")
      .withValueMap(new ValueMap().withString(":v_env", "dev"))

    val results = table.query(query)
    scenarios = results.iterator().asScala.map { r =>
      val id: String = r.asMap().asScala.getOrElse("id", "").toString
      val config: String = r.asMap().asScala.getOrElse("config", "").toString
      Scenario(id, config)
    }.toSeq
  }

  def getScenarios: List[Scenario] = {
    scenarios.toList
  }

  def getScenarioState(id: String): ScenarioState = scenarioState.getOrElse(id, ScenarioState.Waiting)

  def scenarioStateChanged(id: String, newState: ScenarioState, log: String = ""): Unit = {
    scenarioState.put(id, newState)
  }
}
