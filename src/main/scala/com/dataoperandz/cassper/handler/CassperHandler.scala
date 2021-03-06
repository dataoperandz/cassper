package com.dataoperandz.cassper.handler

import java.util
import java.util.Date

import akka.event.slf4j.SLF4JLogging
import com.dataoperandz.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.dataoperandz.cassper.handler.cassandra.search.SearchHandler
import com.dataoperandz.cassper.handler.cassandra.store.StoreHandler
import com.dataoperandz.cassper.handler.file.FileHandler
import com.dataoperandz.cassper.handler.file.scanner.ScannerFactory
import com.dataoperandz.cassper.model.{CassperDetails, FileDetails}
import com.dataoperandz.cassper.util.Constants

import scala.util.{Failure, Success}

/**
  * Load configurations define in application.conf from here
  *
  * @author pramod shehan(pramodshehan@gmail.com)
  */

class CassperHandler(fileHandler: FileHandler,
                     storeHandler: StoreHandler,
                     searchHandler: SearchHandler,
                     scanner: ScannerFactory) extends SLF4JLogging {
  def runScript(keyspace: String): Unit = {
    fileHandler.getFilesName match {
      case Success(fileDetails) =>
        searchHandler.search(keyspace) match {
          case Success(files) =>
            executedFiles(keyspace, fileDetails, files)
          case Failure(exception) =>
            log.error(s"Exception when getting executed scripts reading schema version", exception)
            throw CassperException(CassperErrorCodeEnum.CASSANDRA_SEARCH_ERROR, s"Exception when getting executing" +
              s" scripts ${exception.getMessage}")
        }
      case Failure(exception) =>
        log.error(s"Exception when getting details from script files", exception)
        throw CassperException(CassperErrorCodeEnum.CASSANDRA_SEARCH_ERROR, s"Exception when getting details" +
          s" from script files ${exception.getMessage}")
    }
  }

  private def executedFiles(keyspace: String, fileDetails: List[FileDetails], executedFiles: List[FileDetails]): Unit = {
    log.info(s"Current version of schema public:${fileHandler.getLastExecutedVersion(executedFiles)}")
    val executedScriptArr = new util.ArrayList[Boolean]()
    fileDetails.foreach(file => {
      fileHandler.getLastExecutedVersion(executedFiles) match {
        case Some(lastExecution) =>
          val value = checkFileVersion(keyspace, executedFiles, file, lastExecution)
          if (value) executedScriptArr.add(value)
        case _ =>
          val value = checkFileVersion(keyspace, executedFiles, file, 0)
          if (value) executedScriptArr.add(value)
      }
    })
    executedScriptArr.size() match {
      case 0 =>
        log.info(s"Schema public is up to date.No migration necessary.")
      case _ =>

    }
  }

  private def checkFileVersion(keyspace: String, executedFiles: List[FileDetails], file: FileDetails,
                               lastExecution: Double): Boolean = {
    if (lastExecution < file.version) {
      scanner.getScannerManager.getContent(file.fileName) match {
        case Success(content) =>
          println(file.fileName)
          val now = System.currentTimeMillis
          content.trim.split(";").foreach(query => executeScriptOfFile(keyspace, file, query))
          val end = System.currentTimeMillis
          storeHandler.store(keyspace, CassperDetails(file.version, 1, file.description, file.fileType,
            getFileNameRemovingDirName(file.fileName), file.checksum, Constants.ROLE_NAME,
            new Date(System.currentTimeMillis), end - now, success = true))
          true
        case Failure(exception) =>
          log.error(s"Exception when reading content of script file ${file.fileName}", exception)
          throw CassperException(CassperErrorCodeEnum.CASSANDRA_SEARCH_ERROR, s"Exception when reading content of " +
            s"script file ${file.fileName} , ${exception.getMessage}")
      }
    } else {
      checkCheckSumDifference(executedFiles, file)
      false
    }
  }

  private def checkCheckSumDifference(executedFiles: List[FileDetails], file: FileDetails): Boolean = {
    val fileDetail = executedFiles.find(_.version == file.version)
    fileDetail match {
      case Some(details) =>
        if (details.checksum.equals(file.checksum)) {
          true
        } else {
          throw CassperException(CassperErrorCodeEnum.FILE_CHANGED_ERROR, s"file has been changed ${file.fileName}")
        }
      case _ =>
        throw CassperException(CassperErrorCodeEnum.FILE_CHANGED_ERROR, s"file has been changed  ${file.fileName}")
    }
  }

  private def executeScriptOfFile(keyspace: String, file: FileDetails, query: String): Unit = {
    storeHandler.executeStatement(query.trim) match {
      case Success(value) =>
      //log.info(s"success ${file.fileName}")
      case Failure(exception) =>
        log.error(s"exception => $query", exception)
        throw exception
    }
  }

  private def getFileNameRemovingDirName(fileName: String): String = {
    if (fileName.contains(Constants.CASSPER_DIR + Constants.SUFFIX)) {
      fileName.substring(fileName.indexOf(Constants.SUFFIX) + 1)
    } else {
      fileName
    }
  }
}
