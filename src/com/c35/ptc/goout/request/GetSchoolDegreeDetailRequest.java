package com.c35.ptc.goout.request;

/**
 * 院校学位详情（申请条件，费用，截止日期）等
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetSchoolDegreeDetailRequest {

	private String command = "/school_degreeDetail.action";
	private int schoolId = -1;// 院校id，必须的参数
	private String degree = "";// 学位名称，必须参数

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public String getCommand() {
		return command;
	}

}
