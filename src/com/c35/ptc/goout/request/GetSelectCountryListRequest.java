package com.c35.ptc.goout.request;

/**
 * 获取国家列表
 * @Description:
 * @author: zhuanggy
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetSelectCountryListRequest {

	private String command = "/basic_countries.action";
//	private String command = "/v_getUnSent.action";

	
	public String getCommand() {
		return command;
	}

}
