package com.c35.ptc.goout.bean;

/**
 * 院校专业信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-13
 */
public class SchoolMajorInfo {

	private String groupName;// 组名（学位）
	private String[] majorsCn;// 所有专业,
	private String[] majorsEn;// 所有专业,

	
	public String[] getMajorsCn() {
		return majorsCn;
	}

	
	public void setMajorsCn(String[] majorsCn) {
		this.majorsCn = majorsCn;
	}

	
	public String[] getMajorsEn() {
		return majorsEn;
	}

	
	public void setMajorsEn(String[] majorsEn) {
		this.majorsEn = majorsEn;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 若学位名称相等，则相等
	 */
	@Override
	public int hashCode() {
		return this.groupName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null) {
			if (obj instanceof SchoolMajorInfo) {
				if ((((SchoolMajorInfo) obj).groupName).equals(this.groupName)) {
					return true;
				}
			}
		}
		return false;
	}

}
