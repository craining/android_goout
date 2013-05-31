package com.c35.ptc.goout.request;

/**
 * 获取项目信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetProjectInfoRequest {

	private String command = "/project_info.action";

	private int projectId = -1;// 项目id，必须有（>-1）

	
	public int getProjectId() {
		return projectId;
	}

	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getCommand() {
		return command;
	}

}
