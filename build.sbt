name := "gaia"

version := "0.1"

scalaVersion := "2.12.8"

val akkaActors = "2.5.19"
val akkaHttp = "10.1.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaActors,
  "com.typesafe.akka" %% "akka-stream" % akkaActors,
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}

scalacOptions := Seq("-unchecked", "-deprecation")

assemblyJarName in assembly := "gaia.jar"
