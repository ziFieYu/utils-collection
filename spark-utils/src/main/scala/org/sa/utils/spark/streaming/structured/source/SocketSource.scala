package org.sa.utils.spark.streaming.structured.source

private[source] object SocketSource extends Source {

    object options extends SourceOptions {

        object host extends SourceOption

        object port extends SourceOption

        object includeTimestamp extends SourceOption

    }

}
