package com.bill.crawler.site.fs;

import com.bill.crawler.core.BasePageHandler;
import com.bill.crawler.utility.CommonUtil;

public class FSPageHandler extends BasePageHandler {
	public FSPageHandler() {
		super();
	}

	@Override
	public boolean allowVisitor(String url) {
		boolean retVal = false;

		retVal = true;

		return retVal;
	}

	@Override
	protected boolean allowHandler() {
		String parentUrl = stat.getParentUrl();
		String url = stat.getUrl();

		// parent may be null
		if (CommonUtil.isEmptyString(parentUrl)) {
			return true;
		}

		if (!parentUrl.contains("finalshares") || url.contains("finalshares")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean extractor(String url, String html) {
		return true;
	}
}