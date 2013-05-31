package com.c35.ptc.goout.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 和设备相关
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class PhoneUtil {

	/**
	 * 判断存储卡是否可用
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static boolean existSDcard() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	/**
	 * 获得手机的IMEI
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static String getPhoneImei(Context con) {
		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 获得手机号
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static String getPhoneSimNumber(Context con) {
		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * 获得手机的IMEI
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static String getPhoneImsi(Context con) {
		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	/**
	 * 获得手机设备信息
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public static String getDeviceInfo(Context con) {

		// TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();

		int version = 0;
		String manufacturer = "null";
		String model = "null";
		String device = "null";

		try {
			Class<android.os.Build.VERSION> build_version_class = android.os.Build.VERSION.class;
			// 取得 android 版本
//			java.lang.reflect.Field field;
//			field = build_version_class.getField("SDK_INT");
//			version = (Integer) field.get(new android.os.Build.VERSION());
//			sb.append("SDK_INT = " + version);

			Class<android.os.Build> build_class = android.os.Build.class;
//			// 取得牌子
//			java.lang.reflect.Field manu_field = build_class.getField("MANUFACTURER");
//			manufacturer = (String) manu_field.get(new android.os.Build());
//			sb.append("Manufacturer=" + manufacturer);
			// 取得型號
			java.lang.reflect.Field field2 = build_class.getField("MODEL");
			model = (String) field2.get(new android.os.Build());
			sb.append("MODEL=" + model);
//			// 模組號碼
//			java.lang.reflect.Field device_field = build_class.getField("DEVICE");
//			device = (String) device_field.get(new android.os.Build());
//			sb.append(" DEVICE = " + device);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	// public static boolean checkNetState(Context context) {
	// boolean netstate = false;
	// ConnectivityManager connectivity = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// if (connectivity != null) {
	// NetworkInfo[] info = connectivity.getAllNetworkInfo();
	// if (info != null) {
	// for (int i = 0; i < info.length; i++) {
	// if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	// netstate = true;
	// break;
	// }
	// }
	// }
	// }
	// return netstate;
	// }
}
