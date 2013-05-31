package com.c35.ptc.goout.listviewloader;

import java.io.File;

import android.content.Context;

import com.c35.ptc.goout.GoOutDebug;

/**
 * 文件缓存目录相关的抽象类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright 网上借鉴
 * @Date:2013-3-12
 */
public abstract class AbstractFileCache {

	private String dirString;
	private Context con;

	public AbstractFileCache(Context context) {
		this.con = context;
		dirString = getCacheDir(context);
		boolean ret = FileHelper.createDirectory(dirString);
		GoOutDebug.e("AbstractFileCache", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}

	public File getFile(String url) {
		File f = new File(getSavePath(url, con));
		return f;
	}

	public abstract String getSavePath(String url, Context con);

	public abstract String getCacheDir(Context con);

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

}
