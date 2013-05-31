package com.c35.ptc.goout.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.School;

/**
 * 数据库中学院列表存储的表格的管理，包含收藏与否
 * 
 * 
 * TODO 需添加按国家、按国家和专业查找本地数据库的方法
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class TableSchool {

	private static final String TAG = "TableSchool";

	private Context mContext = null;

	public SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableSchool(Context context) {
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
	 * 插入一条数据
	 * 
	 * @Description:
	 * @param schoolId
	 * @param schoolNameCn
	 * @param schoolNameEn
	 * @param sortKey
	 * @param logoUrl
	 * @param ranking
	 * @param favState
	 * @param favTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public long insertData(int schoolServerId, String schoolNameCn, String schoolNameEn, String sortKey, String logoUrl, int countryId, int ranking, int favState, long favTime) {
		long lResult = -1;
		// Cursor schoolCursor = null;
		// 滤重，不滤重也可，数据库已限定unique
		// try {
		// String qsql = "select * from " + GooutDataBaseHelper.TABLE_SCHOOL + " where " +
		// GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + " = ? ";
		// schoolCursor = mSQLiteDatabase.rawQuery(qsql, new String[] { schoolServerId + "" });
		// if (schoolCursor == null || schoolCursor.getCount() == 0) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_SERVERID, schoolServerId);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_NAMECN, schoolNameCn);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_NAMEEN, schoolNameEn);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_SORTKEY, sortKey);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_RANKING, ranking);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_LOGOURL, logoUrl);
		initialValues.put(GooutDataBaseHelper.TABLE_SHCOOL_COUNTRY_SERVERID, countryId);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE, favState);
		initialValues.put(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME, favTime);
		GoOutDebug.i(TAG, "insert school data-> name = " + schoolNameCn);

		try {
			// mSQLiteDatabase.beginTransaction();
			lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_SCHOOL, "", initialValues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// mSQLiteDatabase.endTransaction();
			// mSQLiteDatabase.setTransactionSuccessful();
		}

		// } else {
		// GoOutDebug.i(TAG, "same, not insert school!");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (schoolCursor != null) {
		// schoolCursor.close();
		// }
		// }
		return lResult;
	}

	/**
	 * 根据本地id删除一条数据
	 * 
	 * @Description:
	 * @param schoolId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public boolean deleteData(String schoolId) {
		GoOutDebug.i(TAG, "delete school data-> schoolid = " + schoolId);
		try {
			mSQLiteDatabase.execSQL("delete from " + GooutDataBaseHelper.TABLE_SCHOOL + " where " + GooutDataBaseHelper.TABLE_SCHOOL_ID + " = ?", new Object[] { schoolId });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 按名次查询
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public ArrayList<School> getSchoolListAsRankSort(int countryId) {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SHCOOL_COUNTRY_SERVERID + "=? ORDER BY " + GooutDataBaseHelper.TABLE_SCHOOL_RANKING + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToSchoolList(cursor);

	}

	/**
	 * 按字母顺序查询
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public ArrayList<School> getSchoolListAsLetterSort(int countryId) {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SHCOOL_COUNTRY_SERVERID + "=" + countryId + " ORDER BY " + GooutDataBaseHelper.TABLE_SCHOOL_SORTKEY + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToSchoolList(cursor);

	}

	/**
	 * 按收藏顺序获得收藏的院校列表
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public ArrayList<School> getFavSchoolListAsTimeSort() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "='1' ORDER BY " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME + " ASC";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToSchoolList(cursor);

	}

	/**
	 * 获得本地院校库中某院校的logo url
	 * 
	 * @Description:
	 * @param schoolServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public String getSchoolLogoUrl(int schoolServerId) {
		String result = "";
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=" + schoolServerId + ";";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				result = cursor.getString(cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_LOGOURL));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		GoOutDebug.v(TAG, "school logo url=" + result);
		return result;
	}

	/**
	 * 更新本地院校的logo
	 * 
	 * @Description:
	 * @param schoolServerId
	 * @param url
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public boolean updateSchoolLogoUrl(int schoolServerId, String url) {
		GoOutDebug.i(TAG, "updateSchoolLogoUrl . schoolServerId = " + schoolServerId);
		try {
			Object[] selectionArgs = new Object[] { url, schoolServerId };
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_SCHOOL + " SET " + GooutDataBaseHelper.TABLE_SCHOOL_LOGOURL + "=? WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=?;", selectionArgs);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 更新院校收藏与否
	 * 
	 * @Description:
	 * @param favour
	 * @param schoolServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public boolean updateSchoolFavourState(School school, int favourState, long favTime) {

		// 先判断库里有没有
		Cursor schoolCursor = null;
		// 滤重，不滤重也可，数据库已限定unique
		try {
			String qsql = "select * from " + GooutDataBaseHelper.TABLE_SCHOOL + " where " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + " = ? ";
			schoolCursor = mSQLiteDatabase.rawQuery(qsql, new String[] { school.getServerId() + "" });
			if (schoolCursor == null || schoolCursor.getCount() == 0) {
				// 不存在，存储
				insertData(school.getServerId(), school.getNameCn(), school.getNameEn(), school.getSortKey(), school.getLogoName(), school.getCountryServerId(), 0, 1, favTime);
			} else {
				// 已存在，更新收藏状态，（不收藏时暂不删除）
				GoOutDebug.i(TAG, "exist, not insert school! just update state");

				Object[] selectionArgs = new Object[] { favourState, favTime, school.getServerId() };
				mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_SCHOOL + " SET " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "=?, " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME + "=? WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=?;", selectionArgs);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (schoolCursor != null) {
				schoolCursor.close();
			}
		}

		//
		// GoOutDebug.i(TAG, "updateSchoolFavourState . schoolServerId = " + schoolServerId);
		// try {
		// Object[] selectionArgs = new Object[] { favourState, favTime, schoolServerId };
		// mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_SCHOOL + " SET " +
		// GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "=?, " +
		// GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME + "=? WHERE " +
		// GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=?;", selectionArgs);
		// // String sql = "UPDATE " + GooutDataBaseHelper.TABLE_SCHOOL + " SET " +
		// // GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "='" + favourState + "', " +
		// // GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME + "='" + favTime + "' WHERE " +
		// // GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "='" + schoolServerId + "';";
		// //GoOutDebug.e(TAG, "sql: " + sql);
		// // mSQLiteDatabase.execSQL(sql, selectionArgs);
		// } catch (Exception e) {
		// e.printStackTrace();
		// return false;
		// }
		return true;
	}

	/**
	 * 判断是否已收藏
	 * 
	 * @Description:
	 * @param schoolServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public boolean getFavState(int schoolServerId) {
		GoOutDebug.i(TAG, "getFavState . schoolServerId = " + schoolServerId);
		boolean result = false;
		Cursor cursor = null;
		try {
			String query = "SELECT " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + " FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=?;";
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { schoolServerId + "" });
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int state = cursor.getInt(cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE));
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
	private ArrayList<School> cursorToSchoolList(Cursor cursor) {
		ArrayList<School> listResult = new ArrayList<School>();
		School s;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexSchoolId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_ID);
				int indexSchoolServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_SERVERID);
				int indexSchoolNameCn = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_NAMECN);
				int indexSchoolNameEn = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_NAMEEN);
				int indexSchoolRanking = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_RANKING);
				int indexSchoolSortkey = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_SORTKEY);
				int indexSchoolLogoUrl = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_LOGOURL);
				int indexSchoolCountryId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SHCOOL_COUNTRY_SERVERID);
				int indexSchoolFavState = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE);
				int indexSchoolFavTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME);

				cursor.moveToFirst();
				do {
					s = new School();
					s.setId(cursor.getInt(indexSchoolId));
					s.setServerId(cursor.getInt(indexSchoolServerId));
					s.setNameCn(cursor.getString(indexSchoolNameCn));
					s.setNameEn(cursor.getString(indexSchoolNameEn));
					s.setRanking(cursor.getInt(indexSchoolRanking));
					s.setSortKey(cursor.getString(indexSchoolSortkey));
					s.setLogoName(cursor.getString(indexSchoolLogoUrl));
					s.setCountryServerId(cursor.getInt(indexSchoolCountryId));
					s.setFavourState(cursor.getInt(indexSchoolFavState));
					s.setFavourTime(cursor.getInt(indexSchoolFavTime));
					listResult.add(s);
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
	 * 收藏的院校ID集合
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public Set<Integer> getFavoritedPids() {
		Set<Integer> pids = new HashSet<Integer>();
		String query = "SELECT " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + " FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "=1";
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					pids.add(cursor.getInt(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return pids;
	}

	/**
	 * 收藏院校记录
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public Set<Integer> getProFavRecords() {
		Set<Integer> records = new HashSet<Integer>();
		String query = "SELECT " + GooutDataBaseHelper.TABLE_SYNC_SCHOOL_FAV_REC_PID + " FROM " + GooutDataBaseHelper.TABLE_SYNC_SCHOOL_FAV_REC;
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					records.add(cursor.getInt(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return records;
	}

	/**
	 * 同步后对收藏的一些操作
	 * 
	 * @Description:
	 * @param keeps
	 *            服务器上告知要收藏的
	 * @param deletes
	 *            服务器告诉要删除的
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public boolean syncSaveOrUpd(Collection<School> keeps, Collection<Integer> deletes) {
		boolean result = false;
		mSQLiteDatabase.beginTransaction();
		try {
			if (deletes != null && !deletes.isEmpty()) {
				for (int pid : deletes) {
					mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_SCHOOL + " SET " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "=0 WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=?;", new Object[] { pid });
				}
			}
			if (keeps != null && !keeps.isEmpty()) {
				ContentValues keepValues = new ContentValues();
				for (School s : keeps) {
					// 已经有的就更新，没有就插入
					String query = "SELECT " + GooutDataBaseHelper.TABLE_SCHOOL_ID + " FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + "=" + s.getServerId();
					Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
					if (cursor != null && cursor.getCount() > 0) {
						keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_ID, cursor.getInt(0));
					}

					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_SERVERID, s.getServerId());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_NAMECN, s.getNameCn());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_NAMEEN, s.getNameEn());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_SORTKEY, s.getSortKey());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_RANKING, s.getRanking());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_LOGOURL, s.getLogoName());
					keepValues.put(GooutDataBaseHelper.TABLE_SHCOOL_COUNTRY_SERVERID, s.getCountryServerId());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE, s.getFavourState());
					keepValues.put(GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_TIME, s.getFavourTime());

					mSQLiteDatabase.insertWithOnConflict(GooutDataBaseHelper.TABLE_SCHOOL, null, keepValues, SQLiteDatabase.CONFLICT_REPLACE);
				}
			}
			// 先清除SyncProFavRecord表里的数据
			mSQLiteDatabase.delete(GooutDataBaseHelper.TABLE_SYNC_SCHOOL_FAV_REC, null, null);
			// 最后要查询出收藏的项目ID放SyncProFavRecord表里
			String query = "SELECT " + GooutDataBaseHelper.TABLE_SCHOOL_SERVERID + " FROM " + GooutDataBaseHelper.TABLE_SCHOOL + " WHERE " + GooutDataBaseHelper.TABLE_SCHOOL_FAVOUR_STATE + "=1";
			Cursor cursor = mSQLiteDatabase.rawQuery(query, null);
			if (cursor != null) {
				ContentValues spfrValues = new ContentValues();
				while (cursor.moveToNext()) {
					spfrValues.clear();
					spfrValues.put(GooutDataBaseHelper.TABLE_SYNC_SCHOOL_FAV_REC_PID, cursor.getInt(0));
					mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_SYNC_SCHOOL_FAV_REC, null, spfrValues);
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
