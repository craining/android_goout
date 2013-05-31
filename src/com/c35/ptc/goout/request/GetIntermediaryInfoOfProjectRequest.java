package com.c35.ptc.goout.request;

/**
 * 获取中介信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetIntermediaryInfoOfProjectRequest {

	private String command = "/intermediary_info.action";
	
	private int intermediaryId = -1;//中介id，必须的参数

	
	public int getIntermediaryId() {
		return intermediaryId;
	}
	
	public void setIntermediaryId(int intermediaryId) {
		this.intermediaryId = intermediaryId;
	}

	public String getCommand() {
		return command;
	}

}
