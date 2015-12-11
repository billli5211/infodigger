package com.bill.crawler.rank;

import com.bill.crawler.engine.JsoupEngine;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.LogUtil;

public class PageRank {

	public static int KEYWORD_WEIGHT = 100;
	public static int TIME_WEIGHT = 10;

	public static void main(String[] args) {
		String url = "http://www.runoob.com/design-pattern/decorator-pattern.html";
		String content = JsoupEngine.getContentByJsoup(url);
		if (!CommonUtil.isEmptyString(content)) {
			ICriteria keywordCriteria = new KeywordCriteria(new DateCriteria());
			int rank = keywordCriteria.meetCriteria(content);
			LogUtil.logger.debug("rank of site:" + url + "is--" + rank);
		}
	}
}
