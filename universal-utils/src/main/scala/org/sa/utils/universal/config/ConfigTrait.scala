package org.sa.utils.universal.config

trait ConfigTrait extends Serializable {
    implicit protected val config: Config
}
