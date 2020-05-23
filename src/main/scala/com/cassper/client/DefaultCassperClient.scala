package com.cassper.client

import com.cassper.handler.CassperHandler
import com.cassper.handler.cassandra.search.DefaultSearchHandler
import com.cassper.handler.cassandra.store.DefaultStoreHandler
import com.cassper.handler.file.DefaultFileHandler
import com.cassper.handler.file.scanner.JarFileClassPathLocationScanner
import com.datastax.driver.core.Session

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultCassperClient(session: Session) extends CassperClient {

  lazy val jar = new JarFileClassPathLocationScanner
  lazy val fileHandler = new DefaultFileHandler(jar)
  lazy val storeHandler = new DefaultStoreHandler(session)
  lazy val searchHandler = new DefaultSearchHandler(session)
  lazy val cassaLogHandler = new CassperHandler(fileHandler, storeHandler, searchHandler)

  override def migrate(keyspace: String): Unit = {
    cassaLogHandler.runScript(keyspace)
  }
}
