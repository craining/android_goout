package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 第三方账户登录反馈
 * @Description:
 * @author: zhuanggy
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-4-19
 */
public class AccountThirdLoginResponse extends BaseResponse {

	private static final String TAG = "AccountThirdLoginResponse";

	private Account account;
	private Boolean loginSuccess = false;


	
	public Boolean getLoginSuccess() {
		return loginSuccess;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);
		account = JsonUtil.parseLoginThirdAccountRequestResult(response);
		loginSuccess = account.isLoginSuccess();
	}
}
