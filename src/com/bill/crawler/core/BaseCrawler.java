package com.bill.crawler.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;

import com.bill.crawler.config.IConfig;
import com.bill.crawler.model.Author;
import com.bill.crawler.stat.CrawlStat;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public abstract class BaseCrawler extends WebCrawler implements IPageHandler {
	private CrawlStat myCrawlStat;

	protected Map<String, Object> customMap;
	private IConfig config;
	private Author author;

	/***********************************************************************/
	/*
	 * use config(IConfig): get CrawlConfig from IConfig, and get csv/template/encoding.
	 * 
	 * use seedGenerator: just to get seeds and set then into CrawlController
	 * 
	 * use author to get domain, get extra data by calling getExtra(), getId(), call isFetchBlog()
	 */
	
	public IConfig getConfig() {
		return config;
	}

	public Author getAuthor() {
		return this.author;
	}

	/***********************************************************************/

	@Override
	public void onStart() {
		myCrawlStat = new CrawlStat();
		customMap = (Map<String, Object>) this.getMyController().getCustomData();
		this.config = (IConfig) customMap.get("config");
		this.author = (Author) customMap.get("author");
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return allowVisitor(href);

	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		myCrawlStat.incProcessedPages();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			myCrawlStat.incTotalLinks(links.size());
			try {
				myCrawlStat.incTotalTextSize(htmlParseData.getText().getBytes("UTF-8").length);
			} catch (UnsupportedEncodingException ignored) {
				logger.error("encoding exception");
			}

			handleData(url, page, html);

			// We dump this crawler statistics after processing every 500 pages
			if ((myCrawlStat.getTotalProcessedPages() % 500) == 0) {
				dumpMyData();
			}
		}
	}

	/**
	 * This function is called by controller to get the local data of this
	 * crawler when job is finished
	 */
	@Override
	public Object getMyLocalData() {
		return myCrawlStat;
	}

	/**
	 * This function is called by controller before finishing the job. You can
	 * put whatever stuff you need here.
	 */
	@Override
	public void onBeforeExit() {
		// dumpMyData();
	}

	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

		if (statusCode != HttpStatus.SC_OK) {

			if (statusCode == HttpStatus.SC_NOT_FOUND) {
				logger.warn("Broken link: {}, this link was found in page: {}", webUrl.getURL(),
						webUrl.getParentUrl());
			} else {
				logger.warn("Non success status for link: {} status code: {}, description: ",
						webUrl.getURL(), statusCode, statusDescription);
			}
		}
	}

	private void dumpMyData() {
		int id = getMyId();
		// You can configure the log to output to file
		logger.info("Crawler {} > Processed Pages: {}", id, myCrawlStat.getTotalProcessedPages());
		logger.info("Crawler {} > Total Links Found: {}", id, myCrawlStat.getTotalLinks());
		logger.info("Crawler {} > Total Text Size: {}", id, myCrawlStat.getTotalTextSize());
	}

	/***********************************************************************/
	/* implement IPageHandler methods */

	@Override
	public abstract boolean allowVisitor(String url);

	@Override
	public abstract boolean handleData(String url, Page page, String html);

}
