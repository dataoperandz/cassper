package com.cassper.client

import com.datastax.driver.core.Session

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait CassperClient {

  def migrate(session: Session): Unit
}
