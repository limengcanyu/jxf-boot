package com.spring.boot.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;

class ReadAliyun {
    @Test
    void read() throws IOException {
        String path = "E:/VSCodeProjects/jxf-docs/数据库设计/公有云采集数据库设计v1.0 - 2021-03-09.xlsx";
        System.out.println(path);
        File file = new File(path);
        Workbook wb = WorkbookFactory.create(file);

        Sheet runInstances = wb.getSheet("RunInstances");

        int firstRowNum = runInstances.getFirstRowNum();
        int lastRowNum = runInstances.getLastRowNum();
//        System.out.println("firstRowNum: " + firstRowNum + " lastRowNum: " + lastRowNum);

        for (int i = firstRowNum; i <= lastRowNum; i++) {
//            System.out.println("currentRowNum: " + i);

            Row row = runInstances.getRow(i);

            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
//            System.out.println("firstCellNum: " + firstCellNum + " lastCellNum: " + lastCellNum);

            for (int j = firstCellNum; j <= lastCellNum; j++) {
//                System.out.println("currentCellNum: " + i);

                Cell cell = row.getCell(j);
                if (ObjectUtils.isEmpty(cell)) {
                    continue;
                }

                CellAddress address = cell.getAddress();

                String cellValue;

//                switch (cell.getCellType()) {
//                    case STRING -> cellValue = cell.getStringCellValue();
//                    case NUMERIC -> cellValue = cell.getNumericCellValue() + "";
//                    case BOOLEAN -> cellValue = cell.getBooleanCellValue() + "";
//                    case BLANK -> cellValue = "";
//                    default -> throw new IllegalStateException("Unexpected value: " + cell.getCellType());
//                }
//                System.out.println("cellValue: " + cellValue);
            }

        }
    }
}


