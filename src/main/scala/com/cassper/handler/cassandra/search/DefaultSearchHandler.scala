package com.cassper.handler.cassandra.search

import com.cassper.cassandra.CassandraCluster
import com.cassper.model.FileDetails
import com.datastax.driver.core.querybuilder.QueryBuilder

/**
 * Get all the executed files details from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultSearchHandler extends SearchHandler with CassandraCluster {
  override def search(): List[FileDetails] = {
    val selectQuery = QueryBuilder.select.all.from("storage_document", "cassalognew")
    val resultSet = session.execute(selectQuery)
    val it = resultSet.iterator();
    var executedScripts = List[FileDetails]()
    while (it.hasNext) {
      val row = it.next()
      executedScripts = FileDetails(row.getDouble("id"), row.getString("script"), "", "",
        row.getString("checksum")) :: executedScripts
    }
    executedScripts.sortBy(_.version)
  }
}
