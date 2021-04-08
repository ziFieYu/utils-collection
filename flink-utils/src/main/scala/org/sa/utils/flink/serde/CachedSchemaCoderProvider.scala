package org.sa.utils.flink.serde

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.flink.formats.avro.SchemaCoder
import org.apache.flink.formats.avro.registry.confluent.ConfluentSchemaRegistryCoder
import org.sa.utils.universal.implicits.BasicConversions._

@SerialVersionUID(4023134423033312666L)
class CachedSchemaCoderProvider(subject: String, url: String, identityMapCapacity: Int ) extends SchemaCoder.SchemaCoderProvider {

    override def get: SchemaCoder = {
        if (subject.notNullAndEmpty)
            new ConfluentSchemaRegistryCoder(subject, new CachedSchemaRegistryClient(url, identityMapCapacity))
        else
            new ConfluentSchemaRegistryCoder(new CachedSchemaRegistryClient(url, identityMapCapacity))
    }

}
