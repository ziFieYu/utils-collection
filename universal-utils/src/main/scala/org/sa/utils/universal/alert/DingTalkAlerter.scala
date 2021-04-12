package org.sa.utils.universal.alert

import org.sa.utils.universal.openapi.DingTalkUtils

/**
 * Created by Stuart Alex on 2021/4/12.
 */
class DingTalkAlerter(url: String,
                      atMobiles: Array[String],
                      isAtAll: Boolean) extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        DingTalkUtils.sendTextMessage(url, s"【$subject】\n$content", atMobiles, isAtAll)
    }
}
