package org.sa.utils.universal.implicits

import org.sa.utils.universal.cli.Renders
import org.sa.utils.universal.implicits.LoggerExtensions._
import org.scalatest.FunSuite
import org.slf4j.{Logger, LoggerFactory}

/**
 * Created by Stuart Alex on 2021/4/6.
 */
class LoggerExtensionTest extends FunSuite {

    test("logging") {
        val logger: Logger = LoggerFactory.getLogger(this.getClass)
        logger.logInfo("aaa")
        logger.logInfo("bbb", Renders.RED)
    }

}
