package com.c35.ptc.goout.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库辅助类，包含数据库的详细信息，表格的统一创建，数据的升级等
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class GooutDataBaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "GooutDataBaseHelper";

	private static final String DB_NAME = "gooutdb.db";// 数据库名称
	private static final int DB_VERSION = 2;// 数据库版本

	/**
	 * 学校表
	 */
	public static final String TABLE_SCHOOL = "school";
	public static final String TABLE_SCHOOL_ID = "_id";
	public static final String TABLE_SCHOOL_SERVERID = "school_serverid";
	public static final String TABLE_SCHOOL_NAMECN = "name_cn";
	public static final String TABLE_SCHOOL_NAMEEN = "name_en";
	public static final String TABLE_SCHOOL_SORTKEY = "sort_key";// 拼音
	public static final String TABLE_SCHOOL_RANKING = "ranking";
	public static final String TABLE_SCHOOL_LOGOURL = "logo_url";
	public static final String TABLE_SHCOOL_COUNTRY_SERVERID = "country_serverid";// 所属国家id
	// public static final String TABLE_SHCOOL_MAJOR_SERVERID = "major_serverids";//对应专业id(多个)
	public static final String TABLE_SCHOOL_FAVOUR_STATE = "favour_state";// 收藏状态
	public static final String TABLE_SCHOOL_FAVOUR_TIME = "favour_time";// 收藏时间
	private static final String SQL_TABLE_SCHOOL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHOOL + " (" + TABLE_SCHOOL_ID + " INTEGER PRIMARY KEY, " + TABLE_SCHOOL_SERVERID + " INTEGER unique," + TABLE_SCHOOL_NAMECN + " varchar(20)," + TABLE_SCHOOL_NAMEEN + " varchar(20), " + TABLE_SCHOOL_SORTKEY + " varchar(20), " + TABLE_SCHOOL_RANKING + " INTEGER, " + TABLE_SCHOOL_LOGOURL + " varchar(20), " + TABLE_SHCOOL_COUNTRY_SERVERID + " INTEGER," + TABLE_SCHOOL_FAVOUR_STATE + " INTEGER DEFAULT 0, " + TABLE_SCHOOL_FAVOUR_TIME + " LONG);";
	private static final String SQL_TABLE_SCHOOL_CLEAR = "DROP TABLE IF EXISTS " + TABLE_SCHOOL;

	/**
	 * 收藏院校同步记录（只记录同步后还属于收藏状态的）
	 */
	public static final String TABLE_SYNC_SCHOOL_FAV_REC = "sync_school_favorite_record";
	public static final String TABLE_SYNC_SCHOOL_FAV_REC_ID = "_id";
	public static final String TABLE_SYNC_SCHOOL_FAV_REC_PID = "sid";

	public static final String SQL_TABLE_SYNC_SCHOOL_FAV_REC_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SYNC_SCHOOL_FAV_REC + "(" + TABLE_SYNC_SCHOOL_FAV_REC_ID + " INTEGER PRIMARY KEY, " + TABLE_SYNC_SCHOOL_FAV_REC_PID + " INTEGER);";
	public static final String SQL_TABLE_SYNC_SCHOOL_FAV_REC_CLEAR = "DROP TABLE IF EXISTS " + TABLE_SYNC_SCHOOL_FAV_REC;

	/**
	 * 联系记录表
	 */
	public static final String TABLE_CONSULTRECORD = "consult_record";
	public static final String TABLE_CONSULTRECORD_ID = "_id";
	public static final String TABLE_CONSULTRECORD_PROJECT_SERVERID = "project_serverid";// int 项目id
	public static final String TABLE_CONSULTRECORD_CALL_RUNTIME = "call_runtime";// int 联系时长
	public static final String TABLE_CONSULTRECORD_CALL_TIME = "call_time";// long 本地浏览时刻
	public static final String TABLE_CONSULTRECORD_CALLER_NUMBER = "caller_number";// string 主叫电话
	public static final String TABLE_CONSULTRECORD_IMSI = "imsi";// string 主叫imsi
	public static final String TABLE_CONSULTRECORD_IMEI = "imei";// 主叫imei(本地数据库是否可以不加此字段？？若程序做备份在恢复到其它手机上就有问题了)
	public static final String TABLE_CONSULTRECORD_CALLED_NUMBER = "called_number";// string 被叫号码
	public static final String TABLE_CONSULTRECORD_CALLED_TYPE = "called_type";// 被叫类型，int 1顾问，0中介
	public static final String TABLE_CONSULTRECORD_CALLED_SERVERID = "called_serverid";// int 被叫者id
	public static final String TABLE_CONSULTRECORD_UPLOAD_STATE = "upload_state";// int 是否已上传，0为未上传，1为已上传

	// 本地显示需要
	public static final String TABLE_CONSULTRECORD_CALL_NAME = "called_name";// 被叫者名字
	public static final String TABLE_CONSULTRECORD_CALLED_LOGONAME = "called_logoname";// 被叫者logo
	public static final String TABLE_CONSULTRECORD_CALLED_SERVER_AREA = "called_serverarea";// 被叫者服务区域
	public static final String TABLE_CONSULTRECORD_CALLED_GROUP = "called_group";// 被叫者所属部门

	private static final String SQL_TABLE_CONSULTRECORD_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONSULTRECORD + " (" + TABLE_CONSULTRECORD_ID + " INTEGER PRIMARY KEY, " + TABLE_CONSULTRECORD_PROJECT_SERVERID + " INTEGER," + TABLE_CONSULTRECORD_CALL_RUNTIME + " INTEGER," + TABLE_CONSULTRECORD_CALL_TIME + " long," + TABLE_CONSULTRECORD_CALLER_NUMBER + " varchar(20), " + TABLE_CONSULTRECORD_IMSI + " varchar(20), " + TABLE_CONSULTRECORD_IMEI + " varchar(20), " + TABLE_CONSULTRECORD_CALLED_NUMBER + " varchar(20), " + TABLE_CONSULTRECORD_CALLED_TYPE + " INTEGER, " + TABLE_CONSULTRECORD_CALLED_SERVERID + " INTEGER, " + TABLE_CONSULTRECORD_UPLOAD_STATE + " INTEGER DEFAULT 0, " + TABLE_CONSULTRECORD_CALL_NAME + " varchar(20), " + TABLE_CONSULTRECORD_CALLED_LOGONAME + " varchar(20), " + TABLE_CONSULTRECORD_CALLED_SERVER_AREA + " varchar(20), " + TABLE_CONSULTRECORD_CALLED_GROUP + " varchar(20));";
	private static final String SQL_TABLE_CONSULTRECORD_CLEAR = "DROP TABLE IF EXISTS " + TABLE_CONSULTRECORD;

	/**
	 * 浏览记录表
	 */
	public static final String TABLE_VISITRECORD = "visit_record";
	public static final String TABLE_VISITRECORD_ID = "_id";
	public static final String TABLE_VISITRECORD_PROJECT_SERVERID = "project_serverid";
	public static final String TABLE_VISITRECORD_READER_ID = "visitor_id";// 陌生人（0）或者用户（注册id); 不必须
	public static final String TABLE_VISITRECORD_READER_TIME = "time";// 访问时间
	public static final String TABLE_VISITRECORD_READER_INFO_IP = "ip";
	public static final String TABLE_VISITRECORD_READER_INFO_IMEI = "imei";
	public static final String TABLE_VISITRECORD_READER_INFO_IMSI = "imsi";
	public static final String TABLE_VISITRECORD_UPLOAD_STATE = "upload_sate";// 上传状态
	private static final String SQL_TABLE_VISITRECORD_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_VISITRECORD + " (" + TABLE_VISITRECORD_ID + " INTEGER PRIMARY KEY, " + TABLE_VISITRECORD_PROJECT_SERVERID + " INTEGER," + TABLE_VISITRECORD_READER_ID + " varchar(20)," + TABLE_VISITRECORD_READER_TIME + " long," + TABLE_VISITRECORD_READER_INFO_IP + " varchar(20), " + TABLE_VISITRECORD_READER_INFO_IMEI + " varchar(20), " + TABLE_VISITRECORD_READER_INFO_IMSI + " varchar(20), " + TABLE_VISITRECORD_UPLOAD_STATE + " INTEGER DEFAULT 0);";
	private static final String SQL_TABLE_VISITRECORD_CLEAR = "DROP TABLE IF EXISTS " + TABLE_VISITRECORD;

	/**
	 * 项目表(目前仅用于收藏记录)
	 */
	public static final String TABLE_PROJECT = "project";
	public static final String TABLE_PROJECT_ID = "_id";
	public static final String TABLE_PROJECT_SERVERID = "project_serverid";
	public static final String TABLE_PROJECT_NAME = "project_name";
	public static final String TABLE_PROJECT_COUNTRY = "country";
	public static final String TABLE_PROJECT_TUITION = "tuition";
	public static final String TABLE_PROJECT_TUITION_UNIT = "tuition_unit";
	public static final String TABLE_PROJECT_TUITION_TIMEUNIT = "tuition_timeunit";
	public static final String TABLE_PROJECT_DEGREE = "degree";
	public static final String TABLE_PROJECT_GPA = "gpa";
	public static final String TABLE_PROJECT_LANGUAGE = "language";
	public static final String TABLE_PROJECT_PUBLISHER_SERVERID = "publisher_serverid";
	public static final String TABLE_PROJECT_PUBLISHER_TYPE = "pubisher_type";
	public static final String TABLE_PROJECT_PUBLISHER_TEL = "publisher_tel";
	public static final String TABLE_PROJECT_LOGNAME = "logo_name";
	public static final String TABLE_PROJECT_ENTERTIME = "enter_time";
	public static final String TABLE_PROJECT_FAVOURSTATE = "favour_state";
	public static final String TABLE_PROJECT_FAVOURTIME = "favour_time";

	public static final String SQL_TABLE_PROJECT_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECT + " (" + TABLE_PROJECT_ID + " INTEGER PRIMARY KEY, " + TABLE_PROJECT_SERVERID + " INTEGER," + TABLE_PROJECT_NAME + " varchar(20)," + TABLE_PROJECT_COUNTRY + " varchar(20)," + TABLE_PROJECT_TUITION + " INTEGER," + TABLE_PROJECT_TUITION_UNIT + " varchar(20)," + TABLE_PROJECT_TUITION_TIMEUNIT + " varchar(20)," + TABLE_PROJECT_DEGREE + " varchar(20)," + TABLE_PROJECT_GPA + " varchar(20)," + TABLE_PROJECT_LANGUAGE + " varchar(20)," + TABLE_PROJECT_PUBLISHER_SERVERID + " INTEGER," + TABLE_PROJECT_PUBLISHER_TYPE + " INTEGER," + TABLE_PROJECT_PUBLISHER_TEL + " varchar(20)," + TABLE_PROJECT_LOGNAME + " varchar(20)," + TABLE_PROJECT_ENTERTIME + " varchar(20)," + TABLE_PROJECT_FAVOURSTATE + " INTEGER," + TABLE_PROJECT_FAVOURTIME + " long);";
	public static final String SQL_TABLE_PROJECT_CLEAR = "DROP TABLE IF EXISTS " + TABLE_PROJECT;

	/**
	 * 收藏项目同步记录（只记录同步后还属于收藏状态的）
	 */
	public static final String TABLE_SYNC_PRO_FAV_REC = "sync_project_favorite_record";
	public static final String TABLE_SYNC_PRO_FAV_REC_ID = "_id";
	public static final String TABLE_SYNC_PRO_FAV_REC_PID = "pid";

	public static final String SQL_TABLE_SYNC_PRO_FAV_REC_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_SYNC_PRO_FAV_REC + "(" + TABLE_SYNC_PRO_FAV_REC_ID + " INTEGER PRIMARY KEY, " + TABLE_SYNC_PRO_FAV_REC_PID + " INTEGER);";
	public static final String SQL_TABLE_SYNC_PRO_FAV_REC_CLEAR = "DROP TABLE IF EXISTS " + TABLE_SYNC_PRO_FAV_REC;

	/**
	 * 国家表
	 */
	public static final String TABLE_COUNTRY = "country";
	public static final String TABLE_COUNTRY_ID = "_id";
	public static final String TABLE_COUNTRY_SERVERID = "server_id";
	public static final String TABLE_COUNTRY_NAMECN = "name_cn";
	public static final String TABLE_COUNTRY_NAME_EN = "name_en";
	public static final String TABLE_COUNTRY_AREA = "area";// 洲
	public static final String TABLE_COUNTRY_ISFIRSTOF_AREA = "firstof_area";// 是否是洲里的第一个国家，若是则显示黑条（洲）

	public static final String SQL_TABLE_COUNTRY_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRY + " (" + TABLE_COUNTRY_ID + " INTEGER PRIMARY KEY, " + TABLE_COUNTRY_SERVERID + " INTEGER unique," + TABLE_COUNTRY_NAMECN + " varchar(20)," + TABLE_COUNTRY_NAME_EN + " varchar(20)," + TABLE_COUNTRY_AREA + " varchar(20)," + TABLE_COUNTRY_ISFIRSTOF_AREA + " INTEGER);";
	public static final String SQL_TABLE_COUNTRY_CLEAR = "DROP TABLE IF EXISTS " + TABLE_COUNTRY;

	/**
	 * 账户表
	 */
	public static final String TABLE_ACCOUNT = "accounts";
	public static final String TABLE_ACCOUNT_ID = "_id";
	public static final String TABLE_ACCOUNT_TYPE = "type";//
	public static final String TABLE_ACCOUNT_NAME = "name";// 账户名（昵称）
	public static final String TABLE_ACCOUNT_UID = "uid"; // string（站内账户id,第三方账户在站内注册的账户id）
	public static final String TABLE_ACCOUNT_THIRD_ID = "third_id"; // string（第三方平台上的id）
	public static final String TABLE_ACCOUNT_LOGO = "logo";// （账户logo） url
	public static final String TABLE_ACCOUNT_ACCESS_TOKEN = "access_token";
	public static final String TABLE_ACCOUNT_REFRESH_TOKEN = "refresh_token";
	public static final String TABLE_ACCOUNT_TOKEN_REQUEST_TIME = "request_time";// 申请token的时间 long
	public static final String TABLE_ACCOUNT_TOKEN_EXPIRES_IN = "expires_in";// 申请token时剩余的秒数 long (qq微博使用)
	public static final String TABLE_ACCOUNT_BIND_MOBILE = "bind_mobile";// 绑定手机号
	public static final String TABLE_ACCOUNT_IS_LOGIN = "is_login";// 是否是登录的账户
	public static final String TABLE_ACCOUNT_PWD = "pwd";//

	public static final String SQL_TABLE_ACCOUNT_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + " (" + TABLE_ACCOUNT_ID + " INTEGER PRIMARY KEY, " + TABLE_ACCOUNT_TYPE + " varchar(20)," + TABLE_ACCOUNT_IS_LOGIN + " INTEGER," + TABLE_ACCOUNT_NAME + " varchar(20)," + TABLE_ACCOUNT_THIRD_ID + " varchar(20)," + TABLE_ACCOUNT_UID + " varchar(20)," + TABLE_ACCOUNT_LOGO + " varchar(20)," + TABLE_ACCOUNT_PWD + " varchar(20)," + TABLE_ACCOUNT_ACCESS_TOKEN + " varchar(20)," + TABLE_ACCOUNT_REFRESH_TOKEN + " varchar(20)," + TABLE_ACCOUNT_TOKEN_REQUEST_TIME + " LONG," + TABLE_ACCOUNT_TOKEN_EXPIRES_IN + " varchar(20)," + TABLE_ACCOUNT_BIND_MOBILE + " varchar(20));";
	public static final String SQL_TABLE_ACCOUNT_CLEAR = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;

	public GooutDataBaseHelper(Context context) {
		/* 当调用getWritableDatabase()或 getReadableDatabase()方法时 则创建一个数据库 */
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* 数据库没有表时创建一个 */
		db.execSQL(SQL_TABLE_SCHOOL_CREATE);
		db.execSQL(SQL_TABLE_CONSULTRECORD_CREATE);
		db.execSQL(SQL_TABLE_VISITRECORD_CREATE);
		db.execSQL(SQL_TABLE_PROJECT_CREATE);
		db.execSQL(SQL_TABLE_COUNTRY_CREATE);
		db.execSQL(SQL_TABLE_ACCOUNT_CREATE);
		db.execSQL(SQL_TABLE_SYNC_PRO_FAV_REC_CREATE);
		db.execSQL(SQL_TABLE_SYNC_SCHOOL_FAV_REC_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 升级时重建表格
		Log.e(TAG, "Goout DB onUpgrade !");
		db.execSQL(SQL_TABLE_SCHOOL_CLEAR);
		db.execSQL(SQL_TABLE_CONSULTRECORD_CLEAR);
		db.execSQL(SQL_TABLE_VISITRECORD_CLEAR);
		db.execSQL(SQL_TABLE_PROJECT_CLEAR);
		db.execSQL(SQL_TABLE_COUNTRY_CLEAR);
		db.execSQL(SQL_TABLE_ACCOUNT_CLEAR);
		db.execSQL(SQL_TABLE_SYNC_PRO_FAV_REC_CLEAR);
		db.execSQL(SQL_TABLE_SYNC_SCHOOL_FAV_REC_CLEAR);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 降级时重建表格
		Log.e(TAG, "Goout DB onDowngrade !");
		db.execSQL(SQL_TABLE_SCHOOL_CLEAR);
		db.execSQL(SQL_TABLE_CONSULTRECORD_CLEAR);
		db.execSQL(SQL_TABLE_VISITRECORD_CLEAR);
		db.execSQL(SQL_TABLE_PROJECT_CLEAR);
		db.execSQL(SQL_TABLE_COUNTRY_CLEAR);
		db.execSQL(SQL_TABLE_ACCOUNT_CLEAR);
		db.execSQL(SQL_TABLE_SYNC_PRO_FAV_REC_CLEAR);
		db.execSQL(SQL_TABLE_SYNC_SCHOOL_FAV_REC_CLEAR);
		onCreate(db);
	}

}
