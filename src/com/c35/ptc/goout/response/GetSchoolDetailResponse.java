package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取院校信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSchoolDetailResponse extends BaseResponse {

	private static final String TAG = "GetSchoolDetailResponse";

	private School schoolInfo;

	
	public School getSchoolInfo() {
		return schoolInfo;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		schoolInfo = JsonUtil.praseSchoolInfo(response);
	}

}
