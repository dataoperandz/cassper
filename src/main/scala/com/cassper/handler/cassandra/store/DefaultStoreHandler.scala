package com.cassper.handler.cassandra.store

import java.time.ZoneId

import com.cassper.cassandra.CassandraCluster
import com.cassper.model.CassperDetails

import scala.util.Try

/**
 * Store all the executed files details from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultStoreHandler extends StoreHandler with CassandraCluster {

  lazy val insertQuery = "insert into storage_document.cassalognew (id, installed_rank, description, type, script," +
    " checksum, installed_by, installed_on, execution_time, success) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

  override def store(cassalog: CassperDetails): Unit = {
    val prepared = session.prepare(insertQuery)
    val defaultZoneId = ZoneId.systemDefault

    //Converting the date to Instant
    val instant = cassalog.installedOn.toInstant

    //Converting the Date to LocalDate
    val localDate = instant.atZone(defaultZoneId).toLocalDate
    val bound = prepared.bind.setDouble("id", cassalog.id).
      setInt("installed_rank", cassalog.installedRank).
      setString("description", cassalog.description).
      setString("type", cassalog.types).
      setString("script", cassalog.script).
      setString("checksum", cassalog.checkSum).
      setString("installed_by", cassalog.installedBy).
      setTimestamp("installed_on", cassalog.installedOn).
      setLong("execution_time", cassalog.time).
      setBool("success", cassalog.success)
    session.execute(bound)
  }

  override def executeStatement(query: String): Try[Unit] = {
    Try {
      session.execute(query)
    }
  }
}