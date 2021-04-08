package org.sa.utils.hadoop.constants

import org.sa.utils.universal.config.{ConfigItem, ConfigTrait}

trait ZookeeperConfigConstants extends ConfigTrait {
    lazy val ZOOKEEPER_CONNECTION: ConfigItem = ConfigItem("zookeeper.connection")
    lazy val ZOOKEEPER_PORT: ConfigItem = ConfigItem("zookeeper.port", 2181)
    lazy val ZOOKEEPER_QUORUM: ConfigItem = ConfigItem("zookeeper.quorum")
}
