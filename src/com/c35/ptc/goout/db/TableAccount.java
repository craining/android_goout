package com.c35.ptc.goout.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 账户表
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-8
 */
public class TableAccount {

	private static final String TAG = "TableAccount";

	private Context mContext = null;

	private static SQLiteDatabase mSQLiteDatabase = null;
	private GooutDataBaseHelper mDatabaseHelper = null;

	public TableAccount(Context context) {
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
	 * 插入一个账号(有则更新，无则插入)
	 * 
	 * @Description:
	 * @param accountType
	 * @param accountName
	 * @param uid
	 * @param logo
	 * @param refreshToken
	 * @param accessToken
	 * @param requestTime
	 * @param expiresIn
	 * @param bindMobile
	 * @param isLogin
	 * @param pwd
	 * @param thirdId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-23
	 */
	public long insertData(String accountType, String accountName, String uid, String logo, String refreshToken, String accessToken, long requestTime, String expiresIn, String bindMobile, int isLogin, String pwd, String thirdId) {
		// 有则更新，无则插入

		long lResult = -1;
		try {
			Cursor accountCursor = null;
			String qsql = "select * from " + GooutDataBaseHelper.TABLE_ACCOUNT + " where " + GooutDataBaseHelper.TABLE_ACCOUNT_THIRD_ID + " = ? ";
			accountCursor = mSQLiteDatabase.rawQuery(qsql, new String[] { thirdId + "" });
			if (accountCursor == null || accountCursor.getCount() == 0) {
				GoOutDebug.e(TAG, "not exist this account just insert!");
				ContentValues initialValues = new ContentValues();
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_TYPE, accountType);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_NAME, accountName);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_UID, uid);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_LOGO, logo);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_REFRESH_TOKEN, refreshToken);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_ACCESS_TOKEN, accessToken);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_REQUEST_TIME, requestTime);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_EXPIRES_IN, expiresIn);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_BIND_MOBILE, bindMobile);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN, isLogin);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_PWD, pwd);
				initialValues.put(GooutDataBaseHelper.TABLE_ACCOUNT_THIRD_ID, thirdId);
				GoOutDebug.i(TAG, "insert consult data-> TABLE_ACCOUNT_NAME = " + accountName);

				lResult = mSQLiteDatabase.insert(GooutDataBaseHelper.TABLE_ACCOUNT, "", initialValues);

			} else {
				GoOutDebug.e(TAG, "exist this account update!");
				// 之前处于登录状态，现在仍处于未登录状态，则仍处于登录状态；之前处于未登录，而现在处于登录，则仍处于登录状态；之前未登录，现在未登录，则为未登录.
				accountCursor.moveToFirst();
				int isPreLogin = accountCursor.getInt(accountCursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN));
				if (isLogin == 1) {
					isPreLogin = isLogin;
				}
				// 若之前登录过第三方账户，且存在绑定的手机号，解绑第三方账户后，再次绑定，此处更新的bindMobile为空，因此先判断之前是否有绑定的手机号，若有的话继续保持
				String binMobliePre = accountCursor.getString(accountCursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_BIND_MOBILE));
				if (!StringUtil.isNull(binMobliePre)) {
					bindMobile = binMobliePre;
				}
				// 同上
				String uidPre = accountCursor.getString(accountCursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_UID));
				if (!StringUtil.isNull(uidPre)) {
					uid = uidPre;
				}
				String sql = "UPDATE " + GooutDataBaseHelper.TABLE_ACCOUNT + " SET " + GooutDataBaseHelper.TABLE_ACCOUNT_TYPE + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_NAME + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_UID + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_LOGO + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_REFRESH_TOKEN + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_ACCESS_TOKEN + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_REQUEST_TIME + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_EXPIRES_IN + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_BIND_MOBILE + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN + "=?, " + GooutDataBaseHelper.TABLE_ACCOUNT_PWD + "=? where " + GooutDataBaseHelper.TABLE_ACCOUNT_THIRD_ID + "=?;";
				mSQLiteDatabase.execSQL(sql, new Object[] { accountType, accountName, uid, logo, refreshToken, accessToken, requestTime, expiresIn, bindMobile, isPreLogin, pwd, thirdId });
				lResult = 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lResult;
	}

	/**
	 * 退出一个账户，不清除token等信息
	 * 
	 * @Description:
	 * @param uid
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-23
	 */
	public boolean exitAccountByLoacalId(int id) {
		GoOutDebug.i(TAG, "existAccountByUid   id = " + id);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_ACCOUNT + " SET " + GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN + "=0 WHERE " + GooutDataBaseHelper.TABLE_ACCOUNT_ID + "=?;", new Object[] { id });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 更新一个账号绑定的手机号
	 */

	/**
	 * 更新一个账号的logo
	 */

	/**
	 * 删除一个账号(根据本地id)
	 * 
	 * @Description:
	 * @param localId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public boolean deleteAccountByLocalId(int localId) {
		GoOutDebug.i(TAG, "deleteAccountByLocalId   localId = " + localId);
		try {
			mSQLiteDatabase.execSQL("DELETE FROM " + GooutDataBaseHelper.TABLE_ACCOUNT + " WHERE " + GooutDataBaseHelper.TABLE_ACCOUNT_ID + "=?;", new Object[] { localId });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 清除一个账号的token
	 * 
	 * @Description:
	 * @param type
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-22
	 */
	public boolean deleteTokenByType(String type) {
		GoOutDebug.i(TAG, "deleteTokenByType   type = " + type);
		try {
			mSQLiteDatabase.execSQL("UPDATE " + GooutDataBaseHelper.TABLE_ACCOUNT + " SET " + GooutDataBaseHelper.TABLE_ACCOUNT_ACCESS_TOKEN + "='' ," + GooutDataBaseHelper.TABLE_ACCOUNT_REFRESH_TOKEN + "='' ," + GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_REQUEST_TIME + "='' ," + GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_EXPIRES_IN + "='' where " + GooutDataBaseHelper.TABLE_ACCOUNT_TYPE + "=?;", new Object[] { type });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获得所有账户
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public ArrayList<Account> getAllAccounts() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_ACCOUNT + " ;";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToAccountList(cursor);

	}

	/**
	 * 根据账户类型查询某个账户
	 * 
	 * @Description:
	 * @param accountType
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public Account getAccountByType(int accountType) {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_ACCOUNT + " WHERE (" + GooutDataBaseHelper.TABLE_ACCOUNT_TYPE + " =?);";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { accountType + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToAccount(cursor);
	}

	/**
	 * 获得当前登录的账户
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public Account getNowLoginAccount() {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_ACCOUNT + " WHERE (" + GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN + " =?);";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { Account.ACCOUNT_IS_LOGIN + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToAccount(cursor);
	}

	/**
	 * 获得某个平台的账户
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public Account getAccount(String type) {
		String query = "SELECT * FROM " + GooutDataBaseHelper.TABLE_ACCOUNT + " WHERE (" + GooutDataBaseHelper.TABLE_ACCOUNT_TYPE + " =?);";
		GoOutDebug.v(TAG, query);
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.rawQuery(query, new String[] { type + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cursorToAccount(cursor);
	}

	/**
	 * 将查询得到的Cursor转为Accounts
	 * 
	 * @Description:
	 * @param cursor
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	private ArrayList<Account> cursorToAccountList(Cursor cursor) {
		ArrayList<Account> listResult = new ArrayList<Account>();
		Account account;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_ID);
				int indexType = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TYPE);
				int indexAccountName = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_NAME);
				int indexAccountUid = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_UID);
				int indexAccountLogo = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_LOGO);
				int indexAccessToken = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_ACCESS_TOKEN);
				int indexRefreshToken = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_REFRESH_TOKEN);
				int indexRequestTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_REQUEST_TIME);
				int indexExpiresId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_EXPIRES_IN);
				int indexBindMobile = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_BIND_MOBILE);
				int indexIsLogin = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN);
				int indexPwd = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_PWD);
				int indexServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_THIRD_ID);

				cursor.moveToFirst();
				do {
					account = new Account();

					account.setLocalId(cursor.getInt(indexId));
					account.setAccountType(cursor.getString(indexType));
					account.setAccountName(cursor.getString(indexAccountName));
					account.setUid(cursor.getString(indexAccountUid));
					account.setLogo(cursor.getString(indexAccountLogo));
					account.setAccessToken(cursor.getString(indexAccessToken));
					account.setRefreshToken(cursor.getString(indexRefreshToken));
					account.setRequestTime(cursor.getLong(indexRequestTime));
					account.setExpiresIn(cursor.getString(indexExpiresId));
					account.setBindMobile(cursor.getString(indexBindMobile));
					account.setIsLogin(cursor.getInt(indexIsLogin));
					account.setPwd(cursor.getString(indexPwd));
					account.setThirdId(cursor.getString(indexServerId));
					listResult.add(account);
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
	 * 将查询得到的Cursor转为Account
	 * 
	 * @Description:
	 * @param cursor
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	private Account cursorToAccount(Cursor cursor) {
		Account account = null;
		if (cursor != null && cursor.getCount() > 0) {
			try {
				int indexId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_ID);
				int indexType = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TYPE);
				int indexAccountName = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_NAME);
				int indexAccountUid = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_UID);
				int indexAccountLogo = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_LOGO);
				int indexAccessToken = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_ACCESS_TOKEN);
				int indexRefreshToken = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_REFRESH_TOKEN);
				int indexRequestTime = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_REQUEST_TIME);
				int indexExpiresId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_TOKEN_EXPIRES_IN);
				int indexBindMobile = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_BIND_MOBILE);
				int indexIsLogin = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_IS_LOGIN);
				int indexPwd = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_PWD);
				int indexServerId = cursor.getColumnIndex(GooutDataBaseHelper.TABLE_ACCOUNT_THIRD_ID);
				cursor.moveToFirst();
				account = new Account();

				account.setLocalId(cursor.getInt(indexId));
				account.setAccountType(cursor.getString(indexType));
				account.setAccountName(cursor.getString(indexAccountName));
				account.setUid(cursor.getString(indexAccountUid));
				account.setLogo(cursor.getString(indexAccountLogo));
				account.setAccessToken(cursor.getString(indexAccessToken));
				account.setRefreshToken(cursor.getString(indexRefreshToken));
				account.setRequestTime(cursor.getLong(indexRequestTime));
				account.setExpiresIn(cursor.getString(indexExpiresId));
				account.setBindMobile(cursor.getString(indexBindMobile));
				account.setIsLogin(cursor.getInt(indexIsLogin));
				account.setPwd(cursor.getString(indexPwd));
				account.setThirdId(cursor.getString(indexServerId));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}

		return account;
	}
}
