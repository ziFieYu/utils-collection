package org.sa.utils.office.excel.workbook

import java.io.FileInputStream

import org.apache.poi.openxml4j.util.ZipSecureFile
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.sa.utils.office.excel.sheet.OOXMLSheet

case class OOXMLWorkBook(excelFileName: String, private val createWhenNotExist: Boolean = true) extends WorkBook {
    this.checkExtension("xlsx")
    this.backup(createWhenNotExist)
    ZipSecureFile.setMinInflateRatio(-1.0d)
    override val workbook: XSSFWorkbook = {
        if (excelFile.exists()) {
            new XSSFWorkbook(new FileInputStream(excelFileName))
        } else {
            new XSSFWorkbook()
        }
    }

    def getSheet(sheetName: String, overwrite: Boolean = false): OOXMLSheet = {
        OOXMLSheet(this.workbook, sheetName, overwrite)
    }

    def writeSheet(sheetName: String, overwrite: Boolean, columns: List[String] = List[String](), rows: List[List[Any]]): this.type = {
        try {
            OOXMLSheet(this.workbook, sheetName, overwrite)
                .writeColumnHeader(columns)
                .writeData(rows)
        } catch {
            case e: Exception =>
                this.success = false
                this.close()
                throw e
        }
        this
    }

}
