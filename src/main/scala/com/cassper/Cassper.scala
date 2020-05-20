package com.cassper

import akka.event.slf4j.SLF4JLogging
import com.cassper.client.{CassperClient, DefaultCassperClient}
import com.cassper.config.CassandraConf
import com.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.datastax.driver.core.Session

import scala.util.{Failure, Success, Try}

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Cassper extends Cassper {

}

class Cassper extends CassandraConf with SLF4JLogging {

  private var client: Option[DefaultCassperClient] = None

  def build(session: Session): CassperClient = {
    if (client.isEmpty) {
      client = Option(new DefaultCassperClient(session))
    }
    checkSchemaVersion(createSchemaVersion(session))
    client.get
  }

  def build(keyspace: String, session: Session): CassperClient = {
    if (client.isEmpty) {
      client = Option(new DefaultCassperClient(session))
    }
    checkSchemaVersion(createSchemaVersion(session, keyspace))
    client.get
  }

  private def checkSchemaVersion(unit: Try[Unit]) = {
    unit match {
      case Success(unit) =>
        log.info("Table schema_version table is created successfully!")
      case Failure(exception) =>
        log.error(s"Failure when creating schema_version table", exception)
        throw CassperException(CassperErrorCodeEnum.FILE_TYPE_ERROR, s"Failure when creating schema_version table $exception")
    }
  }

  private def createSchemaVersion(session: Session): Try[Unit] = {
    Try {
      val keyspace = session.getLoggedKeyspace
      session.execute(cassandraDocumentsTable.replace("<KEY_SPACE>", keyspace))
    }
  }

  private def createSchemaVersion(session: Session, keySpace: String): Try[Unit] = {
    Try {
      session.execute(cassandraDocumentsTable.replace("<KEY_SPACE>", keySpace))
    }
  }
}
