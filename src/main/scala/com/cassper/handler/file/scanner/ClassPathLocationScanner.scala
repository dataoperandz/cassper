package com.cassper.handler.file.scanner

import java.net.URL
import java.util

trait ClassPathLocationScanner {
  def findResourceNames(location: String, locationUrl: URL): util.TreeSet[String]
}
