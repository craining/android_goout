package com.c35.ptc.goout.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.interfaces.GetConsultantInfoOfProjectListener;
import com.c35.ptc.goout.interfaces.GetIntermediaryInfoOfProjectListener;
import com.c35.ptc.goout.interfaces.GetProjectInfoListener;
import com.c35.ptc.goout.interfaces.OutGoingCallListener;
import com.c35.ptc.goout.interfaces.UploadProjectConsultRecordListener;
import com.c35.ptc.goout.interfaces.UploadProjectVisitRecordListener;
import com.c35.ptc.goout.interfaces.ViewEllipsizeTextListener;
import com.c35.ptc.goout.interfaces.ViewPictureFlipperListener;
import com.c35.ptc.goout.receiver.OutGoingCall;
import com.c35.ptc.goout.thirdaccounts.ThirdController;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.TimeUtil;
import com.c35.ptc.goout.util.ViewUtil;
import com.c35.ptc.goout.view.EllipsizeTextView;
import com.c35.ptc.goout.view.OnlineImageView;
import com.c35.ptc.goout.view.PictureFlipperView;
import com.c35.ptc.goout.view.ScrollViewExtend;

/**
 * 项目详情页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
public class ProjectInfoActivity extends Activity implements OnClickListener {

	private static final String TAG = "ProjectInfoActivity";

	private Project mProject;

	private LinearLayout layoutCallConsultant;// 咨询顾问按钮
	private ImageView imgBack;
	private PictureFlipperView picFlipperView;// 顶部可拖动的图片预览控件

	private Button btnFav;// 收藏或取消收藏
	private boolean boolFav = false;

	private ScrollViewExtend scrollBody;// 整个内容
	private LinearLayout layoutLoadInfoShow;
	private TextView textLoadInfoShow;// 显示加载情况
	private Button btnReload;

	private TextView textProjectName;// 项目名称
	private TextView textTuition;// 费用
	private TextView textServicefee;// 服务费
	private TextView textCountry;// 国家
	private TextView textEnterTime;// 入学时间
	private LinearLayout layoutGpaLanguage;// 存放GPA和语言要求的layout
	private TextView textGpa;// Gpa
	private TextView textLanguage;// 语言要求
	private RelativeLayout layoutSchool;// 学院的布局
	private TextView textSchool;// 学院
	private EllipsizeTextView ellTextProjectInfoPart;// 项目简介（部分）
	private TextView textProjectInfoAll;// 项目简介(全部)
	private ImageView imgProjectInfoCtrl;// 展开或关闭项目简介
	private RelativeLayout layoutProjectInfo;// 项目简介
	private RelativeLayout layoutConsultant;// 顾问展示
	private OnlineImageView onlineImgConsultantIc;// 顾问logo
	private TextView textConsultantName;// 顾问姓名
	private TextView textConsultantTel;// 顾问电话
	private TextView textConsultantServerArea;// 顾问服务区域
	private TextView textConsultantDescription;// 顾问所属部门
	private TextView textConsultantGetFail;// 获取发布者信息失败的提示

	private LinearLayout layoutShare;

	private boolean boolOpenIntro = false;// 是否展开简介

	private GoOutController mController;

	private OutGoingCall outCallListener;

	private static final int RESULT_CODE_FROM_CONSULTANT_INFO = 5;// 从顾问详情页返回

	// 控制ui
	private Handler uiHandler;
	private static final int MSG_LOAD_PROJECTINFO_SUCCESS = 0x333;// 项目信息加载成功
	private static final int MSG_LOAD_PROJECTINFO_FAIL = 0x334;// 项目信息加载失败
	private static final int MSG_LOAD_PROJECT_PUBLISHER_SUCCESS = 0x335;// 发布者信息加载成功
	private static final int MSG_LOAD_PROJECT_PUBLISHER_FAIL = 0x336;// 发布者信息加载失败

	// private boolean isConsultantInfoLoadSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_projectinfo);

		mController = GoOutController.getInstance();
		outCallListener = new OutGoingCall(ProjectInfoActivity.this);
		uiHandler = new UiHandler();

		imgBack = (ImageView) findViewById(R.id.img_projectinfo_back);
		scrollBody = (ScrollViewExtend) findViewById(R.id.scroll_projectinfo);
		layoutLoadInfoShow = (LinearLayout) findViewById(R.id.layout_projectinfo_loadinfo);
		textLoadInfoShow = (TextView) findViewById(R.id.text_projectinfo_loadinfo);
		btnReload = (Button) findViewById(R.id.btn_projectinfo_reload);

		layoutCallConsultant = (LinearLayout) findViewById(R.id.btn_projectinfo_docall);
		layoutConsultant = (RelativeLayout) findViewById(R.id.layout_projectinfo_consultant);
		btnFav = (Button) findViewById(R.id.btn_projectinfo_fav);
		picFlipperView = (PictureFlipperView) findViewById(R.id.layout_projectinfo_fliper);
		textProjectName = (TextView) findViewById(R.id.text_projectinfo_name);
		textTuition = (TextView) findViewById(R.id.text_projectinfo_tuition);
		textServicefee = (TextView) findViewById(R.id.text_projectinfo_servicefee);
		textCountry = (TextView) findViewById(R.id.text_projectinfo_country);
		textEnterTime = (TextView) findViewById(R.id.text_projectinfo_entertime);
		layoutGpaLanguage = (LinearLayout) findViewById(R.id.layout_projectinfo_gpa_language);
		textGpa = (TextView) findViewById(R.id.text_projectinfo_gpa);
		textLanguage = (TextView) findViewById(R.id.text_projectinfo_language);
		textSchool = (TextView) findViewById(R.id.text_projectinfo_school);
		ellTextProjectInfoPart = (EllipsizeTextView) findViewById(R.id.ellipsizetext_projectinfo_info_part);
		textProjectInfoAll = (TextView) findViewById(R.id.text_projectinfo_info_all);
		imgProjectInfoCtrl = (ImageView) findViewById(R.id.img_projectinfo_info_updown);
		layoutProjectInfo = (RelativeLayout) findViewById(R.id.layout_projectinfo_info);
		layoutSchool = (RelativeLayout) findViewById(R.id.layout_projectinfo_school);
		onlineImgConsultantIc = (OnlineImageView) findViewById(R.id.onlineimg_projectinfo_consultant_ic);
		textConsultantName = (TextView) findViewById(R.id.text_projectinfo_consultant_name);
		textConsultantTel = (TextView) findViewById(R.id.text_projectinfo_consultant_tel);
		textConsultantServerArea = (TextView) findViewById(R.id.text_projectinfo_consultant_serverarea);
		textConsultantDescription = (TextView) findViewById(R.id.text_projectinfo_consultant_description);
		textConsultantGetFail = (TextView) findViewById(R.id.text_projectinfo_consultant_null);
		layoutShare = (LinearLayout) findViewById(R.id.layout_projectinfo_share);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			mProject = new Project();
			mProject.setServerId(b.getInt("projectServerId"));
			mProject.setName(b.getString("projectName"));
			// GoOutDebug.e(TAG, "type=" + b.getInt("publisherType") + "  id=" +
			// b.getInt("publisherServerId"));
			mProject.setPublisherServerId(b.getInt("publisherServerId"));
			mProject.setPublisherType(b.getInt("publisherType"));
			mProject.setLogoName(b.getString("logoName"));
		}

		if (mProject != null) {
			if (GoOutTest.test) {
				// test
				ctrlIntroShow();
				boolFav = false;// 未收藏
				ctrlFavButtonText();
				Project p = new Project();
				p = GoOutTest.getTestProjectInfo();
				copyProjectInfoValues(p);
				copyConsultantInfo(p);
				showProjectInfo();// 测试
			} else {
				getProjectInfo();// 正常
			}

		} else {
			finish();
			Toast.makeText(ProjectInfoActivity.this, R.string.projectinfo_null, Toast.LENGTH_SHORT).show();
		}

		btnReload.setOnClickListener(this);
		btnFav.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		layoutCallConsultant.setOnClickListener(this);
		layoutSchool.setOnClickListener(this);
		layoutConsultant.setOnClickListener(this);
		layoutProjectInfo.setOnClickListener(this);
		layoutShare.setOnClickListener(this);
		picFlipperView.setOnPictureClickedListener(new OnPictureFlipperOpersListener());
		outCallListener.setOutCallListener(new CallOutListener());
		outCallListener.regist();// 注册通话状态监听器

	}

	/**
	 * 加载详情
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void getProjectInfo() {

		if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
			textLoadInfoShow.setText(R.string.loadnonetwork);
			btnReload.setVisibility(View.VISIBLE);
			GoOutDebug.e(TAG, "NO Net work!!!");
			return;
		} else {
			btnReload.setVisibility(View.GONE);
			textLoadInfoShow.setText(R.string.projectinfo_loading);
			mController.getProjectInfo(mProject.getServerId(), new MyGetProjectInfoListener());
		}

	}

	/**
	 * 显示信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	private void showProjectInfo() {

		ctrlFavButtonText();
		// 加载出详情，显示所有详情，显示收藏按钮，显示咨询顾问按钮
		scrollBody.setVisibility(View.VISIBLE);
		btnFav.setVisibility(View.VISIBLE);
		layoutLoadInfoShow.setVisibility(View.GONE);

		String[] pics = mProject.getPicNames();
		if (pics == null || pics.length < 1) {
			picFlipperView.setVisibility(View.GONE);
		} else {
			for (int i = 0; i < pics.length; i++) {
				pics[i] = ImageUrlUtil.getProjectPhotoUrl(pics[i], mProject.getServerId(), ImageUrlUtil.PHOTO_TYPE_M);
			}
			picFlipperView.setContentView(pics);
		}

		// 项目名称
		textProjectName.setText(mProject.getName());
		// 学费
		if (mProject.getTuition() == 0) {
			textTuition.setText(ViewUtil.getStyleBlackTitleOrangeContent(getApplicationContext(), getString(R.string.projectinfo_fcost), getString(R.string.unknow)));
		} else {
			textTuition.setText(ViewUtil.getStyleBlackTitleOrangeContent(getApplicationContext(), getString(R.string.projectinfo_fcost), mProject.getTuition() + mProject.getTuitionUnit() + "/" + mProject.getTuitionTimeUnit()));
		}
		// textTuition.setText(ViewUtil.getStyleBlackTitleOrangeContent(getApplicationContext(),
		// getString(R.string.projectinfo_fcost), mProject.getTuition() + mProject.getTuitionUnit() + "/" +
		// mProject.getTuitionTimeUnit()));
		// textTuition.setVisibility(mProject.getTuition() == 0 ? View.GONE : View.VISIBLE);
		// 服务费
		// GoOutDebug.e(TAG, "mProject.getServerFee()=" + mProject.getServerFee());
		if (mProject.getServerFee() == 0) {
			GoOutDebug.e(TAG, "mProject.getServerFee()=0!!!!!!!!!!!!");
			textServicefee.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_servicecharge), getString(R.string.unknow)));
		} else {
			textServicefee.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_servicecharge), mProject.getServerFee() + StringUtil.getString(mProject.getServerFeeUnit())));
		}
		// textServicefee.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(),
		// getString(R.string.projectinfo_servicecharge), mProject.getServerFee() +
		// StringUtil.getString(mProject.getServerFeeUnit())));
		// textServicefee.setVisibility(mProject.getServerFee() == 0 ? View.GONE : View.VISIBLE);
		// GoOutDebug.e(TAG, "mProject.getServerFee()=" + mProject.getServerFee());
		// 国家
		if (StringUtil.isNull(mProject.getCountry())) {
			textCountry.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_country), getString(R.string.unknow)));
		} else {
			textCountry.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_country), mProject.getCountry()));
		}
		// textCountry.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(),
		// getString(R.string.projectinfo_country), mProject.getCountry()));
		// textCountry.setVisibility(StringUtil.isNull(mProject.getCountry()) ? View.GONE : View.VISIBLE);
		// 入学时间
		if (StringUtil.isNull(mProject.getEnterTime())) {
			textEnterTime.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_entertime), getString(R.string.unknow)));
		} else {
			textEnterTime.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_entertime), mProject.getEnterTime()));
		}
		// textEnterTime.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(),
		// getString(R.string.projectinfo_entertime), mProject.getEnterTime()));
		// textEnterTime.setVisibility(StringUtil.isNull(mProject.getEnterTime()) ? View.GONE : View.VISIBLE);
		// GPA
		textGpa.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_gpa), mProject.getGpa()));
		textGpa.setVisibility(StringUtil.isNull(mProject.getGpa()) ? View.GONE : View.VISIBLE);
		// 语言要求
		textLanguage.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_languagerequest), mProject.getLanguage()));
		textLanguage.setVisibility(StringUtil.isNull(mProject.getLanguage()) ? View.GONE : View.VISIBLE);
		// 若GPA和语言要求都为空，则不显示包含一条分隔线在内的整个布局
		if (StringUtil.isNull(mProject.getLanguage()) && StringUtil.isNull(mProject.getGpa())) {
			layoutGpaLanguage.setVisibility(View.GONE);
		}
		// 学校
		if (StringUtil.isNull(mProject.getSchoolNameCn() + " " + mProject.getSchoolNameEn())) {
			layoutSchool.setVisibility(View.GONE);
		} else {
			layoutSchool.setVisibility(View.VISIBLE);
			textSchool.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_school), mProject.getSchoolNameCn() + " " + mProject.getSchoolNameEn()));
		}
		// 简介（展开与关闭设置了两个textview，展开时显示textProjectInfoAll，
		// 不展开时显示ellTextProjectInfoPart，其中ellTextProjectInfoPart超过5行带省略号且显示展开与关闭的图片）
		if (StringUtil.isNull(mProject.getIntro())) {
			mProject.setIntro(getString(R.string.projectinfo_info_null));// 若无简介，显示“暂无”
		}
		ellTextProjectInfoPart.setShowLines(5);
		ellTextProjectInfoPart.setTitleStrLength(getString(R.string.projectinfo_info).length());
		ellTextProjectInfoPart.setText(getString(R.string.projectinfo_info) + mProject.getIntro());
		ellTextProjectInfoPart.setTitleSize(getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_TITLE));
		ellTextProjectInfoPart.setContentSize(getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT));
		// 没有超过5行，则不显示展开收起的图标，否则显示
		ellTextProjectInfoPart.setEllipsizeListener(new ViewEllipsizeTextListener() {

			@Override
			public void onEllipsizeShow(boolean show) {
				if (show) {
					imgProjectInfoCtrl.setVisibility(View.VISIBLE);
				} else {
					imgProjectInfoCtrl.setVisibility(View.GONE);
				}
				super.onEllipsizeShow(show);
			}

		});
		textProjectInfoAll.setText(ViewUtil.getStyleBlackTitleGreyContentMiddle(getApplicationContext(), getString(R.string.projectinfo_info), mProject.getIntro()));

		// 显示完成，上传访问记录
		uploadVisitRecord();

		if (GoOutTest.test) {
			if (!StringUtil.isNull(mProject.getLogoName())) {
				// 若logo有变，且本地存储此人记录，则更新
				mController.updateConsultRecordLogo(ProjectInfoActivity.this, mProject.getLogoName(), mProject.getPublisherType(), mProject.getPublisherServerId());
			}
		}
	}

	private void showPublisherInfo() {

		// 发布者信息，若加载完成则显示，否则显示加载失败
		// if (isConsultantInfoLoadSuccess || GoOutTest.test) {
		layoutConsultant.setVisibility(View.VISIBLE);
		textConsultantGetFail.setVisibility(View.GONE);

		// 有顾问电话，则显示咨询顾问按钮
		if (!StringUtil.isNull(mProject.getPublisherTel())) {
			layoutCallConsultant.setVisibility(View.VISIBLE);
		}

		// logo
		if (StringUtil.isNull(mProject.getPublisherLogoName())) {
			onlineImgConsultantIc.setVisibility(View.GONE);
		} else {
			onlineImgConsultantIc.setVisibility(View.VISIBLE);
			onlineImgConsultantIc.setImageViewUrl(ImageUrlUtil.getPublisherLogoUrl(mProject.getPublisherLogoName()), OnlineImageView.PIC_PUBLISHER_LOGO, false, ImageView.ScaleType.FIT_XY);
			// 若logo有变，且本地存储此人记录，则更新
			mController.updateConsultRecordLogo(ProjectInfoActivity.this, mProject.getPublisherLogoName(), mProject.getPublisherType(), mProject.getPublisherServerId());
		}
		// 名字
		if (StringUtil.isNull(mProject.getPublisherName())) {
			textConsultantName.setVisibility(View.GONE);
		} else {
			textConsultantName.setVisibility(View.VISIBLE);
			textConsultantName.setText(mProject.getPublisherName());
		}
		// 电话

		if (StringUtil.isNull(mProject.getPublisherTel())) {
			textConsultantTel.setVisibility(View.GONE);
		} else {
			textConsultantTel.setVisibility(View.VISIBLE);
			textConsultantTel.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), getString(R.string.consultant_tel), mProject.getPublisherTel()));
		}

		// 服务区域
		// GoOutDebug.e(TAG, "mProject.getPublisherServerArea()=" + mProject.getPublisherServerArea());
		if (StringUtil.isNull(mProject.getPublisherServerArea())) {
			textConsultantServerArea.setVisibility(View.GONE);
		} else {
			textConsultantServerArea.setVisibility(View.VISIBLE);
			textConsultantServerArea.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), getString(R.string.consultant_serverarea), mProject.getPublisherServerArea()));
		}
		// 描述
		if (StringUtil.isNull(mProject.getPublisherDescription())) {
			textConsultantDescription.setVisibility(View.GONE);
		} else {
			textConsultantDescription.setVisibility(View.VISIBLE);
			textConsultantDescription.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), mProject.getPublisherDescription(), ""));
		}

		// } else {
		// }
	}

	/**
	 * 将项目的详情保存在mProject中
	 * 
	 * @Description:
	 * @param p
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void copyProjectInfoValues(Project p) {
		// mProject.setLogoName(p.getLogoName());
		mProject.setDegree(p.getDegree());
		// mProject.setName(p.getName());
		mProject.setPicNames(p.getPicNames());
		mProject.setTuition(p.getTuition());
		mProject.setTuitionTimeUnit(p.getTuitionTimeUnit());
		mProject.setTuitionUnit(p.getTuitionUnit());
		mProject.setServerFee(p.getServerFee());
		mProject.setCountry(p.getCountry());
		mProject.setEnterTime(p.getEnterTime());
		mProject.setGpa(p.getGpa());
		mProject.setLanguage(p.getLanguage());
		mProject.setIntro(p.getIntro());

		// 与院校相关
		mProject.setSchoolServerId(p.getSchoolServerId());// 院校id
		mProject.setSchoolNameCn(p.getSchoolNameCn());
		mProject.setSchoolNameEn(p.getSchoolNameEn());
		// 重置下最新获取的id和type
		mProject.setPublisherType(p.getPublisherType());
		mProject.setPublisherServerId(p.getPublisherServerId());
	}

	/**
	 * 将获得的顾问信息保存在mProject中
	 * 
	 * @Description:
	 * @param p
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	private void copyConsultantInfo(Project p) {
		// 与顾问相关
		mProject.setPublisherDescription(p.getPublisherDescription());
		mProject.setPublisherLogoName(p.getPublisherLogoName());
		mProject.setPublisherName(p.getPublisherName());
		mProject.setPublisherServerArea(p.getPublisherServerArea());
		// mProject.setPublisherServerId(p.getPublisherServerId());
		mProject.setPublisherTel(p.getPublisherTel());
		// mProject.setPublisherType(p.getPublisherType());
		mProject.setPublisherProjectCount(p.getPublisherProjectCount());

	}

	/**
	 * 上传访问记录
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	private void uploadVisitRecord() {

		GoOutDebug.e(TAG, "UPLOAD VIsit Record!!!");
		// 上传访问记录(注意参数2)
		mController.uploadOneVisitRecork(mProject.getServerId(), "0", null, PhoneUtil.getPhoneImei(ProjectInfoActivity.this), new UploadProjectVisitRecordListener() {

			@Override
			public void uploadOneVisitRecorkFailed() {
				super.uploadOneVisitRecorkFailed();
				GoOutDebug.e(TAG, "uploadOneVisitRecorkFailed!");
			}

			@Override
			public void uploadOneVisitRecorkFinished() {
				super.uploadOneVisitRecorkFinished();
				GoOutDebug.e(TAG, "uploadOneVisitRecorkFinished!!!!!!!!!!!!!!!");
			}

		});

	}

	/**
	 * 保存并上传通话记录
	 * 
	 * @Description:
	 * @param startTime
	 * @param callTime
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	private void saveAndUploadCallRecord(long startTime, int callRunTime) {
		if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
			// 保存联系记录
			mController.insertConsultRecord(ProjectInfoActivity.this, mProject.getServerId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ProjectInfoActivity.this), PhoneUtil.getPhoneImsi(ProjectInfoActivity.this), PhoneUtil.getPhoneImei(ProjectInfoActivity.this), mProject.getPublisherTel(), mProject.getPublisherType(), mProject.getPublisherServerId(), 1, mProject.getPublisherName(), mProject.getPublisherLogoName(), mProject.getPublisherServerArea(), mProject.getPublisherDescription());

			// 上传联系记录
			mController.uploadOneConsultRecork(mProject.getServerId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ProjectInfoActivity.this), PhoneUtil.getPhoneImsi(ProjectInfoActivity.this), PhoneUtil.getPhoneImei(ProjectInfoActivity.this), mProject.getPublisherTel(), mProject.getPublisherType(), mProject.getPublisherServerId(), new UploadProjectConsultRecordListener() {

				@Override
				public void uploadOneConsultRecorkFailed(long callStratTime) {
					super.uploadOneConsultRecorkFailed(callStratTime);
					GoOutDebug.e(TAG, "uploadOneConsultRecorkFailed!, Upload record failed， update upload state in local db.");
					// 上传失败，更新本地联系记录的上传状态
					mController.updateUploadStateOfConsultRecord(ProjectInfoActivity.this, callStratTime, 0);
				}

				@Override
				public void uploadOneConsultRecorkFinished(long callStratTime) {
					super.uploadOneConsultRecorkFinished(callStratTime);
					GoOutDebug.e(TAG, "uploadOneConsultRecorkFinished!!!!!!!!!!!!!!!!!!!!!!!!!");
				}

			});
		} else {
			// 保存联系记录
			mController.insertConsultRecord(ProjectInfoActivity.this, mProject.getServerId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ProjectInfoActivity.this), PhoneUtil.getPhoneImsi(ProjectInfoActivity.this), PhoneUtil.getPhoneImei(ProjectInfoActivity.this), mProject.getPublisherTel(), mProject.getPublisherType(), mProject.getPublisherServerId(), 0, mProject.getPublisherName(), mProject.getPublisherLogoName(), mProject.getPublisherServerArea(), mProject.getPublisherDescription());
		}
	}

	/**
	 * 控制简介展开或关闭
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	private void ctrlIntroShow() {
		if (boolOpenIntro) {
			ellTextProjectInfoPart.setVisibility(View.GONE);
			textProjectInfoAll.setVisibility(View.VISIBLE);
			imgProjectInfoCtrl.setImageResource(R.drawable.ic_arrow_up);
		} else {
			ellTextProjectInfoPart.setVisibility(View.VISIBLE);
			textProjectInfoAll.setVisibility(View.GONE);
			imgProjectInfoCtrl.setImageResource(R.drawable.ic_arrow_down);
		}
	}

	/**
	 * 显示收藏或取消收藏
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void ctrlFavButtonText() {
		boolFav = mController.getProjectFavState(ProjectInfoActivity.this, mProject.getServerId());
		if (boolFav) {
			btnFav.setText(R.string.projectinfo_title_btn_unfav);// 取消收藏
		} else {
			btnFav.setText(R.string.projectinfo_title_btn_fav);// 收藏
		}

	}

	/**
	 * 收藏或取消收藏
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void ctrlFavOpera() {
		boolean operaResult = false;
		// 收藏或取消收藏
		if (boolFav) {
			// 取消收藏
			operaResult = mController.unFavOneProject(ProjectInfoActivity.this, mProject.getServerId());
		} else {
			// 收藏
			GoOutDebug.e(TAG, "mProject.getLogoName()=" + mProject.getLogoName());
			operaResult = mController.favOneProject(ProjectInfoActivity.this, mProject.getServerId(), mProject.getName(), mProject.getCountry(), mProject.getTuition(), mProject.getTuitionUnit(), mProject.getTuitionTimeUnit(), mProject.getDegree(), mProject.getPublisherServerId(), mProject.getPublisherType(), mProject.getPublisherTel(), mProject.getLogoName(), mProject.getEnterTime(), 1, TimeUtil.getCurrentTimeMillis(), mProject.getGpa(), mProject.getLanguage());

			ThirdController.getInstence().shareHide(ProjectInfoActivity.this, getShareContent());
		}
		if (operaResult) {
			boolFav = !boolFav;// 操作成功后才能设置
			ctrlFavButtonText();
			Toast.makeText(ProjectInfoActivity.this, R.string.doopera_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(ProjectInfoActivity.this, R.string.doopera_fail, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 当此activity不在前端时取消监听
		outCallListener.unRegist();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_projectinfo_back:
			finish();
			break;
		case R.id.btn_projectinfo_fav:
			ctrlFavOpera();
			break;
		case R.id.layout_projectinfo_consultant:
			// 跳转顾问信息页
			outCallListener.unRegist();// 跳转到顾问详情页，取消监听，由顾问详情页监听
			Intent i = new Intent(ProjectInfoActivity.this, ConsultantInfoActivity.class);
			i.putExtra("projectServerId", mProject.getServerId());
			i.putExtra("calledNumber", mProject.getPublisherTel());
			i.putExtra("calledType", mProject.getPublisherType());
			i.putExtra("consultantServerId", mProject.getPublisherServerId());
			i.putExtra("calledLogoName", mProject.getPublisherLogoName());
			i.putExtra("calledServerArea", mProject.getPublisherServerArea());
			i.putExtra("calledDescription", mProject.getPublisherDescription());
			i.putExtra("calledName", mProject.getPublisherName());
			i.putExtra("calledPublisherProjectCount", mProject.getPublisherProjectCount());
			startActivityForResult(i, RESULT_CODE_FROM_CONSULTANT_INFO);
			break;
		case R.id.btn_projectinfo_docall:
			// 打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mProject.getPublisherTel()));
			startActivity(intent);

			break;
		case R.id.layout_projectinfo_info:
			// 展开关闭简介
			boolOpenIntro = !boolOpenIntro;
			ctrlIntroShow();
			break;
		case R.id.layout_projectinfo_school:
			// 跳转到学校信息
			Intent i2 = new Intent(ProjectInfoActivity.this, SchoolMainActivity.class);
			i2.putExtra("schoolServerId", mProject.getSchoolServerId());
			i2.putExtra("schoolNameCn", mProject.getSchoolNameCn());
			i2.putExtra("schoolNameEn", mProject.getSchoolNameEn());
			i2.putExtra("schoolLogoUrl", mController.getSchoolLogoUrl(ProjectInfoActivity.this, mProject.getSchoolServerId()));// 传否？？
			startActivity(i2);
			break;
		case R.id.btn_projectinfo_reload:
			// 重新加载
			getProjectInfo();
			break;

		case R.id.layout_projectinfo_share:
			// 分享
			if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				Intent shareIntent = new Intent(ProjectInfoActivity.this, ShareActivity.class);
				shareIntent.putExtra("shareContent", getShareContent());
				startActivity(shareIntent);
			} else {
				Toast.makeText(ProjectInfoActivity.this, R.string.loadnonetwork_toast, Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 获得要分享的内容
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-23
	 */
	private String getShareContent() {
		String tuition = "";
		if (mProject.getTuition() > 0) {
			tuition = (new StringBuilder(mProject.getTuition() + "").append(mProject.getTuitionUnit()).append("/").append(mProject.getTuitionTimeUnit())).toString();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(getString(R.string.share_content_head)).append(mProject.getName()).append(" ").append(StringUtil.getString(mProject.getCountry())).append(" ").append(StringUtil.getString(mProject.getDegree())).append(" ").append(StringUtil.getString(tuition)).append(getString(R.string.share_content_tail));
		return sb.toString();
	}

	/**
	 * handler控制ui
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-20
	 */
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_LOAD_PROJECTINFO_FAIL:
				// 加载失败
				textLoadInfoShow.setText(R.string.projectinfo_loadfail);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_LOAD_PROJECTINFO_SUCCESS:
				// 加载成功，显示
				showProjectInfo();
				break;

			case MSG_LOAD_PROJECT_PUBLISHER_FAIL:
				// 发布者信息加载失败
				layoutConsultant.setVisibility(View.GONE);
				textConsultantGetFail.setVisibility(View.VISIBLE);
				break;
			case MSG_LOAD_PROJECT_PUBLISHER_SUCCESS:
				// 发布者信息加载成功
				showPublisherInfo();
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_CODE_FROM_CONSULTANT_INFO) {
			// 从顾问详情页返回，重新激活通话监听
			outCallListener.regist();
		}
	}

	/**
	 * 关于预览图片的操作
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-1
	 */
	private class OnPictureFlipperOpersListener implements ViewPictureFlipperListener {

		@Override
		public void onPictureClickedListener(int id) {
			// 预览图片被点击
			GoOutDebug.e(TAG, "onPictureClicked id=" + id);
		}

		@Override
		public void onPictureChangedListener(int id) {
			// 预览图片切换
			GoOutDebug.e(TAG, "onPictureChanged id=" + id);
		}

		@Override
		public void onPictureScrolling(boolean scrolling) {
			// 图片正在切换
		}

		@Override
		public void onPictureScrollingStoped(boolean stop) {
			// 图片切换完成
		}

		@Override
		public void onPictureLongClickedListener(int id) {
			// GoOutDebug.e(TAG, "onPictureLongClicked id=" + id);
			// isScrollUpDownDisabled = false;
		}
	}

	/**
	 * 获得联系记录相关，呼出电话时的电话状态
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-19
	 */
	private class CallOutListener extends OutGoingCallListener {

		@Override
		public void onCallOutGoing(String num) {
			super.onCallOutGoing(num);
			GoOutDebug.e(TAG, "onCallOutGoing=" + num);
		}

		@Override
		public void onCallIdle(String num, long startTime, int callRunTime) {
			super.onCallIdle(num, startTime, callRunTime);
			GoOutDebug.e(TAG, "onCallIdle 空闲，挂断 number=" + num + "callRunTime=" + callRunTime);

			// 上传记录
			if (callRunTime > 0 && mProject.getPublisherTel().equals(num)) {
				saveAndUploadCallRecord(startTime, callRunTime);
			}

		}

		@Override
		public void onCallOffHook(String num) {
			super.onCallOffHook(num);
			GoOutDebug.e(TAG, "onCallOffHook 摘机 number=" + num);
		}

	}

	/**
	 * 加载项目详情的监听器
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-20
	 */
	private class MyGetProjectInfoListener extends GetProjectInfoListener {

		@Override
		public void getProjectInfoFailed() {
			super.getProjectInfoFailed();
			GoOutDebug.e(TAG, "getProjectInfoFailed!");
			uiHandler.sendEmptyMessage(MSG_LOAD_PROJECTINFO_FAIL);
		}

		@Override
		public void getProjectInfoFinished(Project projectInfo) {
			super.getProjectInfoFinished(projectInfo);
			// 获取成功
			if (projectInfo != null) {
				copyProjectInfoValues(projectInfo);
				GoOutDebug.e(TAG, "getProjectInfoFinished!");
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECTINFO_SUCCESS);
				// 在此获得顾问或中介的信息

				// GoOutDebug.e(TAG, "publisher Id = " + mProject.getPublisherServerId());
				switch (mProject.getPublisherType()) {
				case GoOutConstants.TYPE_CONSULTANT:
					// 顾问
					mController.getConsultantInfoOfProject(mProject.getPublisherServerId(), new MyGetConsultantInfoOfProjectListener());
					break;
				case GoOutConstants.TYPE_INTERMEDIARY:
					// 中介
					mController.getIntermediaryInfoOfProject(mProject.getPublisherServerId(), new MyGetIntermediaryInfoOfProjectListener());
					break;

				default:
					uiHandler.sendEmptyMessage(MSG_LOAD_PROJECT_PUBLISHER_FAIL);
					break;
				}

			} else {
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECTINFO_FAIL);
			}

		}
	}

	/**
	 * 加载顾问详情的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-21
	 */
	private class MyGetConsultantInfoOfProjectListener extends GetConsultantInfoOfProjectListener {

		@Override
		public void getConsultantInfoFailed() {
			super.getConsultantInfoFailed();
		}

		@Override
		public void getConsultantInfoFinished(Project projectPublisherInfo) {
			super.getConsultantInfoFinished(projectPublisherInfo);
			if (projectPublisherInfo != null) {
				copyConsultantInfo(projectPublisherInfo);
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECT_PUBLISHER_SUCCESS);
			} else {
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECT_PUBLISHER_FAIL);
			}
		}

	}

	/**
	 * 加载中介详情的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-21
	 */
	private class MyGetIntermediaryInfoOfProjectListener extends GetIntermediaryInfoOfProjectListener {

		@Override
		public void getIntermediaryInfoFailed() {
			super.getIntermediaryInfoFailed();
		}

		@Override
		public void getIntermediaryInfoFinished(Project projectPublisherInfo) {
			super.getIntermediaryInfoFinished(projectPublisherInfo);
			if (projectPublisherInfo != null) {
				copyConsultantInfo(projectPublisherInfo);
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECT_PUBLISHER_SUCCESS);
			} else {
				uiHandler.sendEmptyMessage(MSG_LOAD_PROJECT_PUBLISHER_FAIL);
			}

		}

	}

}
