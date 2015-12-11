package com.bill.crawler.model;

import java.util.HashMap;
import java.util.Map;

import com.bill.crawler.global.SiteConst;
import com.bill.crawler.utility.CommonUtil;
import com.bill.crawler.utility.FilterUtil;

public class Author extends User {
	// http://8204129.blog.51cto.com/8194129/p-2
	// << 1 2 3 4 5 6 >> 页数 ( 2/19 )
	private String id;
	private String domain;
	private String pageSize;
	private String info;
	private String title;

	private Map<String, String> extraMap;

	private AuthorStat stat;

	private int contentCategory;// 1: blog, 2: profile, 3:others

	/*
	 * For 51cto, the rule of defining account and id is simple -- open a
	 * article, check address http://ticktick.blog.51cto.com/823160/1671438
	 * 
	 * The list address is just add "/p2"
	 * http://ticktick.blog.51cto.com/823160/p-2
	 */

	/*
	 * For csdn, the rule is --- article/blog --- account
	 * http://blog.csdn.net/xlgen157387 // blog
	 * http://blog.csdn.net/xlgen157387/article/details/45459445 // article
	 * 
	 * list/profile --- use id http://blog.csdn.net/u010870518/article/list/1 //
	 * list http://my.csdn.net/u010870518 // profile
	 */
	//
	public Author(String account, String id, String title, String nickName, String domain, String pageSize,
			String info) {
		super(account, nickName);

		this.id = id;
		this.setDomain(domain);
		this.pageSize = pageSize;
		this.title = title;

		this.info = info;
		this.extraMap = new HashMap<String, String>();
		this.stat = new AuthorStat();
	}

	public Author(String account, String string, int i) {
		super(account, null);
	}

	public int getContentCategory() {
		return contentCategory;
	}

	public boolean isFetchBlog() {
		return (contentCategory == 1);
	}

	public boolean isFetchProfile() {
		return (contentCategory == 2);
	}

	public boolean validate(String site) {
		if (site.equals(SiteConst.SITE_51CTO)) {
			if (CommonUtil.isEmptyString(domain) || domain.contains("blog.51cto.com/")) {
				// http://8204129.blog.51cto.com/8194129/p-3
				contentCategory = 1;
				domain = FilterUtil.HTTP_PROTOCOL + getAccount() + ".blog.51cto.com/" + getId() + "/";
			} else {
				contentCategory = 2;
				domain = "";// ////// TBD
			}
			
			if(CommonUtil.isEmptyString(this.pageSize)){
				return false;
			}

		} else if (site.equals(SiteConst.SITE_CSDN)) {
			if (domain.startsWith("http://blog.csdn.net")) {
				// http://blog.csdn.net/xlgen157387/article/details/
				contentCategory = 1;
				String CSDNBaseDomain = "http://blog.csdn.net/";
				getExtra().put("CSDNBaseDomain", CSDNBaseDomain);
				domain = CSDNBaseDomain + getAccount() + "/article/details/";
			} else if (domain.startsWith("http://my.csdn.net")) {
				// http://my.csdn.net/u010870518
				contentCategory = 2;
				String CSDNBaseDomain = "http://my.csdn.net/";
				domain = CSDNBaseDomain + getId() + "/";
			}
			
			if(CommonUtil.isEmptyString(this.pageSize)){
				return false;
			}
		}
		
		return true;
	}


	public String getId() {
		return id;
	}

	public String getPageSize() {
		return pageSize;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getInfo() {
		return info;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addExtra(String extra1, String extra2, String extra3) {
		this.extraMap.put(SiteConst.SITE_TAG_EXTRA1, extra1);
		this.extraMap.put(SiteConst.SITE_TAG_EXTRA2, extra2);
		this.extraMap.put(SiteConst.SITE_TAG_EXTRA3, extra3);
	}

	public Map<String, String> getExtra() {
		return this.extraMap;
	}

}
