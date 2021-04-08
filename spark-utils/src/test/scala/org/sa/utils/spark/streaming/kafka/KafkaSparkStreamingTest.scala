package org.sa.utils.spark.streaming.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.sa.utils.spark.streaming.{PartitionProcessor, ResultProcessor}
import org.sa.utils.universal.base.{Alerter, Logging}
import org.sa.utils.universal.config.{Config, FileConfig}

object KafkaSparkStreamingTest extends KafkaStreaming[String, String, String, Int] with App {
    override protected val config: Config = FileConfig()
    override protected val applicationName: String = "test"
    override protected val alerter: Alerter = new Alerter {
        override def alert(subject: String, content: String): Unit = {
            println(s"【$subject】\n$content")
        }
    }
    override protected val kafkaTopics = KAFKA_TOPICS.arrayValue()
    override protected val kafkaBrokers = KAFKA_BROKERS.stringValue
    override protected val kafkaConsumerGroupId = KAFKA_GROUP_ID.stringValue
    override protected val kafkaOffsetConfig = KAFKA_OFFSET_CONFIG.stringValue
    override protected val intervalInSeconds = SPARK_STREAMING_SECONDS.intValue
    override protected val sparkSessionConf: Map[String, String] = Map[String, String]()
    startWithProcessor(PP, RP)
    //startWithProcessFunction(PP.processPartition, N.handlePartitionProcessResult)

    /**
     * RDD处理逻辑
     *
     * @param rdd RDD[T]
     */
    override def processRDD(rdd: RDD[ConsumerRecord[String, String]]): RDD[String] = {
        rdd.map {
            _.value()
        }
    }

}

object N {
    /**
     * Partition处理逻辑
     *
     * @param partition Iterator[T]
     */
    def processPartition(partition: Iterator[String]): Int = {
        var count = 0
        partition.foreach {
            e =>
                println(e)
                count += 1
        }
        count
    }

    def handlePartitionProcessResult(result: Int): Unit = {
        println(s"read $result records in this partition")
    }
}

object PP extends PartitionProcessor[String, Int] {
    /**
     * Partition处理逻辑
     *
     * @param partition Iterator[T]
     */
    override def processPartition(partition: Iterator[String]): Int = {
        var count = 0
        partition.foreach {
            e =>
                println(e)
                count += 1
        }
        count
    }
}

object RP extends ResultProcessor[Int] with Logging {

    /**
     * Partition处理逻辑
     *
     * @param result Iterator[T]
     */
    override def processResult(result: Int): Unit = {
        logInfo(s"read $result records in this partition")
    }
}