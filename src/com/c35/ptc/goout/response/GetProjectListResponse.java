package com.c35.ptc.goout.response;

import java.util.ArrayList;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获得项目列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetProjectListResponse extends BaseResponse {

	private static final String TAG = "GetProjectListResponse";

	private ArrayList<Project> listProjects;// 项目列表

	public ArrayList<Project> getListProjects() {
		return listProjects;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		listProjects = new ArrayList<Project>();

		// Json解析获得项目列表
		listProjects = JsonUtil.parseProjectList(response);

	}
}
