package com.dataoperandz.cassper.handler.file.scanner

import java.io.{BufferedReader, InputStreamReader}
import java.net.{JarURLConnection, URL, URLConnection}
import java.util
import java.util.jar.JarFile

import akka.event.slf4j.SLF4JLogging
import com.dataoperandz.cassper.exception.{CassperErrorCodeEnum, CassperException}
import com.dataoperandz.cassper.util.Constants

import scala.util.{Failure, Success, Try}

class JarFileClassPathLocationScanner extends ClassPathLocationScanner with SLF4JLogging {

  override def findResourceNames(location: String, locationUrl: URL): util.TreeSet[String] = {
    val jarFile: JarFile = getJarFromUrl(locationUrl)
    try findResourceNamesFromJarFile(jarFile, location)
    finally {
      jarFile.close()
    }
  }


  override def getPayload(path: String): Array[Byte] = {
    val in = getClass.getResourceAsStream("/" + path)
    val reader = new BufferedReader(new InputStreamReader(in))
    val content = Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
    content.getBytes
  }

  override def getContent(file: String): Try[String] = {
    Try {
      val in = getClass.getResourceAsStream(Constants.SUFFIX + file)
      val reader = new BufferedReader(new InputStreamReader(in))
      val content = Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
      reader.close()
      content
    }
  }

  private def findResourceNamesFromJarFile(jarFile: JarFile, location: String): util.TreeSet[String] = {
    val resources = new util.TreeSet[String]
    val entries = jarFile.entries
    while (entries.hasMoreElements) {
      val entryName = entries.nextElement.getName
      if (entryName.startsWith(location)) resources.add(entryName)
    }
    resources
  }

  private def getJarFromUrl(locationUrl: URL): JarFile = {
    val con = locationUrl.openConnection
    checkJarUrlConnection(locationUrl, con)
  }

  private def checkJarUrlConnection(locationUrl: URL, con: URLConnection): JarFile = {
    con match {
      case jarCon: JarURLConnection => // usually be the case for traditional JAR files.
        jarCon.setUseCaches(false)
        jarCon.getJarFile
      case _ =>
        getJarFileWhenNoJarUrlConnection(locationUrl) match {
          case Success(jar) => jar
          case Failure(exception) =>
            log.error("Exception when trying to get jar url coneection ", exception)
            throw CassperException(CassperErrorCodeEnum.JAR_PATH_ERROR, exception.getMessage)
        }
    }
  }

  /**
    * No JarURLConnection -> need to resort to URL file parsing.
    * We'll assume URLs of the format "jar:path!/entry", with the protocol
    * being arbitrary as long as following the entry format.
    * We'll also handle paths with and without leading "file:" prefix.
    *
    * @param locationUrl
    * @return Try[JarFile]
    */
  private def getJarFileWhenNoJarUrlConnection(locationUrl: URL): Try[JarFile] = {
    Try {
      val urlFile = locationUrl.getFile
      val separatorIndex = urlFile.indexOf("!/")
      if (separatorIndex != -1) {
        val jarFileUrl = urlFile.substring(0, separatorIndex)
        if (jarFileUrl.startsWith("file:")) {
          new JarFile(new URL(jarFileUrl).toURI.getSchemeSpecificPart)
        } else {
          new JarFile(jarFileUrl)
        }
      } else {
        new JarFile(urlFile)
      }
    }
  }
}
