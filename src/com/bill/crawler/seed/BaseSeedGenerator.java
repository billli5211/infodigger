package com.bill.crawler.seed;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSeedGenerator implements ISeedGenerator {
	protected List<String> seedList = new ArrayList<String>();

	@Override
	public List<String> getSeeds() {
		return seedList;
	}
}
