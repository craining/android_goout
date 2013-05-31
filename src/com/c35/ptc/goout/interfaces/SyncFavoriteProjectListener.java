package com.c35.ptc.goout.interfaces;


public interface SyncFavoriteProjectListener {
	
	/**
	 * 同步成功
	 */
	public static final int SYNC_SUCC = 0;
	/**
	 * 同步失败：未登录
	 */
	public static final int SYNC_NOT_LOGIN = 1;
	/**
	 * 同步失败
	 */
	public static final int SYNC_FAIL = 2;
	/**
	 * 同步中
	 */
	public static final int SYNC_ING = 3;
	/**
	 * 开始同步
	 */
	public static final int SYNC_START = 4;
	
	/**
	 * 同步结果
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-19
	 */
	public void syncResult(int result);
	
}
