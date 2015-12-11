package com.bill.crawler.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// new SimpleDateFormat("yyyy-MM-dd");
	// new SimpleDateFormat("yyyyMMdd");
	private static DateFormat DataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static DateFormat SimpleDataFormat = new SimpleDateFormat("yyyyMMdd");

	public static String getCurrentDate() {
		Date date = new Date();
		return DataFormat.format(date);
	}

	public static String getSimpleCurrentDate() {
		Date date = new Date();
		return SimpleDataFormat.format(date);
	}

	public static String getDateFromString(String dateStr) {
		String retStr = null;
		try {
			Date dateTrans = SimpleDataFormat.parse(dateStr);
			retStr = dateTrans.toString();// dateTrans.toLocaleString()
		} catch (ParseException e) {
			LogUtil.logger.error(e.toString());
		}

		LogUtil.logger.info(retStr);
		return retStr;
	}

	public static void main(String[] args) {
		LogUtil.logger.info(getCurrentDate());

		String dateStr = "2015-11-30";
		getDateFromString(dateStr);

		LogUtil.logger.info("current date:" + getCurrentDate());
	}
}
