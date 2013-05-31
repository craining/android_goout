package com.c35.ptc.goout.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.c35.ptc.goout.R;

/**
 * 字符串处理
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-27
 */
public class StringUtil {

	/**
	 * 字符串为空时的处理
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-27
	 */
	public static String getString(String str) {
		if (str == null || str.equals("null")) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * 判断是否为空
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-27
	 */
	public static boolean isNull(String str) {
		if (str == null || str.equals("") || str.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 现在号码段分配如下：
	 * 
	 * 移动： 139 138 137 136 135 134 147 150 151 152 157 158 159 182 183 187 188
	 * 
	 * 联通： 130 131 132 155 156 185 186 145
	 * 
	 * 电信： 133 153 180 181 189
	 * 
	 * 参考：http://www.jihaoba.com/tools/?com=haoduan
	 * 
	 * @Description:
	 * @param number
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	public static boolean isPhoneNumberFormat(String number) {

		Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

}
