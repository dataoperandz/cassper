
name := "cassper"

version := "0.1"

libraryDependencies ++= {
  val cassandraVersion = "3.1.1"
  val akkaVersion = "2.5.19"

  Seq(
    "com.datastax.cassandra" % "cassandra-driver-core" % cassandraVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )
}

scalaVersion := "2.11.12"
