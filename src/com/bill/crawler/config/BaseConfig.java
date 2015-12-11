package com.bill.crawler.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.bill.crawler.global.CrawlingConst;
import com.bill.crawler.global.CrawlingConst.CrawlPerformanceLevel;
import com.bill.crawler.global.Encoding;
import com.bill.crawler.global.PageTagConst;
import com.bill.crawler.global.PathConst;
import com.bill.crawler.utility.DateUtil;
import com.bill.crawler.utility.FileUtil;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

/*
 * Implements common IConfig interfaces(most important is getDefaultConfig, and getLowestDepthConfig), 
 * and also define some protected class members and methods for the purpose of customizing suitable configuration items.
 */
public abstract class BaseConfig implements IConfig {

	protected String template;
	protected String encoding;

	protected String siteName = null;
	protected String siteAddress = null;
	
	// core config
	private CrawlPerformanceLevel performanceLevel;
	private int threadNum = 0;
	private int digDepth = -1;// unlimited
	private boolean allowBinarySearch = false;

	//
	protected File csv;
	protected File storageFolder;
	protected File imageFolder;

	//
	private String storagePath;
	private String csvPath;
	private String imagePath;
	private String crawleTime = null;

	public BaseConfig() {
		initCoreConfig();
		
		this.template = "";
		this.encoding = Encoding.Encode_gb2312;
	}

	// protected: can be accessed by sub-classes
	protected void initFolder() {
		storagePath = SystemConfig.getPagePath();
		csvPath = SystemConfig.getCsvPath();
		imagePath = SystemConfig.getImagePath();

		/*
		 * create second level folder: date folder
		 */
		crawleTime = DateUtil.getCurrentDate();
		storagePath = SystemConfig.getPagePath() + crawleTime;
		csvPath = csvPath + crawleTime;
		imagePath = imagePath + crawleTime;

		FileUtil.createFolder(storagePath, false, false);
		FileUtil.createFolder(csvPath, false, false);
		imageFolder = FileUtil.createFolder(imagePath, false, false);

		/*
		 * create third level folder: currently just for site storage, if
		 * existing back up first
		 */
		storagePath = storagePath + "/" + siteName;
		storageFolder = FileUtil.createFolder(storagePath, true, false);

		/*
		 * create 4th level folder: just for bin folder under specific page.
		 */
		if (SystemConfig.isEnableBinHandling()) {
			FileUtil.createFolder(storagePath + "/" + PathConst.BinFolderName, false, false);
		}
	}

	protected void intBasicCSV() {
		intCSV();

		try {
			CsvWriter cw = writeBasicTags();
			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}

	}

	protected CsvWriter writeBasicTags() {
		CsvWriter cw = null;
		try {
			cw = new CsvWriter(new FileWriter(csv, true), ',');
			cw.write(PageTagConst.titleTag);
			cw.write(PageTagConst.urlTag);
			cw.write(PageTagConst.anchorTag);
			cw.write(PageTagConst.domainTag);
			cw.write(PageTagConst.lengthTag);
			cw.write(PageTagConst.linkNumTag);
			cw.write(PageTagConst.parentUrlTag);
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
			cw = null;
		}

		return cw;
	}

	// protected: can be override by sub-classes
	protected void intCSV() {
		csv = FileUtil.createFile(csvPath + "/" + siteName + ".csv", true, false);
	}

	/***********************************************************************/
	/* implements interfaces */

	@Override
	public String getSiteAddress() {
		return siteAddress;
	}

	@Override
	public String getSiteName() {
		return siteName;
	}

	@Override
	public String getTime() {
		return crawleTime;
	}

	@Override
	public File getCsv() {
		return csv;
	}

	@Override
	public File getStorageFolder() {
		return storageFolder;
	}

	@Override
	public File getImageFolder() {
		return imageFolder;
	}

	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}
	
	/***********************************************************************/
	// core config related public methods
	
	/*
	 * reason to use 2 levels: 
	 * 		first level is from list page to specified page; 
	 * 		second level is from the specified page to image page inside.
	 */
	@Override
	public CrawlConfig getConfig() {
		this.setDigDepth(2);
		this.setAllowBinarySearch(true);
		
		return createConfig();
	}
	
	@Override
	public int getThreadNum() {
		return threadNum;
	}

	@Override
	public void setPerformanceLevel(int level){
		if(level == 1){
			performanceLevel = CrawlingConst.CrawlPerformanceLevel.LEVEL1;
		}else if(level == 3){
			performanceLevel = CrawlingConst.CrawlPerformanceLevel.LEVEL3;
		}else{
			performanceLevel = CrawlingConst.CrawlPerformanceLevel.LEVEL2;
		}
	}
	

	/***********************************************************************/
	//protected methods
	
	protected int getDigDepth() {
		return digDepth;
	}

	protected void setDigDepth(int digDepth) {
		this.digDepth = digDepth;
	}

	protected boolean isAllowBinarySearch() {
		return allowBinarySearch;
	}

	protected void setAllowBinarySearch(boolean allowBinarySearch) {
		this.allowBinarySearch = allowBinarySearch;
	}

	/***********************************************************************/
	// private methods
	private CrawlConfig createConfig() {
		CrawlConfig config = new CrawlConfig();

		// storage
		config.setCrawlStorageFolder(SystemConfig.getRootPath() + siteName);

		// speed/performance
		setPerformanceParam(config, this.performanceLevel);

		// default value is -1:unlimited depth
		config.setMaxDepthOfCrawling(this.digDepth);

		// default value is -1 for unlimited number of pages
		// config.setMaxPagesToFetch(1000);

		// binary data ? example:contents of pdf, or the metadata of images etc
		config.setIncludeBinaryContentInCrawling(this.isAllowBinarySearch());

		/*
		 * Do you need to set a proxy? If so, you can use:
		 * config.setProxyHost("proxyserver.example.com");
		 * config.setProxyPort(8080);
		 * 
		 * If your proxy also needs authentication:
		 * config.setProxyUsername(username); config.getProxyPassword(password);
		 */

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		return config;
	}

	private void setPerformanceParam(CrawlConfig config, CrawlingConst.CrawlPerformanceLevel level) {
		if (level == CrawlingConst.CrawlPerformanceLevel.LEVEL1) {
			threadNum = CrawlingConst.maxThreadNum;
			config.setPolitenessDelay(CrawlingConst.minRequestDelay);
		} else if (level == CrawlingConst.CrawlPerformanceLevel.LEVEL3) {
			threadNum = CrawlingConst.minThreadNum;
			config.setPolitenessDelay(CrawlingConst.maxRequestDelay);
		} else {
			threadNum = CrawlingConst.moderateThreadNum;
			config.setPolitenessDelay(CrawlingConst.moderateRequestDelay);
		}
	}
	
	private void initCoreConfig(){
		performanceLevel = CrawlPerformanceLevel.LEVEL2;
		threadNum = 0;
		digDepth = -1;// unlimited
		allowBinarySearch = false;
	}


}
