package com.c35.ptc.goout.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;

import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.db.TableSchool;
import com.c35.ptc.goout.interfaces.SyncFavoriteSchoolListener;
import com.c35.ptc.goout.response.SyncFavoriteSchoolResponse;
import com.c35.ptc.goout.util.ConfigUtil;
import com.c35.ptc.goout.util.ServerUtil;

/**
 * 收藏院校同步逻辑处理
 * 
 * @Description:
 * @author:huangyx2
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-18
 */
public class SyncFavoriteSchoolLogic {

	private static boolean syncing = false;

	private static class InstanceHelper {

		static SyncFavoriteSchoolLogic instance = new SyncFavoriteSchoolLogic();
	}

	public static SyncFavoriteSchoolLogic getInstance() {
		return InstanceHelper.instance;
	}

	private static List<SyncFavoriteSchoolListener> listeners = new ArrayList<SyncFavoriteSchoolListener>();

	public void addListener(SyncFavoriteSchoolListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SyncFavoriteSchoolListener listener) {
		listeners.remove(listener);
	}

	/**
	 * 同步
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public void sync() {
		if (syncing) {
			// 正在同步，跳出
			fireListener(SyncFavoriteSchoolListener.SYNC_ING);
			return;
		}
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 执行后台前，可以在此处通知UI
				syncing = true;
				fireListener(SyncFavoriteSchoolListener.SYNC_START);
			}

			@Override
			protected Integer doInBackground(Void... params) {
				// uid
				Account account = GoOutGlobal.getInstance().getAccount();
				if (account == null) {
					return SyncFavoriteSchoolListener.SYNC_NOT_LOGIN;
				}
				// 获取需要同步的数据，取消收藏的或者收藏的
				TableSchool ts = new TableSchool(GoOutGlobal.getInstance());
				ts.open();
				// 收藏的项目ID集合
				Set<Integer> favoritedIds = ts.getFavoritedPids();
				// 最近一次同步后服务器上收藏的项目
				Set<Integer> records = ts.getProFavRecords();
				Set<Integer> keeps = new HashSet<Integer>();
				Set<Integer> deletes = new HashSet<Integer>();
				// 有收藏的情况下，需要将收藏的项目ID提供给服务器
				if (favoritedIds != null && !favoritedIds.isEmpty()) {
					if (records != null && !records.isEmpty()) { // 收藏记录不为空
						for (int pid : favoritedIds) {
							if (!records.contains(pid)) { // 服务器收藏记录不包含已收藏的，需要告诉服务器新收藏
								keeps.add(pid);
							}
						}
						for (int pid : records) {
							if (!favoritedIds.contains(pid)) { // 收藏里已经没有服务器的记录，说明要删除
								deletes.add(pid);
							}
						}
					} else {
						keeps.addAll(favoritedIds);
					}
				} else {
					// 没有收藏的项目
					if (records != null && !records.isEmpty()) { // 服务器收藏记录不为空
						// 此情况说明收藏过的已经取消收藏了，需要将取消收藏的告知服务器
						deletes.addAll(records);
					}
				}
				ts.close();
				// 连接服务器，进行同步
				SyncFavoriteSchoolResponse response = ServerUtil.getInstance().syncFavoriteSchools(deletes, keeps);
				if (!response.isResult()) {
					return SyncFavoriteSchoolListener.SYNC_FAIL;
				}
				ts.open();
				boolean cResult = ts.syncSaveOrUpd(response.getKeepSchools(), response.getDeletedSchools());
				ts.close();
				if (cResult) {
					ConfigUtil.getInstance().setLastTimeForFp(response.getLastTime());
					return SyncFavoriteSchoolListener.SYNC_SUCC;
				}
				return SyncFavoriteSchoolListener.SYNC_FAIL;
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

	private void fireListener(int result) {
		for (SyncFavoriteSchoolListener lis : listeners) {
			lis.syncResult(result);
		}
	}
}
