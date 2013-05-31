package com.c35.ptc.goout;

import java.util.ArrayList;

import android.content.Context;

import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.bean.SchoolDegreeInfo;
import com.c35.ptc.goout.bean.SchoolMajorInfo;
import com.c35.ptc.goout.db.TableSchool;
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
import com.c35.ptc.goout.interfaces.UploadProjectConsultRecordListener;
import com.c35.ptc.goout.interfaces.UploadProjectVisitRecordListener;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.TimeUtil;

/**
 * 测试类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */

public class GoOutTest {

	public static boolean test = false;// 是否是测试状态(假数据)
	public static boolean testShowSchoolSize = false;// 是否 显示院校数目

	private static final String TAG = "TestData";

	public static String imageOnlineUrltest1 = "http://img1.ph.126.net/0EFLTYCtF34BWWQD2uYUfA==/1674213161477008091.jpg";
	public static String imageOnlineUrltest2 = "http://img154.ph.126.net/0PheqjVxyZWATB3crovGOg==/2263621762708573375.jpg";
	public static String imageOnlineUrltest3 = "http://img2.ph.126.net/9o6XDiqM-yzITUgtshaW8Q==/1876875144708652442.jpg";
	public static String imageOnlineUrltest4 = "http://img5.ph.126.net/uny9qYtI6zV3q4fIIGALjw==/2043226855944496405.jpg";
	public static String imageOnlineUrltest5 = "http://img9.ph.126.net/SPAovsm8AugXzvoaQkRdBw==/3100165393492249859.jpg";
	public static String imageOnlineUrltest6 = "http://img8.ph.126.net/IrJY1Gg9NRf6GXOZ-bwSVg==/3256384005567288058.jpg";
	public static String imageOnlineUrltest7 = "http://img5.ph.126.net/u7X6bKafHIe3pubnnGiyZQ==/633600172576755185.jpg";
	public static String imageOnlineUrltest8 = "http://img7.ph.126.net/ZIRor5dJ_uVjSOG7E4pBBQ==/1401463909043931397.jpg";

	public static String[] testImagesUrl = { imageOnlineUrltest1, imageOnlineUrltest2, imageOnlineUrltest3, imageOnlineUrltest4, imageOnlineUrltest5, imageOnlineUrltest6, imageOnlineUrltest7, imageOnlineUrltest8 };

	public static ArrayList<Project> getTestProjectListData() {
		ArrayList<Project> result = new ArrayList<Project>();

		int serverId = 1;

		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称项目名称项目名称项目名称项目名称" + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("英语");
			p.setGpa("3.0");
			p.setDegree("高中");
			result.add(p);
		}
		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称 " + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("法语");
			p.setGpa("4.0");
			p.setDegree("本科");
			result.add(p);
		}
		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称 " + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("法语");
			p.setGpa("4.0");
			p.setDegree("硕士");
			result.add(p);
		}
		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称 " + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("法语");
			p.setGpa("4.0");
			p.setDegree("博士");
			result.add(p);
		}
		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称 " + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("法语");
			p.setGpa("4.0");
			p.setDegree("预科");
			result.add(p);
		}
		for (int i = 0; i < 5; i++) {
			serverId += i;
			Project p = new Project();
			p.setServerId(serverId);
			p.setName("项目名称 " + serverId);
			p.setLogoName(imageOnlineUrltest1);
			p.setCountry("美国");
			p.setEnterTime("2013-05-15");
			p.setTuitionUnit("$");
			p.setTuition(2000);
			p.setTuitionTimeUnit("年");
			p.setLanguage("法语");
			p.setGpa("4.0");
			p.setDegree("本科");
			result.add(p);
		}

		return result;
	}

	public static ArrayList<School> getTestSchoolListData(Context con) {
		ArrayList<School> result = new ArrayList<School>();
		School s;
		int ranking = 1;

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哈佛" + ranking);
		s.setNameEn("Harvard");
		s.setSortKey("HAFO");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("牛津" + ranking);
		s.setNameEn("Oxford");
		s.setSortKey("NIUJIN");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("耶鲁" + ranking);
		s.setNameEn("Yale");
		s.setSortKey("YELU");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哈佛" + ranking);
		s.setNameEn("Harvard");
		s.setSortKey("HAFO");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哈佛" + ranking);
		s.setNameEn("Harvard");
		s.setSortKey("HAFO");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("牛津" + ranking);
			s.setNameEn("Oxford");
			s.setSortKey("NIUJIN");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哈佛" + ranking);
		s.setNameEn("Harvard");
		s.setSortKey("HAFO");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("耶鲁" + ranking);
			s.setNameEn("Yale");
			s.setSortKey("YELU");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("芝加哥大学" + ranking);
			s.setNameEn("Chicago");
			s.setSortKey("ZHIJIAGE");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哈佛" + ranking);
		s.setNameEn("Harvard");
		s.setSortKey("HAFO");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("哈佛" + ranking);
			s.setNameEn("Harvard");
			s.setSortKey("HAFO");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		s = new School();
		s.setServerId(ranking);
		s.setNameCn("哥伦比亚大学" + ranking);
		s.setNameEn("Columbia");
		s.setSortKey("GELUNBIYA");
		s.setRanking(ranking++);
		s.setLogoName(imageOnlineUrltest8);
		result.add(s);

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("芝加哥大学" + ranking);
			s.setNameEn("Chicago");
			s.setSortKey("ZHIJIAGE");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		for (int i = 0; i < 5; i++) {
			s = new School();
			s.setServerId(ranking);
			s.setNameCn("哥伦比亚大学" + ranking);
			s.setNameEn("Columbia");
			s.setSortKey("GELUNBIYA");
			s.setRanking(ranking++);
			s.setLogoName(imageOnlineUrltest8);
			result.add(s);
		}

		TableSchool local = new TableSchool(con);
		local.open();
		for (School s2 : result) {

			local.insertData(s2.getServerId(), s2.getNameCn(), s2.getNameEn(), s2.getSortKey(), s2.getLogoName(), 1, s2.getRanking(), s2.getFavourState(), s2.getFavourTime());
		}
		local.close();
		return result;

	}

	public static ArrayList<Country> getTestCountryDataList() {
		ArrayList<Country> result = new ArrayList<Country>();
		Country c;

		for (int i = 0; i < 10; i++) {
			c = new Country();
			if (i == 0) {
				c.setFirstOfArea(true);
			} else {
				c.setFirstOfArea(false);
			}

			c.setArea("欧洲");
			c.setNameCn("英国");
			result.add(c);
		}

		for (int j = 0; j < 10; j++) {
			c = new Country();
			if (j == 0) {
				c.setFirstOfArea(true);
			} else {
				c.setFirstOfArea(false);
			}

			c.setArea("美洲");
			c.setNameCn("美国");
			result.add(c);
		}
		for (int k = 0; k < 10; k++) {
			c = new Country();
			if (k == 0) {
				c.setFirstOfArea(true);
			} else {
				c.setFirstOfArea(false);
			}

			c.setArea("澳洲");
			c.setNameCn("澳大利亚");
			result.add(c);
		}
		for (int l = 0; l < 10; l++) {
			c = new Country();
			if (l == 0) {
				c.setFirstOfArea(true);
			} else {
				c.setFirstOfArea(false);
			}

			c.setArea("非洲");
			c.setNameCn("刚果");
			result.add(c);
		}
		return result;
	}

	public static ArrayList<Major> getTestMajorDataList() {
		ArrayList<Major> result = new ArrayList<Major>();
		Major c;

		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("C");
			c.setNameCn("传媒");
			result.add(c);
		}
		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("F");
			c.setNameCn("法学");
			result.add(c);
		}
		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("H");
			c.setNameCn("化学");
			result.add(c);
		}
		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("J");
			c.setNameCn("金融");
			result.add(c);
		}

		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("N");
			c.setNameCn("农业");
			result.add(c);
		}
		for (int i = 0; i < 5; i++) {
			c = new Major();
			if (i == 0) {
				c.setFirstOfSortKey(true);
			} else {
				c.setFirstOfSortKey(false);
			}

			c.setSortKey("Z");
			c.setNameCn("哲学");
			result.add(c);
		}

		return result;
	}

	/**
	 * 测试接口
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	public static void testInterfacesOfServer(Context con) {
		GoOutController controller = GoOutController.getInstance();

		// 测试获取项目列表
		controller.getPorjectList(1, new GetProjectListListener() {

			@Override
			public void getProjectListFailed() {
				super.getProjectListFailed();
				GoOutDebug.e(TAG, "getProjectListFailed");
			}

			@Override
			public void getProjectListFinished(ArrayList<Project> projects) {
				super.getProjectListFinished(projects);
				GoOutDebug.e(TAG, "getProjectListFinished!!!!!!!!!!!!!!!");
			}
		});

		// 获得项目信息
		controller.getProjectInfo(1, new GetProjectInfoListener() {

			@Override
			public void getProjectInfoFailed() {
				super.getProjectInfoFailed();
				GoOutDebug.e(TAG, "getProjectInfoFailed");
			}

			@Override
			public void getProjectInfoFinished(Project p) {
				super.getProjectInfoFinished(p);
				GoOutDebug.e(TAG, "getProjectInfoFinished!!!!!!!!!!!!!!!");
			}

		});

		// 获得院校列表

		controller.getSchoolList(1, -1, new GetSchoolListListener() {

			@Override
			public void getSchoolListFailed() {
				super.getSchoolListFailed();
				GoOutDebug.e(TAG, "getSchoolListFailed");
			}

			@Override
			public void getSchoolListFinished(ArrayList<School> school) {
				super.getSchoolListFinished(school);
				GoOutDebug.e(TAG, "getSchoolListFinished!!!!!!!!!!!!!!! ");
			}
		});
		// 获得院校详情

		controller.getSchoolDetail(1, new GetSchoolDetailListener() {

			@Override
			public void getSchoolDetailFailed() {
				super.getSchoolDetailFailed();
				GoOutDebug.e(TAG, "getSchoolDetailFailed");
			}

			@Override
			public void getSchoolDetailFinished(School school) {
				super.getSchoolDetailFinished(school);
				GoOutDebug.e(TAG, "getSchoolDetailFinished!!!!!!!!!!!!!!!");
			}
		});

		// 获得院校的项目列表
		controller.getPorjectListOfSchool(1, new GetProjectListListener() {

			@Override
			public void getProjectListFailed() {
				super.getProjectListFailed();
				GoOutDebug.e(TAG, "getPorjectListOfSchoolFailed");
			}

			@Override
			public void getProjectListFinished(ArrayList<Project> projects) {
				super.getProjectListFinished(projects);
				GoOutDebug.e(TAG, "getPorjectListOfSchoolFinished!!!!!!!!!!!!!!!");
			}

		});

		// 获得院校学位信息
		controller.getSchoolDegreeDetail(1, GoOutConstants.DEGREE_MASTER, new GetSchoolDegreeDetailListener() {

			@Override
			public void getSchoolDegreeDetailFailed() {
				super.getSchoolDegreeDetailFailed();
				GoOutDebug.e(TAG, "getSchoolDegreeDetailFailed");
			}

			@Override
			public void getSchoolDegreeDetailFinished(SchoolDegreeInfo degree) {
				super.getSchoolDegreeDetailFinished(degree);
				GoOutDebug.e(TAG, "getSchoolDegreeDetailFinished!!!!!!!!!!!!!!!");
			}
		});

		// 获得院校学位所对应专业信息
		controller.getSchoolMajor(1, GoOutConstants.DEGREE_MASTER, new GetSchooMajorListener() {

			@Override
			public void getSchooMajorFailed() {
				super.getSchooMajorFailed();
				GoOutDebug.e(TAG, "getSchooMajorFailed");
			}

			@Override
			public void getSchooMajorFinished(SchoolMajorInfo info) {
				super.getSchooMajorFinished(info);
				GoOutDebug.e(TAG, "getSchooMajorFinished!!!!!!!!!!!!!!!");
			}
		});

		// 获得国家列表
		controller.getSelectCountryList(new GetSelectCountryListListener() {

			@Override
			public void getSelectCountryListFailed() {
				super.getSelectCountryListFailed();
				GoOutDebug.e(TAG, "getSchoolListFailed");
			}

			@Override
			public void getSelectCountryListFinished(ArrayList<Country> countrylist) {
				// TODO Auto-generated method stub
				super.getSelectCountryListFinished(countrylist);
				GoOutDebug.e(TAG, "getSelectCountryListFinished!!!!!!!!!!!!!!!");
			}

		});

		// 获得专业列表
		controller.getSelectMajorList(new GetSelectMajorListListener() {

			@Override
			public void getSelectMajorListFailed() {
				super.getSelectMajorListFailed();
				GoOutDebug.e(TAG, "getMajorListFailed");
			}

			@Override
			public void getSelectMajorListFinished(ArrayList<Major> majorList) {
				super.getSelectMajorListFinished(majorList);
				GoOutDebug.e(TAG, "getSelectMajorListFinished!!!!!!!!!!!!!!!");
			}

		});

		// 获得顾问信息
		controller.getConsultantInfoOfProject(1, new GetConsultantInfoOfProjectListener() {

			@Override
			public void getConsultantInfoFailed() {
				super.getConsultantInfoFailed();
				GoOutDebug.e(TAG, "getConsultantInfoFailed");
			}

			@Override
			public void getConsultantInfoFinished(Project pro) {
				super.getConsultantInfoFinished(pro);
				GoOutDebug.e(TAG, "getConsultantInfoFinished!!!!!!!!!!!!!!!");
			}

		});

		// 获得中介信息
		controller.getIntermediaryInfoOfProject(1, new GetIntermediaryInfoOfProjectListener() {

			@Override
			public void getIntermediaryInfoFailed() {
				super.getIntermediaryInfoFailed();
				GoOutDebug.e(TAG, "getIntermediaryInfoFailed");
			}

			@Override
			public void getIntermediaryInfoFinished(Project pro) {
				super.getIntermediaryInfoFinished(pro);
				GoOutDebug.e(TAG, "getIntermediaryInfoFinished!!!!!!!!!!!!!!!");
			}

		});

		// 上传访问记录
		controller.uploadOneVisitRecork(1, "0", null, PhoneUtil.getPhoneImei(con), new UploadProjectVisitRecordListener() {

			@Override
			public void uploadOneVisitRecorkFailed() {
				// TODO Auto-generated method stub
				super.uploadOneVisitRecorkFailed();
				GoOutDebug.e(TAG, "uploadOneVisitRecorkFailed!");
			}

			@Override
			public void uploadOneVisitRecorkFinished() {
				// TODO Auto-generated method stub
				super.uploadOneVisitRecorkFinished();
				GoOutDebug.e(TAG, "uploadOneVisitRecorkFinished!!!!!!!!!!!!!!!");
			}

		});

		// 上传联系记录
		controller.uploadOneConsultRecork(1, 3, TimeUtil.getCurrentTimeMillis(), "110", PhoneUtil.getPhoneImsi(con), PhoneUtil.getPhoneImei(con), "10086", 0, 1, new UploadProjectConsultRecordListener() {

			@Override
			public void uploadOneConsultRecorkFailed(long callStratTime) {
				// TODO Auto-generated method stub
				super.uploadOneConsultRecorkFailed(callStratTime);
				GoOutDebug.e(TAG, "uploadOneConsultRecorkFailed!");
			}

			@Override
			public void uploadOneConsultRecorkFinished(long callStratTime) {
				// TODO Auto-generated method stub
				super.uploadOneConsultRecorkFinished(callStratTime);
				GoOutDebug.e(TAG, "uploadOneConsultRecorkFinished!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

		});
	}

	/**
	 * 返回一条项目详情的测试数据
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public static Project getTestProjectInfo() {
		Project pro = new Project();

		pro.setLogoName(imageOnlineUrltest1);
		pro.setDegree("本科");
		pro.setServerId(1);
		pro.setName("项目名称项目名称项目名称项目名称项目名称");
		pro.setPicNames(testImagesUrl);
		pro.setTuition(2000);
		pro.setTuitionTimeUnit("年");
		pro.setTuitionUnit("$");
		pro.setServerFee(2000);
		pro.setCountry("美国");
		pro.setEnterTime("2013-05-15");
		pro.setGpa("3.0");
		pro.setLanguage("英语");
		pro.setIntro("简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，简介，");

		// 与院校相关
		pro.setSchoolServerId(1);// 院校id
		pro.setSchoolNameCn("哈佛大学 ");
		pro.setSchoolNameEn("Haaaaa aaaaa aaaV ooo oooo ooo ooo oooord");
		// 与顾问相关
		pro.setPublisherDescription("东方国际北美分部");
		pro.setPublisherLogoName(imageOnlineUrltest1);
		pro.setPublisherName("张三");
		pro.setPublisherServerArea("加拿大");
		pro.setPublisherServerId(1);
		pro.setPublisherTel("10086");
		pro.setPublisherType(0);

		return pro;
	}

	public static ArrayList<Country> getCountryList() {
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < 5; i++) {
			Country c = new Country();
			c.setServerId(i);
			c.setNameCn("美国");
		}
		return countries;

	}

	/**
	 * 获得一条院校详情的测试数据
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	public static School getTestSchoolInfo() {
		School s = new School();
		s.setLogoName(imageOnlineUrltest2);
		s.setNameCn("哈佛大学");
		s.setNameEn("harvard");
		s.setPicsNames(testImagesUrl);
		s.setDegrees(new String[] { "高中", "本科", "硕士" });
		s.setIntro("简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介");

		return s;
	}

	/**
	 * 测试timeUtil
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-8
	 */
	public static void testTimeUtil() {
		long timeMillis = TimeUtil.getCurrentTimeMillis();
		GoOutDebug.v(TAG, "TimeUtilTest  getCurrentTimeMillis=" + timeMillis);
		GoOutDebug.v(TAG, "TimeUtilTest  longToStringDate=" + TimeUtil.longToDateString(timeMillis));
		GoOutDebug.v(TAG, "TimeUtilTest  stringDateToLong=" + TimeUtil.dateStringToLong(TimeUtil.longToDateString(timeMillis)));
		GoOutDebug.v(TAG, "TimeUtilTest  longToStringDateTime=" + TimeUtil.longToDateTimeString(timeMillis));
		GoOutDebug.v(TAG, "TimeUtilTest  stringDateTimeToLong=" + TimeUtil.dateTimeStringToLong(TimeUtil.longToDateTimeString(timeMillis)));
	}
}
