package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.SchoolMajorInfo;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取院校专业
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSchooMajorResponse extends BaseResponse {

	private static final String TAG = "GetSchooMajorResponse";
	private SchoolMajorInfo majorInfo;

	public SchoolMajorInfo getMajorInfo() {
		return majorInfo;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		majorInfo = JsonUtil.praseSchoolMajorInfo(response);
	}

}
