package com.cassper.handler.file

import com.cassper.model.FileDetails
import org.scalatest.{FlatSpec, Matchers}

class DefaultFileHandlerTest extends FlatSpec with Matchers {
  "CheckFiles" should "get all the files in cassper folder" in {
    val fileHandler = new DefaultFileHandler
    val files =fileHandler.getFiles
    files should equal(List("/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassper/V2_2_second_second.cql", "/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassper/V2_1_second_one.cql", "/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassper/V3_2_third_second.cql", "/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassper/V1_1_intialization.cql", "/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassper/V1_2_second.cql", "/home/pramod/MyWork/cassper/target/scala-2.11/test-classes/cassalog/V1_3_third.cql"))
  }

  "Get files names " should "get all file names" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getFilesName
    fileNames should equal(List(FileDetails(1.1,"V_1_1__intialization.cql","intialization","cql","A352EC46FE63BD8754EDE1D8467EF459"), FileDetails(1.2,"V_1_2__second.cql","second","cql","5681BE65F2367727EEF2534CB431CEDB"), FileDetails(1.3,"V_1_3__third.cql","third","cql","5681BE65F2367727EEF2534CB431CEDB"), FileDetails(2.1,"V_2_1__second_one.cql","second","cql","5681BE65F2367727EEF2534CB431CEDB"), FileDetails(2.2,"V_2_2__second_second.cql","second","cql","5681BE65F2367727EEF2534CB431CEDB"), FileDetails(3.2,"V_3_2__third_second.cql","third","cql","5681BE65F2367727EEF2534CB431CEDB")))
  }

  "Get version " should "by giving expected file format" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getVersionFromName("V_11_32__initi.cql")
    fileNames should equal(11.32)
  }

  it should "by giving wrong file format" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getVersionFromName("V1_2_initi.cql")
    fileNames match {
      case 1.2 =>
        fail("aaaaaaaaa")
      case _ =>
        fail(message = "Wrong file format")
    }
  }

  it should "by giving wrong file format next" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getVersionFromName("V1_initi.cql")
    fileNames match {
      case 1.2 =>
        fail("aaaaaaaaa")
      case _ =>
        fail(message = "Wrong file format")
    }
  }

  /*"Read files" should "providing all the files" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getFilesName()
    fileHandler.readFiles(fileNames)
  }*/

  "Get description " should "by giving expected file format" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getDescription("V_1_2__init_ssi.cql")
    fileNames should equal("init_ssi")
  }

  it should "by giving another expected description" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getDescription("V_1_2__initi.cql")
    fileNames should equal("initi")
  }

  it should "by giving wrong file format" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getDescription("V_1_2_initi.cql")
    fileNames should equal("initi")
  }

  "Get File type " should "by giving expected file format" in {
    val fileHandler = new DefaultFileHandler
    val fileNames = fileHandler.getFileType("V_1_2__initi.cql")
    fileNames should equal("cql")
  }

 /* "Validate version" should "by giving expected file format" in{
    val fileHandler = new DefaultFileHandler
    val bool = fileHandler.validateVersion("V_1_2_initi.cql")
    println(bool)
  }*/
}
