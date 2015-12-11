package com.bill.crawler.site.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.bill.crawler.core.BasePageHandler;
import com.bill.crawler.global.PageTagConst;
import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;

public class CommonPageHandler extends BasePageHandler {
	public CommonPageHandler() {
		super();
	}

	@Override
	public boolean allowVisitor(String url) {
		return super.allowVisitor(url);
	}

	@Override
	protected boolean allowHandler() {
		String url = stat.getUrl();

		if (FilterUtil.isBinLink(url)) {
			return false;
		}

		return url.startsWith(getAuthor().getDomain());
	}

	@Override
	protected boolean extractor(String url, String html) {
		List list = FilterUtil.getEmails(html);
		if (list == null || list.size() == 0) {
			return false;
		} else {
			map.put(PageTagConst.emailTag, list);
			return true;
		}
	}

	@Override
	protected void saveCSV() {
		List list = (List) map.get(PageTagConst.emailTag);

		if (list == null || list.size() == 0) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			saveEmailIntoCSV((String) list.get(i));
		}
	}

	private void saveEmailIntoCSV(String email) {
		try {
			File csv = getConfig().getCsv();
			CsvWriter cw = new CsvWriter(new FileWriter(csv, true), ',');

			cw.write(email);

			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
	}

	@Override
	protected void savePage(Page page) {

		// no need to save page: just overwrite
		return;
	}

}