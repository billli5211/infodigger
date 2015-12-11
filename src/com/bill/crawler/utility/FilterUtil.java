package com.bill.crawler.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;

public class FilterUtil {

	public static String HTTP_PROTOCOL = "http://";
	public static String HTML_SUFFIX = ".html";
	public static String XML_SUFFIX = ".xml";
	public static String CN_SUFFIX = ".cn";
	public static String COM_SUFFIX = ".com";

	/*
	 * csdn image path:
	 */
	// http://img.blog.csdn.net/20140326132326828
	// http://img.blog.csdn.net/20140326132326828?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGxnZW4xNTczODc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast
	public static String CsdnImagePath = "http://img.blog.csdn.net/";

	public static boolean isCsdnImage(String url) {
		return url.startsWith(CsdnImagePath);
	}

	public static boolean getCsdnImage(String url) {
		return url.startsWith(CsdnImagePath);
	}

	// url path filter
	public static final Pattern BIN_EXTENSIONS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	public static final Pattern NON_IMAGE_BIN_EXTENSIONS = Pattern
			.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern WEB_EXTENSIONS = Pattern.compile(".*(\\.(css|js|htm?l))$");

	private static final Pattern PACKAGE_EXTENSIONS = Pattern.compile(".*(\\.(zip|rar|gz))$");

	private static final Pattern MULTI_MEDIA_EXTENSIONS = Pattern
			.compile(".*(\\.(mp3|mp4|wav|avi|mov|mpeg|ram|m4v|rm|wmv|swf|wma))$");

	public static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

	private static final Pattern SPECIAL_PAGE_EXTENSIONS = Pattern
			.compile("([1-9]{1,3})|([a-z]{1})|(d-[1-9]{1,3})|(p-[1-9]{1,3})");

	// 编译email正则表达式
	private static final Pattern EMALE_PATTERN = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");

	// ;

	// content filter
	public static List<String> getEmails(String str) {
		Matcher matcher = EMALE_PATTERN.matcher(str);

		List<String> list = null;
		String email = null;

		while (matcher.find()) {
			if (list == null) {
				list = new ArrayList<String>();
			}

			email = matcher.group();

			// 1269823266@qq.com<img class
			// 1721913835@qq.com<br
			// 742363596@qq.co
			// tangxj@tiebahl
			if (email.endsWith(CN_SUFFIX) || email.endsWith(COM_SUFFIX)) {
				// right format
			} else {
				int pos = email.lastIndexOf(COM_SUFFIX);
				if (pos > 0) {
					email = email.substring(0, pos + COM_SUFFIX.length());
				} else {
					pos = email.lastIndexOf(CN_SUFFIX);
					if (pos > 0) {
						email = email.substring(0, pos + CN_SUFFIX.length());
					} else {
						continue;
					}
				}
			}

			list.add(email);

		}

		return list;
	}

	// if some page as c, d-12, p-12, should not save--- is special page
	public static boolean isSepcailPage(String url) {
		String str = FileUtil.getLastStringFromUrl(url);
		return SPECIAL_PAGE_EXTENSIONS.matcher(str).matches();
	}

	public static boolean isBinLink(String url) {
		return BIN_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isNonImageBinLink(String url) {
		return NON_IMAGE_BIN_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isMutliMediaLink(String url) {
		return MULTI_MEDIA_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isPackageLink(String url) {
		return PACKAGE_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isWebLink(String url) {
		return WEB_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isImageLink(String url) {
		return IMAGE_EXTENSIONS.matcher(url).matches();
	}

	public static boolean isValidImageData(Page page) {
		// We are only interested in processing images which are bigger than 10k
		boolean isRightSize = page.getContentData().length > (10 * 1024);

		if (isRightSize) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		String str = "http://liuyj.blog.51cto.com/2340749/s1";

		LogUtil.logger.info("url:" + str + " is special:" + isSepcailPage(str));

		//
		str = "http://tieba.baidu.com/p/3507283010?pn=30&qq-pf-to=pcqq.c2c";
		// str = JsoupEngine.getContentByJsoup(str);
		str = "1269823266@qq.com<ss";
		List list = getEmails(str);
		if (list == null || list.size() == 0) {
			LogUtil.logger.info("no email find");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			LogUtil.logger.info(list.get(i).toString());
		}
	}
}
