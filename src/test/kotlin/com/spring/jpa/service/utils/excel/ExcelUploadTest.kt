package com.spring.jpa.service.utils.excel

import com.spring.jpa.test.BaseTest
import com.spring.jpa.utils.excel.ExcelUploader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

class ExcelUploadTest(
    @Autowired val excelUploader: ExcelUploader
) : BaseTest() {

    val xlsxFile = File( "src/main/resources/excel/파일 테스트.xlsx")
    val csvFile = File( "src/main/resources/excel/파일 테스트_1.csv")
    val csvFile2 = File( "src/main/resources/excel/파일 테스트 - 시트1.csv")

    @Test
    fun excelUploaderXlsx(){
        print( xlsxFile )
        excelUploader.uploadXLSX( xlsxFile )
    }

    @Test
    fun excelUploaderCsv(){
        print( csvFile2 )
        excelUploader.uploadCSV( csvFile2 )
    }

}