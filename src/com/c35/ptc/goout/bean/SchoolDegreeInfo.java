package com.c35.ptc.goout.bean;

/**
 * 院校详情的某个学位的信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-13
 */
public class SchoolDegreeInfo {

	private String degreeName;// 名称,如“本科”
	private String educationRequire;// 学历要求， 如“幼儿园毕业”
	private String gpa;// gpa，如“3.0”
	private String language;// 语言要求，如“PBD>60、ETC6>225”
	private String[] endTimes;// 申请截止时间，如“2012-01”
	private int tuituon;// 学费，如“30000”
	private String tuituonUnit;// 学费单位，如“$”

	private String tuituonTimeUnit;// 学费时间单位，如“年”
	private int scholarShip = -1;// 奖学金，0是1否 ， 若为-1表示服务器返回该字段为空

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public String getEducationRequire() {
		return educationRequire;
	}

	public void setEducationRequire(String educationRequire) {
		this.educationRequire = educationRequire;
	}

	public String getGpa() {
		return gpa;
	}

	public void setGpa(String gpa) {
		this.gpa = gpa;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String[] getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String[] endTimes) {
		this.endTimes = endTimes;
	}

	public int getTuituon() {
		return tuituon;
	}

	public void setTuituon(int tuituon) {
		this.tuituon = tuituon;
	}

	public int getScholarShip() {
		return scholarShip;
	}

	public void setScholarShip(int scholarShip) {
		this.scholarShip = scholarShip;
	}

	public String getTuituonUnit() {
		return tuituonUnit;
	}

	public void setTuituonUnit(String tuituonUnit) {
		this.tuituonUnit = tuituonUnit;
	}

	public String getTuituonTimeUnit() {
		return tuituonTimeUnit;
	}

	public void setTuituonTimeUnit(String tuituonTimeUnit) {
		this.tuituonTimeUnit = tuituonTimeUnit;
	}

	/**
	 * 若学位名称相等，则相等
	 */
	@Override
	public int hashCode() {
		return this.degreeName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null) {
			if (obj instanceof SchoolDegreeInfo) {
				if ((((SchoolDegreeInfo) obj).degreeName).equals(this.degreeName)) {
					return true;
				}
			}
		}
		return false;
	}

}
