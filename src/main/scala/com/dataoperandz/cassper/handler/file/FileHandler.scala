package com.dataoperandz.cassper.handler.file

import com.dataoperandz.cassper.model.FileDetails

import scala.util.Try

/**
 * Trait for file related actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait FileHandler {

  def getFilesName: Try[List[FileDetails]]

  def getVersionFromName(fileName: String): Double

//  def readFile(file: String): Try[String]

  def getDescription(file: String): String

  def getFileType(file: String): String

  def getLastExecutedVersion(executedList: List[FileDetails]): Option[Double]
}