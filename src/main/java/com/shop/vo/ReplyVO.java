package com.shop.vo;

import java.util.Date;

public class ReplyVO {

	private int gdsnum;
	private String userid;
	private int repnum;
	private String repcon;
	private Date repdate;
	
	private String username;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getGdsnum() {
		return gdsnum;
	}
	public void setGdsnum(int gdsnum) {
		this.gdsnum = gdsnum;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getRepnum() {
		return repnum;
	}
	public void setRepnum(int repnum) {
		this.repnum = repnum;
	}
	public String getRepcon() {
		return repcon;
	}
	public void setRepcon(String repcon) {
		this.repcon = repcon;
	}
	public Date getRepdate() {
		return repdate;
	}
	public void setRepdate(Date repdate) {
		this.repdate = repdate;
	}
	public static void main(String[] args) {
		ReplyVO vo  =new ReplyVO();
		System.out.println(vo.getRepnum());
	}
}
