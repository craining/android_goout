package com.c35.ptc.goout.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.RecentlyVisit;

public class TableCountry {

	private static final String TAG = "TableCountry";

	private Context mContext = null;

	private static SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableCountry(Context context) {
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
	 * 插入一条国家信息
	 * 
	 * @Description:
	 * @param serverId
	 * @param nameCn
	 * @param nameEn
	 * @param area
	 * @param isFirstOfArea
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public long insertData(int serverId, String nameCn, String nameEn, String area, int isFirstOfArea) {
		long lResult = -1;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(GooutDataBaseHelper.TABLE_COUNTRY_SERVERID, serverId);
			initialValues.put(GooutDataBaseHelper.TABLE_COUNTRY_NAMECN, nameCn);
			initialValues.put(GooutDataBaseHelper.TABLE_COUNTRY_NAME_EN, nameEn);
			initialValues.put(GooutDataBaseHelper.TABLE_COUNTRY_AREA, area);
			initialValues.put(GooutDataBaseHelper.TABLE_COUNTRY_ISFIRSTOF_AREA, isFirstOfArea);
			GoOutDebug.i(TAG, "insert country data-> country = " + nameCn + "   serverId=" + serverId);

			lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_COUNTRY, "", initialValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lResult;
	}

	/**
	 * 获得国家列表
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public ArrayList<Country> getCountries() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_COUNTRY + " ORDER BY " + GooutDataBaseHelper.TABLE_COUNTRY_ID + " ASC";
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
	private ArrayList<Country> cursorToVisitList(Cursor cursor) {
		ArrayList<Country> listResult = new ArrayList<Country>();
		Country c;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_ID);
				int indexServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_SERVERID);
				int indexNameCn = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_NAMECN);
				int indexNameEn = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_NAME_EN);
				int indexArea = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_AREA);
				int indexIsFirstOfArea = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_COUNTRY_ISFIRSTOF_AREA);

				cursor.moveToFirst();
				do {
					c = new Country();
					c.setId(cursor.getInt(indexId));
					c.setServerId(cursor.getInt(indexServerId));
					c.setNameCn(cursor.getString(indexNameCn));
					c.setNameEn(cursor.getString(indexNameEn));
					c.setArea(cursor.getString(indexArea));
					c.setFirstOfArea(cursor.getInt(indexIsFirstOfArea) == 1);

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
