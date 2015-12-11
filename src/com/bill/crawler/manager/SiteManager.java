package com.bill.crawler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bill.crawler.config.IConfig;
import com.bill.crawler.config.SystemConfig;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;
import com.bill.crawler.seed.ISeedGenerator;
import com.bill.crawler.site.fs.*;
import com.bill.crawler.site.cto.*;
import com.bill.crawler.site.common.*;
import com.bill.crawler.site.csdn.*;
import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.LogUtil;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SiteManager {
	private static SiteManager instance = new SiteManager();

	private List<String> siteList;

	public static SiteManager getInstance() {
		return instance;
	}

	private SiteManager() {
		this.siteList = new ArrayList<String>();
		this.siteList.add(SiteConst.SITE_51CTO);
		this.siteList.add(SiteConst.SITE_CSDN);
		this.siteList.add(SiteConst.SITE_COMMON);
		this.siteList.add(SiteConst.SITE_FS);
	}

	public void launchCrawlers() {
		SiteConfReader.readSysConfig();
		if (SystemConfig.getBasePath() == null) {
			LogUtil.logger.error("system config has error!");
			return;
		}
		

		for (String site : siteList) {
			createCrawlers(SiteConst.CONF_PATH + site + FilterUtil.XML_SUFFIX, site);
		}
	}

	private void createCrawlers(String resource, String site) {
		List<Author> authorList = SiteConfReader.readSiteConfig(resource, site);
		if (authorList == null || authorList.size() == 0) {
			LogUtil.logger.info("no author in site" + site);
		} else {
			Author author = null;
			for (int i = 0; i < authorList.size(); i++) {
				author = authorList.get(i);
				boolean isValid = author.validate(site);

				if(isValid){
					createSingleCrawler(site, author);
				}else{
					LogUtil.logger.info("author with account " + author.getAccount() + " is not valid");
				}
				
			}
		}
	}
	
	private void createSingleCrawler(String site, Author author) {
		IConfig config = null;
		ISeedGenerator seedGenerator = null;
		Class crawlerClass = null;
		if (site.equals(SiteConst.SITE_51CTO)) {
			seedGenerator = new CtoSeedGenerator(author);
			config = new CtoConfig(author);
			crawlerClass = CtotPageHandler.class;
		} else if (site.equals(SiteConst.SITE_CSDN)) {
			seedGenerator = new CSDNSeedGenerator(author);
			config = new CSDNConfig(author);
			crawlerClass = CSDNPageHandler.class;
		} else if (site.equals(SiteConst.SITE_FS)) {
			seedGenerator = new FSSeedGenerator(author);
			config = new FSConfig(author);
			crawlerClass = FSPageHandler.class;
		} else if (site.equals(SiteConst.SITE_COMMON)) {
			seedGenerator = new CommonSeedGenerator(author);
			config = new CommonConfig(author);
			crawlerClass = CommonPageHandler.class;
		} else {
			return;
		}

		config.setPerformanceLevel(SystemConfig.getCrawlingPerformanceLevel());
		startCrawler(config, seedGenerator, author, crawlerClass);
	}
	
	private void startCrawler(IConfig siteConfig, ISeedGenerator seedGenerator, Author author, Class crawlerClass) {
		CrawlConfig coreConfig = siteConfig.getConfig();

		PageFetcher pageFetcher = new PageFetcher(coreConfig);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

		CrawlController controller = null;
		try {
			controller = new CrawlController(coreConfig, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			LogUtil.logger.error(e.toString());
		}

		for (String item : seedGenerator.getSeeds()) {
			controller.addSeed(item);
		}
		
		Map<String, Object> customMap = new HashMap<String, Object>(); 
		customMap.put("author", author);
		customMap.put("config", siteConfig);
		
		controller.setCustomData(customMap);

		//controller.startNonBlocking(crawler.getClass(), siteConfig.getThreadNum());
		controller.startNonBlocking(crawlerClass, 1);
		
		if(SystemConfig.isEnableConcurrentCrawling() == false){
			controller.waitUntilFinish();
		}
	}

}
