package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.SchoolDegreeInfo;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取院校学位信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSchoolDegreeDetailResponse extends BaseResponse {


	private static final String TAG = "GetSchoolDegreeDetailResponse";

	private SchoolDegreeInfo degreeInfo;

	
	public SchoolDegreeInfo getDegreeInfo() {
		return degreeInfo;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		degreeInfo = JsonUtil.praseSchoolDegreeInfo(response);
	}


}
