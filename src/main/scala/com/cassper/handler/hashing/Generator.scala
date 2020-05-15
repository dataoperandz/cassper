package com.cassper.handler.hashing

import java.nio.file.{Files, Paths}
import java.security.MessageDigest

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Generator {

  implicit class Helper(val sc: StringContext) extends AnyVal {
    def md5(): String = generate("MD5", sc.parts(0))

    def sha(): String = generate("SHA", sc.parts(0))

    def sha256(): String = generate("SHA-256", sc.parts(0))
  }

  // t is the type of checksum, i.e. MD5, or SHA-512 or whatever
  // path is the path to the file you want to get the hash of
  def generate(t: String, path: String): String = {
    val arr = Files readAllBytes (Paths get path)
    val checksum = MessageDigest.getInstance(t) digest arr
    checksum.map("%02X" format _).mkString
  }
}
