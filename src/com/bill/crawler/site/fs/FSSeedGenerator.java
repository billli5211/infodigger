package com.bill.crawler.site.fs;

import com.bill.crawler.model.Author;
import com.bill.crawler.seed.BaseSeedGenerator;

public class FSSeedGenerator extends BaseSeedGenerator {
	public FSSeedGenerator(Author author) {
		String domain = author.getDomain();
		seedList.add(domain);
		seedList.add(domain + "-2");
		seedList.add(domain + "-3");
	}
}
