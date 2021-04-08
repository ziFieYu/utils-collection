package org.sa.utils.universal.config

import java.util

import com.typesafe.config.ConfigFactory
import org.sa.utils.universal.core.CoreConstants
import org.scalatest.FunSuite

import scala.collection.JavaConversions._
import scala.util.Try

/**
 * Created by yuqitao on 2017/12/6.
 */
class FileConfigTest extends FunSuite {

    test("config 1") {
        val config: Config = FileConfig()
        println("value of empty_config is " + config.getProperty("empty_config"))
        println("value of some_config is " + config.getProperty("some_config"))
        println("value of embedded_config is " + config.getProperty("embedded_config"))
        println("value of embedded_empty_config is " + config.getProperty("embedded_empty_config"))
        println("value of new_config is " + config.newConfigItem("new_config", "new_config_value").stringValue)
        Try {
            config.getProperty("null_config")
        }
            .getOrElse {
                println("value of null_config not found")
            }
        println("value of complex_config is " + config.getProperty("complex_config"))
        config.newConfigItem("un", """a:"1:2",b:"3:4"""").mapValue()
            .foreach(println)
    }

    test("implicit") {
        implicit val config: Config = FileConfig()
        val x = ConfigItem("some_config")
        println(x.stringValue)
        x.newValue(28237478)
        println(x.stringValue)
    }

    test("yaml") {
        System.setProperty(CoreConstants.profileExtensionKey, ".yaml")
        val config = FileConfig()
        val a = config.get[Int]("config.int")
        println("config.int" + a)
        val b = config.get[String]("config.string")
        println("config.string" + b)
        val c = config.get[util.ArrayList[Int]]("config.intlist")
        println("config.intlist" + c)
        val d = config.get[util.ArrayList[String]]("config.stringlist")
        println("config.stringlist" + d)
        val e = config.get[util.ArrayList[util.HashMap[String, Object]]]("config.listmap")
        println("config.listmap" + e)
        val f = config.get[util.HashMap[String, util.ArrayList[util.HashMap[String, Object]]]]("config.maplist")
        println("config.maplist " + f)
        val g = config.get[util.HashMap[String, AnyRef]]("config.map")
        println("config.map " + g)
    }

    test("typesafe-config") {
        System.setProperty("some_config", "1234567890")
        val config1 = ConfigFactory.load("application.properties")
        config1.entrySet().foreach {
            entry => println(entry.getKey, entry.getValue)
        }
        println("=" * 200)
        val config2 = ConfigFactory.load("application.yaml")
        config2.entrySet().foreach {
            entry => println(entry.getKey, entry.getValue)
        }
    }

}
