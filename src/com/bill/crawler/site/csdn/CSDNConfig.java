package com.bill.crawler.site.csdn;

import java.io.IOException;

import com.bill.crawler.config.BaseConfig;
import com.bill.crawler.global.CrawlingConst.CrawlPerformanceLevel;
import com.bill.crawler.global.Encoding;
import com.bill.crawler.global.PageTagConst;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;
import com.bill.crawler.utility.LogUtil;
import com.bill.crawler.utility.PathUtil;
import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

public class CSDNConfig extends BaseConfig {
	public static String SITE_Template = "csdn_blog.html";

	// used for profile info digging
	public static String personDetailTag = "detail";

	public static String blogNameTag = "blogName";
	public static String blogVisitNumTag = "blogVisitNum";
	public static String blogSiteTag = "blogSite";
	public static String resourceNameTag = "resourceName";
	public static String resourceVisitNumTag = "resourceVisitNum";
	public static String resourceSiteTag = "resourceSite";

	private boolean isFetchBlog;

	public CSDNConfig(Author author) {
		super();

		if (author.isFetchBlog()) {
			isFetchBlog = true;
		} else {
			isFetchBlog = false;
		}

		this.siteName = SiteConst.SITE_CSDN + "_" + author.getAccount();
		this.siteAddress = author.getDomain();

		this.template = PathUtil.getTemplatePath(SITE_Template);
		this.encoding = Encoding.Encode_utf8;
		
		// if set to 3: allow to download bin files
		this.setDigDepth(2);
		this.setAllowBinarySearch(true);

		initFolder();
		intCSV();
	}

	@Override
	protected void intCSV() {
		super.intCSV();
		try {
			CsvWriter cw = writeBasicTags();

			if (isFetchBlog) {
				cw.write(PageTagConst.nameTag);
				cw.write(personDetailTag);
				cw.write(PageTagConst.qqTag);
				cw.write(PageTagConst.emailTag);
				cw.write(PageTagConst.telTag);
				cw.write(PageTagConst.weichatTag);
				cw.write(PageTagConst.interestNumTag);
				cw.write(PageTagConst.fanNumTag);

				cw.write(blogNameTag);
				cw.write(blogVisitNumTag);
				cw.write(blogSiteTag);
				cw.write(resourceNameTag);
				cw.write(resourceVisitNumTag);
				cw.write(resourceSiteTag);
			}

			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}

	}
}
