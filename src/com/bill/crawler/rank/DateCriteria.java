package com.bill.crawler.rank;

public class DateCriteria extends BaseCriteria {
	public DateCriteria() {
		super(null);
	}

	@Override
	public int meetCriteria(String content) {
		// TODO Auto-generated method stub
		return 1 * PageRank.TIME_WEIGHT;
	}
}
