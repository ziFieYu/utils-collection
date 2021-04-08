package org.sa.utils.spark.streaming.structured.sink

private[sink] object KafkaSink extends Sink {

    object options extends SinkOptions {

        object `kafka.bootstrap.servers` extends SinkOption

        object topic extends SinkOption

    }

}
