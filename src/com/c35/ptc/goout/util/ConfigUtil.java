package com.c35.ptc.goout.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.c35.ptc.goout.GoOutGlobal;

/**
 * 
 * @Description: 存储PRM系统相关设置信息
 * @author:黄永兴(huangyx2@35.cn)
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2012-2-23
 */
public class ConfigUtil {

	// 项目最后同步时间
	private static final String LAST_TIME_F_P = "LAST_TIME_F_P";
	// 院校最后同步时间
	private static final String LAST_TIME_F_S = "LAST_TIME_F_S";
	
	private static SharedPreferences mShared = null;
	
	private ConfigUtil() {
		mShared = PreferenceManager.getDefaultSharedPreferences(GoOutGlobal.getInstance());
	}
	
	private static ConfigUtil instance;
	
	public static ConfigUtil getInstance(){
		if(instance == null){
			instance = new ConfigUtil();
		}
		return instance;
	}
	/**
	 * 存储项目最后同步时间
	 * @Description:
	 * @param lastTime
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public void setLastTimeForFp(long value){
		putLong(LAST_TIME_F_P, value);
	}
	/**
	 * 获取项目最后同步时间
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public long getLastTimeForFp(){
		return getLong(LAST_TIME_F_P, 0);
	}

	/**
	 * 存储院校最后同步时间
	 * @Description:
	 * @param lastTime
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public void setLastTimeForFs(long value){
		putLong(LAST_TIME_F_S, value);
	}
	/**
	 * 获取院校最后同步时间
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public long getLastTimeForFs(){
		return getLong(LAST_TIME_F_S, 0);
	}
	
	/**
	 * 清除
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public void clear(){
		mShared.edit().clear().commit();
	}

	private void putLong(String key, long value){
		mShared.edit().putLong(key, value).commit();
	}

	private long getLong(String key, long defValue){
		return mShared.getLong(key, defValue);
	}
	
	@SuppressWarnings("unused")
	private void putInt(String key, int value){
		mShared.edit().putInt(key, value).commit();
	}

	@SuppressWarnings("unused")
	private int getInt(String key, int defValue){
		return mShared.getInt(key, defValue);
	}

	@SuppressWarnings("unused")
	private void putString(String key, String value){
		mShared.edit().putString(key, value).commit();
	}

	@SuppressWarnings("unused")
	private String getString(String key, String defValue){
		return mShared.getString(key, defValue);
	}

	@SuppressWarnings("unused")
	private void putBool(String key, boolean value){
		mShared.edit().putBoolean(key, value).commit();
	}

	@SuppressWarnings("unused")
	private boolean getBool(String key, boolean defValue){
		return mShared.getBoolean(key, defValue);
	}

	@SuppressWarnings("unused")
	private void putFloat(String key, float value){
		mShared.edit().putFloat(key, value).commit();
	}

	@SuppressWarnings("unused")
	private float getFloat(String key, float defValue){
		return mShared.getFloat(key, defValue);
	}
}
