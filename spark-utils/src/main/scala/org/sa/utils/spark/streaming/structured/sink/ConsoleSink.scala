package org.sa.utils.spark.streaming.structured.sink

private[sink] object ConsoleSink extends Sink {

    object options extends SinkOptions {

        object truncate extends SinkOption

    }

}
