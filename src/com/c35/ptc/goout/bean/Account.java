package com.c35.ptc.goout.bean;

/**
 * 账号
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-17
 */
public class Account {

	public static final String TYPE_SINA = "sina";
	public static final String TYPE_RENREN = "renren";
	public static final String TYPE_QQWEIBO = "tencent";

	public static final int ACCOUNT_IS_LOGIN = 1;
	public static final int ACCOUNT_NOT_LOGIN = 0;

	private int localId;
	private String accountType = "";
	private String accountName;// 账户名（昵称）
	private String uid; // string（账户id）
	private String logo;// （账户logo） url
	private String accessToken;
	private String refreshToken;
	private int isLogin = -1;// 是否是正在登录的账户
	private long requestTime;// 申请token的时间 long
	private String expiresIn;// 申请token时剩余的秒数 long (qq微博使用)
	private String bindMobile;// 绑定手机号
	private String pwd;// 密码

	private String thirdId;// 第三方账户的id

	private boolean registerOk;// 注册成功，true则accountUid不为空
	private boolean loginSuccess;// 登录成功与否，
	private boolean bindPhoneSuccess;// 绑定手机成功与否
//	private boolean markSuccess;// 验证码确定成功
//
//	
//	public boolean isMarkSuccess() {
//		return markSuccess;
//	}
//
//	
//	public void setMarkSuccess(boolean markSuccess) {
//		this.markSuccess = markSuccess;
//	}

	public boolean isBindPhoneSuccess() {
		return bindPhoneSuccess;
	}

	public void setBindPhoneSuccess(boolean bindPhoneSuccess) {
		this.bindPhoneSuccess = bindPhoneSuccess;
	}

	private int errorCode;// 注册或登录时，返回的错误码

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String serverId) {
		this.thirdId = serverId;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public boolean isRegisterOk() {
		return registerOk;
	}

	public void setRegisterOk(boolean registerOk) {
		this.registerOk = registerOk;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public int getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(int isLogin) {
		this.isLogin = isLogin;
	}

	public int getLocalId() {
		return localId;
	}

	public void setLocalId(int localId) {
		this.localId = localId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

}
