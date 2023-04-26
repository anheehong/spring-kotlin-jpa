package com.spring.jpa.utils.excel

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset


@Service
class ExcelUploader(){

    fun upload( excelFile: MultipartFile ): ArrayList<Any>? {
        val file = File(excelFile.originalFilename)
        excelFile.transferTo( file )

        val fileName = file.name
        val ext = fileName.substring(fileName.lastIndexOf(".") + 1)

        return when( ext ) {
            "xlsx" -> uploadXLSX(file)
            "csv" -> uploadCSV(file)
            else -> null
        }
    }

    fun uploadXLSX(excelFile: File): ArrayList<Any> {

        val opcPackage = OPCPackage.open( excelFile )
        val workbook = XSSFWorkbook(opcPackage)

        val sheet = workbook.getSheetAt(0)
        val readXlsx = ArrayList<Any>()

        for(idx in 0 .. sheet.lastRowNum) {
            val row = sheet.getRow( idx )
            val rowData = ArrayList<Any>()
            for(cellIdx in 0 .. row.lastCellNum) {
                row.getCell( cellIdx )?.let {
                    rowData.add(it)
                }
            }
            readXlsx.add(rowData)
        }
        return readXlsx
    }

    fun uploadCSV(excelFile: File): ArrayList<Any> {

        val bufferedReader = BufferedReader(FileReader(excelFile, Charset.forName("UTF-8")))
        val readCSV = ArrayList<Any>()

        var line = bufferedReader.readLine()
        while (line != null) {
            val lineContents = line.split(",")
            readCSV.add(lineContents)
            println( lineContents )
            line = bufferedReader.readLine()
        }

        return readCSV
    }



}