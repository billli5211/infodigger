package com.bill.crawler.utility;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.google.common.io.Files;

public class CommonUtil {

	public static boolean isEmptyString(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static void save(byte[] data, String filePath) {
		try {
			Files.write(data, new File(filePath));
			LogUtil.logger.info("Stored: {}", filePath);
		} catch (IOException iox) {
			LogUtil.logger.error("Failed to write file: " + filePath, iox);
		}
	}

	public static String trimTitle(String title) {
		if (isEmptyString(title)) {
			return "";
		}

		String str = title.trim();
		str = str.replaceAll(" ", "_").replace(",", "_").replace("，", "_").replace(".", "");

		str = str.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=]*", "");
		return str;
	}

	public static String getRandomName() {
		// get a unique name for storing this image
		String hashedName = UUID.randomUUID() + "";
		return hashedName;
	}

	public static String getExtenstion(String str) {
		String extension = str.substring(str.lastIndexOf('.'));
		return extension;
	}

	public static int getNumberFromString(String text) {
		// 1272人阅读
		String splitter = "人";
		if (text.contains(splitter)) {
			String[] array = text.split(splitter);

			if (array != null && array.length > 1) {
				return Integer.parseInt(array[0]);
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		String str = getNumberFromString("1272人阅读") + "";
		LogUtil.logger.info("result:" + str);

		LogUtil.logger.info("result:" + trimTitle("百度一下，你就知道") + "\n");
		LogUtil.logger.info("result:" + trimTitle("Wikipedia, the free encyclopedia") + "\n");
	}
}
