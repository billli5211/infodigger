package com.bill.crawler.stat;

import java.util.Set;

import org.apache.http.Header;

import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.LogUtil;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class PageStat {

	private Page page;

	private String url;
	private String domain;
	private String parentUrl;
	private String anchor;
	private String title;

	private int length;// html length
	private int linkNum;// Number of outgoing links

	private boolean isImageLink;
	private boolean isValidImageLink;
	private boolean isNonImageBinLink;
	private boolean isPackageLink;

	public PageStat(Page page) {
		this.page = page;

		url = page.getWebURL().getURL();
		domain = page.getWebURL().getDomain();
		parentUrl = page.getWebURL().getParentUrl();
		anchor = page.getWebURL().getAnchor();

		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		title = htmlParseData.getTitle();

		Set<WebURL> links = htmlParseData.getOutgoingUrls();
		linkNum = links.size();

		String html = htmlParseData.getHtml();
		length = html.length();

		isImageLink = FilterUtil.isImageLink(url);
		if (isImageLink) {
			isValidImageLink = FilterUtil.isValidImageData(page);
		} else {
			isValidImageLink = false;
		}

		isNonImageBinLink = FilterUtil.isNonImageBinLink(url);
		isPackageLink = FilterUtil.isPackageLink(url);

	}

	public Page getPage() {
		return page;
	}

	public String getUrl() {
		return url;
	}

	public String getDomain() {
		return domain;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public String getAnchor() {
		return anchor;
	}

	public String getTitle() {
		return title;
	}

	public int getLength() {
		return length;
	}

	public int getLinkNum() {
		return linkNum;
	}

	public boolean isImageLink() {
		return isImageLink;
	}

	public boolean isValidImageLink() {
		return isValidImageLink;
	}

	private static void printContentInfo(Page page) {
		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		String text = htmlParseData.getText();
		String html = htmlParseData.getHtml();
		Set<WebURL> links = htmlParseData.getOutgoingUrls();

		LogUtil.logger.debug("Text length: {}", text.length());
		LogUtil.logger.debug("Html length: {}", html.length());
		LogUtil.logger.debug("Number of outgoing links: {}", links.size());

		LogUtil.logger.debug("---------\n{}\n------------\n", html);
	}

	private static void printSummaryInfo(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		LogUtil.logger.debug("Docid: {}", docid);
		LogUtil.logger.info("URL: {}", url);
		LogUtil.logger.debug("Domain: '{}'", domain);
		LogUtil.logger.debug("Sub-domain: '{}'", subDomain);
		LogUtil.logger.debug("Path: '{}'", path);
		LogUtil.logger.debug("Parent page: {}", parentUrl);
		LogUtil.logger.debug("Anchor text: {}", anchor);

		//
		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			LogUtil.logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				LogUtil.logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}
	}

	public boolean isNonImageBinLink() {
		return isNonImageBinLink;
	}

	public boolean isPackageLink() {
		return isPackageLink;
	}

}
