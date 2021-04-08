package org.sa.utils.hadoop.hbase.pool

import org.apache.commons.pool2.ObjectPool
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.hadoop.hbase.client.Connection
import org.sa.utils.universal.base.Logging

import scala.collection.mutable

/**
 * Created by Stuart Alex on 2017/4/5.
 */
object HBaseConnectionPool extends Logging {
    private val _pool = mutable.Map[String, ObjectPool[Connection]]()
    sys.addShutdownHook {
        this._pool.values.foreach(_.close())
    }

    def apply(zookeeperQuorum: String, zookeeperPort: Int = 2181): ObjectPool[Connection] = {
        val zookeeperConnection = zookeeperQuorum.split(",").map(_ + ":" + zookeeperPort).mkString(",")
        this._pool.getOrElse(zookeeperConnection, {
            this.logInfo(s"HBase Connection with zookeeper connection $zookeeperConnection does not exists, create it and add it into HBaseConnectionPool")
            HBaseConnectionPool.synchronized[ObjectPool[Connection]] {
                val pool = new GenericObjectPool[Connection](HBaseConnectionFactory(zookeeperQuorum, zookeeperPort))
                this._pool += zookeeperConnection -> pool
                pool
            }
        })
    }

}
