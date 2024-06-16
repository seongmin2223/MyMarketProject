package com.mypagingboard.demo.controller;

import com.mypagingboard.demo.controller.DownloadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@RestController
public class FileController {

    private final DownloadUtil downloadUtil;

    public FileController(DownloadUtil downloadUtil) {
        this.downloadUtil = downloadUtil;
    }

    @RequestMapping("/file")
    public void excel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = "src/main/resources/excel/excelsample.xlsx";
        File file = new File(filePath);

        XSSFWorkbook xssfWorkBook;
        XSSFSheet xssfSheet;

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                xssfWorkBook = new XSSFWorkbook(fis);
                xssfSheet = xssfWorkBook.getSheetAt(0);
            }
        } else {
            xssfWorkBook = new XSSFWorkbook();
            xssfSheet = xssfWorkBook.createSheet("Sheet1");
        }

        // 기본 데이터 추가
        XSSFRow row = xssfSheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("sample data1");

        XSSFRow row2 = xssfSheet.createRow(1);
        XSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("sample data2");

        // 파일명 인코딩
        String fileName = "copy_data.xlsx";
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        // 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 엑셀 파일 쓰기
        xssfWorkBook.write(response.getOutputStream());
        xssfWorkBook.close();
    }

    @RequestMapping("/file/zip")
    public void fileZip(HttpServletResponse response, HttpServletRequest request) {
        downloadUtil.downloadZip("src/main/resources/excel/excelsample.xlsx", response);
    }
}
