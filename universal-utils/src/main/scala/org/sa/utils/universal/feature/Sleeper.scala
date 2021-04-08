package org.sa.utils.universal.feature

/**
 * Created by Stuart Alex on 2021/1/29.
 */
trait Sleeper {
    def sleep(): Unit
}

case class TimeSleeper(millisecond: Int) extends Sleeper {
    override def sleep(): Unit = Thread.sleep(millisecond)
}

object NeverStopSleeper extends Sleeper {
    override def sleep(): Unit = {}
}