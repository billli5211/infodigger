package com.bill.crawler.third.sftp;

import com.bill.crawler.utility.LogUtil;
import com.jcraft.jsch.SftpProgressMonitor;

public class SimpleProgressMonitor implements SftpProgressMonitor {
	private long transfered;

	@Override
	public boolean count(long count) {
		transfered = transfered + count;
		LogUtil.logger.info("Currently transferred total size: " + transfered + " bytes");
		return true;
	}

	@Override
	public void end() {
		LogUtil.logger.info("Transferring done.");
	}

	@Override
	public void init(int op, String src, String dest, long max) {
		LogUtil.logger.info("Transferring begin.");
	}
}