package com.cassper.handler.file

import java.io.File

import com.cassper.model.FileDetails

/**
 * Trait for file related actions from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait FileHandler {
  def getFiles: List[File]

  def getFilesName: List[FileDetails]

  def getVersionFromName(fileName: String): Double

  def readFile(file: String): String

  def getDescription(file: String): String

  def getFileType(file: String): String

  def getLastExecutedVersion(executedList: List[FileDetails]): Option[Double]
}