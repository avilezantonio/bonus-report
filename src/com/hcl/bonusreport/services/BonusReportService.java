package com.hcl.bonusreport.services;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.hcl.bonusreport.model.TimeEntry;

public class BonusReportService {

	private static final DataFormatter DATA_FORMATTER = new DataFormatter();

	public static void readAttendanceFile(Path path) {

	}

	public static void readAttendanceFile(File file) {
		Map<String, List<Row>> usersData = getUsersData(file);

		usersData.forEach((k, v) -> {
			if (null != v) {
				System.out.println("User: " + k + "no filtered entries: " + v.size());
				System.out.println("User: " + k + "filtered entries: " + getEntries(v).size());

				getEntries(v).forEach(System.out::println);

			}
		});

	}

	private static Map<String, List<Row>> getUsersData(File file) {
		Map<String, List<Row>> someData = new HashMap<>();
		try {
			final Workbook workbook = WorkbookFactory.create(file);
			final Sheet sheet = workbook.getSheetAt(0);

			List<Row> rows = new ArrayList<>();
			String userName = null;
			for (Row row : sheet) {
				boolean isUser = false;
				for (Cell cell : row) {
					String cellValue = DATA_FORMATTER.formatCellValue(cell);

					if (cellValue.trim().toLowerCase().contains("summaries")) {
						someData.replace(userName, rows);
						rows = new ArrayList<>();
						userName = null;
						break;
					}

					if (isUser && !cellValue.trim().isEmpty()) {
						userName = cellValue;
						someData.put(userName, null);
						break;
					}

					if (null != userName && someData.containsKey(userName) && null == someData.get(userName)
							&& getRowCount(row) > 4 && getRowCount(row) < 7) {
						rows.add(row);
					}

					isUser = isUser ? true : cellValue.toLowerCase().contains("user");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return someData;
	}

	private static void printRow(Row row) {
		StringBuilder builder = new StringBuilder();
		for (Cell c : row) {
			builder.append(DATA_FORMATTER.formatCellValue(c)).append(" ");
		}

		System.out.println(builder);
	}

	private static int getRowCount(Row row) {
		int i = 0;
		for (Cell cell : row) {
			String cellValue = DATA_FORMATTER.formatCellValue(cell);

			if (!cellValue.trim().isEmpty()) {
				i++;
			}
		}
		return i;
	}

	private static List<TimeEntry> getEntries(List<Row> rows) {
		List<TimeEntry> entries = new ArrayList<>();
		for (Row row : rows) {

			int cellCounter = 0;
			TimeEntry entry = new TimeEntry();
			for (Cell cell : row) {
				String cellValue = DATA_FORMATTER.formatCellValue(cell);
				if (!cellValue.trim().isEmpty()) {
					switch (cellCounter++) {
					case 0:
						entry.setDate(cellValue);
						break;
					case 1:
						entry.setDay(cellValue);
						break;
					case 2:
						entry.setIn(cellValue);
						break;
					case 3:
						entry.setOut(cellValue);
						break;
					case 4:
						entry.setDuration(cellValue);
						break;
					case 5:
						entry.setComments(cellValue);
						break;
					}
				}
			}
			entries.add(entry);
		}

		return entries.stream().distinct().collect(Collectors.toList());
	}

}
