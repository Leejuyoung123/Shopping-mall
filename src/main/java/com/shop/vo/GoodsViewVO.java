package com.shop.vo;

import java.util.Date;

public class GoodsViewVO {
	/*
	 * 필드 값은 소문자로 지정해주는게 나중에 헷갈리지 않음 카멜표기법도 나중에 헷갈리기 떄문에 변수명을 지을떄 헷갈리지 않게끔 다른사람이봐도
	 * 한눈에 알아보게 변수명을 작성해야함 why? 나중에 몇달뒤에 봐도 이 변수명이 어떤의미를 가지고있는지 파악하기위함 tbl_goods
	 * 기반으로만든 VO get/set 생성
	 */
	private int gdsnum;
	private String gdsname;
	private String catecode;
	private int gdsprice;
	private int gdsstock;
	private String gdsdes;
	private String gdsimg;
	private Date gdsdate;
	
	private String catecoderef;
	private String catename;
	
	private String gdsthumbimg;
	
	public String getGdsthumbimg() {
		return gdsthumbimg;
	}
	public void setGdsthumbimg(String gdsthumbimg) {
		this.gdsthumbimg = gdsthumbimg;
	}
	public String getCatecoderef() {
		return catecoderef;
	}
	public void setCatecoderef(String catecoderef) {
		this.catecoderef = catecoderef;
	}
	public String getCatename() {
		return catename;
	}
	public void setCatename(String catename) {
		this.catename = catename;
	}
	public int getGdsnum() {
		return gdsnum;
	}
	public void setGdsnum(int gdsnum) {
		this.gdsnum = gdsnum;
	}
	public String getGdsname() {
		return gdsname;
	}
	public void setGdsname(String gdsname) {
		this.gdsname = gdsname;
	}
	public String getCatecode() {
		return catecode;
	}
	public void setCatecode(String catecode) {
		this.catecode = catecode;
	}
	public int getGdsprice() {
		return gdsprice;
	}
	public void setGdsprice(int gdsprice) {
		this.gdsprice = gdsprice;
	}
	public int getGdsstock() {
		return gdsstock;
	}
	public void setGdsstock(int gdsstock) {
		this.gdsstock = gdsstock;
	}
	public String getGdsdes() {
		return gdsdes;
	}
	public void setGdsdes(String gdsdes) {
		this.gdsdes = gdsdes;
	}
	public String getGdsimg() {
		return gdsimg;
	}
	public void setGdsimg(String gdsimg) {
		this.gdsimg = gdsimg;
	}
	public Date getGdsdate() {
		return gdsdate;
	}
	public void setGdsdate(Date gdsdate) {
		this.gdsdate = gdsdate;
	}
	
}
