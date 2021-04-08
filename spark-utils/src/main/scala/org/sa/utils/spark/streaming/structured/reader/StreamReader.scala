package org.sa.utils.spark.streaming.structured.reader

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.sa.utils.spark.streaming.structured.source.{Source, SourceOption}

class StreamReader(private val sparkSession: SparkSession) {
    private val dataStreamReader = sparkSession.readStream

    def inputSource(source: Source): this.type = {
        dataStreamReader.format(source.toString)
        this
    }

    def option(option: SourceOption, optionValue: Any): this.type = {
        dataStreamReader.option(option.toString, optionValue.toString)
        this
    }

    def load(): DataFrame = {
        this.dataStreamReader.load()
    }

}
