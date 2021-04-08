package org.sa.utils.spark.implicits

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
import org.sa.utils.universal.base.Symbols._
import org.sa.utils.universal.cli.PrintConfig
import org.sa.utils.universal.implicits.ArrayConversions._
import org.sa.utils.universal.implicits.BasicConversions._

object DataFrameConversions extends PrintConfig {

    implicit class DataFrameImplicits(dataFrame: DataFrame) {
        lazy val columns: Array[String] = dataFrame.columns
        lazy val toArray: Array[Array[String]] = dataFrame.collect().map(e => columns.map(c => e.get(e.fieldIndex(c)).n2e(null2Empty)))
        lazy val size: Int = this.toArray.length

        /**
         * bool转int
         *
         * @return
         */
        def castBoolean2Int: DataFrame = {
            var df = dataFrame
            df.schema.foreach(sf => {
                sf.dataType.typeName match {
                    case "boolean" => df = df.withColumn(sf.name, df(sf.name).cast(IntegerType))
                    case _ =>
                }
            })
            df
        }

        /**
         * 根据《字段-类型》的映射修改字段类型
         *
         * @param mapping 《字段-类型》映射关系
         * @return
         */
        def cast(mapping: Map[String, String]): DataFrame = {
            var df = dataFrame
            mapping.foreach(m => {
                val column = m._1
                m._2.toLowerCase match {
                    case "bool" => df = df.withColumn(column, df(column).cast(BooleanType))
                    case "boolean" => df = df.withColumn(column, df(column).cast(BooleanType))
                    case "double" => df = df.withColumn(column, df(column).cast(DoubleType))
                    case "float" => df = df.withColumn(column, df(column).cast(FloatType))
                    case "int" => df = df.withColumn(column, df(column).cast(IntegerType))
                    case "integer" => df = df.withColumn(column, df(column).cast(IntegerType))
                    case "long" => df = df.withColumn(column, df(column).cast(LongType))
                    case "bigint" => df = df.withColumn(column, df(column).cast(LongType))
                    case other => if (other.startsWith("decimal"))
                        df = df.withColumn(column, df(column).cast(other))
                }
            })
            df
        }

        /**
         * 显示DataFrame的数据
         *
         * @param length    显示的行数
         * @param truncate  数据长度大于this.defaultNumber时是否省略显示
         * @param alignment 对齐方式
         */
        def prettyShow(length: Int = length, truncate: Boolean = truncate, columns: Seq[String] = this.columns, alignment: Any = alignment): Unit = {
            this.toArray.prettyShow(length, truncate, columns, alignment)
        }

        /**
         * 转换为表格
         *
         * @param length    显示的行数
         * @param truncate  数据长度大于20时是否省略显示
         * @param alignment 对齐方式
         * @return
         */
        def toTable(length: Int = length, truncate: Boolean = truncate, columns: Seq[String] = this.columns, alignment: Any = alignment, explode: => Boolean = explode): (String, Seq[String], String) = {
            this.toArray.toTable(length, truncate, columns, alignment, explode)
        }

        /**
         * DataFrame转Html
         *
         * @return
         */
        def toHtmlTable: String = {
            val header = dataFrame.schema.map(_.name).mkString("<tr><th>", "</th><th>", "</th></tr>")
            val rows = dataFrame.collect().map(_.mkString("<tr><td>", "</td><td>", "</td></tr>")).mkString(lineSeparator)
            s"""<table border=1 cellspacing=0>\n$header$rows"""
        }

        /**
         * 转为JSON
         *
         */
        def toJson: Seq[String] = this.toArray.toJson(columns)

        /**
         * 垂直显示
         *
         */
        def toVertical: Seq[String] = this.toArray.toVertical(columns)

        /**
         * 转为属性表述的XML
         *
         * @param collection 根节点名称
         * @param member     第一层叶子节点名称
         */
        def toXMLWithAttributes(collection: String, member: String): (String, Seq[String], String) = this.toArray.toXMLWithAttributes(collection, member, this.columns)

        /**
         * 转为元素表述的XML
         *
         * @param collection 根节点名称
         * @param member     第一层叶子节点名称
         */
        def toXMLWithElements(collection: String, member: String): (String, Seq[String], String) = this.toArray.toXMLWithElements(collection, member, this.columns)

    }

}
