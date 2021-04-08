package org.sa.utils.spark.common

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.sa.utils.spark.SparkUtils
import org.sa.utils.universal.config.ConfigItem

trait SparkStreamingEnvironment extends SparkBaseEnvironment {
    protected lazy val SPARK_STREAMING_SECONDS: ConfigItem = ConfigItem("spark.streaming.seconds", 5)
    protected val intervalInSeconds: Int
    protected val sparkConf: SparkConf
    protected lazy val streamingContext: StreamingContext = SparkUtils.getStreamingContext(sparkConf, intervalInSeconds)
}
