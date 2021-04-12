package org.sa.utils.flink.kafka

import java.util.Properties

import org.apache.flink.api.common.RuntimeExecutionMode
import org.apache.flink.api.common.restartstrategy.RestartStrategies.RestartStrategyConfiguration
import org.apache.flink.api.common.serialization.DeserializationSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumerBase}
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.sa.utils.flink.common.CheckpointConfigItems
import org.sa.utils.hadoop.kafka.config.KafkaConsumerProperties
import org.sa.utils.universal.base.{Alerter, Logging}
import org.sa.utils.universal.config.Config
import org.sa.utils.universal.implicits.ArrayConversions._
import org.sa.utils.universal.implicits.ExtendedJavaConversions._

/**
 * Created by Stuart Alex on 2021/4/3.
 */
trait CommonFlinkKafkaStreaming[T] extends Logging {
    protected val applicationName: String
    protected val alerter: Alerter
    protected val config: Config
    protected val kafkaSourceTopic: String
    protected val kafkaBrokers: String
    protected val autoCommit: Boolean
    protected val consumerGroupId: String
    protected val additionalConsumerConfig: Map[String, AnyRef]
    protected val deserializationSchema: DeserializationSchema[T]
    protected val checkpointEnabled: Boolean
    protected lazy val checkpointConfigItems: CheckpointConfigItems = null
    protected val offsetResetStrategy: OffsetResetStrategy
    protected val restartStrategyConfiguration: RestartStrategyConfiguration

    def main(args: Array[String]): Unit = {
        try {
            config.parseArguments(args)
            val consumerProperties: Properties =
                KafkaConsumerProperties.builder()
                    .BOOTSTRAP_SERVERS(kafkaBrokers)
                    .AUTO_OFFSET_RESET(offsetResetStrategy)
                    .ENABLE_AUTO_COMMIT(autoCommit)
                    .GROUP_ID(consumerGroupId)
                    .invoke(additionalConsumerConfig)
                    .build()
            logInfo("consumer configuration is following:\n" + consumerProperties.toKeyValuePair.withKeySorted.withKeyPadded(-1, "\t", "\t", "").mkString("\n"))
            val flinkKafkaConsumer: FlinkKafkaConsumerBase[T] =
                new FlinkKafkaConsumer(kafkaSourceTopic, deserializationSchema, consumerProperties)
                    .setStartFromGroupOffsets()
                    .setCommitOffsetsOnCheckpoints(true)
            val streamExecutionEnvironment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment.setRuntimeMode(RuntimeExecutionMode.STREAMING)
            streamExecutionEnvironment.getConfig.setRestartStrategy(restartStrategyConfiguration)
            if (checkpointEnabled) {
                assert(checkpointConfigItems != null, "checkpointConfigItems need to be override")
                streamExecutionEnvironment.enableCheckpointing(checkpointConfigItems.checkpointIntervalInMilliseconds, checkpointConfigItems.checkpointMode)
                streamExecutionEnvironment.getCheckpointConfig.setCheckpointTimeout(checkpointConfigItems.checkpointTimeoutInMilliseconds)
                streamExecutionEnvironment.getCheckpointConfig.setMaxConcurrentCheckpoints(checkpointConfigItems.maxConcurrentCheckpoints)
                streamExecutionEnvironment.getCheckpointConfig.setMinPauseBetweenCheckpoints(checkpointConfigItems.minPauseBetweenCheckpoints)
                streamExecutionEnvironment.getCheckpointConfig.setTolerableCheckpointFailureNumber(checkpointConfigItems.tolerableCheckpointFailureNumber)
                streamExecutionEnvironment.getCheckpointConfig.enableExternalizedCheckpoints(checkpointConfigItems.cleanupMode)
                streamExecutionEnvironment.setStateBackend(checkpointConfigItems.stateBackend)
            }
            val dataStreamSource = streamExecutionEnvironment.addSource(flinkKafkaConsumer)
            execute(dataStreamSource)
            streamExecutionEnvironment.execute(applicationName)
        } catch {
            case e: Exception => alerter.alert(applicationName, e.getMessage)
                throw e
        }
    }

    def execute(dataStreamSource: DataStreamSource[T]): Unit
}
