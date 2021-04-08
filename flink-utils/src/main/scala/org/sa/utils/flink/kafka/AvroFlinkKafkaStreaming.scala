package org.sa.utils.flink.kafka

import org.apache.avro.Schema
import org.sa.utils.flink.serde.CachedSchemaCoderProvider

/**
 * Created by Stuart Alex on 2021/4/6.
 */
trait AvroFlinkKafkaStreaming[T] extends CommonFlinkKafkaStreaming[T] {
    protected val schema: Schema
    protected val schemaRegistryUrl: String
    protected val identityMapCapacity: Int = 1000
    protected lazy val schemaCoderProvider = new CachedSchemaCoderProvider(null, schemaRegistryUrl, identityMapCapacity)
}
