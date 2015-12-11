package com.bill.crawler.config;

import java.io.File;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

public interface IConfig {

	// site
	public String getSiteAddress();

	public String getSiteName();

	// current time
	public String getTime();

	// file/path
	public File getCsv();

	public File getStorageFolder();

	public File getImageFolder();

	public String getTemplate();

	// encoding
	public String getEncoding();

	// core config
	public CrawlConfig getConfig();

	//
	public void setPerformanceLevel(int level);
	public int getThreadNum();
}
