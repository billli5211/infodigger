package com.bill.crawler.third.sftp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.bill.crawler.global.SystemAccount;
import com.jcraft.jsch.ChannelSftp;

public class SFTPAPI {

	public SFTPChannel getSFTPChannel() {
		return new SFTPChannel();
	}

	// just for test purpose
	public String getUploadFilePath() {
		String SITE_Template = "templates/51cto.html";
		String localFile = getClass().getClassLoader().getResource(SITE_Template).getPath();

		return localFile;
	}

	/*
	 * dst: can be dir path, means put file under the dir.
	 */
	public static void uploadFile(String src, String dst) throws Exception {
		Map<String, String> sftpDetails = new HashMap<String, String>();
		// 设置主机ip，端口，用户名，密码
		sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, UploadConfig.ServerAddress);
		sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, SystemAccount.SftpUserName);
		sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, SystemAccount.SftpPassword);
		sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, SFTPConstants.SFTP_DEFAULT_PORT + "");

		SFTPChannel channel = new SFTPChannel();
		ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);

		// bill make dir first.
		chSftp.mkdir(dst);

		File file = new File(src);
		long fileSize = file.length();

		/**
		 * 代码段1 OutputStream out = chSftp.put(dst, ChannelSftp.OVERWRITE); //
		 * 使用OVERWRITE模式 byte[] buff = new byte[1024 * 256]; //
		 * 设定每次传输的数据块大小为256KB int read; if (out != null) {
		 * System.out.println("Start to read input stream"); InputStream is =
		 * new FileInputStream(src); do { read = is.read(buff, 0, buff.length);
		 * if (read > 0) { out.write(buff, 0, read); } out.flush(); } while
		 * (read >= 0); System.out.println("input stream read done."); }
		 **/

		chSftp.put(src, dst, new FileProgressMonitor(fileSize), ChannelSftp.OVERWRITE); // 代码段2

		// chSftp.put(new FileInputStream(src), dst, ChannelSftp.OVERWRITE); //
		// 代码段3

		chSftp.quit();
		channel.closeChannel();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SFTPAPI test = new SFTPAPI();
		String src = test.getUploadFilePath(); // 本地文件名
		String dst = UploadConfig.ServerBasePath; // 目标文件名
		SFTPAPI.uploadFile(src, dst);

	}
}