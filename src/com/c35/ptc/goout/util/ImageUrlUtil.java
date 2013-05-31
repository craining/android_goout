package com.c35.ptc.goout.util;

import android.util.Base64;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;

public class ImageUrlUtil {

	public static final int PHOTO_TYPE_S = 0;// 小图
	public static final int PHOTO_TYPE_M = 1;// 中图
	public static final int PHOTO_TYPE_H = 2;// 大图
	public static final int PHOTO_TYPE_N = 3;// 原图

	/**
	 * 获得院校原图
	 * 
	 * @Description:
	 * @param imgName
	 * @param countryName
	 * @param schoolId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static String getSchoolPhotoUrl(String imgName, String countryName, int schoolId, int type) {
		if (GoOutTest.test) {
			return imgName;// 测试 url
		} else {
			// school.shichenwei.cn/base64(国家)/院校ID/图片名称
			StringBuffer sb = new StringBuffer();
			switch (type) {
			case PHOTO_TYPE_S:
				sb.append(GoOutConstants.GOOUT_SERVER_SCHOOL_PHOTO).append(Base64.encodeToString((countryName).getBytes(), Base64.NO_WRAP)).append("/").append(schoolId + "").append("/s").append(imgName);
				break;
			case PHOTO_TYPE_M:
				sb.append(GoOutConstants.GOOUT_SERVER_SCHOOL_PHOTO).append(Base64.encodeToString((countryName).getBytes(), Base64.NO_WRAP)).append("/").append(schoolId + "").append("/m").append(imgName);
				break;
			case PHOTO_TYPE_H:
				sb.append(GoOutConstants.GOOUT_SERVER_SCHOOL_PHOTO).append(Base64.encodeToString((countryName).getBytes(), Base64.NO_WRAP)).append("/").append(schoolId + "").append("/h").append(imgName);
				break;
			case PHOTO_TYPE_N:
				sb.append(GoOutConstants.GOOUT_SERVER_SCHOOL_PHOTO).append(Base64.encodeToString((countryName).getBytes(), Base64.NO_WRAP)).append("/").append(schoolId + "").append("/").append(imgName);
				break;
			default:
				break;
			}

			return sb.toString();
		}
	}

	/**
	 * 获得院校logo
	 * 
	 * @Description:
	 * @param logoName
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static String getSchoolLogoUrl(String logoName) {
		if (GoOutTest.test) {
			return logoName;// 测试 url
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(GoOutConstants.GOOUT_SERVER_SCHOOL_LOGO).append(logoName);
			return sb.toString();
		}
	}

	/**
	 * 获得发布者,中介，顾问logo
	 * 
	 * @Description:
	 * @param logoName
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static String getPublisherLogoUrl(String logoName) {
		if (GoOutTest.test) {
			return logoName;// 测试 url
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(GoOutConstants.GOOUT_SERVER_PUBLISHER_LOGO).append(logoName);
			return sb.toString();
		}
	}

	/**
	 * 获得项目列表logo的完整url
	 * 
	 * @Description:
	 * @param logoName
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-28
	 */
	public static String getProjectPhotoUrl(String photoName, int projectId, int type) {
		if (GoOutTest.test) {
			return photoName;
		} else {
			StringBuffer sb = new StringBuffer();
			switch (type) {
			case PHOTO_TYPE_S:
				sb.append(GoOutConstants.GOOUT_SERVER_PROJECT_PHOTO).append(projectId + "").append("/s/").append(photoName);
				break;
			case PHOTO_TYPE_M:
				sb.append(GoOutConstants.GOOUT_SERVER_PROJECT_PHOTO).append(projectId + "").append("/m/").append(photoName);
				break;
			case PHOTO_TYPE_H:
				sb.append(GoOutConstants.GOOUT_SERVER_PROJECT_PHOTO).append(projectId + "").append("/h/").append(photoName);
				break;
			case PHOTO_TYPE_N:
				sb.append(GoOutConstants.GOOUT_SERVER_PROJECT_PHOTO).append(projectId + "").append("/o/").append(photoName);
				break;
			default:
				break;
			}
			return sb.toString();
		}
	}
}
