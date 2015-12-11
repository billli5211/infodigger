package com.bill.crawler.site.fs;

import com.bill.crawler.config.BaseConfig;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;

public class FSConfig extends BaseConfig {
	public FSConfig(Author author) {
		super();

		
		if(author.getAccount() != null){
			this.siteName = SiteConst.SITE_FS;
		}else{
			this.siteName = SiteConst.SITE_FS + "_" + author.getAccount();
		}
		
		this.siteAddress = author.getDomain();

		this.template = "";

		initFolder();
		intBasicCSV();
	}
}
