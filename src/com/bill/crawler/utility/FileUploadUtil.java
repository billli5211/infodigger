package com.bill.crawler.utility;

import java.io.File;

import com.bill.crawler.config.SystemConfig;
import com.bill.crawler.global.PathConst;
import com.bill.crawler.third.alioss.AliOSS;
import com.bill.crawler.third.sftp.SFTPAPI;
import com.bill.crawler.third.sftp.UploadConfig;

public class FileUploadUtil {

	public static int uploadOSS(File imageDir) throws Exception {
		File file[] = imageDir.listFiles();
		int size = 0;
		String dateStr = DateUtil.getSimpleCurrentDate();
		File currentFile = null;
		String fileName = null;
		for (int i = 0; i < file.length; i++) {
			currentFile = file[i];
			if (currentFile.isFile()) {
				fileName = currentFile.getName();
				AliOSS.uploadImg(dateStr + "/" + fileName, currentFile.getAbsolutePath());
				size++;
				LogUtil.logger.info("uploaded No:" + size + " images, name is:" + fileName);
			}
		}

		return size;
	}

	public static void uploadCsvFiles(String time) throws Exception {
		// zip first

		String csvPath = SystemConfig.getCsvPath();
		String timePath = csvPath + time;
		String zipName = "csv";// generate csv.zip under timePath
		boolean flag = ZipFileUtil.fileToZip(timePath, csvPath, "csv");
		if (flag) {
			LogUtil.logger.info(">>>>>> csv文件打包成功. <<<<<<");
		} else {
			LogUtil.logger.info(">>>>>> csv文件打包失败. <<<<<<");
			return;
		}

		// upload csv to remote sftp server

		String dst = UploadConfig.ServerBasePath + PathConst.CsvFolderName + "/" + time; // 目标文件名

		String zipFilePath = csvPath + zipName + ".zip";
		File timeFile = new File(zipFilePath);
		if (timeFile.exists() == false) {
			return;
		}

		SFTPAPI.uploadFile(zipFilePath, dst);

		// after finish, delete
		timeFile.delete();
	}

	public static void uploadPageFiles(String time) throws Exception {
		// zip first
		String timePath = SystemConfig.getPagePath() + time;
		boolean flag = ZipFileUtil.fileToZipForPage(timePath, time);
		if (flag) {
			LogUtil.logger.info(">>>>>> page文件打包成功. <<<<<<");
		} else {
			LogUtil.logger.info(">>>>>> page文件打包失败. <<<<<<");
			return;
		}

		// upload pages to remote sftp server
		String dst = UploadConfig.ServerBasePath + PathConst.PageFolderName + "/" + time; // 目标文件名

		File timeFile = new File(timePath);
		if (timeFile.exists() == false) {
			return;
		}

		File[] sites = timeFile.listFiles();

		for (int i = 0; i < sites.length; i++) {
			//
			String src = sites[i].getAbsolutePath();
			if (sites[i].isFile() && src.endsWith(".zip")) {
				SFTPAPI.uploadFile(src, dst);

				// after finish, delete
				sites[i].delete();
			}
		}
	}
}
