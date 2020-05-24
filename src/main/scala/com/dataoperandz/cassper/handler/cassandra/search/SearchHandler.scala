package com.dataoperandz.cassper.handler.cassandra.search

import com.dataoperandz.cassper.model.FileDetails

import scala.util.Try

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait SearchHandler {

  def search(keyspace: String): Try[List[FileDetails]]
}