package com.c35.ptc.goout.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.bean.Code;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.bean.SchoolDegreeInfo;
import com.c35.ptc.goout.bean.SchoolMajorInfo;

public class JsonUtil {

	/**
	 * 解析json获取项目列表信息
	 * 
	 * @Description:
	 * @param jsonObj
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static ArrayList<Project> parseProjectList(String responseStr) {

		try {
			JSONObject obj = new JSONObject(responseStr);
			return parseProjectList(obj.getJSONArray("projectlist"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList<Project>();
	}

	/**
	 * 解析json获取项目列表信息
	 * 
	 * @Description:
	 * @param array
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	public static ArrayList<Project> parseProjectList(JSONArray array) {
		ArrayList<Project> projectList = new ArrayList<Project>();
		if (array != null) {
			Project project;
			try {
				for (int i = 0; i < array.length(); i++) {
					JSONObject projectJson = array.getJSONObject(i);
					JSONObject countryJson = projectJson.getJSONObject("country");
					project = new Project();
					project.setServerId(projectJson.getInt("id"));
					project.setName(projectJson.getString("projectName"));
					// project.setGpa(projectJson.getString("gpa"));
					project.setLanguage(projectJson.getString("language"));
					// 获得图片数组，第一个即列表logo
					JSONArray arryPhotos = projectJson.getJSONArray("photos");
					if (arryPhotos != null && arryPhotos.length() > 0) {
						project.setLogoName(arryPhotos.getString(0));
					}
					project.setDegree(projectJson.getString("degree"));
					project.setCountry(countryJson.getString("name"));
					project.setTuition(projectJson.getInt("tuition"));
					project.setTuitionUnit(projectJson.getString("tuitionUnit"));
					project.setTuitionTimeUnit(projectJson.getString("tuitionTimeUnit"));
					// GoOutDebug.e("JsonUtil", "pubisherType=" + projectJson.getInt("pubisherType") +
					// "pubisherId=" + projectJson.getInt("pubisherId"));
					if (projectJson.has("pubisherType")) {
						project.setPublisherType(projectJson.getInt("pubisherType"));
					}
					if (projectJson.has("pubisherId")) {
						project.setPublisherServerId(projectJson.getInt("pubisherId"));
					}
					// 收藏时间，只在同步收藏时才有
					if (projectJson.has("operateTime")) {
						Long operateTime = projectJson.getLong("operateTime");
						if (operateTime != null) {
							project.setFavourTime(operateTime);
						}
					}
					projectList.add(project);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return projectList;
	}

	/**
	 * 解析出项目详情
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	public static Project praseProjectInfo(String responseStr) {

		Project project = new Project();

		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONObject projectObj = obj.getJSONObject("project");
			JSONObject countryObj = projectObj.getJSONObject("country");
			JSONObject schoolObj = projectObj.getJSONObject("school");
			// JSONObject majorObj = projectObj.getJSONObject("specialtyCategory");

			// project.setLogoUrl(p.getLogoUrl());
			project.setDegree(projectObj.getString("degree"));
			// project.setName(p.getName());
			// 获得图片数组
			JSONArray arryPhotos = projectObj.getJSONArray("photos");
			if (arryPhotos != null && arryPhotos.length() > 0) {
				String[] urls = new String[arryPhotos.length()];
				for (int i = 0; i < urls.length; i++) {
					urls[i] = arryPhotos.getString(i);
				}
				project.setPicNames(urls);
			}

			project.setTuition(projectObj.getInt("tuition"));
			project.setTuitionTimeUnit(projectObj.getString("tuitionTimeUnit"));
			project.setTuitionUnit(projectObj.getString("tuitionUnit"));
			project.setServerFee(projectObj.getInt("serviceCharge"));
			project.setServerFeeUnit(projectObj.getString("serviceChargeUnit"));
			project.setCountry(countryObj.getString("name"));
			project.setEnterTime(projectObj.getString("startTime"));
			// project.setGpa(p.getGpa());
			project.setLanguage(projectObj.getString("language"));
			project.setIntro(projectObj.getString("desription"));

			// 与院校相关
			project.setSchoolServerId(schoolObj.getInt("id"));// 院校id
			project.setSchoolNameCn(schoolObj.getString("cnName"));
			project.setSchoolNameEn(schoolObj.getString("enName"));
			// // 与顾问相关
			// project.setPublisherGroup(p.getPublisherGroup());
			// project.setPublisherLogoUrl(p.getPublisherLogoUrl());
			// project.setPublisherName(p.getPublisherName());
			// project.setPublisherServerArea(p.getPublisherServerArea());
			project.setPublisherServerId(projectObj.getInt("pubisherId"));
			// GoOutDebug.e("JsonUtil", "pubisherId=" + projectObj.getInt("pubisherId"));
			// project.setPublisherTel(p.getPublisherTel());
			project.setPublisherType(projectObj.getInt("pubisherType"));

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/**
		 * \ {"project":{"country":{"id":2,"name":"美国","status":0},"degree":"学士","desription":"发送","endTime":
		 * "2013-03-09"
		 * ,"id":6,"language":"SAT, ","photos":[],"projectName":"金吉利留学服务","pubisherId":0,"pubisherType"
		 * :0,"school"
		 * :{"approved":0,"cnName":"牛津大学","complexRanking":0,"enName":"NiuJin","id":2},"serviceCharge"
		 * :0,"serviceChargeUnit"
		 * :"￥","specialtyCategory":{"cnName":"哲学","id":1},"startTime":"2013-03-05","tuition"
		 * :0,"tuitionUnit":"￥"}}
		 */

		return project;
	}

	/**
	 * 解析项目发布中介的详情
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static Project praseProjectPublisherIntermediaryInfo(String responseStr) {
		Project project = new Project();
		try {
			JSONObject obj = new JSONObject(responseStr);
			project.setPublisherLogoName(obj.getString("logo"));
			project.setPublisherName(obj.getString("name"));
			// TODO
			project.setPublisherTel(obj.getString("telphone"));
			// project.setPublisherTel("10086");

//			GoOutDebug.e("praseProjectPublisherIntermediaryInfo", "obj.getString(serviceArea)=" + obj.getString("serviceArea") + "    obj.getString(cusServiceArea)=" + obj.getString("cusServiceArea"));

			project.setPublisherServerArea(StringUtil.getString(obj.getString("serviceArea")) + StringUtil.getString(obj.getString("cusServiceArea")));
			project.setPublisherAddr(obj.getString("address"));
			// project.setPublisherCity(obj.getString("city"));
			// project.setPublisherProvince(obj.getString("province"));
			project.setPublisherProjectCount(obj.getInt("projectCount"));

			/**
			 * 
			 * {"id":2,"logo":null,"email":null, "address":"北京市崇文区崇文门外大街3号B座12层1201-1210室",
			 * "name":"北京嘉华世达国际教育交流有限公司", "projectCount":1,"serviceArea":null,"cusServiceArea":null,
			 * "telphone":null}
			 */

			/**
			 * id int 中介ID name string 中介名称 logo string logo图片 telphone string 联系电话 email string 邮箱
			 * serviceArea string 服务领域 cusServiceArea string 自定义服务领域 address string 地址 city string 城市 province
			 * string 省份 projectCount int 发布的项目数
			 */

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return project;
	}

	/**
	 * 解析项目发布顾问的详情
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static Project praseProjectPublisherConsultantInfo(String responseStr) {
		Project project = new Project();
		try {
			JSONObject obj = new JSONObject(responseStr);
			project.setPublisherLogoName(obj.getString("photo"));
			project.setPublisherName(obj.getString("name"));
			project.setPublisherTel(obj.getString("telphone"));
			project.setPublisherServerArea(StringUtil.getString(obj.getString("serviceArea")) + StringUtil.getString(obj.getString("cusServiceArea")));
			// project.setPublisherServerArea(obj.getString("serviceArea") + obj.getString("cusServiceArea"));
			project.setPublisherAddr(obj.getString("address"));
			// project.setPublisherCity(obj.getString("city"));
			// project.setPublisherProvince(obj.getString("province"));
			project.setPublisherProjectCount(obj.getInt("projectCount"));
			project.setPublisherInterName(obj.getString("interName"));

			/**
			 * id int 中介ID name string 中介名称 logo string logo图片 telphone string 联系电话 email string 邮箱
			 * serviceArea string 服务领域 cusServiceArea string 自定义服务领域 address string 地址 city string 城市 province
			 * string 省份 projectCount int 发布的项目数
			 */

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return project;
	}

	/**
	 * 解析json获取国家列表
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	public static ArrayList<Country> parseCountryList(String responseStr) {

		ArrayList<Country> countrylist = new ArrayList<Country>();
		Country country;

		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONArray arryArea = obj.getJSONArray("countriesList");

			for (int i = 0; i < arryArea.length(); i++) {
				JSONObject proJson = arryArea.getJSONObject(i);
				String area = proJson.getString("continent");// 获得洲
				JSONArray arryCountry = proJson.getJSONArray("countries");// 获得洲内国家数组
				for (int m = 0; m < arryCountry.length(); m++) {
					JSONObject proJsonCountry = arryCountry.getJSONObject(m);// 获得周国家数组里的某个国家json

					country = new Country();
					if (m == 0) {
						country.setFirstOfArea(true);
					}
					country.setArea(area);
					country.setNameCn(proJsonCountry.getString("name"));
					country.setServerId(proJsonCountry.getInt("id"));
					countrylist.add(country);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return countrylist;
	}

	/**
	 * 解析json获取大专业列表
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	public static ArrayList<Major> parseMajorList(String responseStr) {

		ArrayList<Major> majorList = new ArrayList<Major>();
		Major major;

		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONArray arryMajors = obj.getJSONArray("specialtiesList");

			for (int i = 0; i < arryMajors.length(); i++) {
				JSONObject proJson = arryMajors.getJSONObject(i);
				major = new Major();
				major.setNameCn(proJson.getString("cnName"));
				// major.setNameEn(proJson.getString("enName"));
				major.setSortKey("A");
				major.setServerId(proJson.getInt("id"));
				majorList.add(major);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return majorList;
	}

	/**
	 * 解析出院校列表
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	public static ArrayList<School> parseSchoolList(String responseStr) {

		try {
			JSONObject obj = new JSONObject(responseStr);
			return praseSchoolList(obj.getJSONArray("schoolList"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList<School>();
	}

	/**
	 * 解析院校列表
	 * 
	 * @Description:
	 * @param arrySchools
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public static ArrayList<School> praseSchoolList(JSONArray arrySchools) {

		ArrayList<School> schoolList = new ArrayList<School>();
		School school;

		try {
			// JSONArray arrySchools = obj.getJSONArray("schoolList");

			for (int i = 0; i < arrySchools.length(); i++) {
				JSONObject proJson = arrySchools.getJSONObject(i);
				school = new School();
				school.setNameCn(proJson.getString("cnName"));
				school.setNameEn(proJson.getString("enName"));
				school.setSortKey(proJson.getString("spellName"));
				school.setServerId(proJson.getInt("id"));
				school.setLogoName(proJson.getString("logoSrc"));
				// 收藏时间，只在同步收藏时才有
				if (proJson.has("operateTime")) {
					Long operateTime = proJson.getLong("operateTime");
					if (operateTime != null) {
						school.setFavourTime(operateTime);
					}
				}

				schoolList.add(school);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return schoolList;
		/**
		 * result: {
		 * 
		 * 
		 * 
		 * "schoolList":[
		 * 
		 * 
		 * {"id":1,"enName":"Air University (Maxwell AFB)","logoSrc":"1.jpg",
		 * "spellName":"Kong Jun Da Xue (Mai Ke Si Wei Er Kong Jun Ji De )", "cnName":"空军大学(麦克斯韦尔空军基地)"},
		 */
	}

	/**
	 * 解析院校详情
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static School praseSchoolInfo(String responseStr) {
		School school = new School();

		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONArray arryPhotos = obj.getJSONArray("photos");

			school.setServerId(obj.getInt("id"));
			school.setLogoName(obj.getString("logoSrc"));
			school.setCountryName(obj.getString("country"));

			if (arryPhotos != null && arryPhotos.length() > 0) {
				int count = arryPhotos.length();
				String[] names = new String[count];
				for (int i = 0; i < count; i++) {
					JSONObject photoJson = arryPhotos.getJSONObject(i);
					names[i] = photoJson.getString("photoSrc");
				}
				school.setPicsNames(names);
			}
			String degrees = obj.getString("degree");
			if (degrees != null && degrees.length() > 0) {
				if (degrees.contains(",")) {
					school.setDegrees(degrees.split(","));
				} else {
					school.setDegrees(new String[] { degrees });
				}
			}
			school.setIntro(obj.getString("description"));

			/**
			 * {"id":2235, "photos":[ {"id":2191,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"1.jpg","schoolId":2235},
			 * {"id":13405,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"2.jpg","schoolId":2235},
			 * {"id":13407,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"3.jpg","schoolId":2235},
			 * {"id":13412,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"4.jpg","schoolId":2235},
			 * {"id":13415,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"5.jpg","schoolId":2235},
			 * {"id":13416,"photoDes":"爱荷华大学(爱荷华市)","photoSrc":"6.jpg","schoolId":2235}],
			 * "enName":"University of Iowa (Iowa City)", "approved":0,
			 * "degree":"本科,硕士,博士,MBA,预科,语言中心,专科,网校,", "logoSrc":"2235.jpg", "nature":"公立",
			 * "website":"http:\/\/www.uiowa.edu\/", "cnName":"爱荷华大学(爱荷华市)", "country":"美国", "description":
			 */

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return school;
	}

	/**
	 * 解析学位详情
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-26
	 */
	public static SchoolDegreeInfo praseSchoolDegreeInfo(String responseStr) {

		SchoolDegreeInfo sdi = new SchoolDegreeInfo();
		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONObject expenditureObj = obj.getJSONObject("expenditure");
			JSONObject conditionObj = obj.getJSONObject("condition");
			JSONArray cutoffTimesObjArray = obj.getJSONArray("cutoffTimes");

			sdi.setEducationRequire(conditionObj.getString("educational"));// 学历要求
			// sdi.setGpa("3.0");
			sdi.setLanguage(conditionObj.getString("other"));// TODO 先暂时显示其他要求

			if (cutoffTimesObjArray != null && cutoffTimesObjArray.length() > 0) {
				String[] endTimes = new String[cutoffTimesObjArray.length()];
				for (int i = 0; i < cutoffTimesObjArray.length(); i++) {
					JSONObject endTimesObj = cutoffTimesObjArray.getJSONObject(i);
					endTimes[i] = endTimesObj.getString("cutoffTime");
				}
				sdi.setEndTimes(endTimes);
			}

			sdi.setTuituon(expenditureObj.getInt("tuition"));
			sdi.setTuituonTimeUnit(expenditureObj.getString("tuitionTimeUnit"));
			sdi.setTuituonUnit(expenditureObj.getString("tuitionUnit"));
			sdi.setScholarShip(expenditureObj.getInt("scholarship"));

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		/**
		 * { "expenditure": {"alimony":"12768-21600", "applyCost":100, "applyCostUnit":"美元", "booksCost":-1,
		 * "regCost":-1, "scholarship":-2, "tuition":63092, "tuitionTimeUnit":"全部", "tuitionUnit":"美元"},
		 * 
		 * 
		 * "condition":{ "educational":"本科毕业", "ibt":"100",
		 * "ielts":"7.0","other":"1、雅思：各部分均不得少于6.0分。2、要求提交GMAT考试成绩。"},
		 * 
		 * "cutoffTimes": [ {"cutoffTime":"1月15日","description":"提前录取的申请截止日期"},
		 * {"cutoffTime":"4月15日","description":"常规录取的申请截止日期"} ]
		 * 
		 * }
		 */
		return sdi;
	}

	/**
	 * 解析院校某学位下的专业
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-27
	 */
	public static SchoolMajorInfo praseSchoolMajorInfo(String responseStr) {
		SchoolMajorInfo smi = new SchoolMajorInfo();
		try {

			JSONObject obj = new JSONObject(responseStr);
			JSONArray majorArray = obj.getJSONArray("specialties");

			if (majorArray != null && majorArray.length() > 0) {
				String[] majorsCn = new String[majorArray.length()];
				String[] majorsEn = new String[majorArray.length()];
				for (int i = 0; i < majorArray.length(); i++) {
					JSONObject majorObj = majorArray.getJSONObject(i);
					majorsCn[i] = majorObj.getString("cnName");
				}
				for (int i = 0; i < majorArray.length(); i++) {
					JSONObject majorObj = majorArray.getJSONObject(i);
					majorsEn[i] = majorObj.getString("enName");
				}
				smi.setMajorsCn(majorsCn);
				smi.setMajorsEn(majorsEn);
			} else {
				return null;
			}

			/**
			 * { "specialties":[ { "cnName":"哲学xxx0", "enName":"xxxxxxx0", },{ "cnName":"哲学xxx1"
			 * "enName":"xxxxxxx1", },{ "cnName":"哲学xxx2" "enName":"xxxxxxx2", } ] }
			 */

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return smi;
	}

	/**
	 * 解析上传数据返回结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public static boolean parseUploadResult(String responseStr) {
		boolean result = false;
		try {
			JSONObject obj = new JSONObject(responseStr);
			result = obj.getBoolean("success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 解析发送验证码请求返回结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public static Code parseSendCodeRequestResult(String responseStr) {
		Code code = null;
		try {
			JSONObject obj = new JSONObject(responseStr);
			code = new Code();
			if(obj.has("error")) {
				JSONObject errorObj = obj.getJSONObject("error");
				code.setErrorCode(errorObj.getInt("errorCode"));
				code.setEffective(false);
			} else {
				code.setEffective(obj.getBoolean("result"));
				code.setCode(obj.getString("msmCode"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			code = null;
		}

		return code;

	}

	/**
	 * 解析注册站内账户的结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public static Account parseRegAccountRequestResult(String responseStr) {
		Account account = null;
		try {
			JSONObject obj = new JSONObject(responseStr);
			account = new Account();
			if (obj.has("error")) {
				JSONObject errorObj = obj.getJSONObject("error");
				account.setErrorCode(errorObj.getInt("errorCode"));
				account.setRegisterOk(false);
			} else {
				account.setRegisterOk(obj.getBoolean("result"));
				account.setUid(obj.getString("uid"));
				account.setThirdId(obj.getString("uid"));
			}

			/**
			 * 
			 * result: {"error":{"errorCode":1005},"request":"\/u_phoneReg.action"}
			 * 
			 * 
			 * 1005 手机号已经被注册绑定 1006 第三方账号已经被注册 1007 验证码错误
			 */
		} catch (Exception e) {
			e.printStackTrace();
			account = null;
		}

		return account;
	}

	/**
	 * 解析手机号码登录账户的结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public static Account parseLoginAccountRequestResult(String responseStr) {
		Account account = null;
		try {
			JSONObject obj = new JSONObject(responseStr);
			account = new Account();
			account.setLoginSuccess(obj.getBoolean("result"));
			if (account.isLoginSuccess()) {
				JSONObject userObj = obj.getJSONObject("user");
				account.setUid(userObj.getString("id"));
				account.setBindMobile(userObj.getString("mobilePhone"));
				account.setThirdId(userObj.getString("id"));
			}

			// account.setAccountName(userObj.getString("nickname"));
			// account.setLogo(userObj.getString("photo"));
			// account.setAccountType(Account.getThirdAccountTypeInt(userObj.getString("thirdType")));
			// account.setThirdId(userObj.getString("thirdUid"));
			/**
			 * result Boolean 登录是否成功 user object 用户信息 mobileCodes 返回字段值 类型及范围 说明 id string 用户id mobilePhone
			 * String 手机号码 nickname string 昵称 photo string 头像路径 thirdUid string 第三方账号ID thirdType string
			 * 第三方账号类型
			 */

			/**
			 * {"result":true,
			 * 
			 * "user":{
			 * 
			 * "id":"533db02beb6046128531d63ed7a09eb9",
			 * 
			 * "loginPwd":"111111",
			 * 
			 * "mobilePhone":"18210633121",
			 * 
			 * "registeredTime":"2013-04-18 11:55:54",
			 * 
			 * "updTime":"2013-04-18 11:55:54"} }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			account = null;
		}

		return account;
	}

	/**
	 * 解析第三方账户注册返回结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public static Account parseRegAccountThirdRequestResult(String responseStr) {

		Account account = null;
		try {
			JSONObject obj = new JSONObject(responseStr);
			account = new Account();
			if (obj.has("error")) {
				JSONObject errorObj = obj.getJSONObject("error");
				account.setErrorCode(errorObj.getInt("errorCode"));
				account.setRegisterOk(false);
			} else {
				account.setRegisterOk(obj.getBoolean("result"));
				account.setUid(obj.getString("uid"));
			}

			/**
			 * result: {"uid":"3569d6e1e2a14b62aa60417783259b5d","result":true}
			 */
		} catch (Exception e) {
			e.printStackTrace();
			account = null;
		}

		return account;
	}

	/**
	 * 解析第三方账户登录的结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public static Account parseLoginThirdAccountRequestResult(String responseStr) {
		Account account = null;
		try {
			JSONObject obj = new JSONObject(responseStr);
			account = new Account();
			account.setLoginSuccess(obj.getBoolean("result"));
			if (account.isLoginSuccess()) {
				JSONObject userObj = obj.getJSONObject("user");
				account.setUid(userObj.getString("id"));
				if (userObj.has("mobilePhone")) {
					account.setBindMobile(userObj.getString("mobilePhone"));
				}
				// account.setAccountName(userObj.getString("nickName"));
				// account.setLogo(userObj.getString("photo"));
				// account.setAccountType(userObj.getString("thirdType"));
				// account.setThirdId(userObj.getString("thirdUid"));
			}
			/**
			 * {"result":true,
			 * 
			 * "user":{"id":"3569d6e1e2a14b62aa60417783259b5d",
			 * 
			 * "nickName":"庄广钰","registeredTime":"2013-04-18 18:31:07",
			 * 
			 * "thirdType":"renren","thirdUid":"269295649",
			 * 
			 * "updTime":"2013-04-18 18:31:07"}}
			 */
		} catch (Exception e) {
			e.printStackTrace();
			account = null;
		}

		return account;
	}

	/**
	 * 解析绑定手机返回结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public static Account parseBindPhoneResult(String responseStr) {
		Account account = new Account();
		account.setRegisterOk(false);
		try {
			JSONObject obj = new JSONObject(responseStr);

			if (obj.has("error")) {
				JSONObject errorObj = obj.getJSONObject("error");
				account.setErrorCode(errorObj.getInt("errorCode"));
			} else if (obj.has("result")) {
				account.setRegisterOk(obj.getBoolean("result"));
				// account.setUid(obj.getString("uid"));
			}

			// code = new Code();
			// code.setEffective(obj.getBoolean("result"));
			// code.setCode(obj.getString("msmCode"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return account;

	}

	/**
	 * 解析验证码确定返回结果
	 * 
	 * @Description:
	 * @param responseStr
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	public static boolean parseMarkPhoneResult(String responseStr) {
		boolean result = false;
		try {
			JSONObject obj = new JSONObject(responseStr);
			if (obj.has("result")) {
				result = obj.getBoolean("result");
			}
			// code = new Code();
			// code.setEffective(obj.getBoolean("result"));
			// code.setCode(obj.getString("msmCode"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
}
