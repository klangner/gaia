name := "gaia"

version := "0.1"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

libraryDependencies ++= {
  val akkaActorsV = "2.5.19"
  val akkaHttpV = "10.1.6"
  val twirlV = "1.3.15"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaActorsV,
    "com.typesafe.akka" %% "akka-stream" % akkaActorsV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.play" %% "twirl-api" % twirlV
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}

scalacOptions := Seq("-unchecked", "-deprecation")

assemblyJarName in assembly := "gaia.jar"
