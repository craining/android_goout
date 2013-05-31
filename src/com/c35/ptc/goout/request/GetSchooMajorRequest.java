package com.c35.ptc.goout.request;

/**
 * 获取院校学位对应的专业
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetSchooMajorRequest {

	private String command = "/school_specialties.action";
	private int schoolId = -1;// 院校id，必须参数
	private String degree = "";// 专业对应的学位，必须参数

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getCommand() {
		return command;
	}

}
