package com.c35.ptc.goout.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;

import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.db.TableProject;
import com.c35.ptc.goout.interfaces.SyncFavoriteProjectListener;
import com.c35.ptc.goout.response.SyncFavoriteProResponse;
import com.c35.ptc.goout.util.ConfigUtil;
import com.c35.ptc.goout.util.ServerUtil;

/**
 * 收藏项目同步逻辑处理
 * @Description:
 * @author:huangyx2  
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-4-18
 */
public class SyncFavoriteProjectLogic {

	private static boolean syncing = false;
	
	private static class InstanceHelper{
		static SyncFavoriteProjectLogic instance = new SyncFavoriteProjectLogic();
	}
	
	public static SyncFavoriteProjectLogic getInstance(){
		return InstanceHelper.instance;
	}
	
	private static List<SyncFavoriteProjectListener> listeners = new ArrayList<SyncFavoriteProjectListener>();
	
	public void addListener(SyncFavoriteProjectListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(SyncFavoriteProjectListener listener){
		listeners.remove(listener);
	}
	/**
	 * 同步
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public void sync(){
		if(syncing){
			// 正在同步，跳出
			fireListener(SyncFavoriteProjectListener.SYNC_ING);
			return;
		}
		new AsyncTask<Void, Void, Integer>(){

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 执行后台前，可以在此处通知UI
				syncing = true;
				fireListener(SyncFavoriteProjectListener.SYNC_START);
			}
			
			@Override
			protected Integer doInBackground(Void... params) {
				// uid
				Account account = GoOutGlobal.getInstance().getAccount();
				if(account == null){
					return SyncFavoriteProjectListener.SYNC_NOT_LOGIN;
				}
				// 获取需要同步的数据，取消收藏的或者收藏的
				TableProject tp = new TableProject(GoOutGlobal.getInstance());
				tp.open();
				// 收藏的项目ID集合
				Set<Integer> favoritedIds = tp.getFavoritedPids();
				// 最近一次同步后服务器上收藏的项目
				Set<Integer> records = tp.getProFavRecords();
				Set<Integer> keeps = new HashSet<Integer>();
				Set<Integer> deletes = new HashSet<Integer>();
				// 有收藏的情况下，需要将收藏的项目ID提供给服务器
				if(favoritedIds != null && !favoritedIds.isEmpty()){
					if(records != null && !records.isEmpty()){ // 收藏记录不为空
						for(int pid : favoritedIds){
							if(!records.contains(pid)){ // 服务器收藏记录不包含已收藏的，需要告诉服务器新收藏
								keeps.add(pid);
							}
						}
						for(int pid : records){
							if(!favoritedIds.contains(pid)){ // 收藏里已经没有服务器的记录，说明要删除
								deletes.add(pid);
							}
						}
					}else{
						keeps.addAll(favoritedIds);
					}
				}else{
					// 没有收藏的项目
					if(records != null && !records.isEmpty()){ // 服务器收藏记录不为空
						// 此情况说明收藏过的已经取消收藏了，需要将取消收藏的告知服务器
						deletes.addAll(records);
					}
				}
				tp.close();		
				// 连接服务器，进行同步
				SyncFavoriteProResponse response = ServerUtil.getInstance().syncFavoriteProjects(deletes, keeps);
				if(!response.isResult()){
					return SyncFavoriteProjectListener.SYNC_FAIL;
				}
				tp.open();
				boolean cResult = tp.syncSaveOrUpd(response.getKeepProjects(), response.getDeletedProjects());
				tp.close();
				if(cResult){
					ConfigUtil.getInstance().setLastTimeForFp(response.getLastTime());
					return SyncFavoriteProjectListener.SYNC_SUCC;
				}
				return SyncFavoriteProjectListener.SYNC_FAIL;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				syncing = false;
				// 执行完后台操作后，可以在此处通知UI
				fireListener(result);
			}
		}.execute();
	}
	
	private void fireListener(int result){
		for(SyncFavoriteProjectListener lis : listeners){
			lis.syncResult(result);
		}
	}
}
