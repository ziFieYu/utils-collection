package org.sa.utils.spark.common

import org.sa.utils.universal.config.{ConfigItem, ConfigTrait}

private[common] trait SparkConfigConstants extends ConfigTrait {
    protected lazy val SPARK_APP_NAME: ConfigItem = ConfigItem("spark.app.name", "spark-application")
    protected lazy val SPARK_MASTER: ConfigItem = ConfigItem("spark.master", "local[*]")
}
