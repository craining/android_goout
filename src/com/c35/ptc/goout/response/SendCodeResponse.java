package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Code;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 请求验证码响应
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-17
 */

public class SendCodeResponse extends BaseResponse {

	private static final String TAG = "SendCodeResponse";

	private boolean requestOk = false;
	private int errorCode = -1;

	
	public int getErrorCode() {
		return errorCode;
	}

	private Code codeReturn;

	public Code getCodeReturn() {
		return codeReturn;
	}

	public boolean isRequestOk() {
		return requestOk;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);
		codeReturn = JsonUtil.parseSendCodeRequestResult(response);
		requestOk = codeReturn.isEffective();
		if (!requestOk) {
			errorCode = codeReturn.getErrorCode();
		}
		// 
	}
}
