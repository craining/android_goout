package com.c35.ptc.goout.interfaces;

import android.content.Context;

/**
 * 去电状态监听器
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-19
 */
public class OutGoingCallListener {

	/**
	 * 去电
	 * 
	 * @Description:
	 * @param num
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void onCallOutGoing(String num) {

	}

	/**
	 * 空闲，挂断
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void onCallIdle(String num, long startTime, int callRunTime) {

	}

	/**
	 * 摘机
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void onCallOffHook(String num) {

	}

}
