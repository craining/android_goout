package com.c35.ptc.goout.response;

import java.util.ArrayList;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获得专业列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSelectMajorListResponse extends BaseResponse {

	private static final String TAG = "GetSelectMajorListResponse";

	private ArrayList<Major> majorList;

	public ArrayList<Major> getMajorList() {
		return majorList;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		this.majorList = new ArrayList<Major>();
		this.majorList = JsonUtil.parseMajorList(response);

	}

}
