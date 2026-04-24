package com.spring.boot.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class SpringBootPoiApplicationTests {

	@Test
	void contextLoads() {
		Workbook wb = new XSSFWorkbook();

		// New Sheet
//		Sheet sheet1 = wb.createSheet("new sheet");
//		Sheet sheet2 = wb.createSheet("second sheet");

		// Note that sheet name is Excel must not exceed 31 characters
		// and must not contain any of the any of the following characters:
		// 0x0000
		// 0x0003
		// colon (:)
		// backslash (\)
		// asterisk (*)
		// question mark (?)
		// forward slash (/)
		// opening square bracket ([)
		// closing square bracket (])
		// You can use org.apache.poi.ss.util.WorkbookUtil#createSafeSheetName(String nameProposal)}
		// for a safe way to create valid names, this utility replaces invalid characters with a space (' ')
//		String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]"); // returns " O'Brien's sales   "
//		Sheet sheet3 = wb.createSheet(safeName);

		// Creating Cells
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("new sheet");
		// Create a row and put some cells in it. Rows are 0 based.
		Row row = sheet.createRow(0);
		// Create a cell and put a value in it.
		Cell cell = row.createCell(0);
		cell.setCellValue(1);
		// Or do it on one line.
		row.createCell(1).setCellValue(1.2);
		row.createCell(2).setCellValue(createHelper.createRichTextString("This is a string"));
		row.createCell(3).setCellValue(true);

		try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
			wb.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void creatingDateCells() {
		Workbook wb = new XSSFWorkbook();

		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("new sheet");

		// Create a row and put some cells in it. Rows are 0 based.
		Row row = sheet.createRow(0);

		// Create a cell and put a date value in it.  The first cell is not styled
		// as a date.
		Cell cell = row.createCell(0);
		cell.setCellValue(new Date());
		// we style the second cell as a date (and time).  It is important to
		// create a new cell style from the workbook otherwise you can end up
		// modifying the built in style and effecting not only this cell but other cells.
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
		cell = row.createCell(1);
		cell.setCellValue(new Date());
		cell.setCellStyle(cellStyle);
		//you can also set date as java.util.Calendar
		cell = row.createCell(2);
		cell.setCellValue(Calendar.getInstance());
		cell.setCellStyle(cellStyle);

		// Write the output to a file
		try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
			wb.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void workingWithDifferentTypesOfCells() {
		Workbook wb = new XSSFWorkbook();

		Sheet sheet = wb.createSheet("new sheet");

		Row row = sheet.createRow(2);
		row.createCell(0).setCellValue(1.1);
		row.createCell(1).setCellValue(new Date());
		row.createCell(2).setCellValue(Calendar.getInstance());
		row.createCell(3).setCellValue("a string");
		row.createCell(4).setCellValue(true);
//		row.createCell(5).setCellType(CellType.ERROR);
		row.createCell(5).setCellErrorValue(FormulaError.DIV0.getCode()); // 设置单元格错误值

		// Write the output to a file
		try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
			wb.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
