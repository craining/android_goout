package com.c35.ptc.goout.response;

import java.util.ArrayList;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.util.JsonUtil;

/**
 * 获得国家列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GetSelectCountryListResponse extends BaseResponse {

	private static final String TAG = "GetSelectCountryListResponse";

	private ArrayList<Country> listCountry;

	public ArrayList<Country> getListCountry() {
		return listCountry;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);

		GoOutDebug.i(TAG, "result: " + response);

		// Json解析
		listCountry = new ArrayList<Country>();
		listCountry = JsonUtil.parseCountryList(response);
	}

}
