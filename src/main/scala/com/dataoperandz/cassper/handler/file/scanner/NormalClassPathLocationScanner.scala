package com.dataoperandz.cassper.handler.file.scanner

import java.io.File
import java.net.URL
import java.nio.file.{Files, Paths}
import java.util

import com.dataoperandz.cassper.util.Constants

import scala.collection.JavaConverters._
import scala.util.Try

class NormalClassPathLocationScanner extends ClassPathLocationScanner {
  override def findResourceNames(location: String, locationUrl: URL): util.TreeSet[String] = {
    val cassalogDirectory = getClass.getClassLoader.getResource(Constants.CASSPER_DIR)
    if (cassalogDirectory != null) {
      val file = new File(cassalogDirectory.toURI)
      if (file.exists && file.isDirectory) {
        val fileList = new java.util.ArrayList[String](file.list().toList.asJava)
        new util.TreeSet[String](fileList)
      } else {
        new util.TreeSet[String](new util.ArrayList[String])
      }
    } else {
      new util.TreeSet[String](new util.ArrayList[String])
    }
  }

  override def getPayload(path: String): Array[Byte] = {
    val cassalogDirectory = getClass.getClassLoader.getResource(Constants.CASSPER_DIR)
    val cassperPath = cassalogDirectory.getPath + Constants.SUFFIX + path
    Files readAllBytes (Paths get cassperPath)
  }

  override def getContent(file: String): Try[String] = {
    Try {
      val cassalogDirectory = getClass.getClassLoader.getResource(Constants.CASSPER_DIR)
      val path = cassalogDirectory.getPath + "/"
      val source = scala.io.Source.fromFile(new File(path + file), "iso-8859-1")
      val content = source.mkString
      source.close()
      content
    }
  }
}
