package org.sa.utils.universal.alert

import org.sa.utils.universal.openapi.WeChatWorkUtils

/**
 * Created by Stuart Alex on 2021/4/12.
 */
class WeChatWorkAlerter(url: String) extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        WeChatWorkUtils.sendTextMessage(url, s"【$subject】\n$content")
    }
}