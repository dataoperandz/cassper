package com.dataoperandz.cassper.handler.cassandra.store

import com.dataoperandz.cassper.model.CassperDetails

import scala.util.Try

/**
 * Trait for related file store actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait StoreHandler {
  def store(keyspace: String, cassper: CassperDetails)

  def executeStatement(query: String): Try[Unit]
}