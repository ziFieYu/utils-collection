package org.sa.utils.spark.streaming.structured.sink

object Sinks {
    val kafkaSink: KafkaSink.type = KafkaSink
    val consoleSink: ConsoleSink.type = ConsoleSink
}
