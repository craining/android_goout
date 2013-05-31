package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.JsonUtil;


public class AccountRegResponse extends BaseResponse {

	private static final String TAG = "RegAccountResponse";

	private Account account;
	private Boolean regSuccess = false;

	public Boolean isRegSuccess() {
		return regSuccess;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);
		account = JsonUtil.parseRegAccountRequestResult(response);
		regSuccess = account.isRegisterOk();
	}
}
