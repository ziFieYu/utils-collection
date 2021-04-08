package org.sa.utils.hadoop.constants

import org.sa.utils.universal.config.{ConfigItem, ConfigTrait}

trait YarnConfigConstants extends ConfigTrait {
    lazy val RESOURCE_MANAGER_ADDRESS: ConfigItem = ConfigItem("yarn.rm.address")
}
