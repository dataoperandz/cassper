package com.dataoperandz.cassper.handler.hashing

import java.io.{BufferedReader, InputStreamReader}
import java.security.MessageDigest

import com.dataoperandz.cassper.handler.file.scanner.JarFileClassPathLocationScanner

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Generator {
  val scanner = new JarFileClassPathLocationScanner

  implicit class Helper(val sc: StringContext) extends AnyVal {
    def md5(): String = generate("MD5", sc.parts(0))

    def sha(): String = generate("SHA", sc.parts(0))

    def sha256(): String = generate("SHA-256", sc.parts(0))
  }

  // t is the type of checksum, i.e. MD5, or SHA-512 or whatever
  // path is the path to the file you want to get the hash of
  def generate(t: String, path: String): String = {
    println(s"path -> $path")
    val in = getClass.getResourceAsStream("/" + path)
    val reader = new BufferedReader(new InputStreamReader(in))
    val content = Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
    val checksum = MessageDigest.getInstance(t) digest content.getBytes
    checksum.map("%02X" format _).mkString
  }
}
