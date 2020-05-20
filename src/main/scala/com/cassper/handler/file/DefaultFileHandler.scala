package com.cassper.handler.file

import java.io.{BufferedReader, InputStreamReader}

import akka.event.slf4j.SLF4JLogging
import com.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.cassper.handler.file.scanner.ClassPathLocationScanner
import com.cassper.handler.hashing.Generator
import com.cassper.model.FileDetails
import com.cassper.util.Constants

import scala.util.Try

/**
 * File related all the actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class DefaultFileHandler(scanner: ClassPathLocationScanner) extends FileHandler with SLF4JLogging {

  override def getFilesName: Try[List[FileDetails]] = {
    Try {
      val cassperDirectory = scanner.findResourceNames(Constants.CASSPER_DIR, getClass.getClassLoader.
        getResource(Constants.CASSPER_DIR))
      val value = cassperDirectory.iterator();
      var fileDetails = List[FileDetails]()
      while (value.hasNext) {
        val fileName = value.next()
        if (fileName.contains(".cql")) {
          val details = FileDetails(getVersionFromName(fileName),
            fileName, getDescription(fileName), getFileType(fileName),
            Generator.generate("MD5", fileName))
          fileDetails = details :: fileDetails
        }
      }
      fileDetails.sortBy(_.version)
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

  override def readFile(file: String): Try[String] = {
    Try {
      val in = getClass.getResourceAsStream(Constants.SUFFIX + file);
      val reader = new BufferedReader(new InputStreamReader(in))
      val content = Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
      reader.close()
      content
    }
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

  private def validateFile(file: String): Boolean = {
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