scalaVersion := "2.11.12"

// cassper
name := "cassper"
description := "Schema migrations tool for `Apache Cassandra` that can be used with `JVM` applications."

// groupId, SCM, license information
organization := "io.github.dataoperandz"
homepage := Some(url("https://github.com/dataoperandz/cassper"))
scmInfo := Some(ScmInfo(url("https://github.com/dataoperandz/cassper"), "git@github.com:dataoperandz/cassper.git"))
developers := List(
    Developer("promod shehan", "pramod shehan", "pramod.shehan@gmail.com", url("https://github.com/pramodShehan5")),
    Developer("rahasak", "rahasak", "rahasak@scorelab.org", url("https://gitlab.com/rahasak-labs"))
)
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

// disable publish with scala version, otherwise artifact name will include scala version 
// e.g cassper_2.11
crossPaths := false

// add sonatype repository settings
// snapshot versions publish to sonatype snapshot repository
// other versions publish to sonatype staging repository
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

// realease with sbt-release plugin
import ReleaseTransformations._
releaseCrossBuild := true
//releaseTagName := s"version-${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeRelease"),
  pushChanges
)

// other library dependencies
libraryDependencies ++= {
  val cassandraVersion = "3.1.1"
  val akkaVersion = "2.5.19"
  
  Seq(
      "com.datastax.cassandra"  % "cassandra-driver-core"   % cassandraVersion,
      "com.typesafe.akka"       %% "akka-actor"             % akkaVersion,
      "com.typesafe.akka"       %% "akka-stream"            % akkaVersion,
      "com.typesafe.akka"       %% "akka-slf4j"             % akkaVersion,
      "org.scalatest"           %% "scalatest"              % "3.0.4"                 % Test
    )
}
