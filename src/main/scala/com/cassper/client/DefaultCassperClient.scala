package com.cassper.client

import com.cassper.handler.CassalogHandler
import com.datastax.driver.core.Session

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultCassperClient(session: Session, cassalogHandler: CassalogHandler) extends CassperClient {

  override def migrate(session: Session): Unit = {
    cassalogHandler.runScript()
  }
}
