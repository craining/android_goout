package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取中介信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetIntermediaryInfoOfProjectResponse extends BaseResponse {

	private static final String TAG = "GetIntermediaryInfoResponse";

	private Project projectPublisher;

	public Project getProjectPublisher() {
		return projectPublisher;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		projectPublisher = JsonUtil.praseProjectPublisherIntermediaryInfo(response);

	}

}
