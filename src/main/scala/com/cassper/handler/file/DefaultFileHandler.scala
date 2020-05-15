package com.cassper.handler.file

import java.io.File

import akka.event.slf4j.SLF4JLogging
import com.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.cassper.handler.hashing.Generator
import com.cassper.model.FileDetails

/**
 * File related all the actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultFileHandler extends FileHandler with SLF4JLogging {

  private val SUFFIX = "/"
  private val CASSPER_DIR = "cassper"

  override def getFiles: List[File] = {
    val cassalogDirectory = getClass.getClassLoader.getResource(CASSPER_DIR)
    if (cassalogDirectory != null) {
      val file = new File(cassalogDirectory.toURI)
      if (file.exists && file.isDirectory) {
        file.listFiles().toList
      } else {
        List[File]()
      }
    } else {
      List[File]()
    }
  }

  override def getFilesName: List[FileDetails] = {
    val cassalogDirectory = getClass.getClassLoader.getResource(CASSPER_DIR)
    val path = cassalogDirectory.getPath + SUFFIX
    val file = new File(cassalogDirectory.toURI)
    if (file.exists && file.isDirectory) {
      file.list().map(fileName => {
        FileDetails(getVersionFromName(fileName),
          fileName, getDescription(fileName), getFileType(fileName),
          Generator.generate("MD5", path + fileName))
      }).sortBy(_.version).toList
    } else {
      List[FileDetails]()
    }
  }

  override def getVersionFromName(file: String): Double = {
    if (validateFile(file)) {
      val namesArray = file.split("_")
      namesArray(1).toCharArray.foreach(char => {
        if (!char.isDigit) {
          throw CassperException(CassperErrorCodeEnum.WRONG_FILE_FORMAT_ERROR, s"Wrong file format $file," +
            s"This is the file format => V_1_1_<DESCRIPTION>.cql")
        }
      })
      namesArray(2).toCharArray.foreach(char => {
        if (!char.isDigit) {
          throw CassperException(CassperErrorCodeEnum.WRONG_FILE_FORMAT_ERROR, s"Wrong file format $file," +
            s"This is the file format => V_1_1_<DESCRIPTION>.cql")
        }
      })
      val name = namesArray(1) + "." + namesArray(2)
      name.toDouble
    } else {
      throw CassperException(CassperErrorCodeEnum.WRONG_FILE_FORMAT_ERROR, s"Wrong file format $file")
    }
  }

  override def readFile(file: String): String = {
    val cassalogDirectory = getClass.getClassLoader.getResource(CASSPER_DIR)
    val path = cassalogDirectory.getPath + "/"
    val source = scala.io.Source.fromFile(new File(path + file), "iso-8859-1")
    val content = source.mkString
    source.close()
    content
  }

  override def getLastExecutedVersion(executedList: List[FileDetails]): Option[Double] = {
    executedList.lastOption.map(_.version)
  }

  def getDescription(file: String): String = {
    if (validateFile(file)) {
      val namesArray = file.split("__|\\.")
      namesArray(1)
    } else {
      throw CassperException(CassperErrorCodeEnum.WRONG_FILE_FORMAT_ERROR, "Wrong file format")
    }
  }

  def getFileType(file: String): String = {
    if (validateFile(file)) {
      val namesArray = file.split("[.]")
      if (namesArray(1).equals("cql")) {
        "cql"
      } else {
        throw CassperException(CassperErrorCodeEnum.FILE_TYPE_ERROR, s"This is not correct file type $file")
      }
    } else {
      throw CassperException(CassperErrorCodeEnum.WRONG_FILE_FORMAT_ERROR, "Wrong file format")
    }
  }

  private def validateFile(file: String) : Boolean={
    validateVersion(file) && file.contains("__") && file.contains(".")
  }

  private def validateVersion(file: String): Boolean = {
    val namesArray = file.split("_")
    if (!namesArray(2).isEmpty) {
      val firstVersion = namesArray(1).toCharArray.map(char => {
        char.isDigit
      })
      val secondVersion = namesArray(2).toCharArray.map(char => {
        char.isDigit
      })
      !firstVersion.contains(false) && !secondVersion.contains(false)
    } else {
      false
    }
  }
}