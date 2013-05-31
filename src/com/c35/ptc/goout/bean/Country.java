package com.c35.ptc.goout.bean;

/**
 * 国家信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-4
 */
public class Country {

	private int id;// 本地id
	private int serverId;// id
	private String nameCn;// 中文名
	private String area;// 区域，（洲）
	private boolean isFirstOfArea;// 是区域内第一个国家

	private String nameEn;// 英文名(尚缺省)

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public boolean isFirstOfArea() {
		return isFirstOfArea;
	}

	public void setFirstOfArea(boolean isFirstOfArea) {
		this.isFirstOfArea = isFirstOfArea;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
