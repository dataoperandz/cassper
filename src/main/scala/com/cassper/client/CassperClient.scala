package com.cassper.client

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait CassperClient {

  def migrate(keyspace: String): Unit
}
