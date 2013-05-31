package com.c35.ptc.goout.bean;

/**
 * 最近联系记录
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-4
 */
public class RecentlyConsult {

	private int id;// 本地数据库里的编号
	private int projectId;// true int 项目id
	private int callRuntime;// false int 联系时长
	private long callTime;// false string 联系时间（空为服务器时间）
	private String callerNumber;// true string 主叫电话
	private String callerIMSI;// true string 主叫IMSI
	private String callerIMEI;// true strimg 主叫IMEI
	private String calledNumber;// true string 被叫号码
	private int calledType;// true int 被叫者类型(顾问1、中介0)
	private int calledID;// false int 被叫者ID

	private int uploadState;// 上传与否

	// 供最近联系列表显示需要
	private String calledName;// 被叫者名字
	private String calledLogoName;// 被叫者logo
	private String calledServerArea;// 被叫者服务区域
	private String calledDescription;// 被叫者描述

	public String getCalledName() {
		return calledName;
	}

	public void setCalledName(String calledName) {
		this.calledName = calledName;
	}

	public String getCalledLogoName() {
		return calledLogoName;
	}

	public void setCalledLogoName(String calledLogoName) {
		this.calledLogoName = calledLogoName;
	}

	public String getCalledServerArea() {
		return calledServerArea;
	}

	public void setCalledServerArea(String calledServerArea) {
		this.calledServerArea = calledServerArea;
	}

	public String getCalledDescription() {
		return calledDescription;
	}

	public void setCalledDescription(String calledDescription) {
		this.calledDescription = calledDescription;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getCallRuntime() {
		return callRuntime;
	}

	public void setCallRuntime(int calledRuntime) {
		this.callRuntime = calledRuntime;
	}

	public long getCallTime() {
		return callTime;
	}

	public void setCallTime(long calledTime) {
		this.callTime = calledTime;
	}

	public String getCallerNumber() {
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber) {
		this.callerNumber = callerNumber;
	}

	public String getCallerIMSI() {
		return callerIMSI;
	}

	public void setCallerIMSI(String callerIMSI) {
		this.callerIMSI = callerIMSI;
	}

	public String getCallerIMEI() {
		return callerIMEI;
	}

	public void setCallerIMEI(String callerIMEI) {
		this.callerIMEI = callerIMEI;
	}

	public String getCalledNumber() {
		return calledNumber;
	}

	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}

	public int getCalledType() {
		return calledType;
	}

	public void setCalledType(int calledType) {
		this.calledType = calledType;
	}

	public int getCalledID() {
		return calledID;
	}

	public void setCalledID(int calledID) {
		this.calledID = calledID;
	}

}
