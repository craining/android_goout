package com.c35.ptc.goout.response;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取顾问信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetConsultantInfoOfProjectResponse extends BaseResponse {

	private static final String TAG = "GetConsultantInfoOfProjectResponse";

	private Project projectPublisher;

	public Project getProjectPublisher() {
		return projectPublisher;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// TODO Json解析
		projectPublisher = JsonUtil.praseProjectPublisherConsultantInfo(response);
	}

}
