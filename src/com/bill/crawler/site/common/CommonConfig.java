package com.bill.crawler.site.common;

import java.io.FileWriter;
import java.io.IOException;

import com.bill.crawler.config.BaseConfig;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

public class CommonConfig extends BaseConfig {
	public CommonConfig(Author author) {
		super();

		this.siteName = SiteConst.SITE_COMMON + "_" + author.getAccount();
		this.siteAddress = author.getDomain();
		this.template = "";

		initFolder();

		//
		intCSV();
		try {
			CsvWriter cw = new CsvWriter(new FileWriter(csv, true), ',');
			cw.write(this.siteName);
			cw.write(this.siteAddress);
			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
	}
}
