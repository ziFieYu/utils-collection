package org.sa.utils.spark.streaming.redis.receiver

import org.apache.spark.storage.StorageLevel
import org.sa.utils.spark.streaming.redis.wrapper.JedisWrapper
import org.sa.utils.universal.config.{Config, FileConfig}

/**
 * Created by Stuart Alex on 2017/4/6.
 */
class RedisListReceiver(keySet: Set[String], storageLevel: StorageLevel) extends RedisReceiver(keySet, storageLevel) {
    override protected val config: Config = FileConfig()

    override def getData(j: JedisWrapper, key: String): String = j.lpop(key)
}