package com.dataoperandz.cassper.client

import com.dataoperandz.cassper.handler.CassperHandler
import com.dataoperandz.cassper.handler.cassandra.search.DefaultSearchHandler
import com.dataoperandz.cassper.handler.cassandra.store.DefaultStoreHandler
import com.dataoperandz.cassper.handler.file.DefaultFileHandler
import com.dataoperandz.cassper.handler.file.scanner.{JarFileClassPathLocationScanner, NormalClassPathLocationScanner, ScannerFactory}
import com.datastax.driver.core.Session

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultCassperClient(session: Session) extends CassperClient {

  lazy val jar = new JarFileClassPathLocationScanner
  lazy val normal = new NormalClassPathLocationScanner
  lazy val scannerFactory = new ScannerFactory(jar, normal)
  lazy val fileHandler = new DefaultFileHandler(scannerFactory)
  lazy val storeHandler = new DefaultStoreHandler(session)
  lazy val searchHandler = new DefaultSearchHandler(session)
  lazy val cassaLogHandler = new CassperHandler(fileHandler, storeHandler, searchHandler)

  override def migrate(keyspace: String): Unit = {
    cassaLogHandler.runScript(keyspace)
  }
}
