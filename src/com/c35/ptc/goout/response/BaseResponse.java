package com.c35.ptc.goout.response;

/**
 * 所有response都继承此类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class BaseResponse {

	private String responseData = "";

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public void initFeild(String response) {
		responseData = response;
	}

}
