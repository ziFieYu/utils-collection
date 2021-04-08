package org.sa.utils.spark.streaming.structured

import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.sa.utils.spark.streaming.structured.reader.{KafkaStreamReader, SocketStreamReader, StreamReader}
import org.sa.utils.spark.streaming.structured.source.InputSources._
import org.sa.utils.spark.streaming.structured.writer.{ConsoleStreamWriter, KafkaStreamWriter, StreamWriter}

import scala.reflect.ClassTag

object StructuredStreamingUtils extends App {

    def readFromFile(sparkSession: SparkSession): StreamReader = {
        this.readStream(sparkSession).inputSource(fileSource)
    }

    def readStream(sparkSession: SparkSession): StreamReader = {
        new StreamReader(sparkSession)
    }

    def readFromKafka(sparkSession: SparkSession, brokers: String): KafkaStreamReader = {
        new KafkaStreamReader(sparkSession, brokers)
    }

    def readFromRate(sparkSession: SparkSession): StreamReader = {
        this.readStream(sparkSession).inputSource(rateSource)
    }

    def readFromSocket(sparkSession: SparkSession, host: String, port: Int): SocketStreamReader = {
        SocketStreamReader(sparkSession, host: String, port: Int)
    }

    implicit class DataFrameImplicit(dataFrame: DataFrame) {

        def wrap(): StreamWriter[Row] = {
            new StreamWriter(dataFrame.writeStream)
        }

        def write2Kafka(brokers: String): KafkaStreamWriter[Row] = {
            new KafkaStreamWriter(dataFrame.writeStream, brokers)
        }

        def print2Console: ConsoleStreamWriter[Row] = {
            ConsoleStreamWriter(dataFrame.writeStream)
        }

    }

    implicit class WriterImplicit[T: ClassTag](dataStreamWriter: DataStreamWriter[T]) {
        def wrap(): StreamWriter[T] = {
            new StreamWriter(dataStreamWriter)
        }

        def write2Kafka(brokers: String): KafkaStreamWriter[T] = {
            new KafkaStreamWriter(dataStreamWriter, brokers)
        }

        def print2Console: ConsoleStreamWriter[T] = {
            ConsoleStreamWriter(dataStreamWriter)
        }
    }

}
