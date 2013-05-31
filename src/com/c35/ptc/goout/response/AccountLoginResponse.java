package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.JsonUtil;


public class AccountLoginResponse extends BaseResponse {

	private static final String TAG = "AccountLoginResponse";

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
		account = JsonUtil.parseLoginAccountRequestResult(response);
		loginSuccess = account.isLoginSuccess();
	}
}
