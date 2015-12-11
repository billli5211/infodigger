package com.bill.crawler.site.cto;

import com.bill.crawler.model.Author;
import com.bill.crawler.seed.BaseSeedGenerator;

public class CtoSeedGenerator extends BaseSeedGenerator {
	public CtoSeedGenerator(Author author) {
		if (author.isFetchBlog()) {
			// http://ticktick.blog.51cto.com/823160/p-12
			String str = author.getDomain() + "p-";
			
			int size = Integer.parseInt(author.getPageSize());

			for (int i = 1; i <= size; i++) {
				seedList.add(str + i);
			}
		} else {
			// 51cto seed eg:
			// "http://home.51cto.com/index.php?s=/space/10208987/"
			for (int i = 9000000; i < 11208987; i++) {// last time started from
														// 100000
				seedList.add(author.getDomain() + "?s=/space/" + i + "/");
				i = i + 100;
			}
		}

	}
}
