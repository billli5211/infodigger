package com.bill.crawler.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bill.crawler.config.SystemConfig;
import com.bill.crawler.engine.JsoupEngine;
import com.bill.crawler.global.PathConst;
import com.bill.crawler.stat.PageStat;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.DateUtil;
import com.bill.crawler.utility.FileUtil;
import com.bill.crawler.utility.FilterUtil;
import com.bill.crawler.utility.FileUploadUtil;
import com.bill.crawler.utility.LogUtil;
import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;

public class BasePageHandler extends BaseCrawler {

	protected Map<Object, Object> map;
	protected String title;
	protected PageStat stat;
	protected byte[] pageContent;

	private static boolean hasTheadExecuteUpload = false;

	public BasePageHandler() {
		map = new HashMap<Object, Object>();
	}

	/***********************************************************************/
	// override parent methods.
	
	@Override
	public boolean allowVisitor(String url) {
		if (FilterUtil.isBinLink(url)) {
			return false;
		}

		return url.startsWith(getAuthor().getDomain());
	}

	@Override
	public boolean handleData(String url, Page page, String html) {
		boolean retVal = true;

		// get common stat
		map.clear();
		stat = new PageStat(page);

		if (!allowHandler()) {
			return false;
		}

		pageContent = page.getContentData();
		if (!FilterUtil.isBinLink(url)) {
			retVal = extractor(url, html);
		}

		if (retVal) {
			title = CommonUtil.trimTitle(stat.getTitle());
			saveCSV();
			savePage(page);
		} else {
			return false;
		}

		return true;
	}
	
	@Override
	public void onBeforeExit() {
		super.onBeforeExit();

		if (hasTheadExecuteUpload == false) {
			uploadFiles();
		}
	}

	/***********************************************************************/
	// protected, can be override by sub-classes
	protected boolean allowHandler() {
		return true;
	}

	protected boolean extractor(String url, String html) {
		return true;
	}


	
	/***********************************************************************/
	// protected save*** methods.
	protected void saveCSV() {
		try {
			File csv = getConfig().getCsv();
			CsvWriter cw = new CsvWriter(new FileWriter(csv, true), ',');

			saveCommonCSV(cw);

			cw.endRecord();
			cw.close();
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
	}

	protected void savePage(Page page) {

		String url = stat.getUrl();

		if (FilterUtil.isBinLink(url)) {
			if (stat.isNonImageBinLink() && !SystemConfig.isEnableBinHandling()) {
				return;
			} else {
				saveBin();
			}

			return;
		}

		// common html page
		String filePath = getPageStoragePath(url);
		CommonUtil.save(pageContent, filePath);
	}

	// protected: can be accessed by sub-classes
	protected void saveCommonCSV(CsvWriter cw) {
		try {
			cw.write(stat.getTitle());
			cw.write(stat.getUrl());
			cw.write(stat.getAnchor());
			cw.write(stat.getDomain());
			cw.write(stat.getLength() + "");
			cw.write(stat.getLinkNum() + "");
			cw.write(stat.getParentUrl());
		} catch (IOException e) {
			LogUtil.logger.error(e.toString());
		}
	}
	
	/***********************************************************************/
	// private methods

	/*
	 * Save binary
	 */
	private void saveBin() {
		// first save image
		String url = stat.getUrl();
		boolean isImageLink = stat.isImageLink();

		String folderPath = null;
		if (isImageLink) {
			folderPath = getConfig().getImageFolder().getAbsolutePath();
		} else {
			folderPath = getConfig().getStorageFolder().getAbsolutePath() + "/" + PathConst.BinFolderName;
		}

		String fileName = FileUtil.getLastStringFromUrl(url);
		if (fileName.length() < 4) {
			fileName = CommonUtil.getRandomName() + "_" + fileName;
		}

		String filePath = folderPath + "/" + fileName;

		CommonUtil.save(pageContent, filePath);
		logger.info("stored:" + url + "  parent:" + stat.getParentUrl());

		// second modify parent page
		if (isImageLink) {
			modifyImageSrcInParentPage(fileName, PathConst.ImageWebPath + DateUtil.getSimpleCurrentDate()
					+ "/" + fileName);
		}
	}

	private void modifyImageSrcInParentPage(String oldImgSrc, String newImgSrc) {

		if (CommonUtil.isEmptyString(stat.getParentUrl())) {
			return;
		}

		// get parent page
		String filePath = getPageStoragePath(stat.getParentUrl());
		if (FileUtil.isFileExist(filePath)) {
			boolean result = JsoupEngine.modifyLinkSrcInFile(filePath, getConfig().getEncoding(), oldImgSrc,
					newImgSrc);
			if (result) {
				// save
				logger.info("modified file:" + filePath + "  with image path:" + newImgSrc);
			}
		}
	}

	private String getPageStoragePath(String url) {
		String folderPath = getConfig().getStorageFolder().getAbsolutePath();
		String pageFileName = FileUtil.getNewPageName(url);
		return folderPath + "/" + pageFileName;
	}

	private synchronized void uploadFiles() {
		if (SystemConfig.isEnableDataUploadToServer() && hasTheadExecuteUpload == false) {
			hasTheadExecuteUpload = true;
			try {

				int uploadedNum = FileUploadUtil.uploadOSS(getConfig().getImageFolder());
				logger.info("uploaded file num:" + uploadedNum);

				// next upload files
				String time = getConfig().getTime();
				FileUploadUtil.uploadCsvFiles(time);
				FileUploadUtil.uploadPageFiles(time);

			} catch (Exception e) {
				LogUtil.logger.error(e.toString());
			}
		}

	}

}
