package com.c35.ptc.goout.util;

import java.io.File;

import com.c35.ptc.goout.GoOutConstants;

/**
 * 初始化
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class InitUtil {

	/**
	 * 初始化存储卡目录
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static void InitFileDir() {
		File storeImages = new File(GoOutConstants.IMAGES_STORE_SDCARD_PATH);
		if (!storeImages.exists()) {
			storeImages.mkdirs();
		}
	}
}
