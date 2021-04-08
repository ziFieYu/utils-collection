package org.sa.utils.spark.sql

import org.apache.spark.sql.DataFrame
import org.sa.utils.spark.SparkUtils

/**
 * Created by Stuart Alex on 2016/5/26.
 */
object SparkSQL {
    private lazy val sparkSession = SparkUtils.getSparkSession()

    def mysql: SparkMySQLUtils.type = SparkMySQLUtils

    def sqlServer: SparkSQLServerUtils.type = SparkSQLServerUtils

    def sql(sql: String): DataFrame = sparkSession.sql(sql)

}
