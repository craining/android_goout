package com.c35.ptc.goout.request;

/**
 * 上传一条访问记录
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-7
 */
public class UploadProjectVisitRecordRequest {

	private String command = "/project_newVisitRecord.action";

	private int projectId;// 项目id,必须
	private String readerId;// 陌生人（0）或者用户（注册id); 不必须
	private String readerTime;// 浏览时间（空为服务器时间）;不必须
	private String readerInfo;// 浏览者信息（IP / IMEI / IMSI）;必须

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getReaderId() {
		return readerId;
	}

	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}

	public String getReaderTime() {
		return readerTime;
	}

	public void setReaderTime(String readerTime) {
		this.readerTime = readerTime;
	}

	public String getReaderInfo() {
		return readerInfo;
	}

	public void setReaderInfo(String readerInfo) {
		this.readerInfo = readerInfo;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

}
