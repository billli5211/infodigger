package com.bill.crawler.model;

public class AuthorStat {
	/*
	 * 文章数：150 评论数：338 访问量：970440 无忧币：12689 博客积分：6074 博客等级：8 注册日期：2006-11-11
	 */
	private int articleNum;
	private int commentNum;
	private int visitNum;
	private int credit;
	private int rank;
	private String registerDate;

	public int getArticleNum() {
		return articleNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public int getCredit() {
		return credit;
	}

	public int getRank() {
		return rank;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
}
