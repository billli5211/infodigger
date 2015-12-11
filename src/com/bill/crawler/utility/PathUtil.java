package com.bill.crawler.utility;

import java.io.File;

public class PathUtil {
	public static String getTemplatePath(String name) {
		/*
		 * URL url = getClass().getClassLoader().getResource(SITE_Template);
		 * this.template = url.getPath();
		 */
		//String str = ClassLoader.getSystemResource("").getPath();
		String str = PathUtil.class.getResource("/").toString();
		
		str = str + "resources/templates/" + name;

		LogUtil.logger.info("template path:" + str);
		return str;
	}

	public static void main(String[] args) throws Exception {

		/*
		 * return: /Users/billytech
		 */

		// get system user root path
		LogUtil.logger.info(System.getProperty("user.home"));

		/*
		 * return : /Users/billytech/Desktop/server/workspace
		 */

		// get project root path
		LogUtil.logger.info(System.getProperty("user.dir"));
		//

		// new file, located in project path
		LogUtil.logger.info(new File("").getAbsolutePath());

		/*
		 * return :
		 * file:/Users/billytech/Desktop/server/workspace/bin/com/bill/crawler
		 * /utility/
		 */

		// path of specified class
		LogUtil.logger.info(PathUtil.class.getResource("").toString());

		/*
		 * return: file:/Users/billytech/Desktop/server/workspace/bin/
		 */

		// path of the root of specified class
		LogUtil.logger.info(PathUtil.class.getResource("/").toString());

		// path of thread context

		LogUtil.logger.info(Thread.currentThread().getContextClassLoader().getResource("").toString());

		// path of classloader of specified class
		LogUtil.logger.info(PathUtil.class.getClassLoader().getResource("").toString());

		// path of Classloader
		LogUtil.logger.info(ClassLoader.getSystemResource("").toString());

	}
}
