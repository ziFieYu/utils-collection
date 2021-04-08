package org.sa.utils.database.pool.redis

import org.sa.utils.universal.base.Logging
import org.sa.utils.universal.implicits.BasicConversions._
import redis.clients.jedis.{Jedis, JedisPool, Pipeline}

import scala.collection.mutable

/**
 * Created by Stuart Alex on 2017/4/5.
 */
object RedisClientPool extends Logging {
    private val _pool = mutable.Map[(String, Int, String), JedisPool]()
    sys.addShutdownHook {
        this._pool.values.foreach { pool => pool.destroy() }
    }

    def getPipeline(host: String, port: Int, password: String): Pipeline = {
        val jedis = RedisClientPool(host, port, password)
        jedis.pipelined
    }

    def apply(host: String, port: Int, password: String): Jedis = {
        val pool = this._pool.getOrElse((host, port, password), {
            this.logInfo(s"RedisClientPool $host-$port-$password does not exists, create it and add it into RedisProducerPool")
            RedisClientPool.synchronized[JedisPool] {
                val pool = new JedisPool(host, port)
                this._pool += (host, port, password) -> pool
                pool
            }
        }).getResource
        if (password.notNullAndEmpty)
            pool.auth(password)
        pool
    }

    def close(): Unit = {
        _pool.values.foreach(_.close)
    }

}