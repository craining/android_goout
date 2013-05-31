package com.c35.ptc.goout.bean;

/**
 * 专业信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-4
 */
public class Major {

	private int serverId;// id
	private String nameCn;// 中文名
	private String sortKey;// 拼音
	private boolean isFirstOfSortKey;// 是否是首字母分组中的第一个
	private String nameEn;// 英文名

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

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public boolean isFirstOfSortKey() {
		return isFirstOfSortKey;
	}

	public void setFirstOfSortKey(boolean isFirstOfSortKey) {
		this.isFirstOfSortKey = isFirstOfSortKey;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * {"specialtiesList":[
	 * 
	 * {"cnName":"军事","enName":"","id":1},
	 * 
	 * {"cnName":"农学","enName":"","id":2},
	 * 
	 * {"cnName":"医学","enName":"","id":3},
	 * 
	 * {"cnName":"历史学","enName":"","id":4},
	 * 
	 * {"cnName":"哲学","enName":"","id":5},
	 * 
	 * {"cnName":"工学","enName":"","id":6},
	 * 
	 * {"cnName":"教育学","enName":"","id":7},
	 * 
	 * {"cnName":"文学","enName":"","id":8},
	 * 
	 * ]}
	 */

}
