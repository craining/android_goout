package com.c35.ptc.goout.request;

/**
 * 获取项目列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class GetProjectListRequest {

	private String command = "/project_list.action";

	private String degree;// 按学位获取
	private int serverPublisherId;// 按发布者id获取
	private int serverCountryId;// 按国家id获取
	private int publisherType;// 按发布者类型获取
	private int serverSchoolId;// 按学校id获取

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public int getServerPublisherId() {
		return serverPublisherId;
	}

	public void setServerPublisherId(int serverPublisherId) {
		this.serverPublisherId = serverPublisherId;
	}

	public int getServerCountryId() {
		return serverCountryId;
	}

	public void setServerCountryId(int serverCountryId) {
		this.serverCountryId = serverCountryId;
	}

	public int getPublisherType() {
		return publisherType;
	}

	public void setPublisherType(int publisherType) {
		this.publisherType = publisherType;
	}

	public int getServerSchoolId() {
		return serverSchoolId;
	}

	public void setServerSchoolId(int serverSchoolId) {
		this.serverSchoolId = serverSchoolId;
	}

	public String getCommand() {
		return command;
	}

}
