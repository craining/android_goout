package com.c35.ptc.goout.bean;

/**
 * 收藏项目同步记录（只记录同步后还属于收藏状态的， 相当于最后一次同步后服务器上存在的数据）
 * @Description:
 * @author:huangyx2  
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-4-18
 */
public class SyncProFavRecord {

	private int id;
	
	private int pid;

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public int getPid() {
		return pid;
	}

	
	public void setPid(int pid) {
		this.pid = pid;
	}
}
