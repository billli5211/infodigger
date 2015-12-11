package com.bill.crawler.site.cto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bill.crawler.config.SystemConfig;
import com.bill.crawler.core.BasePageHandler;
import com.bill.crawler.engine.JsoupEngine;
import com.bill.crawler.global.PageTagConst;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

public class CtotPageHandler extends BasePageHandler {

	private static String NAME = "姓名";
	private static String SEX = "性别";
	private static String LOCATION = "现居住地";
	private static String QQ = "QQ";
	private static String DATE = "注册时间";

	private boolean EnableDebug = true;
	private String DebugPageUrl = "wKiom1Y8laHB5C7eAAEjK3Ml8GU546.jpg";

	public CtotPageHandler() {
		super();
	}

	@Override
	public boolean allowVisitor(String url) {
		boolean retVal = false;

		// for debug only
		/*
		 * if(url.contains("1335075")){ logger.info("url:" + url); }
		 */

		if (FilterUtil.isImageLink(url)) {
			retVal = true;
		}

		if (!retVal && url.startsWith(getAuthor().getDomain())) {
			retVal = true;
		}

		if (retVal) {
			logger.info("allowed to visit:" + url);
		} else {
			logger.info("not allowed to visit:" + url);
		}

		return retVal;
	}

	@Override
	protected boolean allowHandler() {
		String parentUrl = stat.getParentUrl();
		String url = stat.getUrl();

		// let breakpoint work
		if (EnableDebug) {
			if (url.contains(DebugPageUrl)) {
				logger.info("url:" + url);
			}
		}

		// parent may be null
		if (CommonUtil.isEmptyString(parentUrl)) {
			logger.info("not allowed to handle since parant is null:" + url);
			return false;
		}

		if (stat.isValidImageLink()) {
			logger.info("allowed to handle image:" + url);
			return true;
		} else if (stat.isImageLink()) {
			// means small image
			return false;
		}

		// the page with the title named "51CTO技术家园" is for login, just ignore.
		if (stat.getTitle() != null && stat.getTitle().equals(CtoConfig.SiteExtra_51cto_Title)) {
			logger.info("not allowed to handle login page:" + url);
			return false;
		}

		if (url.startsWith(getAuthor().getDomain())) {// formerly parentUrl
														// instead of url,
														// should be wrong?
			if (FilterUtil.isSepcailPage(url)) {
				return false;
			}

			logger.info("allowed to handle since path is correct:" + url);
			return true;
		} else {
			logger.info("not allowed to handle since path is not correct:" + url);
			return false;
		}
	}

	@Override
	protected boolean extractor(String url, String html) {
		if (getAuthor().isFetchBlog()) {
			return extractorForBlog(url, html);
		} else {
			return extractorForProfile(url, html);
		}
	}

	private boolean extractorForBlog(String url, String html) {

		Document doc = Jsoup.parse(html);

		Elements elements = doc.select("div.showContent");
		if (elements == null) {
			return true;
		}
		Element showContentElement = elements.first();
		if (showContentElement == null) {
			return true;
		}

		elements = doc.select("div.showTitle");
		Element titleElement = null;
		if (elements != null && elements.size() > 0) {
			titleElement = elements.first();
		}

		Document newDoc = null;
		if (SystemConfig.isSavePureContent()) {
			newDoc = JsoupEngine.createNewDoc();
			Elements headTitleElements = newDoc.getElementsByTag("title");
			if (headTitleElements != null && headTitleElements.size() > 0) {
				Element headTitleElement = headTitleElements.first();
				headTitleElement.text(titleElement.text());
			}
			newDoc.body().append(titleElement.html());
			newDoc.body().append(showContentElement.html());
		} else {
			newDoc = JsoupEngine.getDocFromFile(getConfig().getTemplate(), getConfig().getEncoding());

			Element newShowContentElement = newDoc.select("div.showBox").first();

			// change title
			if (titleElement != null) {

				// change title in head part
				Elements headTitleElements = newDoc.getElementsByTag("title");
				if (headTitleElements != null && headTitleElements.size() > 0) {
					Element headTitleElement = headTitleElements.first();
					headTitleElement.text(titleElement.text());
				}

				// change title in content part
				newShowContentElement.appendChild(titleElement);
			}

			// change content
			newShowContentElement.appendChild(showContentElement);
		}

		pageContent = newDoc.html().getBytes();

		return true;
	}

	private boolean extractorForProfile(String url, String html) {
		boolean retVal = false;
		String str = null;

		Document doc = Jsoup.parse(html);

		// name and visit amount
		Elements contents = doc.select("#host_name");
		String name = null;
		int visitAmount = 0;
		if (contents != null && contents.first() != null) {
			str = contents.first().text();

			if (str != null && str.isEmpty() == false) {

				String[] tempStrArray = str.split(" ");

				String visitNum = null;
				if (tempStrArray.length > 3) {
					name = tempStrArray[0];
					visitNum = tempStrArray[3];
				}

				if (visitNum == null || visitNum.isEmpty()) {
					return retVal;
				} else {
					visitAmount = Integer.parseInt(visitNum);
					map.put(PageTagConst.nameTag, name);
					map.put(PageTagConst.visitNumTag, visitNum);
				}
			}
		}

		// check whether continue
		if (visitAmount < 100) {
			return retVal;
		} else {
			logger.info(name + ": " + visitAmount);
		}

		// to parse other info

		Elements info = doc.select("div.info");
		Elements list = info.select("li");

		int size = list.size();

		// one by one
		/*
		 * Element sexE = list.get(0); sex = sexE.children().get(1).text();
		 * 
		 * Element locationE = list.get(1); location =
		 * locationE.children().get(1).text(); Element qqE = list.get(2); qq =
		 * qqE.children().get(1).text(); Element dateE = list.get(3); date =
		 * dateE.children().get(1).text();
		 */

		for (int i = 0; i < size; i++) {
			Element item = list.get(i);
			String key = item.children().get(0).text();
			str = item.children().get(1).text();

			if (key.contains(NAME)) {
				if (!str.isEmpty()) {
					map.put(PageTagConst.nameTag, str);// override the name
				}

			} else if (key.contains(SEX)) {
				map.put(PageTagConst.sexTag, str);
			} else if (key.contains(LOCATION)) {
				map.put(PageTagConst.locationTag, str);
			} else if (key.contains(QQ)) {
				map.put(PageTagConst.qqTag, str);
			} else if (key.contains(DATE)) {
				map.put(PageTagConst.dateTag, str);
			}
		}

		return true;
	}

	@Override
	protected void saveCSV() {
		if (getAuthor().isFetchBlog()) {
			super.saveCSV();
		} else {
			try {
				File csv = getConfig().getCsv();
				CsvWriter cw = new CsvWriter(new FileWriter(csv, true), ',');

				saveCommonCSV(cw);

				cw.write((String) map.get(PageTagConst.nameTag));
				cw.write((String) map.get(PageTagConst.visitNumTag));
				cw.write((String) map.get(PageTagConst.locationTag));
				cw.write((String) map.get(PageTagConst.qqTag));
				cw.write((String) map.get(PageTagConst.sexTag));
				cw.write((String) map.get(PageTagConst.dateTag));

				cw.endRecord();
				cw.close();
			} catch (IOException e) {
				LogUtil.logger.error(e.toString());
			}
		}

	}

}