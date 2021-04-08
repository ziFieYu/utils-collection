package org.sa.utils.office.excel

import org.sa.utils.universal.implicits.BasicConversions._
import org.scalatest.FunSuite

class SplitTest extends FunSuite {

    test("split") {
        """plat=android,pname=com.gamezhaocha.app,"pid='945040115','e66712b7bf8'","posid=102,108,113""""
            .splitDoubleQuotedString(",")
            .foreach(println)
    }

}
