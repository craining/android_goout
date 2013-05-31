package com.c35.ptc.goout.request;

/**
 * 上传一条联系记录(拨打电话)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class UploadProjectConsultRecordRequest {

	private String command = "/project_newConsultRecord.action";

	private int projectId;// true int 项目id
	private int callRuntime;// false int 联系时长
	private String callTime;// false string 浏览时间（空为服务器时间）
	private String callerNumber;// true string 主叫电话
	private String callerIMSI;// true string 主叫IMSI
	private String callerIMEI;// true strimg 主叫IMEI
	private String calledNumber;// true string 被叫号码
	private int calledType;// true int 被叫者类型(顾问1、中介0)
	private int calledID;// false int 被叫者ID
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
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
	
	public void setCallRuntime(int callRuntime) {
		this.callRuntime = callRuntime;
	}
	
	public String getCallTime() {
		return callTime;
	}
	
	public void setCallTime(String callTime) {
		this.callTime = callTime;
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
