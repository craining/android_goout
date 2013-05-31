package com.c35.ptc.goout.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.request.AccountBindPhoneRequest;
import com.c35.ptc.goout.request.AccountLoginRequest;
import com.c35.ptc.goout.request.AccountMarkPhoneRequest;
import com.c35.ptc.goout.request.AccountRegRequest;
import com.c35.ptc.goout.request.AccountThirdLoginRequest;
import com.c35.ptc.goout.request.AccountThirdRegRequest;
import com.c35.ptc.goout.request.GetConsultantInfoOfProjectRequest;
import com.c35.ptc.goout.request.GetIntermediaryInfoOfProjectRequest;
import com.c35.ptc.goout.request.GetProjectInfoRequest;
import com.c35.ptc.goout.request.GetProjectListRequest;
import com.c35.ptc.goout.request.GetSchooMajorRequest;
import com.c35.ptc.goout.request.GetSchoolDegreeDetailRequest;
import com.c35.ptc.goout.request.GetSchoolDetailRequest;
import com.c35.ptc.goout.request.GetSchoolListRequest;
import com.c35.ptc.goout.request.GetSelectCountryListRequest;
import com.c35.ptc.goout.request.GetSelectMajorListRequest;
import com.c35.ptc.goout.request.SendCodeRequest;
import com.c35.ptc.goout.request.SyncFavoriteProRequest;
import com.c35.ptc.goout.request.SyncFavoriteSchoolRequest;
import com.c35.ptc.goout.request.UploadProjectConsultRecordRequest;
import com.c35.ptc.goout.request.UploadProjectVisitRecordRequest;
import com.c35.ptc.goout.response.AccountBindPhoneResponse;
import com.c35.ptc.goout.response.AccountLoginResponse;
import com.c35.ptc.goout.response.AccountMarkPhoneResponse;
import com.c35.ptc.goout.response.AccountRegResponse;
import com.c35.ptc.goout.response.AccountThirdLoginResponse;
import com.c35.ptc.goout.response.AccountThirdRegResponse;
import com.c35.ptc.goout.response.BaseResponse;
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
import com.c35.ptc.goout.response.SyncFavoriteProResponse;
import com.c35.ptc.goout.response.SyncFavoriteSchoolResponse;
import com.c35.ptc.goout.response.UploadProjectConsultRecordResponse;
import com.c35.ptc.goout.response.UploadProjectVisitRecordResponse;

/**
 * 与服务器通信
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-6
 */
public class ServerUtil {

	private static final String TAG = "ServerUtil";

	private static final boolean IS_POST_REQUEST = true;// 是否是POST请求方式
	private static final int TIME_OUT = 10000;// 连接超时时间
	private static final String CHARSET_UTF8 = "UTF-8";

	// private static final int GOOUT_SERVER_PORT = 8080;//请求的服务器端口

	// 单例
	private static ServerUtil uniqueInstance = null;

	private ServerUtil() {
	}

	public static ServerUtil getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ServerUtil();
		}
		return uniqueInstance;
	}

	/***************************************** 以下为发起不同的接口请求 *********************************************************/

	/**
	 * 获得某国家项目列表
	 * 
	 * @Description:
	 * @param countryServerId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public GetProjectListResponse getProjectList(int countryServerId) {
		GetProjectListRequest request = new GetProjectListRequest();
		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();

		NameValuePair nameValuePair1 = new BasicNameValuePair("countryId", countryServerId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);

		GetProjectListResponse response = new GetProjectListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得某院校项目列表
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public GetProjectListResponse getProjectListOfSchool(int serverSchoolId) {
		GetProjectListRequest request = new GetProjectListRequest();

		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();// 初始化RequestUrl

		NameValuePair nameValuePair1 = new BasicNameValuePair("schoolId", serverSchoolId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);
		GetProjectListResponse response = new GetProjectListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得某发布者的项目列表
	 * 
	 * @Description:
	 * @param publisherType
	 * @param publisherId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public GetProjectListResponse getProjectListOfSchool(int publisherType, int publisherId) {
		GetProjectListRequest request = new GetProjectListRequest();

		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();// 初始化RequestUrl

		NameValuePair nameValuePair1 = new BasicNameValuePair("pubisherId", publisherId + "");
		NameValuePair nameValuePair2 = new BasicNameValuePair("pubisherType", publisherType + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);
		params.add(nameValuePair2);
		GetProjectListResponse response = new GetProjectListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得项目信息
	 * 
	 * @Description:
	 * @param projectId
	 *            (必须设置，且>-1)
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public GetProjectInfoResponse getProjectInfo(int projectId) {
		GetProjectInfoRequest request = new GetProjectInfoRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();

		NameValuePair nameValuePair1 = new BasicNameValuePair("id", projectId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);

		GetProjectInfoResponse response = new GetProjectInfoResponse();
		try {
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得院校列表
	 * 
	 * @Description:
	 * @param countryId
	 *            必须大于-1
	 * @param majorId
	 *            为-1则忽略
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public GetSchoolListResponse getSchoolList(int countryId, int specialtryId) {
		GetSchoolListRequest request = new GetSchoolListRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("country", countryId + "");
		NameValuePair nameValuePair2 = new BasicNameValuePair("specialtry", specialtryId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);
		params.add(nameValuePair2);

		GetSchoolListResponse response = new GetSchoolListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得院校简介信息
	 * 
	 * @Description:
	 * @param schoolId
	 *            学校Id 必须合法
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public GetSchoolDetailResponse getSchoolDetail(int schoolId) {
		GetSchoolDetailRequest request = new GetSchoolDetailRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("school", schoolId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);

		GetSchoolDetailResponse response = new GetSchoolDetailResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得院校学位信息
	 * 
	 * @Description:
	 * @param schoolId
	 *            学校Id 必须合法
	 * @param degree
	 *            学位名称 必须合法
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public GetSchoolDegreeDetailResponse getSchoolDegreeDetail(int schoolId, String degree) {
		GetSchoolDegreeDetailRequest request = new GetSchoolDegreeDetailRequest();
		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("school", schoolId + "");
		NameValuePair nameValuePair2 = new BasicNameValuePair("degree", degree + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);
		params.add(nameValuePair2);

		GetSchoolDegreeDetailResponse response = new GetSchoolDegreeDetailResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得院校某学位所对应的专业信息
	 * 
	 * @Description:
	 * @param schoolId
	 *            院校id(必须设置)
	 * @param degree
	 *            学位名称(必须设置)
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public GetSchooMajorResponse getSchoolMajor(int schoolId, String degree) {
		GetSchooMajorRequest request = new GetSchooMajorRequest();
		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("school", schoolId + "");
		NameValuePair nameValuePair2 = new BasicNameValuePair("degree", degree);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);
		params.add(nameValuePair2);

		GetSchooMajorResponse response = new GetSchooMajorResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得国家列表
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public GetSelectCountryListResponse getSelectCountryList() {
		GetSelectCountryListRequest request = new GetSelectCountryListRequest();
		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();

		GetSelectCountryListResponse response = new GetSelectCountryListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, null, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 获得专业列表
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public GetSelectMajorListResponse getSelectMajorList() {
		GetSelectMajorListRequest request = new GetSelectMajorListRequest();
		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();

		GetSelectMajorListResponse response = new GetSelectMajorListResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, null, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 获得顾问信息
	 * 
	 * @Description:
	 * @param consultantId
	 *            顾问Id必须合法
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public GetConsultantInfoOfProjectResponse getConsultantInfoOfProject(int consultantId) {
		// TODO 代码优化
		GetConsultantInfoOfProjectRequest request = new GetConsultantInfoOfProjectRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("id", consultantId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);

		GetConsultantInfoOfProjectResponse response = new GetConsultantInfoOfProjectResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 获得中介信息
	 * 
	 * @Description:
	 * @param intermediaryId
	 *            中介Id必须合法
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public GetIntermediaryInfoOfProjectResponse getIntermediaryInfoOfProject(int intermediaryId) {
		// TODO 代码优化
		GetIntermediaryInfoOfProjectRequest request = new GetIntermediaryInfoOfProjectRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		NameValuePair nameValuePair1 = new BasicNameValuePair("id", intermediaryId + "");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(nameValuePair1);

		GetIntermediaryInfoOfProjectResponse response = new GetIntermediaryInfoOfProjectResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 
	 * @Description:
	 * @param projectId
	 *            int 项目id,必须参数
	 * @param callRuntime
	 *            int 联系时长，非必须参数
	 * @param callTime
	 *            string 拨打时间
	 * @param callerNumber
	 *            string 主叫电话, 必须参数
	 * @param callerIMSI
	 *            string 主叫IMSI,必须参数
	 * @param callerIMEI
	 *            strimg 主叫IMEI，必须参数
	 * @param calledNumber
	 *            string 被叫号码，必须参数
	 * @param calledType
	 *            int 被叫者类型(顾问1、中介0)，必须参数
	 * @param calledID
	 *            int 被叫者ID，非必须参数
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public UploadProjectConsultRecordResponse uploadProjectConsultRecord(int projectServerId, int callRuntime, String callTime, String callerNumber, String callerIMSI, String callerIMEI, String calledNumber, int calledType, int calledID) {
		UploadProjectConsultRecordRequest request = new UploadProjectConsultRecordRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("projectId", projectServerId + "");
		params.add(nameValuePair1);
		if (callRuntime > 0) {
			NameValuePair nameValuePair2 = new BasicNameValuePair("callRuntime", callRuntime + "");
			params.add(nameValuePair2);
		}
		if (callTime != null) {
			NameValuePair nameValuePair3 = new BasicNameValuePair("callTime", callTime);
			params.add(nameValuePair3);
		}
		if (callerNumber != null) {
			NameValuePair nameValuePair4 = new BasicNameValuePair("callerNumber", callerNumber);
			params.add(nameValuePair4);
		}

		if (callerIMSI != null) {
			NameValuePair nameValuePair5 = new BasicNameValuePair("callerIMSI", callerIMSI);
			params.add(nameValuePair5);
		}

		if (callerIMEI != null) {
			NameValuePair nameValuePair6 = new BasicNameValuePair("callerIMEI", callerIMEI);
			params.add(nameValuePair6);
		}

		if (calledNumber != null) {
			NameValuePair nameValuePair7 = new BasicNameValuePair("calledNumber", calledNumber);
			params.add(nameValuePair7);
		}
		NameValuePair nameValuePair8 = new BasicNameValuePair("calledType", calledType + "");
		params.add(nameValuePair8);

		NameValuePair nameValuePair9 = new BasicNameValuePair("calledID", calledID + "");
		params.add(nameValuePair9);

		UploadProjectConsultRecordResponse response = new UploadProjectConsultRecordResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 上传一条浏览记录
	 * 
	 * @Description:
	 * @param projectServerId
	 *            项目id,必须
	 * @param readerId
	 *            陌生人（0）或者用户（注册id); 不必须
	 * @param readerTime
	 *            浏览时间（空为服务器时间）;不必须
	 * @param readerInfo
	 *            浏览者信息（IP / IMEI / IMSI）;必须
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public UploadProjectVisitRecordResponse uploadProjectVisitRecord(int projectServerId, String readerId, String readerTime, String readerInfo) {
		UploadProjectVisitRecordRequest request = new UploadProjectVisitRecordRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("projectId", projectServerId + "");
		params.add(nameValuePair1);
		if (readerId == null) {
			readerId = "0";
		}
		NameValuePair nameValuePair2 = new BasicNameValuePair("readerId", readerId + "");
		params.add(nameValuePair2);
		if (readerTime != null) {
			NameValuePair nameValuePair3 = new BasicNameValuePair("readerTime", readerTime + "");
			params.add(nameValuePair3);
		}

		NameValuePair nameValuePair4 = new BasicNameValuePair("readerInfo", readerInfo + "");
		params.add(nameValuePair4);

		UploadProjectVisitRecordResponse response = new UploadProjectVisitRecordResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 请求发送验证码
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public SendCodeResponse sendCodeResponse(String mobileNumber) {
		SendCodeRequest request = new SendCodeRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("mobile", mobileNumber + "");
		params.add(nameValuePair1);

		SendCodeResponse response = new SendCodeResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 注册站内账户
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param code
	 * @param pwd
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public AccountRegResponse regAccount(String mobileNumber, String code, String pwd) {
		AccountRegRequest request = new AccountRegRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("mobile", mobileNumber + "");
		NameValuePair nameValuePair2 = new BasicNameValuePair("code", code + "");
		NameValuePair nameValuePair3 = new BasicNameValuePair("password", pwd + "");
		params.add(nameValuePair1);
		params.add(nameValuePair2);
		params.add(nameValuePair3);

		AccountRegResponse response = new AccountRegResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 登录站内账户
	 * 
	 * @Description:
	 * @param mobileNumber
	 * @param pwd
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public AccountLoginResponse loginAccount(String mobileNumber, String pwd) {
		AccountLoginRequest request = new AccountLoginRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("mobile", mobileNumber + "");
		NameValuePair nameValuePair3 = new BasicNameValuePair("password", pwd + "");
		params.add(nameValuePair1);
		params.add(nameValuePair3);

		AccountLoginResponse response = new AccountLoginResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 第三方账户注册
	 * 
	 * @Description:
	 * @param thirdId
	 * @param type
	 * @param nickname
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public AccountThirdRegResponse regAccountThird(String thirdId, String type, String nickname) {
		GoOutDebug.e(TAG, "regAccountThirdResponse  thirdId=" + thirdId + "  type=" + type + "   nickname=" + nickname);
		AccountThirdRegRequest request = new AccountThirdRegRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("uid", thirdId);
		NameValuePair nameValuePair2 = new BasicNameValuePair("type", type);
		NameValuePair nameValuePair3 = new BasicNameValuePair("nickName", nickname);
		params.add(nameValuePair1);
		params.add(nameValuePair2);
		params.add(nameValuePair3);

		AccountThirdRegResponse response = new AccountThirdRegResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 第三方账户登录
	 * 
	 * @Description:
	 * @param thirdId
	 * @param type
	 * @param nickname
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public AccountThirdLoginResponse loginAccountThird(String thirdId, String type) {
		GoOutDebug.e(TAG, "loginAccountThird  thirdId=" + thirdId + "  type=" + type);
		AccountThirdLoginRequest request = new AccountThirdLoginRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("uid", thirdId);
		NameValuePair nameValuePair2 = new BasicNameValuePair("type", type);
		params.add(nameValuePair1);
		params.add(nameValuePair2);

		AccountThirdLoginResponse response = new AccountThirdLoginResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 收藏项目同步
	 * 
	 * @Description:
	 * @param uid
	 * @param lastTime
	 *            最后一次同步时间
	 * @param deleted
	 *            要取消收藏的项目
	 * @param keeps
	 *            要收藏的项目
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public SyncFavoriteProResponse syncFavoriteProjects(Collection<Integer> deleted, Collection<Integer> keeps) {
		SyncFavoriteProResponse response = new SyncFavoriteProResponse();
		try {
			String url = SyncFavoriteProRequest.getCommond();
			List<NameValuePair> params = SyncFavoriteProRequest.initParams(deleted, keeps);
			sendRequest(response, url, params, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 收藏院校同步
	 * 
	 * @Description:
	 * @param uid
	 * @param lastTime
	 *            最后一次同步时间
	 * @param deleted
	 *            要取消收藏的项目
	 * @param keeps
	 *            要收藏的项目
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-4-18
	 */
	public SyncFavoriteSchoolResponse syncFavoriteSchools(Collection<Integer> deleted, Collection<Integer> keeps) {
		SyncFavoriteSchoolResponse response = new SyncFavoriteSchoolResponse();
		try {
			String url = SyncFavoriteSchoolRequest.getCommond();
			List<NameValuePair> params = SyncFavoriteSchoolRequest.initParams(deleted, keeps);
			sendRequest(response, url, params, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * mark phone
	 * 
	 * @Description:
	 * @param mobile
	 * @param device
	 * @param imei
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public AccountMarkPhoneResponse markPhone(String mobile, String device, String imei, String imsi) {

		GoOutDebug.e(TAG, "markPhone  mobile=" + mobile + "  device=" + device + "   imei=" + imei + "  imsi=" + imsi);
		AccountMarkPhoneRequest request = new AccountMarkPhoneRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("mobile", mobile);
		NameValuePair nameValuePair2 = new BasicNameValuePair("device", device);
		NameValuePair nameValuePair3 = new BasicNameValuePair("imei", imei);
		NameValuePair nameValuePair4 = new BasicNameValuePair("imsi", imsi);
		params.add(nameValuePair1);
		params.add(nameValuePair2);
		params.add(nameValuePair3);
		params.add(nameValuePair4);

		AccountMarkPhoneResponse response = new AccountMarkPhoneResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
		// mobile, device（设备型号）, imei
	}

	/**
	 * bind phone
	 * 
	 * @Description:
	 * @param mobile
	 * @param code
	 * @param uid
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public AccountBindPhoneResponse bindPhone(String mobile, String code, String uid) {

		GoOutDebug.e(TAG, "bindPhone  mobile=" + mobile + "  code=" + code + "   uid=" + uid);
		AccountBindPhoneRequest request = new AccountBindPhoneRequest();

		// 初始化RequestUrl
		String requestUrl = GoOutConstants.GOOUT_SERVER_API + request.getCommand();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		NameValuePair nameValuePair1 = new BasicNameValuePair("mobile", mobile);
		NameValuePair nameValuePair2 = new BasicNameValuePair("code", code);
		NameValuePair nameValuePair3 = new BasicNameValuePair("uid", uid);
		params.add(nameValuePair1);
		params.add(nameValuePair2);
		params.add(nameValuePair3);

		AccountBindPhoneResponse response = new AccountBindPhoneResponse();
		try {
			// 执行
			sendRequest(response, requestUrl, params, IS_POST_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
		// mobile, device（设备型号）, imei
	}

	/******************************** 以下为http get请求 **************************************/

	/**
	 * 发送请求
	 * 
	 * @Description:
	 * @param url
	 *            目标url
	 * @param params
	 *            参数
	 * @param isPost
	 *            是否为Post提交
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyongxing
	 * @date:2012-6-29
	 */
	public BaseResponse sendRequest(BaseResponse response, String url, List<NameValuePair> params, boolean isPost) {
		String result = null;
		try {
			if (isPost) {
				result = postMethod(url, params);
			} else {
				result = getMethod(url, params);
			}
			if (!StringUtil.isNull(result)) {
				response.initFeild(result);
			}
		} catch (UnsupportedEncodingException e) {
			GoOutDebug.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		} catch (ClientProtocolException e) {
			GoOutDebug.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		} catch (IOException e) {
			GoOutDebug.e(TAG, " sendRequest " + url + " error:" + e.getMessage());
		}

		return response;
	}

	private HttpURLConnection getConnection(String uri) {
		HttpURLConnection httpConn = null;
		URL url = null;
		try {
			url = new URL(uri);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setConnectTimeout(TIME_OUT);
			httpConn.setReadTimeout(TIME_OUT);
			// 打开读写属性，默认均为false
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setInstanceFollowRedirects(true);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return httpConn;
	}

	/**
	 * 提交POST请求
	 * 
	 * @Description:
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @see:
	 * @since:
	 * @author: huangyongxing
	 * @date:2012-8-8
	 */
	private String postMethod(String url, List<NameValuePair> params) throws IOException {

		HttpURLConnection conn = getConnection(url);
		if (conn == null) {
			return null;
		}
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setRequestProperty(" Content-Type ", " application/x-www-form-urlencoded ");

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		StringBuffer buffer = new StringBuffer();
		if (params != null) {
			int i = 0;
			for (NameValuePair value : params) {
				buffer.append(i == 0 ? "" : "&");
				buffer.append(value.getName());
				buffer.append("=");
				String v = value.getValue();
				// if(!StringUtil.isEmpty(v, true)){
				if (!StringUtil.isNull(v)) {
					buffer.append(URLEncoder.encode(v, CHARSET_UTF8));
				}
				i++;
			}
		}
		out.writeBytes(buffer.toString());
		out.flush();
		out.close(); // flush and close

		String result = null;
		GoOutDebug.i(TAG, "response code :" + conn.getResponseCode());
		if (conn.getResponseCode() == 200) {
			if ("gzip".equalsIgnoreCase(conn.getContentEncoding())) {
				result = requestResult(conn.getInputStream(), true);
			} else {
				result = requestResult(conn.getInputStream(), false);
			}
		}

		conn.disconnect();

		return result;
	}

	/**
	 * 提交GET请求，不用httpurlconnection是为了和4.0兼容
	 * 
	 * @Description:
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @see:
	 * @since:
	 * @author: huangyongxing
	 * @date:2012-8-8
	 */
	private String getMethod(String url, List<NameValuePair> params) throws IOException {
		StringBuffer buffer = new StringBuffer();

		// 拼url
		if (params != null) {
			int i = 0;
			for (NameValuePair value : params) {
				buffer.append(i == 0 ? "?" : "&");
				buffer.append(value.getName());
				buffer.append("=");
				String v = value.getValue();
				// if (!StringUtil.isEmpty(v, true)) {
				if (!StringUtil.isNull(v)) {
					buffer.append(URLEncoder.encode(value.getValue(), CHARSET_UTF8));
				}
				i++;
			}
		}

		HttpParams httpParams = new BasicHttpParams();
		// 设置超时时间
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(url + buffer.toString());
		GoOutDebug.i(TAG, "REQUEST URL:" + url + buffer.toString());
		HttpResponse response = client.execute(get);

		String result = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			Header header = entity.getContentEncoding();
			if (header == null || !"gzip".equalsIgnoreCase(header.getValue())) {
				result = requestResult(entity.getContent(), false);
			} else {
				result = requestResult(entity.getContent(), true);
			}
		}
		return result;
	}

	/**
	 * 解析服务器返回数据
	 * 
	 * @Description:
	 * @param is
	 * @param isGzip
	 * @return
	 * @throws IOException
	 * @see:
	 * @since:
	 * @author: huangyongxing
	 * @date:2012-8-8
	 */
	private String requestResult(InputStream is, boolean isGzip) throws IOException {
		BufferedReader bufferedReader = null;
		StringBuilder builder = new StringBuilder();
		try {
			if (isGzip) {
				is = new GZIPInputStream(is);
			}
			bufferedReader = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
			// 读取服务器返回数据，转换成BufferedReader
			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
				builder.append(s);
			}
		} finally {
			is.close();
			bufferedReader.close();
		}
		return builder.toString();
	}

}
