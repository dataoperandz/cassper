package com.cassper.handler.cassandra.search

import com.cassper.model.FileDetails

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait SearchHandler {
  def search(): List[FileDetails]
}