package org.sa.utils.universal.alert

import org.sa.utils.universal.base.MailAgent
import org.sa.utils.universal.implicits.BasicConversions._

/**
 * Created by Stuart Alex on 2021/4/12.
 */
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