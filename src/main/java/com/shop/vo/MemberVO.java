package com.shop.vo;

import java.util.Date;

public class MemberVO {
	// tb_member 기반으로 get/set vo 생성
	private String userid;
	private String userpass;
	private String username;
	private String userphon;
	private String useraddr1;
	private String useraddr2;
	private String useraddr3;
	private Date regdate;
	private int verify;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserphon() {
		return userphon;
	}
	public void setUserphon(String userphon) {
		this.userphon = userphon;
	}
	public String getUseraddr1() {
		return useraddr1;
	}
	public void setUseraddr1(String useraddr1) {
		this.useraddr1 = useraddr1;
	}
	public String getUseraddr2() {
		return useraddr2;
	}
	public void setUseraddr2(String useraddr2) {
		this.useraddr2 = useraddr2;
	}
	public String getUseraddr3() {
		return useraddr3;
	}
	public void setUseraddr3(String useraddr3) {
		this.useraddr3 = useraddr3;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public int getVerify() {
		return verify;
	}
	public void setVerify(int verify) {
		this.verify = verify;
	}
	
}
