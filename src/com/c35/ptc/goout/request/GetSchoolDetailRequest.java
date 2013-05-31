package com.c35.ptc.goout.request;

/**
 * 获取院校详情
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetSchoolDetailRequest {

	private String command = "/school_detail.action";
	private int schoolId = -1;// 学校id，必须参数

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
