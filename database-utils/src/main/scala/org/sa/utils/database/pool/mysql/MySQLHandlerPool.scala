package org.sa.utils.database.pool.mysql

import org.apache.commons.pool2.ObjectPool
import org.apache.commons.pool2.impl.GenericObjectPool
import org.sa.utils.database.handler.MySQLHandler
import org.sa.utils.universal.base.Logging
import org.sa.utils.universal.feature.Pool

/**
 * Created by Stuart Alex on 2021/2/20.
 */
object MySQLHandlerPool extends Pool[MySQLHandler] with Logging {

    def apply(url: String, properties: Map[String, AnyRef]): ObjectPool[MySQLHandler] = {
        this._pool.getOrElse(url, {
            this.logInfo(s"MySQLHandler with url $url does not exists, create it and add it into MySQLHandler Pool")
            synchronized[ObjectPool[MySQLHandler]] {
                val pool = new GenericObjectPool[MySQLHandler](new MySQLHandlerFactory(url, properties))
                pool.addObject()
                this._pool.put(url, pool)
                pool
            }
        })
    }
}
