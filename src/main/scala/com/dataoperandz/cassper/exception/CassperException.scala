package com.dataoperandz.cassper.exception

/**
 * Exception for Cassper from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class CassperException(errorCode: CassperErrorCodeEnum.Value,
                            errorMessage: String,
                            cause: Throwable = null) extends Exception(errorMessage, cause)