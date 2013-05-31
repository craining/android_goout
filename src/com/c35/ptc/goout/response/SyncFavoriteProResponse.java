package com.c35.ptc.goout.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.util.JsonUtil;

public class SyncFavoriteProResponse extends BaseResponse {

	private boolean result;

	private long lastTime;

	private List<Integer> deletedProjects;

	private List<Project> keepProjects;

	public boolean isResult() {
		return result;
	}

	public long getLastTime() {
		return lastTime;
	}

	public List<Integer> getDeletedProjects() {
		return deletedProjects;
	}

	public List<Project> getKeepProjects() {
		return keepProjects;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		GoOutDebug.i("SyncFavoriteProResponse", "result: " + response);
		try {
			JSONObject obj = new JSONObject(response);
			keepProjects = JsonUtil.parseProjectList(obj.getJSONArray("keepProjects"));
			result = obj.getBoolean("result");
			lastTime = obj.getLong("lastTime");
			JSONArray deleIds = obj.getJSONArray("deletedProjects");
			if (deleIds != null) {
				deletedProjects = new ArrayList<Integer>();
				int len = deleIds.length();
				for (int i = 0; i < len; i++) {
					deletedProjects.add(deleIds.getInt(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
