package com.cassper.model

import java.util.Date

/**
 * Cassper related details from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class CassperDetails(id: Double,
                          installedRank: Int,
                          description: String,
                          types: String,
                          script: String,
                          checkSum: String,
                          installedBy: String,
                          installedOn: Date,
                          time: Long,
                          success: Boolean)