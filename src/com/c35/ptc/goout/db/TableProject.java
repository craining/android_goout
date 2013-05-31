package com.c35.ptc.goout.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;

/**
 * 项目表，现用于离线显示项目收藏列表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-8
 */
public class TableProject {

	private static final String TAG = "TableProject";

	private Context mContext = null;

	private static SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableProject(Context context) {
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
	 * 插入一条项目信息
	 * 
	 * @Description:
	 * @param projectServerId
	 * @param projectNameCn
	 * @param country
	 * @param tuition
	 * @param tuitionUnit
	 * @param tuitionTimeUnit
	 * @param degree
	 * @param publisherId
	 * @param publisherType
	 * @param publisherTel
	 * @param logoUrl
	 * @param entreTime
	 * @param favourState
	 * @param favourTime
	 * @param gpa
	 * @param language
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public long insertData(int projectServerId, String projectNameCn, String country, int tuition, String tuitionUnit, String tuitionTimeUnit, String degree, int publisherId, int publisherType, String publisherTel, String logoName, String entreTime, int favourState, long favourTime, String gpa, String language) {

		GoOutDebug.v(TAG, "insertData-->" + "projectServerId=" + projectServerId + "projectNameCn=" + projectNameCn + "country=" + country + "country= " + country + "tuitionUnit=" + tuitionUnit + "tuitionTimeUnit=" + tuitionTimeUnit + "degree=" + degree + "publisherId" + publisherId + "publisherType=" + publisherType + "publisherTel=" + publisherTel + "logName=" + logoName + "entreTime=" + entreTime + "favourState=" + favourState + "favourTime=" + favourTime);

		long lResult = -1;
		Cursor projectCursor = null;
		// 滤重，不滤重也可，数据库已限定unique
		try {
			String qsql = "select * from " + GooutDataBaseHelper.TABLE_PROJECT + " where " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + " = ? ";
			projectCursor = mSQLiteDatabase.rawQuery(qsql, new String[] { projectServerId + "" });
			if (projectCursor == null || projectCursor.getCount() == 0) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_SERVERID, projectServerId);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_NAME, projectNameCn);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_COUNTRY, country);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION, tuition);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION_UNIT, tuitionUnit);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION_TIMEUNIT, tuitionTimeUnit);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_DEGREE, degree);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_SERVERID, publisherId);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_TYPE, publisherType);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_TEL, publisherTel);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_LOGNAME, logoName);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_ENTERTIME, entreTime);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE, favourState);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_FAVOURTIME, favourTime);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_GPA, gpa);
				initialValues.put(GooutDataBaseHelper.TABLE_PROJECT_LANGUAGE, language);

				GoOutDebug.i(TAG, "insert project data-> name = " + projectNameCn);
				lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_PROJECT, "", initialValues);
			} else {
				GoOutDebug.i(TAG, "same, not insert project!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (projectCursor != null) {
				projectCursor.close();
			}
		}
		return lResult;
	}

	/**
	 * 根据本地id删除一条数据
	 * 
	 * @Description:
	 * @param projectId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public boolean deleteData(int projectServerId) {
		GoOutDebug.i(TAG, "delete project data-> project = " + projectServerId);
		try {
			mSQLiteDatabase.execSQL("delete from " + GooutDataBaseHelper.TABLE_PROJECT + " where " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + " = ?", new Object[] { projectServerId + "" });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 按收藏顺序获得项目列表
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public ArrayList<Project> getFavProjectListAsTimeSort() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_PROJECT + " WHERE " + GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + "='1' ORDER BY " + GooutDataBaseHelper.TABLE_PROJECT_FAVOURTIME + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToProjectList(cursor);

	}

	/**
	 * 更新项目收藏状态
	 * 
	 * @Description:
	 * @param favourState
	 * @param projectServerId
	 * @param favTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public boolean updateProjectFavourState(int favourState, int projectServerId, long favTime) {
		// TODO 删除还是更新。。。。??
		GoOutDebug.i(TAG, "updateProjectFavourState . projectServerId = " + projectServerId);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_PROJECT + " SET " + GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + "=?, " + GooutDataBaseHelper.TABLE_PROJECT_FAVOURTIME + "=? WHERE " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + "=?;", new Object[] { favourState, favTime, projectServerId });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断是否已收藏
	 * 
	 * @Description:
	 * @param projectServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public boolean getFavState(int projectServerId) {
		GoOutDebug.i(TAG, "getFavState . projectServerId = " + projectServerId);
		boolean result = false;
		Cursor cursor = null;
		try {
			String query = "SELECT " + GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + " FROM " + GooutDataBaseHelper.TABLE_PROJECT + " WHERE " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + "=?;";
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { projectServerId + "" });
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int state = cursor.getInt(cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE));
				if (state == 1) {
					result = true;
				} else {
					result = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return result;
	}

	/**
	 * 将查询得到的Cursor转为school列表
	 * 
	 * @Description:
	 * @param cursor
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	private ArrayList<Project> cursorToProjectList(Cursor cursor) {
		ArrayList<Project> listResult = new ArrayList<Project>();
		Project p;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexProjectId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_ID);
				int indexProjectServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_SERVERID);
				int indexProjectNameCn = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_NAME);
				int indexProjectCountry = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_COUNTRY);
				int indexTuition = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_TUITION);
				int indexTuitionUnit = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_TUITION_UNIT);
				int indexTuitionTimeUnit = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_TUITION_TIMEUNIT);
				int indexDegree = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_DEGREE);
				int indexPublisherServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_SERVERID);
				int indexPublisherType = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_TYPE);
				int indexPublisherTel = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_TEL);
				int indexLogoName = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_LOGNAME);
				int indexEnterTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_ENTERTIME);
				int indexFavourState = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE);
				int indexFavourTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_FAVOURTIME);
				int indexGpa = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_GPA);
				int indexLanguage = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_PROJECT_LANGUAGE);

				cursor.moveToFirst();
				do {
					p = new Project();
					p.setId(cursor.getInt(indexProjectId));
					p.setServerId(cursor.getInt(indexProjectServerId));
					p.setName(cursor.getString(indexProjectNameCn));
					p.setCountry(cursor.getString(indexProjectCountry));
					p.setTuition(cursor.getInt(indexTuition));
					p.setTuitionUnit(cursor.getString(indexTuitionUnit));
					p.setTuitionTimeUnit(cursor.getString(indexTuitionTimeUnit));
					p.setDegree(cursor.getString(indexDegree));
					p.setPublisherServerId(cursor.getInt(indexPublisherServerId));
					p.setPublisherType(cursor.getInt(indexPublisherType));
					p.setPublisherTel(cursor.getString(indexPublisherTel));
					p.setLogoName(cursor.getString(indexLogoName));
					p.setEnterTime(cursor.getString(indexEnterTime));
					p.setFavourState(cursor.getInt(indexFavourState));
					p.setFavourTime(cursor.getLong(indexFavourTime));
					p.setGpa(cursor.getString(indexGpa));
					p.setLanguage(cursor.getString(indexLanguage));

					listResult.add(p);
				} while (cursor.moveToNext());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}

		}

		return listResult;
	}
	/**
	 * 收藏的项目ID集合
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public Set<Integer> getFavoritedPids(){
		Set<Integer> pids = new HashSet<Integer>();
		String query = "SELECT " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + " FROM " 
						+ GooutDataBaseHelper.TABLE_PROJECT + " WHERE " 
						+ GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + "=1";
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
			if(cursor != null){
				while(cursor.moveToNext()){
					pids.add(cursor.getInt(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return pids;
	}
	/**
	 * 收藏项目记录
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public Set<Integer> getProFavRecords(){
		Set<Integer> records = new HashSet<Integer>();
		String query = "SELECT " + GooutDataBaseHelper.TABLE_SYNC_PRO_FAV_REC_PID + " FROM " + GooutDataBaseHelper.TABLE_SYNC_PRO_FAV_REC;
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
			if(cursor != null){
				while(cursor.moveToNext()){
					records.add(cursor.getInt(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return records;
	}
	/**
	 * 同步后对收藏的一些操作
	 * @Description:
	 * @param keeps 服务器上告知要收藏的
	 * @param deletes 服务器告诉要删除的
	 * @return
	 * @see: 
	 * @since: 
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public boolean syncSaveOrUpd(Collection<Project> keeps, Collection<Integer> deletes){
		boolean result = false;
		mSQLiteDatabase.beginTransaction();
		try {
			if(deletes != null && !deletes.isEmpty()){
				for(int pid : deletes){
					mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_PROJECT + " SET " 
							+ GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + "=0 WHERE " 
							+ GooutDataBaseHelper.TABLE_PROJECT_SERVERID + "=?;", 
							new Object[] {pid });
				}
			}
			if(keeps != null && !keeps.isEmpty()){
				ContentValues keepValues = new ContentValues();
				for(Project p : keeps){
					// 已经有的就更新，没有就插入
					String query = "SELECT " + GooutDataBaseHelper.TABLE_PROJECT_ID + " FROM " 
							+ GooutDataBaseHelper.TABLE_PROJECT + " WHERE " 
							+ GooutDataBaseHelper.TABLE_PROJECT_SERVERID + "=" + p.getServerId();
					Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
					if(cursor != null && cursor.getCount() > 0){
						keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_ID, cursor.getInt(0));
					}
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_SERVERID, p.getServerId());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_NAME, p.getName());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_COUNTRY, p.getCountry());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION, p.getTuition());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION_UNIT, p.getTuitionUnit());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_TUITION_TIMEUNIT, p.getTuitionTimeUnit());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_DEGREE, p.getDegree());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_SERVERID, p.getPublisherServerId());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_PUBLISHER_TYPE, p.getPublisherType());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_LOGNAME, p.getLogoName());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE, 1);
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_FAVOURTIME, p.getFavourTime());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_GPA, p.getGpa());
					keepValues.put(GooutDataBaseHelper.TABLE_PROJECT_LANGUAGE, p.getLanguage());
					mSQLiteDatabase.insertWithOnConflict(GooutDataBaseHelper.TABLE_PROJECT, null, keepValues, SQLiteDatabase.CONFLICT_REPLACE);
				}
			}
			// 先清除SyncProFavRecord表里的数据
			mSQLiteDatabase.delete(GooutDataBaseHelper.TABLE_SYNC_PRO_FAV_REC, null, null);
			// 最后要查询出收藏的项目ID放SyncProFavRecord表里
			String query = "SELECT " + GooutDataBaseHelper.TABLE_PROJECT_SERVERID + " FROM " 
					+ GooutDataBaseHelper.TABLE_PROJECT + " WHERE " 
					+ GooutDataBaseHelper.TABLE_PROJECT_FAVOURSTATE + "=1";
			Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
			if(cursor != null){
				ContentValues spfrValues = new ContentValues();
				while(cursor.moveToNext()){
					spfrValues.clear();
					spfrValues.put(GooutDataBaseHelper.TABLE_SYNC_PRO_FAV_REC_PID, cursor.getInt(0));
					mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_SYNC_PRO_FAV_REC, null, spfrValues);
				}
			}
			mSQLiteDatabase.setTransactionSuccessful();
			result = true;
	   } finally {
		   mSQLiteDatabase.endTransaction();
	   }
		return result;
	}
}
