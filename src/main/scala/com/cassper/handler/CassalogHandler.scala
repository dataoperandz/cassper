package com.cassper.handler

import java.util.Date

import akka.event.slf4j.SLF4JLogging
import com.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.cassper.handler.cassandra.search.SearchHandler
import com.cassper.handler.cassandra.store.StoreHandler
import com.cassper.handler.file.FileHandler
import com.cassper.model.{CassperDetails, FileDetails}

import scala.util.{Failure, Success}

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class CassalogHandler(fileHandler: FileHandler, storeHandler: StoreHandler, searchHandler: SearchHandler) extends SLF4JLogging {
  def runScript(keyspace:String): Unit = {
    val fileDetails = fileHandler.getFilesName
    val executedFiles = searchHandler.search(keyspace)

    fileDetails.foreach(file => {
      fileHandler.getLastExecutedVersion(searchHandler.search(keyspace)) match {
        case Some(lastExecution) =>
          checkFileVersion(keyspace, executedFiles, file, lastExecution)
        case _ =>
          checkFileVersion(keyspace, executedFiles, file, 0)
          //throw CassperException(CassperErrorCodeEnum.LAST_EXECUTED_VERSION_ERROR, "No last executed version")
      }
    })
  }

  private def checkFileVersion(keyspace:String, executedFiles: List[FileDetails], file: FileDetails, lastExecution: Double): Unit = {
    if (lastExecution < file.version) {
      val query = fileHandler.readFile(file.fileName)
      println(query)
      executeScriptOfFile(keyspace, file, query)
    } else {
      checkCheckSumDifference(executedFiles, file)
    }
  }

  private def checkCheckSumDifference(executedFiles: List[FileDetails], file: FileDetails): Boolean = {
    val fileDetail = executedFiles.find(_.version == file.version)
    fileDetail match {
      case Some(details) =>
        if (details.checksum.equals(file.checksum)) {
          true
        } else {
          throw CassperException(CassperErrorCodeEnum.FILE_CHANGED_ERROR, "file has been changed")
        }
      case _ =>
        throw CassperException(CassperErrorCodeEnum.FILE_CHANGED_ERROR, "file has been changed")
    }
  }

  private def executeScriptOfFile(keyspace:String, file: FileDetails, query: String): Unit = {
    val now = System.currentTimeMillis
    storeHandler.executeStatement(query) match {
      case Success(value) =>
        val end = System.currentTimeMillis
        storeHandler.store(keyspace, CassperDetails(file.version, 1, file.description, file.fileType, file.fileName,
          file.checksum, "cassalog", new Date(System.currentTimeMillis), (end - now), success = true))
        println(s"success ${file.fileName}")
      case Failure(exception) =>
        println("exception =>", exception)
    }
  }
}
