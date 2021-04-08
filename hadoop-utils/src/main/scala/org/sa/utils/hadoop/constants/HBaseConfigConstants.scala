package org.sa.utils.hadoop.constants

import org.sa.utils.universal.config.{ConfigItem, ConfigTrait}

trait HBaseConfigConstants extends ConfigTrait {
    lazy val HBASE_MASTER: ConfigItem = ConfigItem("hbase.master")
}
