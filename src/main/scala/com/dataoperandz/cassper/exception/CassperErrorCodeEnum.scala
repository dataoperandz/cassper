package com.dataoperandz.cassper.exception

object CassperErrorCodeEnum extends Enumeration {
  type AssertionType = Value

  val FILE_TYPE_ERROR = Value("FILE_TYPE_ERROR")
  val WRONG_FILE_FORMAT_ERROR = Value("WRONG_FILE_FORMAT_ERROR")
  val LAST_EXECUTED_VERSION_ERROR = Value("LAST_EXECUTED_VERSION_ERROR")
  val FILE_CHANGED_ERROR = Value("FILE_CHANGED_ERROR")
  val JAR_PATH_ERROR = Value("JAR_PATH_ERROR")
  val CASSANDRA_SEARCH_ERROR = Value("CASSANDRA_SEARCH_ERROR")
}