package org.sa.utils.spark.hbase

import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.sql.datasources.hbase.HBaseTableCatalog
import org.sa.utils.hadoop.constants.ZookeeperConfigConstants
import org.sa.utils.hadoop.hbase.implicts.HBaseImplicits._
import org.sa.utils.hadoop.hbase.{HBaseCatalog, HBaseEnvironment}
import org.sa.utils.spark.SparkUtils
import org.sa.utils.spark.sql.SparkSQL
import org.sa.utils.universal.cli.PrintConfig
import org.sa.utils.universal.config.{Config, FileConfig}
import org.scalatest.FunSuite

class SparkHBaseTest extends FunSuite with HBaseEnvironment with PrintConfig with ZookeeperConfigConstants {
    private lazy val handler = SparkHBaseHandler(zookeeperQuorum, zookeeperPort)
    override protected val config: Config = FileConfig()
    override protected val zookeeperQuorum: String = ZOOKEEPER_QUORUM.stringValue
    override protected val zookeeperPort: Int = ZOOKEEPER_PORT.intValue


    test("HBase RDD to DataFrame") {
        val hBaseCatalog = HBaseCatalog("", "User", "id",
            Map(
                "level" -> ("cf", "level", "string"),
                "nickname" -> ("cf", "nickname", "string"),
                "user_id" -> ("cf", "user_id", "string")
            )
        )
        val x = handler.df(hBaseCatalog)
        x.show()
    }


    test("Read by catalog") {
        val catalog =
            """{
              |	"table": {
              |		"namespace": "default",
              |		"name": "User"
              |	},
              |	"rowkey": "key",
              |	"columns": {
              |		"rk": {
              |			"cf": "rowkey",
              |			"col": "key",
              |			"type": "string"
              |		},
              |		"user_id": {
              |			"cf": "cf",
              |			"col": "user_id",
              |			"type": "string"
              |		},
              |		"nickname": {
              |			"cf": "cf",
              |			"col": "nickname",
              |			"type": "string"
              |		},
              |		"level": {
              |			"cf": "cf",
              |			"col": "level",
              |			"type": "int"
              |		}
              |	}
              |}""".stripMargin
        val x = SparkUtils.getSparkSession().read.options(Map(HBaseTableCatalog.tableCatalog -> catalog)).format("org.apache.hadoop.hbase.spark").load()
        x.show()
        x.createOrReplaceGlobalTempView("x")
        SparkSQL.sql("select * from x").show()
    }

    test("HBase RDD") {
        val conf = this.configuration
        conf.set(TableInputFormat.INPUT_TABLE, "User")
        SparkUtils.getSparkSession().sparkContext
            .newAPIHadoopRDD(this.configuration, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
            .foreachPartition(_.foreach(_._2.prettyShow(render, alignment, linefeed)))
    }
}
