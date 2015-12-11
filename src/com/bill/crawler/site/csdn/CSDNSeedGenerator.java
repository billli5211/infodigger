package com.bill.crawler.site.csdn;

import com.bill.crawler.model.Author;
import com.bill.crawler.seed.BaseSeedGenerator;

public class CSDNSeedGenerator extends BaseSeedGenerator {

	public CSDNSeedGenerator(Author author) {
		/*
		 * this is the entry of the author
		 * seedList.add("http://my.csdn.net/dog250");
		 * seedList.add("http://my.csdn.net/windeal");
		 */

		/*
		 * this is the blog entry of the author
		 */
		// seedList.add("http://blog.csdn.net/dog250");
		// seedList.add("http://blog.csdn.net/windeal");

		// http://blog.csdn.net/xlgen157387 : blog
		// http://my.csdn.net/u010870518 // profile
		// http://blog.csdn.net/u010870518/article/list/1 // blog pages
		// http://blog.csdn.net/xlgen157387/article/details/45459445 // blog
		// article
		if (author.isFetchBlog()) {
			String str = author.getExtra().get("CSDNBaseDomain") + author.getId() + "/article/list/";

			int size = Integer.parseInt(author.getPageSize());

			for (int i = 1; i <= size; i++) {
				seedList.add(str + i);
			}
		} else {
			seedList.add(author.getDomain());
		}
	}

}
