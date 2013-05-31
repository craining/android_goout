package com.c35.ptc.goout;

import android.util.Log;

/**
 * 全局Log开关
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-26
 */
public class GoOutDebug {

	private static final boolean LOG = true;

	public static void d(String tag, String msg) {
		if (LOG)
			Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.d(tag, msg, tr);
	}

	public static void e(String tag, String msg) {
		if (LOG)
			Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.e(tag, msg, tr);
	}

	public static void v(String tag, String msg) {
		if (LOG)
			Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.v(tag, msg, tr);
	}

	public static void i(String tag, String msg) {
		if (LOG)
			Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.i(tag, msg, tr);
	}

	public static void p(String msg) {
		if (LOG)
			System.out.println(msg);
	}

}
