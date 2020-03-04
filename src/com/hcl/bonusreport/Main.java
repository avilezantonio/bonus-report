package com.hcl.bonusreport;

import java.io.File;

import javax.swing.JFileChooser;

import com.hcl.bonusreport.services.BonusReportService;

public class Main {

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();

		chooser.showOpenDialog(null);

		File file = chooser.getSelectedFile();

		BonusReportService.readAttendanceFile(file);
	}

}
