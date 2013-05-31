package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.JsonUtil;


public class AccountMarkPhoneResponse extends BaseResponse {

	private static final String TAG = "AccountMarkPhoneResponse";

	private Boolean markSuccess = false;

 

	
	public Boolean getMarkSuccess() {
		return markSuccess;
	}



	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);
		markSuccess = JsonUtil.parseMarkPhoneResult(response);
	}
}
