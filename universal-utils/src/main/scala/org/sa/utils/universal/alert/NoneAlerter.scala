package org.sa.utils.universal.alert

/**
 * Created by Stuart Alex on 2021/4/12.
 */
class NoneAlerter extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        // do nothing
    }
}