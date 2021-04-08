package org.sa.utils.database.pool.mysql

import org.apache.commons.pool2.impl.DefaultPooledObject
import org.apache.commons.pool2.{BasePooledObjectFactory, PooledObject}
import org.sa.utils.database.handler.MySQLHandler
import org.sa.utils.universal.base.Logging

/**
 * Created by Stuart Alex on 2021/2/20.
 */
class MySQLHandlerFactory(url: String, properties: Map[String, AnyRef]) extends BasePooledObjectFactory[MySQLHandler] with Logging {
    override def create(): MySQLHandler = MySQLHandler(url, properties)

    override def wrap(t: MySQLHandler): PooledObject[MySQLHandler] = new DefaultPooledObject[MySQLHandler](t)
}
