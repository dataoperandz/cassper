package com.cassper

import java.io.File

import com.cassper.Config.CassandraConf
import com.cassper.cassandra.CassandraCluster
import com.cassper.handler.CassalogHandler
import com.cassper.handler.cassandra.search.DefaultSearchHandler
import com.cassper.handler.cassandra.store.DefaultStoreHandler
import com.cassper.handler.file.DefaultFileHandler
import com.cassper.handler.hashing.Generator

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Demo extends CassandraCluster with CassandraConf {

  val fileHandler = new DefaultFileHandler
  val storeHandler = new DefaultStoreHandler
  val searchHandler = new DefaultSearchHandler
  val cassalogHandler = new CassalogHandler(fileHandler, storeHandler, searchHandler)

  def main(args: Array[String]): Unit = {

    //    val fileStream = getClass.getResourceAsStream("/json-sample.js")
    //    val lines = Source.fromInputStream(fileStream).getLines
    //    lines.foreach(line => println(line))
    //    getListOfFiles("")
    val cassalogDirectory = getClass.getClassLoader.getResource("cassalog")
    println(cassalogDirectory.getPath)
    //val currentDirectory = new java.io.File(".").getCanonicalPath
    //    storeHandler.store(Cassalog(1,1,"","","","","", "",null, 1, true))
    session.execute(cassandraKeyspace)
    session.execute(cassandraDocumentsTable)
    //    storeHandler.executeStatement("") match {
    //      case Success(unit) =>
    //        println("Success")
    //      case Failure(exception) =>
    //        println(exception)
    //    }
    cassalogHandler.runScript()
    val aa = Generator.generate("MD5", "/home/pramod/MyWork/cassalog/target/scala-2.11/classes/cassalog/V_1_1__intialization.cql")
    println(aa)

    //    val now = new Date()
    //    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
    //    println(simpleDateFormat.format(now))
    searchHandler.search()
  }


  /*def readAttachment(name: String) = {
    val source = scala.io.Source.fromFile(, "iso-8859-1").mkString
//    val byteArray = source.map(_.toByte).toArray

    source.close()

  }*/

  def getListOfFiles(dir: String): List[File] = {
    val cassalogDirectory = getClass.getClassLoader.getResource("cassalog")

    val file = new File(cassalogDirectory.toURI)
    if (file.exists && file.isDirectory) {
      val aa = file.listFiles().toList
      val source = scala.io.Source.fromFile(aa(0), "iso-8859-1").mkString
      aa
    } else {
      List[File]()
    }
  }

}