package com.dataoperandz.cassper.handler.cassandra.search

import com.dataoperandz.cassper.model.FileDetails
import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder

import scala.util.Try

/**
 * Get all the executed files details from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultSearchHandler(session: Session) extends SearchHandler {

  override def search(keyspace: String): Try[List[FileDetails]] = {
    Try {
      val selectQuery = QueryBuilder.select.all.from(keyspace, "schema_version")
      val resultSet = session.execute(selectQuery)
      val iterator = resultSet.iterator();
      var executedScripts = List[FileDetails]()
      while (iterator.hasNext) {
        val row = iterator.next()
        executedScripts = FileDetails(row.getDouble("id"), row.getString("script"), "", "",
          row.getString("checksum")) :: executedScripts
      }
      executedScripts.sortBy(_.version)
    }
  }
}
