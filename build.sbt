name := "cassper"

version := "0.1"

// pom settings for sonatype
organization := "io.github.dataoperandz"
homepage := Some(url("https://github.com/dataoperandz/cassper"))
scmInfo := Some(ScmInfo(url("https://github.com/dataoperandz/cassper"), "git@github.com:dataoperandz/cassper.git"))
developers := List(Developer("rahasak", "rahasak", "rahasak@scorelab.org", url("https://github.com/rahasak")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true
crossPaths := false

// add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

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
