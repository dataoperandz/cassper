package com.cassper.cassandra

import akka.event.slf4j.SLF4JLogging
import com.datastax.driver.core.{Cluster, Session}

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait CassandraCluster extends CassandraConnection with SLF4JLogging {
val cassandraHosts = List("localhost")
  lazy val cluster: Cluster = {
    val builder = Cluster.builder()
    for (cp <- cassandraHosts) builder.addContactPoint(cp)
    builder.withPort(9042)
    builder.withPoolingOptions(poolingOptions)

    builder.build()
  }

  lazy val session: Session = getSession()

  private def getSession(): Session = {
      cluster.connect()
  }
}