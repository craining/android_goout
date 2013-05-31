package com.c35.ptc.goout.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.util.ConfigUtil;

/**
 * 同步请求action及参数
 * @Description:
 * @author:huangyx2  
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-4-18
 */
public class SyncFavoriteProRequest {

	/** 收藏项目同步action */
	private static  String commond = "/u_syncProjects.action";
	
	public static String getCommond(){
		return GoOutConstants.GOOUT_SERVER_API + commond;
	}
	
	public static List<NameValuePair> initParams(Collection<Integer> delets, Collection<Integer> keeps){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uid", GoOutGlobal.getInstance().getAccount().getUid()));
		params.add(new BasicNameValuePair("lastTime", ConfigUtil.getInstance().getLastTimeForFp() + ""));
		if(delets != null && !delets.isEmpty()){
			StringBuffer d = new StringBuffer();
			int i = 0;
			for(int pid : delets){
				if(i>0){
					d.append(",");
				}
				d.append(pid);
				i++;
			}
			params.add(new BasicNameValuePair("delete", d.toString()));
		}
		if(keeps != null && !keeps.isEmpty()){
			StringBuffer k = new StringBuffer();
			int i = 0;
			for(int pid : keeps){
				if(i>0){
					k.append(",");
				}
				k.append(pid);
				i++;
			}
			params.add(new BasicNameValuePair("keep", k.toString()));
		}
		return params;
	}
}
