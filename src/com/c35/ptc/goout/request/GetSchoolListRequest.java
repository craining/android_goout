package com.c35.ptc.goout.request;

/**
 * 获取院校列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetSchoolListRequest {

	private String command = "/school_list.action";

	private int country = -1;// 国家ID(必须有且大于-1)
	private int specialtry = -1;// 专业大类

	
	public int getCountry() {
		return country;
	}

	
	public void setCountry(int country) {
		this.country = country;
	}

	
	public int getSpecialtry() {
		return specialtry;
	}

	
	public void setSpecialtry(int specialtry) {
		this.specialtry = specialtry;
	}

	public String getCommand() {
		return command;
	}

}
