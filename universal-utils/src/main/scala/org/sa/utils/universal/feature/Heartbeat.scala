package org.sa.utils.universal.feature

import java.util.concurrent.Executors

import org.sa.utils.universal.base.Logging
import org.sa.utils.universal.cli.CliUtils
import org.sa.utils.universal.implicits.BasicConversions._

/**
 * Created by Stuart Alex on 2017/12/20.
 */
trait Heartbeat extends Logging {
    protected val number = 10
    protected val interval = 500
    private val threadPool = Executors.newFixedThreadPool(1)
    private var exit = false
    protected val render: String

    /**
     * 启动心跳
     *
     * @param title 标题
     */
    def startHeartbeat(title: String): Unit = {
        this.exit = false
        this.threadPool.execute(Heartbeat(title))
    }

    /**
     * 停止心跳
     */
    def stopHeartbeat(): Unit = {
        if (!this.exit) {
            this.exit = true
            Thread.sleep(this.interval * 2)
        }
    }

    /**
     * 心跳
     *
     * @param title 标题
     */
    case class Heartbeat(title: String) extends Runnable {

        override def run(): Unit = {
            var count = 0
            while (!exit) {
                CliUtils.deleteCurrentRow()
                (title + "." * (count % number + 1)).prettyPrint(render)
                count += 1
                Thread.sleep(interval)
            }
            CliUtils.deleteCurrentRow()
        }

    }

}
