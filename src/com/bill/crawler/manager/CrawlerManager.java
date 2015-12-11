package com.bill.crawler.manager;

import com.bill.crawler.utility.LogUtil;

/*
 * The entrance of the application.
 * Able to startup different crawlers with many threads at same time.
 */
public class CrawlerManager {

	public static void main(String[] args) throws Exception {
		SiteManager siteManager = SiteManager.getInstance();

		siteManager.launchCrawlers();

		LogUtil.logger.info("Crawling task is on-going...");
	}
}