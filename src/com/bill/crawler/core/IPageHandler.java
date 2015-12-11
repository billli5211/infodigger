package com.bill.crawler.core;

import edu.uci.ics.crawler4j.crawler.Page;

public interface IPageHandler {

	public boolean allowVisitor(String url);

	public boolean handleData(String url, Page page, String html);
}
