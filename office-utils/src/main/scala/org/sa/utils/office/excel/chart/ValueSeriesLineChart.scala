package org.sa.utils.office.excel.chart

import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart
import org.sa.utils.office.excel.enumeration.ExcelEnumerations.{Direction, LineChartGrouping, Order}
import org.sa.utils.office.excel.sheet.OOXMLSheet

class ValueSeriesLineChart(val ooxmlSheet: OOXMLSheet,
                           override val chartTitle: String,
                           val direction: Direction.Value = Direction.vertical,
                           override val xAxisColumnName: String,
                           override val yAxisColumnName: String = "",
                           override val seriesValueColumn: String,
                           override val seriesValues: Array[String] = Array[String](),
                           val rowOffset: Int = 0,
                           val height: Int = 20,
                           val columnOffset: Int = 0,
                           val width: Int = 10,
                           val order: Order.Value = Order.desc,
                           override val take: Int = 1,
                           override val grouping: LineChartGrouping.Value = LineChartGrouping.standard,
                           override val labeled: Boolean = false) extends LineChart with ValueSeries {

    override def paddingData(ctLineChart: CTLineChart): Unit = {
        LineChart.paddingData(ctLineChart, labeled, this.xAxisValues, this.seriesMapping)
    }

}
