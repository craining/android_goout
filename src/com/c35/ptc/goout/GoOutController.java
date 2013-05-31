package com.c35.ptc.goout;

import java.util.ArrayList;

import android.content.Context;

import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.RecentlyConsult;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.db.TableAccount;
import com.c35.ptc.goout.db.TableConsultRecord;
import com.c35.ptc.goout.db.TableCountry;
import com.c35.ptc.goout.db.TableProject;
import com.c35.ptc.goout.db.TableSchool;
import com.c35.ptc.goout.interfaces.AccountBindPhoneListener;
import com.c35.ptc.goout.interfaces.AccountLoginListener;
import com.c35.ptc.goout.interfaces.AccountMarkPhoneListener;
import com.c35.ptc.goout.interfaces.AccountRegListener;
import com.c35.ptc.goout.interfaces.AccountThirdLoginListener;
import com.c35.ptc.goout.interfaces.GetConsultantInfoOfProjectListener;
import com.c35.ptc.goout.interfaces.GetIntermediaryInfoOfProjectListener;
import com.c35.ptc.goout.interfaces.GetProjectInfoListener;
import com.c35.ptc.goout.interfaces.GetProjectListListener;
import com.c35.ptc.goout.interfaces.GetSchooMajorListener;
import com.c35.ptc.goout.interfaces.GetSchoolDegreeDetailListener;
import com.c35.ptc.goout.interfaces.GetSchoolDetailListener;
import com.c35.ptc.goout.interfaces.GetSchoolListListener;
import com.c35.ptc.goout.interfaces.GetSelectCountryListListener;
import com.c35.ptc.goout.interfaces.GetSelectMajorListListener;
import com.c35.ptc.goout.interfaces.SendCodeListener;
import com.c35.ptc.goout.interfaces.UploadProjectConsultRecordListener;
import com.c35.ptc.goout.interfaces.UploadProjectVisitRecordListener;
import com.c35.ptc.goout.response.AccountBindPhoneResponse;
import com.c35.ptc.goout.response.AccountLoginResponse;
import com.c35.ptc.goout.response.AccountMarkPhoneResponse;
import com.c35.ptc.goout.response.AccountRegResponse;
import com.c35.ptc.goout.response.AccountThirdLoginResponse;
import com.c35.ptc.goout.response.AccountThirdRegResponse;
import com.c35.ptc.goout.response.GetConsultantInfoOfProjectResponse;
import com.c35.ptc.goout.response.GetIntermediaryInfoOfProjectResponse;
import com.c35.ptc.goout.response.GetProjectInfoResponse;
import com.c35.ptc.goout.response.GetProjectListResponse;
import com.c35.ptc.goout.response.GetSchooMajorResponse;
import com.c35.ptc.goout.response.GetSchoolDegreeDetailResponse;
import com.c35.ptc.goout.response.GetSchoolDetailResponse;
import com.c35.ptc.goout.response.GetSchoolListResponse;
import com.c35.ptc.goout.response.GetSelectCountryListResponse;
import com.c35.ptc.goout.response.GetSelectMajorListResponse;
import com.c35.ptc.goout.response.SendCodeResponse;
import com.c35.ptc.goout.response.UploadProjectConsultRecordResponse;
import com.c35.ptc.goout.response.UploadProjectVisitRecordResponse;
import com.c35.ptc.goout.util.ServerUtil;
import com.c35.ptc.goout.util.TimeUtil;

/**
 * 调用与服务器通信的方法，并将返回结果存入数据库
 * 
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class GoOutController {

	private static final String TAG = "GoOutController";

	// 单例
	private static GoOutController uniqueInstance = null;

	// private final Object mLockSaveSchools = new Object();

	private GoOutController() {
	}

	public static GoOutController getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new GoOutController();
		}
		return uniqueInstance;
	}

	/********************************* 以下与本地数据库相关 *******************************************/

	/**
	 * 获得本地项目收藏列表
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized ArrayList<Project> getFavProjects(Context con) {
		ArrayList<Project> list = new ArrayList<Project>();

		try {
			TableProject db = new TableProject(con);
			db.open();
			list = db.getFavProjectListAsTimeSort();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 收藏一条项目
	 * 
	 * @Description:
	 * @param con
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
	 * @param langage
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean favOneProject(Context con, int projectServerId, String projectNameCn, String country, int tuition, String tuitionUnit, String tuitionTimeUnit, String degree, int publisherId, int publisherType, String publisherTel, String logoName, String entreTime, int favourState, long favourTime, String gpa, String language) {
		GoOutDebug.v(TAG, "favOneProject");
		long result = -1;
		try {
			TableProject db = new TableProject(con);
			db.open();
			result = db.insertData(projectServerId, projectNameCn, country, tuition, tuitionUnit, tuitionTimeUnit, degree, publisherId, publisherType, publisherTel, logoName, entreTime, favourState, favourTime, gpa, language);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}

		return result != -1;
	}

	/**
	 * 根据项目的serverid取消收藏一条项目
	 * 
	 * @Description:
	 * @param con
	 * @param serverProjectId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean unFavOneProject(Context con, int serverProjectId) {
		boolean result = false;
		try {
			TableProject db = new TableProject(con);
			db.open();
			result = db.deleteData(serverProjectId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 判断项目是否已收藏
	 * 
	 * @Description:
	 * @param con
	 * @param serverProjectId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean getProjectFavState(Context con, int serverProjectId) {
		boolean result = false;
		try {
			TableProject db = new TableProject(con);
			db.open();
			result = db.getFavState(serverProjectId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 获得本地院校收藏列表
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized ArrayList<School> getFavSchools(Context con) {
		ArrayList<School> list = new ArrayList<School>();

		try {
			TableSchool db = new TableSchool(con);
			db.open();
			list = db.getFavSchoolListAsTimeSort();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 插入多条院校信息
	 * 
	 * @Description:
	 * @param con
	 * @param countryId
	 * @param schools
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	public synchronized void insertSchools(Context con, int countryId, ArrayList<School> schools) {

		GoOutDebug.v(TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!save schools");

		try {
			TableSchool local = new TableSchool(con);
			local.open();
			for (School s2 : schools) {
				local.insertData(s2.getServerId(), s2.getNameCn(), s2.getNameEn(), s2.getSortKey(), s2.getLogoName(), countryId, s2.getRanking(), s2.getFavourState(), s2.getFavourTime());
			}
			local.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 插入一条院校数据
	 * 
	 * @Description:
	 * @param con
	 * @param countryId
	 * @param schoolServerId
	 * @param schoolNameCn
	 * @param schoolNameEn
	 * @param sortKey
	 * @param logoName
	 * @param ranking
	 * @param favState
	 * @param favTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	public synchronized boolean insertOneSchool(Context con, int countryId, int schoolServerId, String schoolNameCn, String schoolNameEn, String sortKey, String logoName, int ranking, int favState, long favTime) {
		long result = -1;
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.insertData(schoolServerId, schoolNameCn, schoolNameEn, sortKey, logoName, countryId, ranking, favState, favTime);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		return result != -1;
	}

	/**
	 * 收藏一所院校
	 * 
	 * @Description:
	 * @param con
	 * @param schoolServerId
	 * @param favTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean favOneSchool(Context con, School school, long favTime) {
		boolean result = false;
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.updateSchoolFavourState(school, 1, favTime);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 取消收藏一所院校
	 * 
	 * @Description:
	 * @param con
	 * @param schoolServerId
	 * @param favTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean unFavOneSchool(Context con, School school) {
		boolean result = false;
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.updateSchoolFavourState(school, 0, 0);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;

	}

	/**
	 * 获得某院校收藏状态
	 * 
	 * @Description:
	 * @param con
	 * @param serverSchoolId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized boolean getSchoolFavState(Context con, int serverSchoolId) {
		boolean result = false;
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.getFavState(serverSchoolId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 得到本地某个院校的logo
	 * 
	 * @Description:
	 * @param con
	 * @param schoolServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public synchronized String getSchoolLogoUrl(Context con, int schoolServerId) {
		String result = "";
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.getSchoolLogoUrl(schoolServerId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 更新某个院校的logo
	 * 
	 * @Description:
	 * @param con
	 * @param serverSchoolId
	 * @param url
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public synchronized boolean updateSchoolLogoUrl(Context con, int serverSchoolId, String url) {
		boolean result = false;
		try {
			TableSchool db = new TableSchool(con);
			db.open();
			result = db.updateSchoolLogoUrl(serverSchoolId, url);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 插入一条联系记录
	 * 
	 * @Description:
	 * @param con
	 * @param projectId
	 * @param callRunTime
	 * @param callTime
	 * @param callerNumber
	 * @param imsi
	 * @param imei
	 * @param calledNumber
	 * @param calledType
	 * @param calledServerId
	 * @param isUpload
	 * @param calledName
	 * @param calledLogoUrl
	 * @param calledServerArea
	 * @param calledGroup
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	public synchronized boolean insertConsultRecord(Context con, int projectId, int callRunTime, long callTime, String callerNumber, String imsi, String imei, String calledNumber, int calledType, int calledServerId, int isUpload, String calledName, String calledLogoUrl, String calledServerArea, String calledGroup) {

		long result = -1;
		try {
			TableConsultRecord db = new TableConsultRecord(con);
			db.open();
			result = db.insertData(projectId, callRunTime, callTime, callerNumber, imsi, imei, calledNumber, calledType, calledServerId, isUpload, calledName, calledLogoUrl, calledServerArea, calledGroup);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}

		return result != -1;

	}

	/**
	 * 更新一条联系记录的上传状态
	 * 
	 * @Description:
	 * @param con
	 * @param callTime
	 * @param state
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	public synchronized boolean updateUploadStateOfConsultRecord(Context con, long callTime, int state) {
		boolean result = false;
		try {
			TableConsultRecord db = new TableConsultRecord(con);
			db.open();
			result = db.updateUploadStateByTime(callTime, state);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;

	}

	/**
	 * 更新最近联系人的logo
	 * 
	 * @Description:
	 * @param con
	 * @param logoName
	 * @param type
	 * @param serverId
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-28
	 */
	public synchronized void updateConsultRecordLogo(Context con, String logoName, int type, int serverId) {
		try {
			TableConsultRecord db = new TableConsultRecord(con);
			db.open();
			db.updateLogoIfChanged(logoName, type, serverId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获得最近联系列表
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized ArrayList<RecentlyConsult> getRecentlyCommunicateListAll(Context con) {
		ArrayList<RecentlyConsult> list = new ArrayList<RecentlyConsult>();

		try {
			TableConsultRecord db = new TableConsultRecord(con);
			db.open();
			list = db.getRecentlyConsultAll();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 获得上传失败的联系记录
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public synchronized ArrayList<RecentlyConsult> getRecentlyCommunicateListUploadFailed(Context con) {
		ArrayList<RecentlyConsult> list = new ArrayList<RecentlyConsult>();

		try {
			TableConsultRecord db = new TableConsultRecord(con);
			db.open();
			list = db.getRecentlyConsultDisUploaded();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 保存国家列表
	 * 
	 * @Description:
	 * @param con
	 * @param countries
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public synchronized void insertCountriesData(Context con, ArrayList<Country> countries) {
		try {
			TableCountry db = new TableCountry(con);
			db.open();
			for (Country c : countries) {
				db.insertData(c.getServerId(), c.getNameCn(), c.getNameEn(), c.getArea(), c.isFirstOfArea() ? 1 : 0);
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得本地国家列表
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public synchronized ArrayList<Country> getCountries(Context con) {
		ArrayList<Country> list = new ArrayList<Country>();

		try {
			TableCountry db = new TableCountry(con);
			db.open();
			list = db.getCountries();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/***** 与账户相关 *****/
	// /**
	// * 插入一个账户
	// *
	// * @Description:
	// * @param con
	// * @param accountType
	// * @param accountName
	// * @param serverId
	// * @param logo
	// * @param token
	// * @param requestTime
	// * @param openId
	// * @param expiresIn
	// * @param bindMobile
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-17
	// */
	// public synchronized boolean insertAccount(Context con, int accountType, String accountName, String uid,
	// String logo, String refreshToken, String accessToken, long requestTime, String openId, String
	// expiresIn, String bindMobile, int isLogin, String pwd) {
	// long result = -1;
	// try {
	// TableAccount db = new TableAccount(con);
	// db.open();
	// result = db.insertData(accountType, accountName, uid, logo, refreshToken, accessToken, requestTime,
	// openId, expiresIn, bindMobile, isLogin, pwd);
	// db.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result != -1;
	// }

	/**
	 * 插入一个账户
	 * 
	 * @Description:
	 * @param con
	 * @param accountType
	 * @param accountName
	 * @param serverId
	 * @param logo
	 * @param token
	 * @param requestTime
	 * @param openId
	 * @param expiresIn
	 * @param bindMobile
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public synchronized boolean insertAccount(Context con, Account account) {
		long result = -1;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			result = db.insertData(account.getAccountType(), account.getAccountName(), account.getUid(), account.getLogo(), account.getRefreshToken(), account.getAccessToken(), account.getRequestTime(), account.getExpiresIn(), account.getBindMobile(), account.getIsLogin(), account.getPwd(), account.getThirdId());
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result != -1;
	}

	/**
	 * 清除一个账号的token(已登录的第三方账户解绑时用)
	 * 
	 * @Description:
	 * @param con
	 * @param type
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-22
	 */
	public synchronized boolean deleteAccountToken(Context con, String type) {
		boolean result = false;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			result = db.deleteTokenByType(type);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 退出账户（不清除token，不解绑）
	 * 
	 * @Description:
	 * @param con
	 * @param type
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-23
	 */
	public synchronized boolean exitAccount(Context con, int accountLocalId) {
		boolean result = false;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			result = db.exitAccountByLoacalId(accountLocalId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得当前登录的账户
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public synchronized Account getNowLoginAccount(Context con) {
		Account account = null;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			account = db.getNowLoginAccount();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	/**
	 * 删除一条账户数据
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public synchronized boolean deleteOneAccount(Context con, int localId) {
		boolean result = false;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			result = db.deleteAccountByLocalId(localId);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得某个类型的账户
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public synchronized Account getLocalAccountInfo(Context con, String type) {
		Account account = null;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			account = db.getAccount(type);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	/**
	 * 获得所有账户列表
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public synchronized ArrayList<Account> getLocalAccounts(Context con) {
		ArrayList<Account> accounts = null;
		try {
			TableAccount db = new TableAccount(con);
			db.open();
			accounts = db.getAllAccounts();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accounts;
	}

	/***************************** 以下与网络通信相关 ***********************************************/

	/**
	 * 获取某国家的项目列表
	 * 
	 * @Description:
	 * @param countryId
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public void getPorjectList(final int countryId, final GetProjectListListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetProjectListResponse response = serverUtil.getProjectList(countryId);

					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getProjectListFinished(response.getListProjects());
					} else {
						listener.getProjectListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getProjectListFailed();
				}

			}
		}).start();
	}

	/**
	 * 获取某院校的项目列表
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getPorjectListOfSchool(final int serverSchoolId, final GetProjectListListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetProjectListResponse response = serverUtil.getProjectListOfSchool(serverSchoolId);

					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getProjectListFinished(response.getListProjects());
					} else {
						listener.getProjectListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getProjectListFailed();
				}

			}
		}).start();
	}

	/**
	 * 获取某发布者的项目列表
	 * 
	 * @Description:
	 * @param publisherType
	 * @param publisherId
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public void getPorjectListOfPublisher(final int publisherType, final int publisherId, final GetProjectListListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetProjectListResponse response = serverUtil.getProjectListOfSchool(publisherType, publisherId);

					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getProjectListFinished(response.getListProjects());
					} else {
						listener.getProjectListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getProjectListFailed();
				}

			}
		}).start();
	}

	/**
	 * 获取项目信息
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getProjectInfo(final int projectId, final GetProjectInfoListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetProjectInfoResponse response = serverUtil.getProjectInfo(projectId);
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getProjectInfoFinished(response.getProject());
					} else {
						listener.getProjectInfoFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getProjectInfoFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取院校列表
	 * 
	 * @Description:
	 * @param countryId
	 * @param specialtryId
	 *            -1表示所有专业
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void getSchoolList(final int countryId, final int specialtryId, final GetSchoolListListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();

					GetSchoolListResponse response = serverUtil.getSchoolList(countryId, specialtryId);

					if (response != null && !(response.getResponseData().equals(""))) {
						// TODO
						// ArrayList<School> schools = new ArrayList<School>();
						// schools = ;
						// if (schools != null && schools.size() > 0) {
						// // TODO存库！！！！
						// insertSchools();
						//
						// } else {
						// listener.getSchoolListFinished(true);
						// }

						listener.getSchoolListFinished(response.getListSchools());

					} else {
						listener.getSchoolListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getSchoolListFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取院校简介信息
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getSchoolDetail(final int schoolId, final GetSchoolDetailListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetSchoolDetailResponse response = serverUtil.getSchoolDetail(schoolId);
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getSchoolDetailFinished(response.getSchoolInfo());
					} else {
						listener.getSchoolDetailFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getSchoolDetailFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取院校学位信息
	 * 
	 * @Description:
	 * @param schoolId
	 * @param degree
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-7
	 */
	public void getSchoolDegreeDetail(final int schoolId, final String degree, final GetSchoolDegreeDetailListener listener) {
		GoOutDebug.e(TAG, "getSchoolDegreeDetail = " + degree);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetSchoolDegreeDetailResponse response = serverUtil.getSchoolDegreeDetail(schoolId, degree);
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getSchoolDegreeDetailFinished(response.getDegreeInfo());
					} else {
						listener.getSchoolDegreeDetailFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getSchoolDegreeDetailFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取院校专业
	 * 
	 * @Description:
	 * @param schoolId
	 * @param degree
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-7
	 */
	public void getSchoolMajor(final int schoolId, final String degree, final GetSchooMajorListener listener) {
		GoOutDebug.e(TAG, "getSchoolMajor = " + degree);
		// 由于一次加载多个学位的专业，因此线程放到activity里
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		try {
			ServerUtil serverUtil = ServerUtil.getInstance();
			GetSchooMajorResponse response = serverUtil.getSchoolMajor(schoolId, degree);
			if (response != null && !(response.getResponseData().equals(""))) {
				listener.getSchooMajorFinished(response.getMajorInfo());
			} else {
				listener.getSchooMajorFailed();
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.getSchooMajorFailed();
		}
		// }
		// }).start();
	}

	/**
	 * 获取国家列表
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getSelectCountryList(final GetSelectCountryListListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetSelectCountryListResponse response = serverUtil.getSelectCountryList();
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getSelectCountryListFinished(response.getListCountry());
					} else {
						listener.getSelectCountryListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getSelectCountryListFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取专业列表
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getSelectMajorList(final GetSelectMajorListListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetSelectMajorListResponse response = serverUtil.getSelectMajorList();
					if (response != null && !(response.getResponseData().equals(""))) {
						ArrayList<Major> majors = new ArrayList<Major>();
						majors = response.getMajorList();
						listener.getSelectMajorListFinished(majors);
					} else {
						listener.getSelectMajorListFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.getSelectMajorListFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取顾问信息
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getConsultantInfoOfProject(final int consultantId, final GetConsultantInfoOfProjectListener listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetConsultantInfoOfProjectResponse response = serverUtil.getConsultantInfoOfProject(consultantId);
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getConsultantInfoFinished(response.getProjectPublisher());
					} else {
						listener.getConsultantInfoFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getConsultantInfoFailed();
				}
			}
		}).start();
	}

	/**
	 * 获取中介信息
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void getIntermediaryInfoOfProject(final int intermediaryId, final GetIntermediaryInfoOfProjectListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					GetIntermediaryInfoOfProjectResponse response = serverUtil.getIntermediaryInfoOfProject(intermediaryId);
					if (response != null && !(response.getResponseData().equals(""))) {
						listener.getIntermediaryInfoFinished(response.getProjectPublisher());
					} else {
						listener.getIntermediaryInfoFailed();
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getIntermediaryInfoFailed();
				}

			}
		}).start();
	}

	/**
	 * 上传联系记录
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void uploadOneConsultRecork(final int projectServerId, final int callRuntime, final long callTime, final String callerNumber, final String callerIMSI, final String callerIMEI, final String calledNumber, final int calledType, final int calledID, final UploadProjectConsultRecordListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					UploadProjectConsultRecordResponse response = serverUtil.uploadProjectConsultRecord(projectServerId, callRuntime, TimeUtil.longToDateTimeString(callTime), callerNumber, callerIMSI, callerIMEI, calledNumber, calledType, calledID);
					if (response.isUploadSuccess()) {
						// 上传成功
						listener.uploadOneConsultRecorkFinished(callTime);
					} else {
						listener.uploadOneConsultRecorkFailed(callTime);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.uploadOneConsultRecorkFailed(callTime);
				}
			}
		}).start();
	}

	/**
	 * 上传访问记录
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public void uploadOneVisitRecork(final int projectServerId, final String readerId, final String readerTime, final String readerInfo, final UploadProjectVisitRecordListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					UploadProjectVisitRecordResponse response = serverUtil.uploadProjectVisitRecord(projectServerId, readerId, readerTime, readerInfo);
					if (response.isUploadSuccess()) {
						// 上传成功
						listener.uploadOneVisitRecorkFinished();
					} else {
						listener.uploadOneVisitRecorkFailed();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.uploadOneVisitRecorkFailed();
				}
			}
		}).start();
	}

	/**
	 * 请求发送验证码
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public void getCode(final String mobileNumber, final SendCodeListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					SendCodeResponse response = serverUtil.sendCodeResponse(mobileNumber);
					if (response.isRequestOk()) {
						// 请求成功
						listener.sendCodeRequestFinish(response.getCodeReturn().getCode());
					} else {
						listener.sendCodeRequestFail(response.getErrorCode());
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.sendCodeRequestFail(-1);
				}
			}
		}).start();

	}

	/**
	 * 注册站内账户
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public void regAccount(final String code, final String password, final String mobileNumber, final AccountRegListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountRegResponse response = serverUtil.regAccount(mobileNumber, code, password);
					if (response.isRegSuccess()) {
						// 请求成功
						listener.regAccountFinished(response.getAccount());
					} else {
						listener.regAccountFailed(response.getAccount() == null ? 0 : response.getAccount().getErrorCode());
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.regAccountFailed(0);
				}
			}
		}).start();

	}

	/**
	 * 登录站内账户
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public void loginAccount(final String password, final String mobileNumber, final AccountLoginListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountLoginResponse response = serverUtil.loginAccount(mobileNumber, password);
					if (response.getLoginSuccess()) {
						// 请求成功
						listener.accountLoginSuccess(response.getAccount());
					} else {
						listener.accountLoginFail();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.accountLoginFail();
				}
			}
		}).start();

	}

	/**
	 * 第三方账号注册
	 * 
	 * @Description:
	 * @param code
	 * @param password
	 * @param mobileNumber
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public void regAccountThird(final String thirdId, final String type, final String nickname, final AccountRegListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountThirdRegResponse response = serverUtil.regAccountThird(thirdId, type, nickname);
					if (response.isRegSuccess()) {
						// 请求成功
						listener.regAccountFinished(response.getAccount());
					} else {
						listener.regAccountFailed(response.getAccount() == null ? 0 : response.getAccount().getErrorCode());
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.regAccountFailed(0);
				}
			}
		}).start();

	}

	/**
	 * 登录第三方账户
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public void loginAccountThird(final String thirdId, final String type, final AccountThirdLoginListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountThirdLoginResponse response = serverUtil.loginAccountThird(thirdId, type);
					if (response.getLoginSuccess()) {
						// 请求成功
						listener.accountLoginSuccess(response.getAccount());
					} else {
						listener.accountLoginFail();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.accountLoginFail();
				}
			}
		}).start();

	}

	/**
	 * mark phone
	 * 
	 * @Description:
	 * @param thirdId
	 * @param type
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public void markPhone(final String mobile, final String device, final String imei, final String imsi, final AccountMarkPhoneListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountMarkPhoneResponse response = serverUtil.markPhone(mobile, device, imei, imsi);
					if (response.getMarkSuccess()) {
						// 请求成功
						listener.accountMarkFinished(true);
					} else {
						listener.accountMarkFail();
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.accountMarkFail();
				}
			}
		}).start();

	}

	/**
	 * mark phone
	 * 
	 * @Description:
	 * @param thirdId
	 * @param type
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public void bindPhone(final String mobile, final String code, final String uid, final AccountBindPhoneListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerUtil serverUtil = ServerUtil.getInstance();
					AccountBindPhoneResponse response = serverUtil.bindPhone(mobile, code, uid);
					if (response.getBindSuccess()) {
						// 请求成功
						listener.bindSuccess();
					} else {
						listener.bindFail(response.getErrorCode());
					}

				} catch (Exception e) {
					e.printStackTrace();
					listener.bindFail(0);
				}
			}
		}).start();

	}

}
