package com.c35.ptc.goout.response;

import java.util.ArrayList;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获取院校列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSchoolListResponse extends BaseResponse {

	private static final String TAG = "GetSchoolListResponse";

	private ArrayList<School> listSchools;// json解析出的院校列表

	public ArrayList<School> getListSchools() {
		return listSchools;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		listSchools = new ArrayList<School>();
		listSchools = JsonUtil.parseSchoolList(response);
	}

}
