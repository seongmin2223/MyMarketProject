package com.mypagingboard.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class DownloadUtil {

    public void downloadZip(String filePath, HttpServletResponse response) {
        try {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"download.zip\"");

            try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
                // 파일 경로에서 파일명 추출
                Path path = Path.of(filePath);
                // 파일 추가
                addFileToZip(path, zos);
            } catch (FileNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + filePath);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new IllegalArgumentException("파일을 다운로드할 수 없습니다: " + e.getMessage());
            }

            response.flushBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFileToZip(Path filePath, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }
}
