package com.shop.vo;

public class CategoryVO {
	private String catename;
	private String catecode;
	private String catecoderef;
	private int level;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCatename() {
		return catename;
	}
	public void setCatename(String catename) {
		this.catename = catename;
	}
	public String getCatecode() {
		return catecode;
	}
	public void setCatecode(String catecode) {
		this.catecode = catecode;
	}
	public String getCatecoderef() {
		return catecoderef;
	}
	public void setCatecoderef(String catecoderef) {
		this.catecoderef = catecoderef;
	}
	
}
