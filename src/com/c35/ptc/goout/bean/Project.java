package com.c35.ptc.goout.bean;

import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.util.ServerUtil;

/**
 * 项目信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class Project {

	private int id;// 本地数据库里的id

	private int serverId;// 服务器上项目的id
	private String name;// 名称
	private String country;// 国家

	private int tuition;// 学费
	private String tuitionUnit;// 学费货币单位
	private String tuitionTimeUnit;// 学费时间单位
	private String degree;// 学位

	private int serverFee;// 服务费
	private String serverFeeUnit;// 服务费单位

	private int publisherServerId;// 发布者服务器上的id
	private int publisherType;// 发布者类型（中介：0 顾问：1）
	private String publisherTel;// 发布者电话
	private String publisherName;// 发布者名字
	private String publisherServerArea;// 发布者服务区域
	private String publisherDescription;// 发布者所属部门
	private String publisherLogoName;// 发布者头像
	private String publisherAddr;// 发布者地址
	private String publisherCity;// 发布者城市
	private String publisherProvince;// 发布者省份
	private int publisherProjectCount;// 发布者发布项目数目
	private int publisherEmail;// 发布者email
	private String publisherInterName;//顾问的话，此为其所属中介

	
	public String getPublisherInterName() {
		return publisherInterName;
	}

	
	public void setPublisherInterName(String publisherInterName) {
		this.publisherInterName = publisherInterName;
	}

	private String logoName;// 图标(获得picUrls后，默认第一个为列表上显示的图标)

	private String enterTime;// 入学时间
	private String[] picNames;// 包含的图片URL

	private int favourState;// 收藏状态，0为未收藏，1为收藏
	private long favourTime;// 收藏时间

	private String schoolNameCn;// 院校中文名
	private String schoolNameEn;// 院校英文名
	private int schoolServerId;// 院校id

	private String intro;// 简介

	// 以下两个需要在获得列表时返回
	private String gpa;// gpa
	private String language;// 语言要求

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGpa() {
		return gpa;
	}

	public void setGpa(String gpa) {
		this.gpa = gpa;
	}

	public int getServerFee() {
		return serverFee;
	}

	public void setServerFee(int serverFee) {
		this.serverFee = serverFee;
	}

	public String getServerFeeUnit() {
		return serverFeeUnit;
	}

	public void setServerFeeUnit(String serverFeeUnit) {
		this.serverFeeUnit = serverFeeUnit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int id) {
		this.serverId = id;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String url) {
		this.logoName = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}

	public int getTuition() {
		return tuition;
	}

	public void setTuition(int tuition) {
		this.tuition = tuition;
	}

	public String getTuitionUnit() {
		return tuitionUnit;
	}

	public void setTuitionUnit(String tuitionUnit) {
		this.tuitionUnit = tuitionUnit;
	}

	public String getTuitionTimeUnit() {
		return tuitionTimeUnit;
	}

	public void setTuitionTimeUnit(String tuitionTimeUnit) {
		this.tuitionTimeUnit = tuitionTimeUnit;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String[] getPicNames() {
		return picNames;
	}

	public void setPicNames(String[] picNames) {
		this.picNames = picNames;
	}

	public int getPublisherType() {
		return publisherType;
	}

	public void setPublisherType(int publisherType) {
		this.publisherType = publisherType;
	}

	public String getPublisherTel() {
		return publisherTel;
	}

	public void setPublisherTel(String publisherTel) {
		this.publisherTel = publisherTel;
	}

	public int getFavourState() {
		return favourState;
	}

	public void setFavourState(int favourState) {
		this.favourState = favourState;
	}

	public long getFavourTime() {
		return favourTime;
	}

	public void setFavourTime(long favourTime) {
		this.favourTime = favourTime;
	}

	public String getSchoolNameCn() {
		return schoolNameCn;
	}

	public void setSchoolNameCn(String schoolNameCn) {
		this.schoolNameCn = schoolNameCn;
	}

	public String getSchoolNameEn() {
		return schoolNameEn;
	}

	public void setSchoolNameEn(String schoolNameEn) {
		this.schoolNameEn = schoolNameEn;
	}

	public int getSchoolServerId() {
		return schoolServerId;
	}

	public void setSchoolServerId(int schoolServerId) {
		this.schoolServerId = schoolServerId;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getPublisherServerId() {
		return publisherServerId;
	}

	public void setPublisherServerId(int publisherServerId) {
		this.publisherServerId = publisherServerId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherServerArea() {
		return publisherServerArea;
	}

	public void setPublisherServerArea(String publisherServerArea) {
		this.publisherServerArea = publisherServerArea;
	}

	public String getPublisherDescription() {
		return publisherDescription;
	}

	public void setPublisherDescription(String publisherDescription) {
		this.publisherDescription = publisherDescription;
	}

	public String getPublisherLogoName() {
		return publisherLogoName;
	}

	public void setPublisherLogoName(String publisherLogoUrl) {
		this.publisherLogoName = publisherLogoUrl;
	}

	public String getPublisherAddr() {
		return publisherAddr;
	}

	public void setPublisherAddr(String publisherAddr) {
		this.publisherAddr = publisherAddr;
	}

	public String getPublisherCity() {
		return publisherCity;
	}

	public void setPublisherCity(String publisherCity) {
		this.publisherCity = publisherCity;
	}

	public String getPublisherProvince() {
		return publisherProvince;
	}

	public void setPublisherProvince(String publisherProvince) {
		this.publisherProvince = publisherProvince;
	}

	public int getPublisherProjectCount() {
		return publisherProjectCount;
	}

	public void setPublisherProjectCount(int publisherProjectCount) {
		this.publisherProjectCount = publisherProjectCount;
	}

	public int getPublisherEmail() {
		return publisherEmail;
	}

	public void setPublisherEmail(int publisherEmail) {
		this.publisherEmail = publisherEmail;
	}
}
