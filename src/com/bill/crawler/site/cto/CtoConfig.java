package com.bill.crawler.site.cto;

import java.io.IOException;

import com.bill.crawler.config.BaseConfig;
import com.bill.crawler.global.Encoding;
import com.bill.crawler.global.PageTagConst;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;
import com.bill.crawler.utility.LogUtil;
import com.bill.crawler.utility.PathUtil;
import com.csvreader.CsvWriter;

public class CtoConfig extends BaseConfig {
	public static String SiteExtra_51cto_Title = "51CTO技术家园";
	public static String SITE_Template = "51cto_blog.html";

	public CtoConfig(Author author) {
		super();

		this.siteName = SiteConst.SITE_51CTO + "_" + author.getAccount();
		this.siteAddress = author.getDomain();

		initFolder();

		if (author.isFetchBlog()) {
			this.template = PathUtil.getTemplatePath(SITE_Template);
			this.encoding = Encoding.Encode_utf8;

			intBasicCSV();
		} else {
			super.intCSV();

			try {
				CsvWriter cw = writeBasicTags();
				cw.write(PageTagConst.nameTag);
				cw.write(PageTagConst.visitNumTag);
				cw.write(PageTagConst.locationTag);
				cw.write(PageTagConst.qqTag);
				cw.write(PageTagConst.sexTag);
				cw.write(PageTagConst.dateTag);
				cw.endRecord();
				cw.close();
			} catch (IOException e) {
				LogUtil.logger.error(e.toString());
			}
		}

	}
}
