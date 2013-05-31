package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
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

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.bean.SchoolDegreeInfo;
import com.c35.ptc.goout.interfaces.GetSchoolDegreeDetailListener;
import com.c35.ptc.goout.interfaces.GetSchoolDetailListener;
import com.c35.ptc.goout.interfaces.ViewEllipsizeTextListener;
import com.c35.ptc.goout.interfaces.ViewSchoolDegreeListener;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.ViewUtil;
import com.c35.ptc.goout.view.EllipsizeTextView;
import com.c35.ptc.goout.view.OnlineImageView;
import com.c35.ptc.goout.view.SchoolDegreeView;

/**
 * 院校页面中的简介页面
 * 
 * (显示学位以及该学位的信息，以后可写成一个单独的控件)
 * 
 * ( 先加载院校简介等信息，显示出来，再加载学位信息，再显示出学位来)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */

public class SchoolIntroActivity extends Activity implements OnClickListener {

	private static final String TAG = "SchoolIntroActivity";
	private ImageView imgBack;

	private LinearLayout layoutLoadInfo;// 加载信息
	private TextView textLoadInfo;
	private Button btnReload;
	private LinearLayout layoutLoadContent;// 显示加载的内容

	private OnlineImageView onlineImgIc;// 顶部院校图片
	private TextView textNameCn;
	private TextView textNameEn;
	private EllipsizeTextView elliptextIntroPart;// 院校简介，展示一部分时的textview
	private TextView textIntroAll;// 院校简介，展示所有内容时的textview
	private TextView textDegreeLoadInfo;// 学位信息加载失败或空

	private RelativeLayout layoutIntro;// 简介的整个容器，点击可展开或关闭简介
	private ImageView imgIntroCtrl;// 简介展开或关闭时的右下角箭头
	private boolean boolOpenIntro;// 简介开关

	private ArrayList<SchoolDegreeInfo> mArrayDegreesInfo;// 所有学位的信息
	private SchoolDegreeView[] mDegreesView;// 每个学位信息的展示控件对应此数组里的一个

	private LinearLayout layoutShowDegrees;// 显示所有学位的整个容器

	private Button btnFav;// 收藏或取消收藏

	private GoOutController mController;

	private Handler uiHandler;
	private static final int MSG_DEGREE_OPEND = 0x666;// 某个学位被展开
	private static final int MSG_LOAD_INTRO_SUCCESS = 0x667;// 加载院校详情成功
	private static final int MSG_LOAD_INTRO_FAIL = 0x668;// 加载院校详情失败
	private static final int MSG_LOAD_DEGREES_SUCCESS = 0x669;// 加载学位详情失败
	private static final int MSG_LOAD_DEGREES_FAIL = 0x665;// 加载学位详情失败

	private int degreesCount = 0;// 学位计数，用来判断所有学位是否加载完成

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_school_intro);

		uiHandler = new UIHandler();
		mController = GoOutController.getInstance();

		layoutLoadContent = (LinearLayout) findViewById(R.id.layout_schoolintro_loadcontent);
		layoutLoadInfo = (LinearLayout) findViewById(R.id.layout_schoolintro_loadinfo);
		textLoadInfo = (TextView) findViewById(R.id.text_schoolintro_loadinfo);
		btnReload = (Button) findViewById(R.id.btn_schoolintro_reload);

		imgBack = (ImageView) findViewById(R.id.img_schoolintro_back);
		btnFav = (Button) findViewById(R.id.btn_schoolintro_fav);
		onlineImgIc = (OnlineImageView) findViewById(R.id.onlineimg_schoolintro_ic);
		textNameCn = (TextView) findViewById(R.id.text_schoolintro_namecn);
		textNameEn = (TextView) findViewById(R.id.text_schoolintro_nameen);
		textDegreeLoadInfo = (TextView) findViewById(R.id.text_schoolintro_degrees_null);
		elliptextIntroPart = (EllipsizeTextView) findViewById(R.id.ellipsizetext_schoolintro_intro_part);
		textIntroAll = (TextView) findViewById(R.id.text_schoolintro_intro_all);
		imgIntroCtrl = (ImageView) findViewById(R.id.img_schoolintro_infoupdown);
		layoutIntro = (RelativeLayout) findViewById(R.id.layout_schoolintro_intro);
		layoutShowDegrees = (LinearLayout) findViewById(R.id.layout_schoolintro_degrees);

		btnReload.setOnClickListener(this);
		btnFav.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		layoutIntro.setOnClickListener(this);

		getSchoolInfo();
	}

	/**
	 * 加载院校详情（此页中除学位信息外的数据）
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void getSchoolInfo() {
		// 显示其它Activity传递过来的logo、name
		// GoOutDebug.e(TAG, "set logo : " + SchoolMainActivity.mSchool.getSchoolLogoUrl());
		// GoOutDebug.e(TAG, "set logo : " +
		// ImageUrlUtil.getSchoolLogoUrl(SchoolMainActivity.mSchool.getLogoName()));
		// onlineImgIc.setImageViewUrl(SchoolMainActivity.mSchool.getSchoolLogoUrl(),
		// OnlineImageView.PIC_SEZE_SMALL, false);
		onlineImgIc.setImageViewUrl(ImageUrlUtil.getSchoolLogoUrl(SchoolMainActivity.mSchool.getLogoName()), OnlineImageView.PIC_SCHOOL_LOGO, false, ImageView.ScaleType.FIT_XY);
		textNameCn.setText(SchoolMainActivity.mSchool.getNameCn());
		textNameEn.setText(SchoolMainActivity.mSchool.getNameEn());

		if (GoOutTest.test) {
			// test
			School s = GoOutTest.getTestSchoolInfo();
			copySchoolValues(s);
			SchoolDegreeInfo a;
			mArrayDegreesInfo = new ArrayList<SchoolDegreeInfo>();
			for (int i = 0; i < 3; i++) {
				a = new SchoolDegreeInfo();
				a.setDegreeName("高中");
				a.setEducationRequire("幼儿园毕业");
				a.setGpa("3.0");
				a.setLanguage("CET6 > 250");
				a.setEndTimes(new String[] { "2013-04" });
				a.setTuituon(3000);
				a.setTuituonUnit("￥");
				a.setTuituonTimeUnit("年");
				a.setScholarShip(0);
				mArrayDegreesInfo.add(a);
			}
			showIntro();
			showDegreeInfo();
		} else {
			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				textLoadInfo.setText(R.string.loadnonetwork);
				btnReload.setVisibility(View.VISIBLE);
				return;
			} else {
				// 先获得院校简介等信息
				btnReload.setVisibility(View.GONE);
				textLoadInfo.setText(R.string.schoolinfo_loading);
				mController.getSchoolDetail(SchoolMainActivity.mSchool.getServerId(), new MyGetSchoolInfoListener());
			}
		}

	}

	/**
	 * 显示除学位以外的信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void showIntro() {

		layoutLoadInfo.setVisibility(View.GONE);
		layoutLoadContent.setVisibility(View.VISIBLE);
		// btnFav.setVisibility(View.VISIBLE);

		// 简介等信息
		// GoOutDebug.e(TAG, "reset logo : " + SchoolMainActivity.mSchool.getSchoolLogoUrl());
		// GoOutDebug.e(TAG, "reset logo : " +
		// ImageUrlUtil.getSchoolLogoUrl(SchoolMainActivity.mSchool.getLogoName()));
		// onlineImgIc.setImageViewUrl(SchoolMainActivity.mSchool.getSchoolLogoUrl(),
		// OnlineImageView.PIC_SEZE_SMALL, false);// 更新logo的显示，可能logo会变更，或从其它activity传过来的url为空
		onlineImgIc.setImageViewUrl(ImageUrlUtil.getSchoolLogoUrl(SchoolMainActivity.mSchool.getLogoName()), OnlineImageView.PIC_SCHOOL_LOGO, false, ImageView.ScaleType.FIT_XY);// 更新logo的显示，可能logo会变更，或从其它activity传过来的url为空
		elliptextIntroPart.setShowLines(5);
		elliptextIntroPart.setTitleStrLength(getString(R.string.projectinfo_info).length());
		elliptextIntroPart.setTitleSize(getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_BIG_TITLE));
		elliptextIntroPart.setContentSize(getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_BIG_CONTENT));
		elliptextIntroPart.setText(getString(R.string.projectinfo_info) + SchoolMainActivity.mSchool.getIntro());
		// 没有超过5行，则不显示展开收起的图标，否则显示
		elliptextIntroPart.setEllipsizeListener(new ViewEllipsizeTextListener() {

			@Override
			public void onEllipsizeShow(boolean show) {
				if (show) {
					imgIntroCtrl.setVisibility(View.VISIBLE);
				} else {
					imgIntroCtrl.setVisibility(View.GONE);
				}
				super.onEllipsizeShow(show);
			}

		});
		textIntroAll.setText(ViewUtil.getStyleBlackTitleGreyContentBig(getApplicationContext(), getString(R.string.projectinfo_info), SchoolMainActivity.mSchool.getIntro()));

	}

	/**
	 * 将院校属性取出
	 * 
	 * @Description:
	 * @param s
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void copySchoolValues(School s) {
		if (SchoolMainActivity.mSchool == null)
			SchoolMainActivity.mSchool = new School();
		SchoolMainActivity.mSchool.setPicsNames(s.getPicsNames());
		SchoolMainActivity.mSchool.setDegrees(s.getDegrees());
		SchoolMainActivity.mSchool.setIntro(s.getIntro());
		SchoolMainActivity.mSchool.setCountryName(s.getCountryName());
		if (SchoolMainActivity.mSchool.getLogoName() == null || !SchoolMainActivity.mSchool.getLogoName().equals(s.getLogoName())) {
			// 1、院校的logo有变化，
			// 2、或本地未存储院校，且从项目详情页过来的，SchoolMainActivity.mSchool.getLogoUrl()为null或""，
			SchoolMainActivity.mSchool.setLogoName(s.getLogoName());// 显示刚从服务器上加载下来的logo
			// 并更新数据库中存储的院校的url(目前收藏的院校存储到本地了)
			mController.updateSchoolLogoUrl(SchoolIntroActivity.this, SchoolMainActivity.mSchool.getServerId(), s.getLogoName());
		}
	}

	/**
	 * 显示学位信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	private void showDegreeInfo() {
		if (mArrayDegreesInfo != null && mArrayDegreesInfo.size() > 0) {
			layoutShowDegrees.removeAllViews();// 可能会出现两次调用此方法的情况，造成重复显示。尚未查明原因，因此显示前先清空一下。
			mDegreesView = new SchoolDegreeView[mArrayDegreesInfo.size()];
			// GoOutDebug.e(TAG, "show Degree size=" + mDegreesView.length);
			for (int i = 0; i < mDegreesView.length; i++) {
				mDegreesView[i] = new SchoolDegreeView(SchoolIntroActivity.this);
				mDegreesView[i].setContentView(mArrayDegreesInfo.get(i), i);
				mDegreesView[i].setViewSchoolDegreeListener(new OnSchollDegreeViewListener());
				layoutShowDegrees.addView(mDegreesView[i]);
			}

			openAllDegreeShow();
		} else {
			textDegreeLoadInfo.setText(R.string.schoolinfo_degree_null);
			textDegreeLoadInfo.setVisibility(View.VISIBLE);
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
		// 收藏状态
		SchoolMainActivity.boolIsFav = mController.getSchoolFavState(SchoolIntroActivity.this, SchoolMainActivity.mSchool.getServerId());
		if (SchoolMainActivity.boolIsFav) {
			btnFav.setText(R.string.projectinfo_title_btn_unfav);// 取消收藏
		} else {
			btnFav.setText(R.string.projectinfo_title_btn_fav);// 收藏
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		ctrlFavButtonText();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_schoolintro_back:
			finish();
			break;
		case R.id.layout_schoolintro_intro:
			// 展开或关闭简介
			ctrlIntro();
			break;
		case R.id.btn_schoolintro_fav:
			// 收藏或取消收藏
			if (SchoolMainActivity.ctrlFavOpera(SchoolIntroActivity.this, mController)) {
				ctrlFavButtonText();
			}
			break;
		case R.id.btn_schoolintro_reload:
			// 重新加载
			getSchoolInfo();
			break;
		default:
			break;
		}

	}

	/**
	 * 开关简介
	 * 
	 * @Description:
	 * @see:
	 * @sin)ce:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void ctrlIntro() {
		boolOpenIntro = !boolOpenIntro;
		if (boolOpenIntro) {
			elliptextIntroPart.setVisibility(View.GONE);
			textIntroAll.setVisibility(View.VISIBLE);
			imgIntroCtrl.setImageResource(R.drawable.ic_arrow_up);
		} else {
			elliptextIntroPart.setVisibility(View.VISIBLE);
			textIntroAll.setVisibility(View.GONE);
			imgIntroCtrl.setImageResource(R.drawable.ic_arrow_down);
		}

	}

	/**
	 * 互斥开关学位信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void mutextDegreeShow(int id) {
		for (int i = 0; i < mDegreesView.length; i++) {
			if (i != id) {
				mDegreesView[i].setOpenClose(false);
			}
		}
	}

	/**
	 * 展开所有学位信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void openAllDegreeShow() {
		for (int i = 0; i < mDegreesView.length; i++) {
			mDegreesView[i].setOpenClose(true);
		}
	}

	/**
	 * handler接收msg改变ui
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-14
	 */
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_DEGREE_OPEND:
				// uiHandler.removeMessages(MSG_DEGREE_OPEND);
				// mutextDegreeShow(msg.arg1);//暂不互斥
				break;

			case MSG_LOAD_INTRO_SUCCESS:
				// 加载院校简介成功
				showIntro();

				break;
			case MSG_LOAD_INTRO_FAIL:
				// 加载失败
				textLoadInfo.setText(R.string.schoolinfo_loadfail);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_LOAD_DEGREES_SUCCESS:
				// 加载学位信息成功
				showDegreeInfo();
				break;
			case MSG_LOAD_DEGREES_FAIL:
				// 加载学位信息失败
				textDegreeLoadInfo.setVisibility(View.VISIBLE);
				textDegreeLoadInfo.setText(R.string.schoolinfo_degree_fail);

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 条目被展开关闭的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-14
	 */
	private class OnSchollDegreeViewListener implements ViewSchoolDegreeListener {

		@Override
		public void onItemOpened(int id) {
			Message msg = new Message();
			msg.arg1 = id;
			msg.what = MSG_DEGREE_OPEND;
			uiHandler.sendMessage(msg);
			GoOutDebug.e(TAG, "onItemOpened = " + id);
		}

		@Override
		public void onItemClosed(int id) {
			GoOutDebug.e(TAG, "onItemClosed = " + id);
		}
	}

	/**
	 * 加载院校详情的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-20
	 */
	private class MyGetSchoolInfoListener extends GetSchoolDetailListener {

		@Override
		public void getSchoolDetailFailed() {
			super.getSchoolDetailFailed();
			GoOutDebug.e(TAG, "getSchoolDetailFailed");
			uiHandler.sendEmptyMessage(MSG_LOAD_INTRO_FAIL);
		}

		@Override
		public void getSchoolDetailFinished(School school) {
			super.getSchoolDetailFinished(school);
			GoOutDebug.e(TAG, "getSchoolDetailFinished");
			if (school != null) {
				// bGetSchooInfoOk = true;
				copySchoolValues(school);
				uiHandler.sendEmptyMessage(MSG_LOAD_INTRO_SUCCESS);

				// 加载学位信息
				mArrayDegreesInfo = new ArrayList<SchoolDegreeInfo>();
				String[] degrees = school.getDegrees();
				if (degrees != null && degrees.length > 0) {
					degreesCount = 0;
					for (int i = 0; i < degrees.length; i++) {
						// GoOutDebug.v(TAG, "load degree info：" + degrees[i]);
						mController.getSchoolDegreeDetail(SchoolMainActivity.mSchool.getServerId(), degrees[i], new MyGetSchoolDegreeDetailListener(degrees[i]));
					}
					// for (String degree : degrees) {
					// }
				}

				// else {
				// // 不含学位信息，加载成功
				// uiHandler.sendEmptyMessage(MSG_LOAD_INTRO_SUCCESS);
				// }

				// if (bGetSchoolDegreeOk && bGetSchooInfoOk) {
				// // 都加载完成，显示
				// uiHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
				// }
			} else {
				uiHandler.sendEmptyMessage(MSG_LOAD_INTRO_FAIL);
			}

		}

	}

	/**
	 * 加载学位信息的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-20
	 */
	private class MyGetSchoolDegreeDetailListener extends GetSchoolDegreeDetailListener {

		private String degreeName;// 由于返回的json里没有degree名字，所以加此变量

		public MyGetSchoolDegreeDetailListener(String degreeName) {
			this.degreeName = degreeName;
		}

		@Override
		public void getSchoolDegreeDetailFailed() {
			super.getSchoolDegreeDetailFailed();
			GoOutDebug.e(TAG, "getSchoolDegreeDetailFailed");
			uiHandler.sendEmptyMessage(MSG_LOAD_DEGREES_FAIL);
			degreesCount = 0;
		}

		@Override
		public void getSchoolDegreeDetailFinished(SchoolDegreeInfo degreeInfo) {
			super.getSchoolDegreeDetailFinished(degreeInfo);

			degreesCount++;
			// GoOutDebug.e(TAG, "getSchoolDegreeDetailFinished degreesCount=" + degreesCount + "all length="
			// + SchoolMainActivity.mSchool.getDegrees().length);

			// 以下代码是：若学位里的信息为空，只显示学位条
			// if (degreeInfo == null) {
			// degreeInfo = new SchoolDegreeInfo();
			// }
			// degreeInfo.setDegreeName(degreeName);
			// mArrayDegreesInfo.add(degreeInfo);

			// 以下代码是：若学位里的信息为空，则不显示该学位
			if (degreeInfo != null) {
				degreeInfo.setDegreeName(degreeName);
				mArrayDegreesInfo.add(degreeInfo);
				GoOutDebug.e(TAG, "not null add one degreeInfo!!!!");
			}

			// 判断是否获得完所有的学位信息
			if (degreesCount == SchoolMainActivity.mSchool.getDegrees().length) {
				// 都加载完成，显示
				degreesCount = 0;
				GoOutDebug.e(TAG, "MSG_LOAD_SUCCESS!!!!");
				uiHandler.sendEmptyMessage(MSG_LOAD_DEGREES_SUCCESS);
			}
		}

	}
}
