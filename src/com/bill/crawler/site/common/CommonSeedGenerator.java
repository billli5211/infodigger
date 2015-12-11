package com.bill.crawler.site.common;

import com.bill.crawler.model.Author;
import com.bill.crawler.seed.BaseSeedGenerator;

/*
 * http://tieba.baidu.com/p/3507283010?pn=1&qq-pf-to=pcqq.c2c  需要登陆才能访问所有页面 账号： xiaosuobo@163.com    1990wangjian 能全自动提取里面的邮箱就可以了
 */
public class CommonSeedGenerator extends BaseSeedGenerator {
	public CommonSeedGenerator(Author author) {
		// http://tieba.baidu.com/p/3507283010?pn=30&qq-pf-to=pcqq.c2c
		/*
		 * String lastPart = "&qq-pf-to=pcqq.c2c"; for (int i = 1; i <=
		 * SiteManager.CommonLimit; i++) { seedList.add(SiteManager.CommonDomain
		 * + i + lastPart); //seedList.add(SiteManager.CommonDomain + i); }
		 */

		int size = Integer.parseInt(author.getPageSize());
		for (int i = 0; i < size; i++) {
			seedList.add(author.getDomain() + i + "00");
		}
	}
}
