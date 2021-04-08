package org.sa.utils.universal.cli

import java.util.Properties

import org.apache.commons.cli.{HelpFormatter, Options}
import org.sa.utils.universal.base.{Logging, Symbols}
import org.sa.utils.universal.cli.Renders.Render
import org.sa.utils.universal.implicits.BasicConversions._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CliUtils extends Logging {
    /**
     * 转义符
     */
    private val ESCAPE = "\u001b["
    /**
     * 清除屏幕（光标位置不动）
     */
    private val CLEAR_SCREEN = "2J"
    /**
     * 光标上移
     */
    private val UP = "A"
    /**
     * 光标下移
     */
    private val DOWN = "B"
    /**
     * 光标左移
     */
    private val LEFT = "C"
    /**
     * 光标右移
     */
    private val RIGHT = "D"
    /**
     * 光标定点
     */
    private val POINTER = "H"
    /**
     * 删除光标后所有文本
     */
    private val DELETE_ALL_AFTER_CURSOR = "K"
    /**
     * 保存当前光标位置
     */
    private val STORE = "s"
    /**
     * 恢复上次光标位置
     */
    private val RESTORE = "u"

    /**
     * 清除屏幕（光标位置不动）
     */
    def clearScreen(): String = this.ESCAPE + this.CLEAR_SCREEN

    /**
     * 清除屏幕（光标移至最左上角）
     */
    def clearScreen2TopLeft(): String = this.ESCAPE + CLEAR_SCREEN + ESCAPE + "0;0" + this.POINTER

    /**
     * 删除当前行
     */
    def deleteCurrentRow(): Unit = printf(move2Begin() + deleteAllAfterCursor())

    /**
     * 向上删除若干行
     *
     * @param n 删除的行数
     */
    def deleteRowsUpward(n: Int): Unit = {
        for (_ <- 0 until n)
            printf(up(1) + move2Begin() + deleteAllAfterCursor())
    }

    /**
     * 删除光标后所有文本
     */
    def deleteAllAfterCursor(): String = this.ESCAPE + this.DELETE_ALL_AFTER_CURSOR

    /**
     * 光标移至行首
     */
    def move2Begin(): String = Symbols.carriageReturn

    /**
     * 上移若干行
     *
     * @param n 行数
     */
    def up(n: Int): String = this.ESCAPE + n + this.UP

    /**
     * 下移若干行
     *
     * @param n 行数
     */
    def down(n: Int): String = this.ESCAPE + n + this.DOWN

    /**
     * 左移若干行
     *
     * @param n 行数
     */
    def left(n: Int): String = this.ESCAPE + n + this.LEFT

    /**
     * 光标移至指定行和列
     *
     * @param x 行呀
     * @param y 列呀
     * @return
     */
    def point(x: Int, y: Int): String = this.ESCAPE + x + ";" + y + this.POINTER

    /**
     * 渲染文本
     *
     * @param string  原始文本
     * @param renders 渲染器
     * @return
     */
    def render(string: String, renders: Render*) = s"${this.ESCAPE}${renders.mkString(";")}m$string${this.ESCAPE}${Renders.RESET}m"

    /**
     * 渲染文本
     *
     * @param stringRenderPair 原始文本和渲染器配对
     * @return
     */
    def render(stringRenderPair: Array[(String, Render)]): String = {
        stringRenderPair.map { case (str, rend) => render(str, rend) }.mkString
    }

    /**
     * 重置所有设置
     */
    def reset(): String = this.ESCAPE + Renders.RESET

    /**
     * 恢复上次保存的光标位置
     */
    def restore(): String = this.ESCAPE + this.RESTORE

    /**
     * 右移若干行
     *
     * @param n 行数
     */
    def right(n: Int): String = this.ESCAPE + n + this.RIGHT

    /**
     * 保存光标当前所在位置
     */
    def store(): String = this.ESCAPE + this.STORE

    /**
     * 打印程序帮助文档
     *
     * @param usageSyntax 用法语法
     * @param header      文档首部显示的文字
     * @param options     程序选项列表
     * @param footer      文档尾部显示的文字
     */
    def printHelp(usageSyntax: String, header: String, options: Options, footer: String): Unit = {
        val helpFormatter = new HelpFormatter()
        helpFormatter.setWidth(150)
        helpFormatter.setSyntaxPrefix("")
        helpFormatter.printHelp(usageSyntax, header, options, footer)
    }

    /**
     * 打印输出帮助文档
     *
     * @param help 命令——帮助
     */
    def printHelp(help: Array[(String, String)], render: String): Unit = this.printHelp(help.toList, render)

    /**
     * 打印输出帮助文档
     *
     * @param help 命令——帮助
     */
    def printHelp(help: Seq[(String, String)], render: String): Unit = {
        val maxLength = help.map(_._1).map(_.length).max
        help.map {
            e =>
                val parts = e._2.split("\n")
                if (parts.length > 1)
                    e._1.pad(maxLength, ' ', -1) + "\t" + parts.head + "\n" + parts.tail.map(" " * maxLength + "\t" + _).mkString("\t", "\n\t", "")
                else
                    e._1.pad(maxLength, ' ', -1) + "\t" + e._2
        }
            .foreach(_.prettyPrintln(render))
    }

    /**
     * 解析命令行参数
     *
     * @param args 命令行参数
     */
    def parseArguments(args: Array[String]): Properties = {
        logInfo(s"received args: ${args.mkString(" ")}")
        val properties = new Properties()
        val listBuffer = ListBuffer[String]()
        val argumentsMapping = mutable.Map[String, Any]()
        var i = 0
        while (i < args.length) {
            if (args(i).startsWith("--")) {
                if (args(i).contains("=")) {
                    val kva = args(i).drop(2).split("=")
                    i += 1
                    // --current-key=value
                    if (kva.length == 1)
                        argumentsMapping += kva(0) -> ""
                    else
                        argumentsMapping += kva(0) -> kva(1)
                } else {
                    if (args.length > i + 1) {
                        if (args(i + 1).startsWith("--")) {
                            // --current-key --next-key
                            argumentsMapping += args(i).drop(2) -> true
                            i += 1
                        } else {
                            // --current-key value
                            argumentsMapping += args(i).drop(2) -> args(i + 1)
                            i += 2
                        }
                    } else {
                        // --last-key
                        argumentsMapping += args(i).drop(2) -> true
                        i += 1
                    }
                }
            } else {
                // parameter
                listBuffer += args(i)
                i += 1
            }
        }
        for ((key, value) <- argumentsMapping) {
            properties.put(key, value.toString)
            logInfo(s"parsed key $key, value $value")
        }
        properties
    }

}