package gaia.apps.core

object Api {

  final case class RunJob(config: String)

  sealed trait JobResult

  case object Success extends JobResult

  case class Failure(reason: String) extends JobResult

}
