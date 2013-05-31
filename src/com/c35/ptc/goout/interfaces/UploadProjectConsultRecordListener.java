package com.c35.ptc.goout.interfaces;

/**
 * 上传联系记录的回调
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class UploadProjectConsultRecordListener {

	/**
	 * 上传失败，根据通话的开始时间判断是哪条记录
	 * 
	 * @Description:
	 * @param callStartTime
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	public void uploadOneConsultRecorkFailed(long callStartTime) {

	}

	/**
	 * 上传成功，根据通话的开始时间判断是哪条记录
	 * 
	 * @Description:
	 * @param callStartTime
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	public void uploadOneConsultRecorkFinished(long callStartTime) {

	}

}
