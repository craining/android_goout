package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 上传访问记录
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class UploadProjectVisitRecordResponse extends BaseResponse {

	private static final String TAG = "UploadProjectVisitRecordResponse";
	private boolean uploadSuccess = false;// 上传成功与否

	public boolean isUploadSuccess() {
		return uploadSuccess;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		uploadSuccess = JsonUtil.parseUploadResult(response);
	}

}
