package gaia.apps.core

object Api {

  final case class RunJob(id: String, config: String)

  sealed trait JobResult

  case class JobSucceeded(jobId: String) extends JobResult

  case class JobFailed(jobId: String, reason: String) extends JobResult

}
