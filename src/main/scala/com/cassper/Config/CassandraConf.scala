package com.cassper.Config

import com.typesafe.config.ConfigFactory

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait CassandraConf {
  val cassandraConf = ConfigFactory.load("schema.conf")
  lazy val cassandraKeyspace = cassandraConf.getString("schema.createKeyspace")
  lazy val cassandraDocumentsTable = cassandraConf.getString("schema.createTableDocuments")
}
