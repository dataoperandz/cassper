/*

package com.cassper

import java.io.File

import com.cassper.config.CassandraConf
import com.cassper.cassandra.CassandraCluster
import com.cassper.handler.cassperHandler
import com.cassper.handler.cassandra.search.DefaultSearchHandler
import com.cassper.handler.cassandra.store.DefaultStoreHandler
import com.cassper.handler.file.{DefaultFileHandler, JarFileClassPathLocationScanner}

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Demo extends CassandraCluster with CassandraConf {

  val jar = new JarFileClassPathLocationScanner
  val fileHandler = new DefaultFileHandler(jar)
  val storeHandler = new DefaultStoreHandler
  val searchHandler = new DefaultSearchHandler
  val cassperHandler = new cassperHandler(fileHandler, storeHandler, searchHandler)

  def main(args: Array[String]): Unit = {

    session.execute(cassandraKeyspace.replace("<KEY_SPACE>", "cassper_keyspace"))
    session.execute(cassandraDocumentsTable.replace("<KEY_SPACE>", "cassper_keyspace"))
    cassperHandler.runScript("cassper_keyspace")
    //    storeHandler.executeStatement("") match {
    //      case Success(unit) =>
    //        println("Success")
    //      case Failure(exception) =>
    //        println(exception)
    //    }
    //    val fileStream = getClass.getResourceAsStream("/json-sample.js")
    //    val lines = Source.fromInputStream(fileStream).getLines
    //    lines.foreach(line => println(line))
    //    getListOfFiles("")
    //    val cassperDirectory = getClass.getClassLoader.getResource("cassper")
    //    println(cassperDirectory.getPath)
    //val currentDirectory = new java.io.File(".").getCanonicalPath
    //    storeHandler.store(cassper(1,1,"","","","","", "",null, 1, true))
    /*val aa = Generator.generate("MD5", "/home/pramod/MyWork/cassper/target/scala-2.11/classes/cassper/V_1_1__intialization.cql")
    println(aa)*/

    //    val now = new Date()
    //    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
    //    println(simpleDateFormat.format(now))
//    searchHandler.search()
  }


  /*def readAttachment(name: String) = {
    val source = scala.io.Source.fromFile(, "iso-8859-1").mkString
//    val byteArray = source.map(_.toByte).toArray

    source.close()

  }*/

  def getListOfFiles(dir: String): List[File] = {
    val cassperDirectory = getClass.getClassLoader.getResource("cassper")

    val file = new File(cassperDirectory.toURI)
    if (file.exists && file.isDirectory) {
      val aa = file.listFiles().toList
      val source = scala.io.Source.fromFile(aa(0), "iso-8859-1").mkString
      aa
    } else {
      List[File]()
    }
  }

}
*/
