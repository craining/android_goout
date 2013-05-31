package com.c35.ptc.goout;

import java.util.ArrayList;

import android.app.Application;

import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.AccountUtil;

/**
 * 全局变量
 * 
 * (只有在应用程序中所有Activity都destroy时才会destrory)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-15
 */
public class GoOutGlobal extends Application {

	private Country mCountrySchoolList;// 院校列表中显示的过滤的国家
	private Country mCountryProjectList;// 院校首页显示的项目过滤的国家

	private ArrayList<Project> arrayProjects;// 首页项目列表信息

	// public static ArrayList<School> arraySchools;// 学校列表信息
	// private ArrayList<School> arraySchoolsSortedByRank;// 按排名顺序的学校
	private ArrayList<School> arraySchoolsSortedByLetter;// 按字母顺序的学校

	private ArrayList<Country> arraySelectCountry;// 国家列表
	private ArrayList<Major> arraySelectMajor;// 大专业列表

	private ArrayList<Project> arrayProjectsOfSchool;// 院校项目列表信息

	private Account account;//当前登录的账户


	private static GoOutGlobal instance;

	public GoOutGlobal() {
		instance = this;
	}

	public static GoOutGlobal getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		GoOutDebug.e("GoOutGlobal", "Application on start!");
		
		//尝试获得当前登录的账户
		try {
			AccountUtil.getNowLoginAccount(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// if (DEVELOPER_MODE) {
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads()
		// .detectDiskWrites()
		// .detectNetwork() // or .detectAll() for all detectable problems
		// .penaltyLog()
		// .build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects()
		// .detectLeakedClosableObjects()
		// .penaltyLog()
		// .penaltyDeath()
		// .build());
		// }

		super.onCreate();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ArrayList<Project> getArrayProjects() {
		return arrayProjects;
	}

	public void setArrayProjects(ArrayList<Project> arrayProjects) {
		this.arrayProjects = arrayProjects;
	}

	// public ArrayList<School> getArraySchoolsSortedByRank() {
	// return arraySchoolsSortedByRank;
	// }
	//
	// public void setArraySchoolsSortedByRank(ArrayList<School> arraySchoolsSortedByRank) {
	// this.arraySchoolsSortedByRank = arraySchoolsSortedByRank;
	// }

	public ArrayList<School> getArraySchoolsSortedByLetter() {
		return arraySchoolsSortedByLetter;
	}

	public void setArraySchoolsSortedByLetter(ArrayList<School> arraySchoolsSortedByLetter) {
		this.arraySchoolsSortedByLetter = arraySchoolsSortedByLetter;
	}

	public ArrayList<Country> getArraySelectCountry() {
		return arraySelectCountry;
	}

	public void setArraySelectCountry(ArrayList<Country> arraySelectCountry) {
		this.arraySelectCountry = arraySelectCountry;
	}

	public ArrayList<Major> getArraySelectMajor() {
		return arraySelectMajor;
	}

	public void setArraySelectMajor(ArrayList<Major> arraySelectMajor) {
		this.arraySelectMajor = arraySelectMajor;
	}

	public ArrayList<Project> getArrayProjectsOfSchool() {
		return arrayProjectsOfSchool;
	}

	public void setArrayProjectsOfSchool(ArrayList<Project> arrayProjectsOfSchool) {
		this.arrayProjectsOfSchool = arrayProjectsOfSchool;
	}

	public Country getmCountrySchoolList() {
		return mCountrySchoolList;
	}

	public void setmCountrySchoolList(Country mCountrySchoolList) {
		this.mCountrySchoolList = mCountrySchoolList;
	}

	public Country getmCountryProjectList() {

		return mCountryProjectList;
	}

	public void setmCountryProjectList(Country mCountryProjectList) {
		this.mCountryProjectList = mCountryProjectList;
	}
}
