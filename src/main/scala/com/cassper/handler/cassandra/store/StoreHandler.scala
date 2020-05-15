package com.cassper.handler.cassandra.store

import com.cassper.model.CassperDetails

import scala.util.Try

/**
 * Trait for related file store actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait StoreHandler {
  def store(cassalog: CassperDetails)

  def executeStatement(query: String): Try[Unit]
}