package com.c35.ptc.goout.request;

/**
 * 获取顾问详情
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetConsultantInfoOfProjectRequest {

	private String command = "/adviser_info.action";

	private int consultantId = -1;// 必须的参数

	public int getConsultantId() {
		return consultantId;
	}

	public void setConsultantId(int consultantId) {
		this.consultantId = consultantId;
	}

	public String getCommand() {
		return command;
	}

}
