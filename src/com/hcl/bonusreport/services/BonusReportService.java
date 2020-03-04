package com.hcl.bonusreport.services;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class BonusReportService {

	public static void readAttendanceFile(Path path) {

	}

	public static void readAttendanceFile(File file) {
		try {
			Workbook workbook = WorkbookFactory.create(file);
			// Retrieving the number of sheets in the Workbook
			System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			System.out.println("Retrieving Sheets using Iterator");
			while (sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();
				System.out.println("=> " + sheet.getSheetName());
			}

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Create a DataFormatter to format and get each cell's value as
			// String
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : sheet) {
				for (Cell cell : row) {
					String cellValue = dataFormatter.formatCellValue(cell);
					if (!cellValue.trim().isEmpty()) {
						System.out.print(cellValue + "\t");
					}
				}
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void readAttendanceFile(String path) {

	}

}
