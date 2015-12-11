package com.bill.crawler.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

import com.bill.crawler.config.SystemConfig;
import com.bill.crawler.global.SiteConst;
import com.bill.crawler.model.Author;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.LogUtil;

/*
 * to read site config info.
 * in order to support xml config file, need to rely on common-configuration and commons-lang jars.
 */
public class SiteConfReader {

	static void readSysConfig() {
		String str = null;
		ResourceBundle res = ResourceBundle.getBundle(SiteConst.CONF_PATH + SiteConst.FILE_SYSTEM);

		SystemConfig.setBasePath(res.getString(SiteConst.SYSTEM_TAG_BasePath));

		str = res.getString(SiteConst.SYSTEM_TAG_Enable_Data_Upload_To_Server);
		if (str.equals("true")) {
			SystemConfig.setEnableDataUploadToServer(true);
		} else {
			SystemConfig.setEnableDataUploadToServer(false);
		}

		str = res.getString(SiteConst.SYSTEM_TAG_Save_Pure_Content);
		if (str.equals("true")) {
			SystemConfig.setSavePureContent(true);
		} else {
			SystemConfig.setSavePureContent(false);
		}

		str = res.getString(SiteConst.SYSTEM_TAG_EnableBin);
		if (str.equals("true")) {
			SystemConfig.setEnableBinHandling(true);
		} else {
			SystemConfig.setEnableBinHandling(false);
		}
		
		str = res.getString(SiteConst.SYSTEM_TAG_Enable_Concurrent_Crawling);
		if (str.equals("true")) {
			SystemConfig.setEnableConcurrentCrawling(true);
		} else {
			SystemConfig.setEnableBinHandling(false);
		}
		
		str = res.getString(SiteConst.SYSTEM_TAG_Crawling_Performance_Level);
		int crawlingPerformanceLevel = Integer.parseInt(str);
		SystemConfig.setCrawlingPerformanceLevel(crawlingPerformanceLevel);
	}

	public static List<Author> readSiteConfig(String resource, String site) {
		HierarchicalConfiguration config;
		try {
			config = new XMLConfiguration(resource);
		} catch (ConfigurationException e) {
			LogUtil.logger.error(e.toString());
			return null;
		}
		//config.setExpressionEngine(new XPathExpressionEngine());

		// whether the site is enabled
		int siteEnabled = config.getInt(SiteConst.SITE_TAG_ENABLE);
		if (siteEnabled != 1) {
			LogUtil.logger.info("site:" + site + " is not enabled");
			return null;
		}

		String authorPath = SiteConst.SITE_TAG_AUTHORS;
		HierarchicalConfiguration lineConfig = (HierarchicalConfiguration) config.subset(authorPath);
		List<Author> authorList = parseMutliLines(lineConfig);

		return authorList;
	}

	private static List<Author> parseMutliLines(HierarchicalConfiguration lineConfig) {
		Node root = lineConfig.getRoot();
		List<ConfigurationNode> children = root.getChildren();

		Author author = null;

		List<Author> authorList = new ArrayList<Author>();
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			if (SiteConst.SITE_TAG_AUTHOR.equals(child.getName())) {
				author = parseAuthor(child);
				if (author != null) {
					authorList.add(author);
				}
			}
		}

		return authorList;
	}

	private static Author parseAuthor(Node authorNode) {
		List<ConfigurationNode> children = authorNode.getChildren();

		String account = null;
		String nickName = null;
		String id = null;
		String domain = null;
		String pageSize = null;
		String info = null;
		String title = null;
		String extra1 = null;
		String extra2 = null;
		String extra3 = null;

		String name = null;
		String value = null;
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			
			value = (String) child.getValue();
			if(CommonUtil.isEmptyString(value)){
				continue;
			}
			name = child.getName();

			if (SiteConst.SITE_TAG_DOMAIN.equals(name)) {
				domain = value;
			} else if (SiteConst.SITE_TAG_ACCOUNT.equals(name)) {
				account = value;
			} else if (SiteConst.SITE_TAG_ID.equals(name)) {
				id = value;
			} else if (SiteConst.SITE_TAG_PAGESIZE.equals(name)) {
				pageSize = value;
			} else if (SiteConst.SITE_TAG_ENABLE.equals(name)) {
				if(value.equals("1")){
					continue;
				}else{
					return null;
				}
			} else if (SiteConst.SITE_TAG_TITLE.equals(name)) {
				title = value;
			} else if (SiteConst.SITE_TAG_NICKNAME.equals(name)) {
				nickName = value;
			} else if (SiteConst.SITE_TAG_INFO.equals(name)) {
				info = value;
			} else if (SiteConst.SITE_TAG_EXTRA1.equals(name)) {
				extra1 = value;
			} else if (SiteConst.SITE_TAG_EXTRA2.equals(name)) {
				extra2 = value;
			} else if (SiteConst.SITE_TAG_EXTRA3.equals(name)) {
				extra3 = value;
			} else {
				LogUtil.logger.error("error occurred when parse author");
				return null;
			}
		}

		Author author = new Author(account, id, title, nickName, domain, pageSize, info);
		author.addExtra(extra1, extra2, extra3);

		return author;
	}

	public static void main(String args[]){
		
		// test bundle
		String str = null;
		ResourceBundle res = ResourceBundle.getBundle("conf/system.properties");

		str = res.getString(SiteConst.SYSTEM_TAG_BasePath);
		LogUtil.logger.info(str);
		str = res.getString(SiteConst.SYSTEM_TAG_Enable_Data_Upload_To_Server);
		LogUtil.logger.info(str);
		
		/*
		
		// text xml 
		try
		{
		    XMLConfiguration config = new XMLConfiguration("conf/test.xml");
		    // do something with config
		    
			String backColor = config.getString("colors.background");
			String textColor = config.getString("colors.text");
			String linkNormal = config.getString("colors.link[@normal]");
			String defColor = config.getString("colors.default");
			int rowsPerPage = config.getInt("rowsPerPage");
			List<Object> buttons = config.getList("buttons.name");
			LogUtil.logger.info("button names:" + buttons.toString());
		}
		catch(ConfigurationException cex)
		{
		    // something went wrong, e.g. the file was not found
			LogUtil.logger.error("cex:" + cex.toString());
		}
		
		
		try
		{
		    XMLConfiguration config = new XMLConfiguration("conf/site_51cto.xml");
		    // do something with config
		    
			String name = config.getString("name");
			int enabled = config.getInt("enable");

			LogUtil.logger.info(name);
		}
		catch(ConfigurationException cex)
		{
		    // something went wrong, e.g. the file was not found
			LogUtil.logger.error("cex:" + cex.toString());
		}
	
	*/
	}
}
