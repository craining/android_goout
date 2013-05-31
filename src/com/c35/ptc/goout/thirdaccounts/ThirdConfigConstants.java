package com.c35.ptc.goout.thirdaccounts;

/**
 * 暂时保存各个账号的应用注册信息(后期考虑安全性起见可能需要从服务器获取)
 * 
 * 
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-17
 */
public class ThirdConfigConstants {

	public static final int PLATFORM_SINA = 1;
	public static final int PLATFORM_RENREN = 2;
	public static final int PLATFORM_QQWEIBO = 3;
	//授权时的操作
	public static final int THIRD_BIND = 0;// 第三方绑定
	public static final int THIRD_LOGIN = 1;// 第三方登录
	public static final int THIRD_REG = 2; // 第三方注册

	// 新浪
	// （测试用）
//	 public static final String SINA_APP_KEY = "2471322837";
//	 public static final String SINA_APP_SECRET = "93e8d77c6f432effbd45340b35e564cf";
//	 public static final String SINA_OAUTH_CALLBACK = "http://craining.blog.163.com/";

	public static final String SINA_APP_KEY = "1289570252";
	public static final String SINA_APP_SECRET = "9c26bd8d6a3d4a60e860961550ae1cb1";
	public static final String SINA_OAUTH_CALLBACK = "http://api.nengchuqu.com";
	public static final String Sina_Authorize2 = "https://api.weibo.com/oauth2/authorize?client_id=" + SINA_APP_KEY + "" + "&response_type=code&redirect_uri=" + SINA_OAUTH_CALLBACK;

	// 人人
	// （测试用）
	// public static final String RENREN_APP_ID = "231482";
	// public static final String RENREN_API_KEY = "2ad4cfc7bbf34f39834f8c1c6d1da1c6";
	// public static final String RENREN_SECRET_KEY = "98b41549635d41d09a76dade091db8f6";
	// public static final String RENREN_OAUTH_CALLBACK = "http://graph.renren.com/oauth/login_success.html";

	public static final String RENREN_APP_ID = "232482";
	public static final String RENREN_API_KEY = "a4aaa8138e994c3180ce0f8d1a6980b0";
	public static final String RENREN_SECRET_KEY = "9644f638bfce4e3eaa2156b13afdfc15";
	public static final String RENREN_OAUTH_CALLBACK = "http://api.nengchuqu.com";
	/**
	 * 需要加权限，见: http://wiki.dev.renren.com/wiki/%E6%9D%83%E9%99%90%E5%88%97%E8%A1%A8
	 */
	public static final String Renren_Authorize2 = "https://graph.renren.com/oauth/authorize?client_id=" + RENREN_APP_ID + "" + "&response_type=code&redirect_uri=" + RENREN_OAUTH_CALLBACK + "&scope=" + "publish_share,status_update";//
	public static final String Renren_Access_Token = "https://graph.renren.com/oauth/token?";
	// QQ微博
	// （测试用）
	// public static final String QQWEIBO_API_KEY = "801339872";
	// public static final String QQWEIBO_SECRET_KEY = "ef4f11cfd127f72cd250f6db2b16f470";
	// public static final String QQWEIBO_OAUTH_CALLBACK = "http://craining.blog.163.com/";

	public static final String QQWEIBO_API_KEY = "100703961";
	public static final String QQWEIBO_SECRET_KEY = "1c6b905d05a08d18f5f6c3eefa876dd0";
	public static final String QQWEIBO_OAUTH_CALLBACK = "http://api.nengchuqu.com";
	public static final String QQWEIBO_Authorize_2 = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=" + QQWEIBO_API_KEY + "" + "&response_type=code&redirect_uri=" + QQWEIBO_OAUTH_CALLBACK;
}
