package com.c35.ptc.goout.listviewloader;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.util.FileManager;

import android.content.Context;

/**
 * 获得图片缓存目录
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright 网上借鉴
 * @Date:2013-3-12
 */
public class FileCache extends AbstractFileCache {

	public FileCache(Context context) {
		super(context);

	}

	@Override
	public String getSavePath(String url, Context con) {
		String filename = String.valueOf(url.hashCode());
		// GoOutDebug.e("FileCache", "cache dir = " + getCacheDir(con) + filename);
		return getCacheDir(con) + filename;
	}

	@Override
	public String getCacheDir(Context con) {

		return FileManager.getSaveFilePath(con);
	}

}
