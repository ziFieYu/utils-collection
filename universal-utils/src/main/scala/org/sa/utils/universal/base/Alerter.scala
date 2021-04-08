package org.sa.utils.universal.base

import org.sa.utils.universal.implicits.BasicConversions._
import org.sa.utils.universal.openapi.{DingTalkUtils, WeChatWorkUtils}

class MailAlerter(host: String,
                  port: Int,
                  username: String,
                  password: String,
                  sender: String,
                  recipients: Array[String]) extends Alerter {
    assert(recipients.notNull && recipients.nonEmpty, "recipients can't be null or empty")
    private val mailAgent = new MailAgent(host, port, username, password, sender, recipients)

    override def alert(subject: String, content: String): Unit = {
        mailAgent.send(subject, content, recipients)
    }
}

class WeChatWorkAlerter(url: String) extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        WeChatWorkUtils.sendTextMessage(url, s"【$subject】\n$content")
    }
}

class DingTalkAlerter(url: String,
                      atMobiles: Array[String],
                      isAtAll: Boolean) extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        DingTalkUtils.sendTextMessage(url, s"【$subject】\n$content", atMobiles, isAtAll)
    }
}

object LoggerAlerter extends Alerter with Logging {
    override def alert(subject: String, content: String): Unit = {
        logError(s"【$subject】>>>>>$content")
    }
}

object PrintAlerter extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        println(s"【$subject】>>>>>$content")
    }
}

object NoneAlerter extends Alerter {
    override def alert(subject: String, content: String): Unit = {
        // do nothing
    }
}

trait Alerter {

    def alert(subject: String, content: String)

}