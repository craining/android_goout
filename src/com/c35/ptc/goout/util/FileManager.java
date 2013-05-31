package com.c35.ptc.goout.util;

import android.content.Context;

import com.c35.ptc.goout.GoOutConstants;

/**
 * 文件缓存目录管理
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-12
 */
public class FileManager {

	/**
	 * 获得图片缓存目录
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public static String getSaveFilePath(Context con) {
		if (PhoneUtil.existSDcard()) {
			return GoOutConstants.IMAGES_STORE_SDCARD_PATH + "/";
		} else {
			return con.getCacheDir().toString() + "/";
		}
	}
}
