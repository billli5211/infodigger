package com.bill.crawler.global;

public class CrawlingConst {
	// shows the number of concurrent threads that should be initiated for
	// crawling
	public static int maxThreadNum = 1000;
	public static int moderateThreadNum = 100;
	public static int minThreadNum = 10;

	public static int maxRequestDelay = 1000;// polite Request Delay:1000
												// milliseconds
	// between requests
	public static int moderateRequestDelay = 100;
	public static int minRequestDelay = 10;

	// level1 is with high speed but may be risky.
	public enum CrawlPerformanceLevel {
		LEVEL1, LEVEL2, LEVEL3
	}
}
