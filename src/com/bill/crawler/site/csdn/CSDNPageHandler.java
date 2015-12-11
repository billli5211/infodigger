package com.bill.crawler.site.csdn;

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
import com.bill.crawler.global.WhiteBlackNameList;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

public class CSDNPageHandler extends BasePageHandler {
	private boolean EnableDebug = true;
	private String DebugPageUrl = "http://img.blog.csdn.net/20150503151532900";

	public CSDNPageHandler() {
		super();
	}

	@Override
	public boolean allowVisitor(String url) {
		if (getAuthor().isFetchBlog() == false) {
			return super.allowVisitor(url);
		}

		boolean retVal = false;

		if (url.startsWith(WhiteBlackNameList.SiteGitHub)) {
			return retVal;// to improve performance.
		}

		if (url.startsWith(WhiteBlackNameList.SiteGoogle)) {
			return retVal;
		}

		// let breakpoint work
		if (EnableDebug) {
			if (url.contains(DebugPageUrl)) {
				logger.info("url:" + url);
			}
		}

		if (FilterUtil.isImageLink(url) || FilterUtil.isCsdnImage(url)) {
			retVal = true;
		}

		if (!retVal && url.startsWith(getAuthor().getDomain())) {
			retVal = true;
		}

		// allow seed of course
		String seedUrl = getAuthor().getExtra().get("CSDNBaseDomain") + getAuthor().getId()
				+ "/article/list/";
		if (!retVal && url.startsWith(seedUrl)) {
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
		if (getAuthor().isFetchBlog() == false) {
			return super.allowHandler();
		}

		String parentUrl = stat.getParentUrl();
		String url = stat.getUrl();

		// let breakpoint work
		if (EnableDebug) {
			if (url.contains(DebugPageUrl)) {
				logger.info("url:" + url);
			}
		}

		if (url.startsWith(WhiteBlackNameList.SiteGitHub)) {
			return false;// to improve performance.
		}

		if (url.startsWith(WhiteBlackNameList.SiteGoogle)) {
			return false;
		}

		// parent may be null
		if (CommonUtil.isEmptyString(parentUrl)) {
			logger.info("not allowed to handle since parant is null:" + url);
			return false;
		}

		if (stat.isPackageLink()) {
			logger.info("allowed to handle package file:" + url);
			return false;
		}

		if (stat.isImageLink() || FilterUtil.isCsdnImage(url)) {
			return true;
		}

		if (url.startsWith(getAuthor().getDomain())) {
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

		Elements elements = doc.select("div.article_content");
		if (elements == null) {
			return true;
		}
		Element showContentElement = elements.first();
		if (showContentElement == null) {
			return true;
		}

		elements = doc.select("div.article_title");
		Element titleElement = null;
		if (elements != null && elements.size() > 0) {
			titleElement = elements.first();
		}

		Document newDoc = null;
		if (SystemConfig.isSavePureContent()) {
			newDoc = JsoupEngine.createNewDoc();
			newDoc.body().append(titleElement.html());
			newDoc.body().append(showContentElement.html());
		} else {
			newDoc = JsoupEngine.getDocFromFile(getConfig().getTemplate(), getConfig().getEncoding());
			Element newShowContentElement = newDoc.select("div.details").first();
			if (titleElement != null) {
				newShowContentElement.appendChild(titleElement);
			}

			newShowContentElement.appendChild(showContentElement);
		}

		pageContent = newDoc.html().getBytes();

		return true;
	}

	private boolean extractorForProfile(String url, String html) {
		String str = null;

		Document doc = Jsoup.parse(html);

		// name
		Element element = doc.select("dt.person-nick-name").first();

		if (element == null) {
			return false;
		}

		str = element.text();
		map.put(PageTagConst.nameTag, str);
		this.title = str;

		element = doc.select("dd.person-detail").first();
		if (element == null) {
			return false;
		}
		str = element.text();
		map.put(CSDNConfig.personDetailTag, str);

		// switch engine to fetch data again, since cannot get part of info.
		doc = JsoupEngine.getDocByJsoup(url);
		element = doc.select("dd.focus_num").first();
		if (element == null) {
			return false;
		}
		str = element.text();
		map.put(PageTagConst.interestNumTag, str);

		element = doc.select("dd.fans_num").first();
		if (element == null) {
			return false;
		}
		str = element.text();
		map.put(PageTagConst.fanNumTag, str);

		//

		str = doc.select("div.mod_contact").select("span.email").text();
		map.put(PageTagConst.emailTag, str);

		str = doc.select("div.mod_contact").select("span.qq").text();
		map.put(PageTagConst.qqTag, str);

		str = doc.select("div.mod_contact").select("span.weixin").text();
		map.put(PageTagConst.weichatTag, str);

		str = doc.select("div.mod_contact").select("span.modile").text();
		map.put(PageTagConst.telTag, str);

		Elements list = doc.select("div.list-blog.list.activeContent");
		int size = list.size();
		Element item = null;
		String linkHref = null;
		String linkText = null;
		int maxNum = 0;
		for (int i = 0; i < size; i++) {
			item = list.get(i).select("span.dTime").first();
			if (item == null) {
				return false;
			}

			int num = CommonUtil.getNumberFromString(item.text());
			if (num > maxNum) {
				maxNum = num;

				// get <a>
				item = list.get(i).select("a.tit").first();
				linkHref = item.attr("href");
				linkText = item.text();
			}
		}

		if (maxNum > 1000) {
			map.put(CSDNConfig.blogNameTag, linkText);
			map.put(CSDNConfig.blogVisitNumTag, maxNum + "");
			map.put(CSDNConfig.blogSiteTag, linkHref);
		}

		//
		list = doc.select("div.list_resource");
		size = list.size();
		for (int i = 0; i < size; i++) {
			item = list.get(i).select("span.dTime").first();
			int num = CommonUtil.getNumberFromString(item.text());
			if (num > maxNum) {
				maxNum = num;

				// get <a>
				item = list.get(i).select("a.tit").first();
				linkHref = item.attr("href");
				linkText = item.text();
			}
		}

		if (maxNum > 100) {
			map.put(CSDNConfig.resourceNameTag, linkText);
			map.put(CSDNConfig.resourceVisitNumTag, maxNum + "");
			map.put(CSDNConfig.resourceSiteTag, linkHref);
		}

		return true;
	}

	@Override
	protected void saveCSV() {
		try {
			File csv = getConfig().getCsv();
			CsvWriter cw = new CsvWriter(new FileWriter(csv, true), ',');

			saveCommonCSV(cw);

			if (getAuthor().isFetchBlog() == false) {
				cw.write((String) map.get(PageTagConst.nameTag));
				cw.write((String) map.get(CSDNConfig.personDetailTag));
				cw.write((String) map.get(PageTagConst.qqTag));
				cw.write((String) map.get(PageTagConst.emailTag));
				cw.write((String) map.get(PageTagConst.telTag));

				cw.write((String) map.get(PageTagConst.weichatTag));
				cw.write((String) map.get(PageTagConst.interestNumTag));
				cw.write((String) map.get(PageTagConst.fanNumTag));

				cw.write((String) map.get(CSDNConfig.blogNameTag));
				cw.write((String) map.get(CSDNConfig.blogVisitNumTag));
				cw.write((String) map.get(CSDNConfig.blogSiteTag));

				cw.write((String) map.get(CSDNConfig.resourceNameTag));
				cw.write((String) map.get(CSDNConfig.resourceVisitNumTag));
				cw.write((String) map.get(CSDNConfig.resourceSiteTag));
			}

			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
	}
}