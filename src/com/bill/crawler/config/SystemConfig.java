package com.bill.crawler.config;

import java.io.File;

import com.bill.crawler.global.PathConst;
import com.bill.crawler.utility.FileUtil;

public class SystemConfig {
	// read from system config
	private static String basePath;
	private static boolean enableDataUploadToServer;
	private static boolean savePureContent;
	private static boolean enableBinHandling;
	
	private static boolean enableConcurrentCrawling;
	private static int crawlingPerformanceLevel;
			

	// create after get basePath
	private static String rootPath;
	private static String pagePath;
	private static String csvPath;
	private static String imagePath;

	private static void createFolder() {
		
		rootPath = SystemConfig.getBasePath() + PathConst.RootFolderName + "/";
		pagePath = SystemConfig.getBasePath() + PathConst.PageFolderName + "/";
		csvPath = SystemConfig.getBasePath() + PathConst.CsvFolderName + "/";
		imagePath = SystemConfig.getBasePath() + PathConst.ImageFolderName + "/";

		/*
		 * create first level folders if does not exist; no need to back up if
		 * has exited.
		 */
		FileUtil.createFolder(basePath, true, false);
		FileUtil.createFolder(rootPath, false, false);
		FileUtil.createFolder(pagePath, false, false);
		FileUtil.createFolder(csvPath, false, false);
		FileUtil.createFolder(imagePath, false, false);
	}

	public static String getBasePath() {
		return basePath;
	}

	public static String getRootPath() {
		return rootPath;
	}

	public static String getPagePath() {
		return pagePath;
	}

	public static String getCsvPath() {
		return csvPath;
	}

	public static String getImagePath() {
		return imagePath;
	}

	public static void setBasePath(String path) {
		basePath = path;
		createFolder();
	}

	public static boolean isEnableDataUploadToServer() {
		return enableDataUploadToServer;
	}

	public static void setEnableDataUploadToServer(boolean enabled) {
		enableDataUploadToServer = enabled;
	}

	public static boolean isSavePureContent() {
		return savePureContent;
	}

	public static void setSavePureContent(boolean enabled) {
		savePureContent = enabled;
	}

	public static boolean isEnableBinHandling() {
		return enableBinHandling;
	}

	public static void setEnableBinHandling(boolean enabled) {
		enableBinHandling = enabled;
	}

	public static boolean isEnableConcurrentCrawling() {
		return enableConcurrentCrawling;
	}

	public static void setEnableConcurrentCrawling(boolean enableConcurrentCrawling) {
		SystemConfig.enableConcurrentCrawling = enableConcurrentCrawling;
	}

	public static int getCrawlingPerformanceLevel() {
		return crawlingPerformanceLevel;
	}

	public static void setCrawlingPerformanceLevel(int crawlingPerformanceLevel) {
		SystemConfig.crawlingPerformanceLevel = crawlingPerformanceLevel;
	}
}
