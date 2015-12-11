package com.bill.crawler.engine;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.LogUtil;

public class JsoupEngine {
	public static String DefaultHtml = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /> <title></title></head>"
			+ "<body></body></html>";

	public static Document createNewDoc() {
		Document doc = Jsoup.parse(DefaultHtml);

		return doc;
	}

	public static Document getDocFromFile(String path, String lang) {
		File input = new File(path);
		if (!input.exists()) {
			return null;
		}

		Document doc = null;
		try {
			doc = Jsoup.parse(input, lang);
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}

		return doc;
	}

	public static boolean modifyLinkSrcInFile(String path, String lang, String oldSrc, String newSrc) {
		boolean retVal = false;

		Document doc = getDocFromFile(path, lang);
		if (doc != null) {
			Elements images = doc.select("img[src$=" + oldSrc + "]");

			if (images != null && images.size() > 0) {
				images.attr("src", newSrc);
				retVal = true;
			}

			Elements aLink = doc.select("a[href$=" + oldSrc + "]");

			if (aLink != null && aLink.size() > 0) {
				aLink.attr("href", newSrc);
				retVal = true;
			}

			if (retVal) {
				// save back to file
				CommonUtil.save(doc.html().getBytes(), path);
			}
		}

		return retVal;
	}

	public static Document getDocByJsoup(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).data("jquery", "java").userAgent("Mozilla").cookie("auth", "token")
					.timeout(50000).get();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}

		return doc;
	}

	public static String getContentByJsoup(String url) {
		String content = "";
		try {
			System.out.println("time=====start");
			Date startdate = new Date();
			Document doc = Jsoup.connect(url).data("jquery", "java").userAgent("Mozilla")
					.cookie("auth", "token").timeout(50000).get();
			Date enddate = new Date();
			Long time = enddate.getTime() - startdate.getTime();
			System.out.println("使用Jsoup耗时==" + time);
			System.out.println("time=====end");
			content = doc.toString();// 获取网站的源码html内容
			System.out.println(doc.title());// 获取网站的标题
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
		// System.out.println(content);
		return content;
	}

	public static String getDivContentByJsoup(String content) {
		String divContent = "";
		Document doc = Jsoup.parse(content);
		Elements divs = doc.getElementsByClass("main_left");
		divContent = divs.toString();
		// System.out.println("div==="+divContent);
		return divContent;
	}

	/**
	 * 使用jsoup分析divContent 1.获取链接 2.获取url地址（绝对路径）
	 */
	public static void getLinksByJsoup(String divContent) {
		String abs = "http://www.iteye.com/";
		Document doc = Jsoup.parse(divContent, abs);
		Elements linkStrs = doc.getElementsByTag("li");
		System.out.println("链接===" + linkStrs.size());
		for (Element linkStr : linkStrs) {
			String url = linkStr.getElementsByTag("a").attr("abs:href");
			String title = linkStr.getElementsByTag("a").text();
			System.out.println("标题:" + title + " url:" + url);
		}
	}

	public static void main(String[] args) throws IOException {

		/**
		 * 执行分析程序
		 */
		String url = "http://www.iteye.com/";
		String HtmlContent = getContentByJsoup(url);
		String divContent = getDivContentByJsoup(HtmlContent);
		getLinksByJsoup(divContent);
	}
}
