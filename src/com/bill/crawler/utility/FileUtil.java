package com.bill.crawler.utility;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	/*
	 * get the new new for a html file
	 */
	public static String getNewPageName(String url) {
		String lastStr = getLastStringFromUrl(url);
		if (url.endsWith(FilterUtil.HTML_SUFFIX)) {
			return lastStr;
		} else {
			return lastStr + FilterUtil.HTML_SUFFIX;
		}
	}

	public static String getFileNameFromUrl(String url) {
		String retVal = null;

		// http://liuyj.blog.51cto.com/2340749/1711412
		retVal = url.trim();
		if (retVal.startsWith(FilterUtil.HTTP_PROTOCOL)) {
			retVal = retVal.substring(FilterUtil.HTTP_PROTOCOL.length());
		}
		retVal = retVal.replace("/", "_").replace(" ", "").replace(".", "_");

		return retVal;
	}

	public static String getLastStringFromUrl(String url) {
		String retVal = null;

		// http://liuyj.blog.51cto.com/2340749/1711412
		retVal = url.trim();

		if (retVal.endsWith("/") && retVal.length() > 2) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}

		retVal = retVal.substring(retVal.lastIndexOf("/") + 1);

		return retVal;
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);

		if (file.isFile() && file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// if needBake, dont need care about needDeleteFirst
	public static File createFile(String path, boolean needBake, boolean needDeleteFirst) {
		File file = new File(path);

		if (file.isFile() && file.exists()) {
			if (needBake) {
				backUpFile(file);
			} else if (needDeleteFirst) {
				file.delete();
			}
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}

		return file;
	}

	public static File createFolder(String path, boolean needBake, boolean needDeleteFirst) {
		File folder = new File(path);
		if (folder.exists() && folder.isDirectory()) {
			if (needBake) {
				backUpFile(folder);
			} else if (needDeleteFirst) {
				deleteDir(folder);
			}

		}

		folder.mkdirs();
		return folder;
	}

	// back up folder or file by renaming
	public static void backUpFile(File file) {
		String fileName = file.getName();
		String newPath = file.getAbsolutePath() + "_" + CommonUtil.getRandomName();
		if (file.isFile()) {
			newPath = newPath + CommonUtil.getExtenstion(fileName);
		}
		File newFile = new File(newPath);
		file.renameTo(newFile);
	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public static void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			LogUtil.logger.info("Successfully deleted empty directory: " + dir);
		} else {
			LogUtil.logger.info("Failed to delete empty directory: " + dir);
		}
	}

	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		boolean retVal = false;
		if (dir.isDirectory()) {
			String[] children = dir.list();// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return retVal;
				}
			}
		}
		// 目录此时为空，可以删除
		retVal = dir.delete();
		if (retVal) {
			LogUtil.logger.info("Successfully deleted populated directory: " + dir.getAbsolutePath());
		} else {
			LogUtil.logger.info("Failed to delete populated directory: " + dir.getAbsolutePath());
		}

		return retVal;
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		String result = null;
		String str = "http://liuyj.blog.51cto.com/2340749/p-122.html";

		/*
		 * String newDir2 = "/Users/billytech/Desktop/mydata/storeFolder";
		 * createFolder(newDir2, true, false);
		 * 
		 * // String url = "http://liuyj.blog.51cto.com/2340749/1711412.zip";
		 * result = getLastStringFromUrl(url);
		 * logger.info("file name from url is:" + result);
		 */
	}
}