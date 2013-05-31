package com.c35.ptc.goout.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.RecentlyConsult;

/**
 * 联系记录的表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-8
 */
public class TableConsultRecord {

	private static final String TAG = "TableConsultRecord";

	private Context mContext = null;

	private static SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableConsultRecord(Context context) {
		this.mContext = context;
	}

	public void open() throws SQLException {
		mDatabaseHelper = new GooutDataBaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	public void close() {
		mDatabaseHelper.close();
	}

	/**
	 * 插入一条数据到联系记录表，（记得数据库的open close）
	 * 
	 * @Description:
	 * @param projectId
	 *            项目id
	 * @param callRunTime
	 *            联系时长
	 * @param callTime
	 *            本地浏览时刻
	 * @param callerNumber
	 *            主叫电话
	 * @param imsi
	 * @param imei
	 *            主叫imei(本地数据库是否可以不加此字段？？若程序做备份在恢复到其它手机上就有问题了)
	 * @param callNumber
	 *            被叫号码
	 * @param callType
	 *            被叫类型，int 1顾问，0中介
	 * @param callId
	 *            被叫者id
	 * @param isUpload
	 *            是否已上传，0为未上传，1为已上传
	 * @param calledName
	 *            被叫者名字
	 * @param calledLogoName
	 *            被叫者logo
	 * @param calledServerArea
	 *            被叫者服务区域
	 * @param calledGroup
	 *            被叫者所属部门
	 * 
	 * 
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */

	public long insertData(int projectId, int callRunTime, long callTime, String callerNumber, String imsi, String imei, String calledNumber, int calledType, int calledServerId, int isUpload, String calledName, String calledLogoName, String calledServerArea, String calledGroup) {
		long lResult = -1;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_PROJECT_SERVERID, projectId);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_RUNTIME, callRunTime);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_TIME, callTime);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLER_NUMBER, callerNumber);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_IMSI, imsi);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_IMEI, imei);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_NUMBER, calledNumber);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_TYPE, calledType);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVERID, calledServerId);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_NAME, calledName);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_LOGONAME, calledLogoName);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVER_AREA, calledServerArea);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_GROUP, calledGroup);
			initialValues.put(GooutDataBaseHelper.TABLE_CONSULTRECORD_UPLOAD_STATE, isUpload);
			GoOutDebug.i(TAG, "insert consult data-> callTime = " + callTime);

			lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_CONSULTRECORD, "", initialValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lResult;
	}

	/**
	 * 更新上传状态
	 * 
	 * @Description:
	 * @param id
	 *            本地数据库id
	 * @param state
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public boolean updateUploadStateById(int id, int state) {
		GoOutDebug.i(TAG, "updateUploadState id-> id = " + id);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " SET " + GooutDataBaseHelper.TABLE_CONSULTRECORD_UPLOAD_STATE + "=? where " + GooutDataBaseHelper.TABLE_CONSULTRECORD_ID + "=?;", new Object[] { state, id });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 更新上传状态
	 * 
	 * @Description:
	 * @param id
	 * @param state
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public boolean updateUploadStateByTime(long calltime, int state) {
		GoOutDebug.i(TAG, "updateUploadState calltime-> calltime = " + calltime);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " SET " + GooutDataBaseHelper.TABLE_CONSULTRECORD_UPLOAD_STATE + "=? where " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_TIME + "=?;", new Object[] { state, calltime });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 若logo已变，则更新
	 * 
	 * @Description:
	 * @param logoName
	 * @param type
	 * @param serverId
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-28
	 */
	public void updateLogoIfChanged(String logoName, int type, int serverId) {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " WHERE( " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_TYPE + "=? " + " AND " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVERID + "=?);";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { type + "", serverId + "" });
			if (cursor != null && cursor.getCount() > 0) {
				GoOutDebug.e(TAG, "exist record! ");
				cursor.moveToFirst();// 此处可能有多个数据，但所有logo都一致。
				// 存在此中介或顾问的联系记录
				if (!logoName.equals(cursor.getString(cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_LOGONAME)))) {
					GoOutDebug.e(TAG, "exist record! and need update logo");
					// logo已变, 更新
					mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " SET " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_LOGONAME + "=? WHERE( " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_TYPE + "=? and " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVERID + "=?);", new Object[] { logoName, type, serverId });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得所有未上传过的联系记录
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public ArrayList<RecentlyConsult> getRecentlyConsultDisUploaded() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " WHERE " + GooutDataBaseHelper.TABLE_CONSULTRECORD_UPLOAD_STATE + "='0' ORDER BY " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_TIME + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToConsultList(cursor);

	}

	/**
	 * 获得所有联系记录(时间顺序)
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public ArrayList<RecentlyConsult> getRecentlyConsultAll() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_CONSULTRECORD + " ORDER BY " + GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_TIME + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToConsultList(cursor);

	}

	/**
	 * 将查询得到的Cursor转为RecentlyConsult列表
	 * 
	 * @Description:
	 * @param cursor
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	private ArrayList<RecentlyConsult> cursorToConsultList(Cursor cursor) {
		ArrayList<RecentlyConsult> listResult = new ArrayList<RecentlyConsult>();
		RecentlyConsult c;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_ID);
				int indexProjectId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_PROJECT_SERVERID);
				int indexCallRunTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_RUNTIME);
				int indexCallTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_TIME);
				int indexCallerNumber = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLER_NUMBER);
				int indexImsi = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_IMSI);
				int indexImei = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_IMEI);
				int indexCalledNumber = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_NUMBER);
				int indexCallType = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_TYPE);
				int indexCallId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVERID);
				int indexUploadState = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_UPLOAD_STATE);

				int indexCalledName = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALL_NAME);
				int indexCalledLogoName = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_LOGONAME);
				int indexCalledServerArea = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_SERVER_AREA);
				int indexCalledGroup = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_CONSULTRECORD_CALLED_GROUP);

				cursor.moveToFirst();
				do {
					c = new RecentlyConsult();
					c.setId(cursor.getInt(indexId));
					c.setProjectId(cursor.getInt(indexProjectId));
					c.setCallRuntime(cursor.getInt(indexCallRunTime));
					c.setCallTime(cursor.getLong(indexCallTime));
					c.setCalledNumber(cursor.getString(indexCallerNumber));
					c.setCallerIMSI(cursor.getString(indexImsi));
					c.setCallerIMEI(cursor.getString(indexImei));
					c.setCalledNumber(cursor.getString(indexCalledNumber));
					// GoOutDebug.e(TAG, "getCalled tel:" + cursor.getString(indexCalledNumber));
					c.setCalledType(cursor.getInt(indexCallType));
					c.setCalledID(cursor.getInt(indexCallId));
					c.setUploadState(cursor.getInt(indexUploadState));

					c.setCalledName(cursor.getString(indexCalledName));
					c.setCalledLogoName(cursor.getString(indexCalledLogoName));
					c.setCalledServerArea(cursor.getString(indexCalledServerArea));
					c.setCalledDescription(cursor.getString(indexCalledGroup));

					listResult.add(c);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}

		return listResult;
	}
}
