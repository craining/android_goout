package com.c35.ptc.goout.bean;

import android.util.Base64;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.util.ServerUtil;

/**
 * 学院信息
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class School implements Comparable<School> {

	private int id;// 本地id

	private int serverId; // 服务器上院校的id
	private String logoName; // 院校的logo文件名
	private String nameCn;// 中文名
	private String nameEn;// 英文名
	private String sortKey;// 中文拼音，用于排序
	private int ranking;// 名次

	private int favourState;// 收藏状态，0为未收藏，1为已收藏
	private long favourTime;// 收藏时间

	private int countryServerId;// 所属国家id
	private String countryName;// 国家名称（目前用于显示院校图片）

	private String intro;// 简介
	private String[] picsNames;// 图片urls
	private String[] degrees;// 所有专业

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getCountryServerId() {
		return countryServerId;
	}

	public void setCountryServerId(int countryServerId) {
		this.countryServerId = countryServerId;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String[] getPicsNames() {
		return picsNames;
	}

	public void setPicsNames(String[] picsNames) {
		this.picsNames = picsNames;
	}

	public String[] getDegrees() {
		return degrees;
	}

	public void setDegrees(String[] degrees) {
		this.degrees = degrees;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int schoolId) {
		this.serverId = schoolId;
	}

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	/**
	 * ArrayList里不严重hashCode，重不重写hashCode无所谓.但HashMap里用到School对象时需要重写
	 * 
	 * 
	 * 仅当serverId、nameCN、nameEn相等时，可视为对象相等
	 */

	@Override
	public int hashCode() {
		// GoOutDebug.e("School", "hashCode");
		return this.countryServerId + this.nameCn.hashCode() + this.nameEn.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// GoOutDebug.e("School", "equals");
		if (this == obj) {
			return true;
		}
		if (obj != null) {
			if (obj instanceof School) {
				if (((School) obj).countryServerId == this.countryServerId && (((School) obj).nameCn).equals(this.nameCn) && (((School) obj).nameEn).equals(this.nameEn)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(School another) {
		// 从大到小排序
		// return javaBean.getValue().compareTo(this.getValue());
		// 从小到大排序
		return this.getSortKey().compareTo(another.getSortKey());
	}

}
