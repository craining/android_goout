package com.c35.ptc.goout.bean;

/**
 * 最近浏览记录
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-8
 */
public class RecentlyVisit {

	private int id;// 本地数据库里的编号
	private int projectId;// 项目id,必须
	private String readerId;// 陌生人（0）或者用户（注册id); 不必须
	private long readerTime;// 浏览时间（空为服务器时间）;不必须
	private String readerInfoIp;// 浏览者IP
	private String readerInfoImei;// 浏览者imei
	private String readerInfoImsi;// 浏览者imsi
	private int uploadState;// 上传状态

	public int getUploadState() {
		return uploadState;
	}

	public void setUploadState(int uploadState) {
		this.uploadState = uploadState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public long getReaderTime() {
		return readerTime;
	}

	public void setReaderTime(long readerTime) {
		this.readerTime = readerTime;
	}

	public String getReaderInfoIp() {
		return readerInfoIp;
	}

	public void setReaderInfoIp(String readerInfoIp) {
		this.readerInfoIp = readerInfoIp;
	}

	public String getReaderInfoImei() {
		return readerInfoImei;
	}

	public void setReaderInfoImei(String readerInfoImei) {
		this.readerInfoImei = readerInfoImei;
	}

	public String getReaderInfoImsi() {
		return readerInfoImsi;
	}

	public void setReaderInfoImsi(String readerInfoImsi) {
		this.readerInfoImsi = readerInfoImsi;
	}

}
