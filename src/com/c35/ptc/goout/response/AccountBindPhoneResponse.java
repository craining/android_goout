package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.JsonUtil;

/***
 * 绑定手机号
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-19
 */
public class AccountBindPhoneResponse extends BaseResponse {

	private static final String TAG = "AccountBindPhoneResponse";

	private Account account;
	private Boolean bindSuccess = false;
	private int errorCode;

	
	public int getErrorCode() {
		return errorCode;
	}

	public Boolean getBindSuccess() {
		return bindSuccess;
	}

//	public Account getAccount() {
//		return account;
//	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);
		account = JsonUtil.parseBindPhoneResult(response);
		bindSuccess = account.isBindPhoneSuccess();
		errorCode = account.getErrorCode();
	}
}
