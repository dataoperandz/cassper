/*
package com.cassper.cassandra

import akka.event.slf4j.SLF4JLogging
import com.datastax.driver.core.{HostDistance, PoolingOptions}

/**
 * Load configurations define in application.conf from here
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait CassandraConnection extends SLF4JLogging {

  lazy val poolingOptions: PoolingOptions = {
    new PoolingOptions()
      .setConnectionsPerHost(HostDistance.LOCAL, 4, 10)
      .setConnectionsPerHost(HostDistance.REMOTE, 2, 4)
  }
}*/
