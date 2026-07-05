package com.example.meeting.service.utils;

import com.example.meeting.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtils {
    public static List<User> parseExcel(MultipartFile file) {
        List<User> users = new ArrayList<>();

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i ++ ) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String name = getCellStringValue(row.getCell(0));
                String workNum = getCellStringValue(row.getCell(1));
                String phoneNum = getCellStringValue(row.getCell(2));
                Long departmentId = getCellLongValue(row.getCell(3));

                users.add(new User(null, null, null, false, name, workNum, phoneNum, departmentId));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> null;
        };
    }
    private static Long getCellLongValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> Long.parseLong(cell.getStringCellValue());
            case NUMERIC -> (long) cell.getNumericCellValue();
            default -> null;
        };
    }
}
