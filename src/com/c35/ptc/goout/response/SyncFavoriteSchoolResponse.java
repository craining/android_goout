package com.c35.ptc.goout.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.JsonUtil;

public class SyncFavoriteSchoolResponse extends BaseResponse {

	private boolean result;

	private long lastTime;

	private List<Integer> deletedSchools;

	private List<School> keepSchools;

	public List<Integer> getDeletedSchools() {
		return deletedSchools;
	}

	public List<School> getKeepSchools() {
		return keepSchools;
	}

	public boolean isResult() {
		return result;
	}

	public long getLastTime() {
		return lastTime;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		GoOutDebug.i("SyncFavoriteSchoolResponse", "result: " + response);
		try {
			JSONObject obj = new JSONObject(response);
			keepSchools = JsonUtil.praseSchoolList(obj.getJSONArray("keepSchools"));
			result = obj.getBoolean("result");
			lastTime = obj.getLong("lastTime");
			JSONArray deleIds = obj.getJSONArray("deletedSchools");
			if (deleIds != null) {
				deletedSchools = new ArrayList<Integer>();
				int len = deleIds.length();
				for (int i = 0; i < len; i++) {
					deletedSchools.add(deleIds.getInt(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
