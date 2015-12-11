package com.bill.crawler.manager;

import com.bill.crawler.model.Author;

/*
 * Create Author object required by SiteManager.
 */
public class AuthorManager {

	/************************************ 51CTO Author Definition *******************************************************/
	/*
	 * naming rule: open an article, the first is account, the second is id. Use
	 * account as the last part of var definition
	 */
	public static String Account_xmskf = "xmskf";// for test -- contains less
													// pages
	public static String Account_ticktick = "tickTick";
	public static String Account_8204129 = "8204129";
	public static String Account_wangwei007 = "wangwei007";
	/*
	 * http://me2xp.blog.51cto.com/ http://yttitan.blog.51cto.com/
	 * http://taokey.blog.51cto.com/ http://gaowenlong.blog.51cto.com/
	 */
	public static String Account_me2xp = "me2xp";
	public static String Account_yttitan = "yttitan";
	public static String Account_taokey = "taokey";
	public static String Account_gaowenlong = "gaowenlong";// lots of
															// imagess:600M

	public static String Account_liuyj = "liuyj";

	public static String Account_mabofeng = "mabofeng";

	public static String Account_aiilive = "aiilive";

	// http://gauyanm.blog.51cto.com/629619/1715801
	public static String Account_gauyanm = "gauyanm";
	public static String Account_rainbird = "rainbird";

	/************************************ CSDN Author Definition *******************************************************/
	/* get account from blog/article, and get id from list/profile */
	public static String Account_csdn_xlgen157387 = "xlgen157387";// contain
																	// binary
																	// files to
																	// download
	public static String Account_csdn_diannet = "diannet";// "瞧俺老孙的";// for test
	public static String Account_csdn_iispring = "iispring";// from article

	/************************************ Create Author *******************************************************/
	public static Author getAuthor(String account) {
		/************************************ 51CTO Author Creation *******************************************************/
		if (account.equals(Account_ticktick)) {
			// according article address: http://ticktick.blog.51cto.com/823160/
			return new Author(account, "823160", 12);
		} else if (account.equals(Account_8204129)) {
			// nickname myeit: 高焕堂
			// http://8204129.blog.51cto.com/8194129/p-3
			return new Author(account, "8194129", 19);
		} else if (account.equals(Account_liuyj)) {
			// http://liuyj.blog.51cto.com/2340749/p-3
			return new Author(account, "2340749", 6);
		} else if (account.equals(Account_xmskf)) {
			// http://xmskf.blog.51cto.com/1502830/p-1
			// the account is just for test image saving
			return new Author(account, "1502830", 1);
		} else if (account.equals(Account_mabofeng)) {
			return new Author(account, "2661587", 12);
		} else if (account.equals(Account_wangwei007)) {
			// http://wangwei007.blog.51cto.com/68019/1101757
			// nickname is lover007
			return new Author(account, "68019", 14);
		} else if (account.equals(Account_aiilive)) {
			// http://aiilive.blog.51cto.com/1925756/1719008
			return new Author(account, "1925756", 31);
		} else if (account.equals(Account_me2xp)) {
			// http://me2xp.blog.51cto.com/6716920/1719384
			return new Author(account, "6716920", 3);
		} else if (account.equals(Account_yttitan)) {
			// http://yttitan.blog.51cto.com/70821/1719772
			return new Author(account, "70821", 19);
		} else if (account.equals(Account_taokey)) {
			// http://taokey.blog.51cto.com/4633273/1719375
			return new Author(account, "4633273", 4);
		} else if (account.equals(Account_gaowenlong)) {
			// http://gaowenlong.blog.51cto.com/451336/1719357
			return new Author(account, "451336", 31);
		} else if (account.equals(Account_gauyanm)) {
			// http://gauyanm.blog.51cto.com/629619/1715801
			return new Author(account, "629619", 26);
		} else if (account.equals(Account_rainbird)) {
			// http://rainbird.blog.51cto.com/211214/1600504
			return new Author(account, "211214", 41);
		}

		/************************************ CSDN Author Creation *******************************************************/
		else if (account.equals(Account_csdn_xlgen157387)) {
			// http://blog.csdn.net/xlgen157387 : blog
			// http://blog.csdn.net/u010870518/article/list/1 // blog pages
			return new Author(account, "u010870518", 29);// 29
		} else if (account.equals(Account_csdn_diannet)) {
			// http://blog.csdn.net/diannet/article/list/2
			// http://blog.csdn.net/diannet/article/details/572019
			// http://my.csdn.net/diannet
			return new Author(account, account, 2);
		} else if (account.equals(Account_csdn_iispring)) {
			// blog http://blog.csdn.net/iispring
			// article: http://blog.csdn.net/iispring/article/details/47690011

			// list: http://blog.csdn.net/sunqunsunqun/article/list/1
			// profile: http://my.csdn.net/sunqunsunqun
			return new Author(account, "sunqunsunqun", 11);
		}

		return null;
	}
}
