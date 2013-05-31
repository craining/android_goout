package com.c35.ptc.goout.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.RecentlyVisit;

/**
 * 浏览记录数据表
 * 
 * (暂时不用，后期加最近浏览时使用)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-8
 */
public class TableVisitRecord {

	private static final String TAG = "TableVisitRecord";

	private Context mContext = null;

	private static SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableVisitRecord(Context context) {
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
	 * 保存一条浏览记录
	 * 
	 * @Description:
	 * @param projectId
	 * @param visitorId
	 *            (0为游客，否则为注册用户的id)
	 * @param visitTime
	 * @param ip
	 * @param imsi
	 * @param imei
	 * @param uploadState
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public long insertData(int projectId, String visitorId, long visitTime, String ip, String imsi, String imei, int uploadState) {
		long lResult = -1;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_PROJECT_SERVERID, projectId);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_READER_ID, visitorId);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_READER_TIME, visitTime);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IP, ip);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IMEI, imei);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IMSI, imsi);
			initialValues.put(GooutDataBaseHelper.TABLE_VISITRECORD_UPLOAD_STATE, uploadState);
			GoOutDebug.i(TAG, "insert visit data-> visitTime = " + visitTime + "   visitorId=" + visitorId);

			lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_VISITRECORD, "", initialValues);
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
	 * @param state
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public boolean updateUploadState(int id, int state) {
		GoOutDebug.i(TAG, "updateUploadState id-> id = " + id);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_VISITRECORD + " SET " + GooutDataBaseHelper.TABLE_VISITRECORD_UPLOAD_STATE + "='?' where " + GooutDataBaseHelper.TABLE_VISITRECORD_ID + "=?;", new Object[] { state, id });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
	public ArrayList<RecentlyVisit> getRecentlyConsultDisUploaded() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_VISITRECORD + " WHERE " + GooutDataBaseHelper.TABLE_VISITRECORD_UPLOAD_STATE + "='0' " + " ORDER BY " + GooutDataBaseHelper.TABLE_VISITRECORD_READER_TIME + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToVisitList(cursor);

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
	private ArrayList<RecentlyVisit> cursorToVisitList(Cursor cursor) {
		ArrayList<RecentlyVisit> listResult = new ArrayList<RecentlyVisit>();
		RecentlyVisit v;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_ID);
				int indexProjectId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_PROJECT_SERVERID);
				int indexVisitorId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_READER_ID);
				int indexVisitTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_READER_TIME);
				int indexVisitIP = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IP);
				int indexVisitImei = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IMEI);
				int indexVisitImsi = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_READER_INFO_IMSI);
				int indexVisitUploadState = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_VISITRECORD_UPLOAD_STATE);

				cursor.moveToFirst();
				do {
					v = new RecentlyVisit();
					v.setId(cursor.getInt(indexId));
					v.setProjectId(cursor.getInt(indexProjectId));
					v.setReaderId(cursor.getString(indexVisitorId));
					v.setReaderTime(cursor.getLong(indexVisitTime));
					v.setReaderInfoIp(cursor.getString(indexVisitIP));
					v.setReaderInfoImei(cursor.getString(indexVisitImei));
					v.setReaderInfoImsi(cursor.getString(indexVisitImsi));
					v.setUploadState(cursor.getInt(indexVisitUploadState));
					listResult.add(v);
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
