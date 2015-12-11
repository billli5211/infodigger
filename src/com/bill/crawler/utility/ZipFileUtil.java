package com.bill.crawler.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.bill.crawler.config.SystemConfig;

/**
 * 将文件打包成ZIP压缩文件
 * 
 * @author LanP
 * @since 2012-3-1 15:47
 */
public final class ZipFileUtil {

	public static String zipFile(String dateStr) {
		String sourceFilePath = null;
		String zipFilePath = null;
		if (fileToZip(sourceFilePath, zipFilePath, dateStr)) {
			return zipFilePath + "/" + dateStr;
		} else {
			return null;
		}
	}

	/*
	 * folder will be: 2015121000/ cto: p1.html, p2.htm cto.zip csdn: a1.html,
	 * a2.html csdn.zip
	 * 
	 * file path:
	 * /Users/billytech/Desktop/mydata4/page/20151203143141/default/1710458.html
	 * input： timePath == /Users/billytech/Desktop/mydata4/page/20151203143141/
	 * timeName == 20151203143141
	 */
	public static boolean fileToZipForPage(String timePath, String timeName) {
		boolean retVal = false;
		File timeFile = new File(timePath);
		if (timeFile.exists() == false) {
			return retVal;
		}

		File[] sites = timeFile.listFiles();
		if (null == sites || sites.length < 1) {
			return retVal;
		}

		String siteName = null;
		for (int i = 0; i < sites.length; i++) {
			//
			if (sites[i].isDirectory()) {
				siteName = sites[i].getName();
				retVal = fileToZip(sites[i], timePath, siteName);
				if (retVal == false) {
					break;
				}
			}
		}

		return retVal;
	}

	public static boolean fileToZip(File sourceFile, String zipFilePath, String fileName) {
		boolean retVal = false;

		File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
		if (zipFile.exists()) {
			LogUtil.logger.info(">>>>>> " + zipFilePath + " 目录下存在名字为：" + fileName + ".zip" + " 打包文件. <<<<<<");
			return retVal;
		}

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {

			File[] sourceFiles = sourceFile.listFiles();
			if (null == sourceFiles || sourceFiles.length < 1) {
				LogUtil.logger.info(">>>>>> 待压缩的文件目录：" + sourceFile.getAbsolutePath()
						+ " 里面不存在文件,无需压缩. <<<<<<");
			} else {
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				byte[] bufs = new byte[1024 * 10];
				for (int i = 0; i < sourceFiles.length; i++) {
					if (sourceFiles[i].isDirectory()) {
						continue;
					}
					// 创建ZIP实体,并添加进压缩包
					ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
					zos.putNextEntry(zipEntry);
					// 读取待压缩的文件并写进压缩包里
					fis = new FileInputStream(sourceFiles[i]);
					bis = new BufferedInputStream(fis, 1024 * 10);
					int read = 0;
					while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
						zos.write(bufs, 0, read);
					}
				}
				retVal = true;

			}
		} catch (FileNotFoundException e) {
			LogUtil.logger.error(e.toString());
			throw new RuntimeException(e);
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				LogUtil.logger.error(e.toString());
				throw new RuntimeException(e);
			}
		}

		return retVal;
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * 
	 * @param sourceFilePath
	 *            待压缩的文件路径
	 * @param zipFilePath
	 *            压缩后存放路径
	 * @param fileName
	 *            压缩后文件的名称
	 * @return flag
	 * 
	 * 
	 *         sourceFilePath:/Users/billytech/Desktop/mydata4/csv/
	 *         20151203143141 zipFilePath: /Users/billytech/Desktop/mydata4/csv/
	 *         fileName:timeName --- 20151203143141
	 * 
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			LogUtil.logger.info(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 不存在. <<<<<<");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					LogUtil.logger.info(">>>>>> " + zipFilePath + " 目录下存在名字为：" + fileName + ".zip"
							+ " 打包文件. <<<<<<");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						LogUtil.logger.info(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩. <<<<<<");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {
							if (sourceFiles[i].isDirectory()) {
								continue;
							}

							// 创建ZIP实体,并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				LogUtil.logger.error(e.toString());
				throw new RuntimeException(e);
			} catch (IOException e) {
				LogUtil.logger.error(e.toString());
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					LogUtil.logger.error(e.toString());
					throw new RuntimeException(e);
				}
			}
		}

		return flag;
	}

	/**
	 * 将文件打包成ZIP压缩文件,main方法测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String sourceFilePath = SystemConfig.getPagePath() + "20151202/csdn";
		String zipFilePath = SystemConfig.getBasePath();
		String fileName = "Page";
		boolean flag = ZipFileUtil.fileToZip(sourceFilePath, zipFilePath, fileName);
		if (flag) {
			LogUtil.logger.info(">>>>>> page文件打包成功. <<<<<<");
		} else {
			LogUtil.logger.info(">>>>>> page文件打包失败. <<<<<<");
		}

		sourceFilePath = SystemConfig.getCsvPath();
		fileName = "Csv.zip";
		flag = ZipFileUtil.fileToZip(sourceFilePath, zipFilePath, fileName);
		if (flag) {
			LogUtil.logger.info(">>>>>> csv文件打包成功. <<<<<<");
		} else {
			LogUtil.logger.info(">>>>>> csv文件打包失败. <<<<<<");
		}
	}
}