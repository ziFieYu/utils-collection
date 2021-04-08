package org.sa.utils.office.excel.chart

import org.openxmlformats.schemas.drawingml.x2006.chart.{CTPie3DChart, CTPieChart}
import org.sa.utils.office.excel.enumeration.ExcelEnumerations.Direction
import org.sa.utils.office.excel.sheet.OOXMLSheet

class ColumnSeriesPieChart(val ooxmlSheet: OOXMLSheet,
                           override val chartTitle: String,
                           val direction: Direction.Value = Direction.vertical,
                           override val xAxisColumnName: String,
                           val seriesNameColumns: Array[String],
                           val rowOffset: Int = 0,
                           val height: Int = 20,
                           val columnOffset: Int = 0,
                           val width: Int = 10,
                           override val labeled: Boolean = false,
                           override val is3D: Boolean = false) extends PieChart with ColumnSeries {

    override protected def paddingData(ctPieChart: CTPieChart): Unit = {
        val ctPieSeries = ctPieChart.addNewSer()
        PieChart.paddingData(ctPieSeries, this.xAxisValues, this.seriesMapping)
    }

    override protected def paddingData(ctPie3DChart: CTPie3DChart): Unit = {
        val ctPieSeries = ctPie3DChart.addNewSer()
        PieChart.paddingData(ctPieSeries, this.xAxisValues, this.seriesMapping)
    }

}