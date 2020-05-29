package com.dataoperandz.cassper.handler.hashing

import java.security.MessageDigest

import com.dataoperandz.cassper.handler.file.scanner.JarFileClassPathLocationScanner

/**
  * Load configurations define in application.conf from here
  *
  * @author pramod shehan(pramodshehan@gmail.com)
  */

object Generator {
  val scanner = new JarFileClassPathLocationScanner

  implicit class Helper(val sc: Array[Byte]) extends AnyVal {
    def md5(): String = generate("MD5", sc)

    def sha(): String = generate("SHA", sc)

    def sha256(): String = generate("SHA-256", sc)
  }

  // t is the type of checksum, i.e. MD5, or SHA-512 or whatever
  // path is the path to the file you want to get the hash of
  def generate(t: String, payload: Array[Byte]): String = {
    val checksum = MessageDigest.getInstance(t) digest payload
    checksum.map("%02X" format _).mkString
  }
}
