package com.bill.crawler.model;

public class User {

	private String account;
	private String password;

	private String name;// nickname
	private String avtar;
	private String sex;
	private String age;

	// general info
	private String description;

	// contact
	private String email;
	private String qq;
	private String weichat;
	private String tel;
	private String mobile;

	// background
	private String company;
	private String jobTitle;
	private String school;
	private String location;

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getAvtar() {
		return avtar;
	}

	public String getSex() {
		return sex;
	}

	public String getAge() {
		return age;
	}

	public String getDescription() {
		return description;
	}

	public String getEmail() {
		return email;
	}

	public String getQq() {
		return qq;
	}

	public String getWeichat() {
		return weichat;
	}

	public String getTel() {
		return tel;
	}

	public String getMobile() {
		return mobile;
	}

	public String getCompany() {
		return company;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getSchool() {
		return school;
	}

	public String getLocation() {
		return location;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAvtar(String avtar) {
		this.avtar = avtar;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setWeichat(String weichat) {
		this.weichat = weichat;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public User(String account, String nickName) {
		this.account = account;
		this.name = nickName;
	}
}
