package org.sa.utils.hadoop.kafka

import java.lang

import org.sa.utils.hadoop.kafka.config.KafkaConsumerProperties
import org.sa.utils.universal.config.FileConfig
import org.scalatest.FunSuite

import scala.collection.JavaConversions._

/**
 * Created by Stuart Alex on 2021/1/28.
 */
class PropertiesBuilderTest extends FunSuite {

    test("build-consumer-config") {
        val config = FileConfig()
        val dynamicParameters =
            config.newConfigItem("config.dynamic")
                .arrayValue()
                .map(key => key -> config.getProperty(s"config.dynamic.$key"))
                .toMap
        KafkaConsumerProperties.builder()
            .BOOTSTRAP_SERVERS("1,2,3,4")
            .put("enable.auto.commit", new lang.Boolean(true))
            .invoke(dynamicParameters)
            .build()
            .entrySet()
            .foreach {
                e => println(s"${e.getKey} is set to ${e.getValue}")
            }
    }

}
