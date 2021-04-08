package org.sa.utils.office.excel.chart

import org.apache.poi.ss.usermodel.charts.ChartDataSource
import org.openxmlformats.schemas.drawingml.x2006.chart.{CTPie3DChart, CTPieChart, CTPieSer}

object PieChart {

    def paddingData(ctPieSeries: CTPieSer, xAxisValues: Array[String], seriesMapping: Map[String, ChartDataSource[Number]]): Unit = {
        // 饼图只有一个图例（系列），如果take指定3、2、1，恰好是取倒数第3、2、1个系列
        val seriesName = seriesMapping.keys.head
        ctPieSeries.addNewTx().setV(seriesName)
        ctPieSeries.addNewIdx().setVal(0)
        // 横坐标区，用literal string的方式，也可以去定位sheet中的起始行列生成一个CellRangeAddress
        val ctStringData = ctPieSeries.addNewCat().addNewStrLit()
        for (m <- xAxisValues.indices) {
            val stringValue = ctStringData.addNewPt()
            stringValue.setIdx(m)
            stringValue.setV(xAxisValues(m))
        }
        // 数据区域
        val series = seriesMapping(seriesName)
        ctPieSeries.addNewVal().addNewNumRef().setF(series.getFormulaString)
        // 显示边框线
        ctPieSeries.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(Array[Byte](0, 0, 0))
    }

}

trait PieChart extends Chart {
    val chartTypeName = s"Pie${if (is3D) "3D" else ""}Chart"
    protected val is3D: Boolean
    protected val labeled: Boolean

    override def plot(): PieChart.this.type = {
        logInfo(s"start plot ${if (labeled) "labeled " else ""}${if (is3D) "3D " else ""}${this.getClass.getSimpleName} ${this.chartTitle} in sheet ${this.sheet.getSheetName}")
        this.setTitle(ctChart)
        this.setLegend(ctChart)
        if (is3D) {
            //val pie3DChart = this.createNewPie3DChart()
            val pie3DChart = this.createNewChart().asInstanceOf[CTPie3DChart]
            this.paddingData(pie3DChart)
            this.setDataLabels(pie3DChart.addNewDLbls()).label(labeled)
        } else {
            //val pieChart = this.createNewPieChart()
            val pieChart = this.createNewChart().asInstanceOf[CTPieChart]
            this.paddingData(pieChart)
            this.setDataLabels(pieChart.addNewDLbls()).label(labeled)
        }
        this
    }

    protected def paddingData(ctPieChart: CTPieChart): Unit

    protected def paddingData(ctPie3DChart: CTPie3DChart): Unit

    private def createNewPieChart(): CTPieChart = {
        val ctPlotArea = ctChart.getPlotArea
        val ctPieChart = ctPlotArea.addNewPieChart()
        ctPieChart.addNewVaryColors().setVal(true)
        ctPieChart
    }

    private def createNewPie3DChart(): CTPie3DChart = {
        val ctPlotArea = ctChart.getPlotArea
        val ctPie3DChart = ctPlotArea.addNewPie3DChart()
        ctPie3DChart.addNewVaryColors().setVal(true)
        ctPie3DChart
    }

}
