package com.bill.crawler.rank;

import java.util.ArrayList;

public class KeywordCriteria extends BaseCriteria {
	public static String ios = "ios";
	public static String android = "android";
	public static String mobile = "mobile";
	public static String web = "web";
	public static String server = "server";
	public static String java = "java";

	public static ArrayList<String> keywords = new ArrayList<String>();
	static {
		keywords.add(ios);
		keywords.add(android);
		keywords.add(mobile);
		keywords.add(web);
		keywords.add(server);
		keywords.add(java);
		keywords.add("设计模式");
	}

	public KeywordCriteria(ICriteria decoratedCriteria) {
		super(decoratedCriteria);
	}

	public static boolean isKeywordContained(String content) {
		boolean retVal = false;

		for (String str : keywords) {
			if (content.contains(str)) {
				retVal = true;
				break;
			}
		}

		return retVal;
	}

	@Override
	public int meetCriteria(String content) {
		int retVal = 0;
		if (this.decoratedCriteria != null) {
			retVal += this.decoratedCriteria.meetCriteria(content);
		}

		int value = 0;
		ArrayList<String> list = this.criteriaList;
		if (list == null) {
			list = keywords;
		}
		if (list != null) {
			for (String str : list) {
				if (content.contains(str)) {
					value++;
				}
			}
		}

		return retVal + value * PageRank.KEYWORD_WEIGHT;
	}

}
