package com.dataoperandz.cassper.handler.file.scanner

import com.dataoperandz.cassper.util.Constants

/**
  * Select cassper files scanner according to path
  *
  * @author pramod shehan(pramodshehan@gmail.com)
  */


class ScannerFactory(jarScanner: JarFileClassPathLocationScanner, normalScanner: NormalClassPathLocationScanner) {
  def getScannerManager: ClassPathLocationScanner = {
    val cassalogDirectory = getClass.getClassLoader.getResource(Constants.CASSPER_DIR)
    if (cassalogDirectory.getPath.contains(".jar")) {
      jarScanner
    } else {
      normalScanner
    }
  }
}
