package com.c35.ptc.goout;

/**
 * 常量
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class GoOutConstants {

	public static final String IMAGES_STORE_SDCARD_PATH = "/mnt/sdcard/goout/images";// 存储图片的临时目录(若无存储卡，则为系统cache目录)
	// public static final String INNER_PATH = "/data/data/com.c35.ptc.goout/files";
	public static final int SCHOOL_SORTED_TYPE_RANK = 1;// 按名次排序
	public static final int SCHOOL_SORTED_TYPE_LETTER = 2;// 按首字母排序
	// 缺省时过滤的国家
	public static final String DEFAULT_COUNTRY_OF_SCHOOLLIST = "美国";
	public static final String DEFAULT_COUNTRY_OF_PROJECTLIST = "美国";
	// 学位名称，过滤、请求服务器数据时会用到
	public static final String DEGREE_HIGHSCHOOL = "高中";
	public static final String DEGREE_BACHELOR = "本科";
	public static final String DEGREE_MASTER = "硕士";
	public static final String DEGREE_DOCTOR = "博士";
	public static final String DEGREE_PREPARATORY = "预科";
	// 发布者类型
	public static final int TYPE_INTERMEDIARY = 1;// 中介
	public static final int TYPE_CONSULTANT = 2;// 顾问

	/**
	 * 服务器url
	 */

	public static final String GOOUT_SERVER_API = "http://api.nengchuqu.com";// 接口请求的服务器Url或ip
	// public static final String GOOUT_SERVER_API = "http://192.168.1.214/GoOutApi";// 接口请求的服务器Url或ip

	public static final String GOOUT_SERVER_SCHOOL_LOGO = "http://school.nengchuqu.com/logo/";// 院校logo目录
	public static final String GOOUT_SERVER_SCHOOL_PHOTO = "http://school.nengchuqu.com/photo/";// 院校照片目录
	public static final String GOOUT_SERVER_PROJECT_PHOTO = "http://images.nengchuqu.com/project/";// 项目图片目录
	public static final String GOOUT_SERVER_PUBLISHER_LOGO = "http://images.nengchuqu.com/adviser/avatar/s/";// 发布者logo
	// 项目图片目录

	// public static final String PHONE_NUMBER_SERVER = "15555215556";// 发送短信验证码的号码
	public static final String PHONE_NUMBER_SERVER = "15311307035";// 发送短信验证码的号码

	public static final int ERROR_CODE_MOBILE_ALREADY_BIND = 1005;// 手机号已经被注册绑定
	public static final int ERROR_CODE_THIRD_ALREADY_REGISTER = 1006;// 第三方账号已经被注册
	public static final int ERROR_CODE_CODE_ERROR = 1007;// 验证码错误

	public static final String official_QQWeiBo = " @nengchuqu ";
	public static final String official_SinaBo = " @能出去V ";
	public static final String official_RenRen = "";
	public static final int SHARE_MAX_LENGTH = 128;// 140-12

	/**
	 * QQ Weibo 能出去 用户名：pub@nengchuqu.com
	 * 
	 * sina weibo： 能出去V 用户名：pub@nengchuqu.com
	 */
}
