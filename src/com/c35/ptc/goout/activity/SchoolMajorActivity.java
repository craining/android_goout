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
import android.widget.TextView;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.SchoolMajorInfo;
import com.c35.ptc.goout.interfaces.GetSchooMajorListener;
import com.c35.ptc.goout.interfaces.ViewSchoolMajorListener;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.view.SchoolMajorView;

/**
 * 院校中的专业页面
 * 
 * 
 * (此页暂时将加载失败、无网络时的“重试”按钮去掉，因为degrees依托于简介页面获得的所有学位数据)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */

public class SchoolMajorActivity extends Activity implements OnClickListener {

	private static final String TAG = "SchoolMajorActivity";

	// 加载中等信息展示
	private LinearLayout layoutShowInfo;
	private TextView textShowInfo;
	// private Button btnReLoad;

	private ImageView imgBack;
	private LinearLayout layoutMajors;
	// private SchoolMajorInfo[] schoolMajors;
	private ArrayList<SchoolMajorInfo> mArraySchoolMajorInfos;
	private SchoolMajorView[] schoolMajorViews;

	private Handler uiHandler;
	private static final int MSG_MAJOR_OPENED = 0x705;// 专业展开
	private static final int MSG_LOAD_SUCCESS = 0x706;// 加载完成
	private static final int MSG_LOAD_FAIL = 0x707;// 加载失败
	private static final int MSG_NULL = 0x708;// 暂无数据（没有degrees）

	private Button btnFav;// 收藏或取消收藏
	private GoOutController mController;

	private int degreesCount = 0;// 学位计数，每加载一个学位的专业，计数加1，便可判断所有学位是否加载完成
	
	private boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_school_major);

		uiHandler = new UiHandler();
		mController = GoOutController.getInstance();

		btnFav = (Button) findViewById(R.id.btn_schoolmajor_fav);
		imgBack = (ImageView) findViewById(R.id.img_schoolmajor_back);
		layoutMajors = (LinearLayout) findViewById(R.id.layout_schoolmajor_body);
		layoutShowInfo = (LinearLayout) findViewById(R.id.layout_schoolmajor_loadinfo);
		textShowInfo = (TextView) findViewById(R.id.text_schoolmajor_loadinfo);
		// btnReLoad = (Button) findViewById(R.id.btn_schoolmajor_reload);

		// btnReLoad.setOnClickListener(this);
		btnFav.setOnClickListener(this);
		imgBack.setOnClickListener(this);
	}

	/**
	 * 加载专业信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void getMajorInfo() {

		if (GoOutTest.test) {
			// // test
			SchoolMajorInfo a;
			mArraySchoolMajorInfos = new ArrayList<SchoolMajorInfo>();
			for (int i = 0; i < 4; i++) {
				a = new SchoolMajorInfo();
				a.setGroupName("高中");
				a.setMajorsCn(new String[] { "软件工程", "网络工程", "计算机科学与技术", "网络工程", "计算机科学与技术", "网络工程", "计算机科学与技术", "网络工程", "计算机科学与技术", "网络工程" });
				a.setMajorsEn(new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" });
				mArraySchoolMajorInfos.add(a);
			}
			showInfo();
		} else {
			final String[] degrees = SchoolMainActivity.mSchool.getDegrees();
			// String[] degrees = new String[] { "高中", "高中" };//test
			if (degrees != null && degrees.length > 0) {
				if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					textShowInfo.setText(R.string.loadnonetwork);
					// btnReLoad.setVisibility(View.VISIBLE);
					return;
				} else {
					// 获取所有学位的专业信息
					// btnReLoad.setVisibility(View.GONE);
					mArraySchoolMajorInfos = new ArrayList<SchoolMajorInfo>();
					textShowInfo.setText(R.string.schoolmajor_loading);
					degreesCount = 0;
					isLoading = true;
					new Thread(new Runnable() {

						@Override
						public void run() {
							for (int i = 0; i < degrees.length; i++) {
								mController.getSchoolMajor(SchoolMainActivity.mSchool.getServerId(), degrees[i], new MyGetSchoolMajorListener(degrees[i]));
							}
						}
					}).start();
				}

			} else {
				// 可能的原因有：
				// 一、在SchoolIntro页面尚未获得所有degree前进入此页；二、此院校没有任何degree。
				// 提示“暂无专业数据”
				GoOutDebug.e(TAG, "degrees of school is null");
				uiHandler.sendEmptyMessage(MSG_NULL);
			}

		}

	}

	/**
	 * 显示专业信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void showInfo() {

		layoutShowInfo.setVisibility(View.GONE);

		layoutMajors.removeAllViews();

		schoolMajorViews = new SchoolMajorView[mArraySchoolMajorInfos.size()];
		for (int i = 0; i < schoolMajorViews.length; i++) {
			schoolMajorViews[i] = new SchoolMajorView(SchoolMajorActivity.this);
			schoolMajorViews[i].setContentView(mArraySchoolMajorInfos.get(i), i);
			schoolMajorViews[i].setViewSchoolMajorListener(new OnSchollMajorViewListener());
			// if (i == 0) {
			// schoolMajorViews[i].setBackgroundResource(R.drawable.bg_roundcorner_top);
			// }
			layoutMajors.addView(schoolMajorViews[i]);
		}

		// if (schoolMajorViews.length > 0) {
		// mutexMajorShow(0);
		// }
		// openAllMajorShow();//暂时不全部展开

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
		// 若为空，可能是无网络,重新加载
		if (!isLoading && (schoolMajorViews == null || schoolMajorViews.length < 0 )) {
			getMajorInfo();
			GoOutDebug.e(TAG, "load!");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_schoolmajor_back:
			finish();
			break;
		case R.id.btn_schoolmajor_fav:
			// 收藏或取消收藏
			if (SchoolMainActivity.ctrlFavOpera(SchoolMajorActivity.this, mController)) {
				ctrlFavButtonText();
			}
			break;
		case R.id.btn_schoolmajor_reload:
			// 重新加载
			getMajorInfo();
			break;
		default:
			break;
		}

	}

	// /**
	// * 互斥显示专业分组
	// *
	// * @Description:
	// * @param id
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-3-13
	// */
	// private void mutexMajorShow(int id) {
	// for (int i = 0; i < schoolMajorViews.length; i++) {
	// if (id != i) {
	// schoolMajorViews[i].setOpenClose(false);// 关闭分组
	// }
	// }
	// }

	/**
	 * 展开所有专业信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void openAllMajorShow() {
		for (int i = 0; i < schoolMajorViews.length; i++) {
			schoolMajorViews[i].setOpenClose(true);
		}
	}

	/**
	 * 接收handler消息控制ui
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-14
	 */
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_MAJOR_OPENED:
				// mutexMajorShow(msg.arg1);// 互斥展开
				break;
			case MSG_LOAD_SUCCESS:
				// 加载成功
				isLoading = false;
				showInfo();

				break;
			case MSG_LOAD_FAIL:
				// 加载失败
				textShowInfo.setText(R.string.schoolmajor_loadfail);
				// btnReLoad.setVisibility(View.VISIBLE);

				break;
			case MSG_NULL:
				textShowInfo.setText(R.string.schoolmajor_loadnull);
				// btnReLoad.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 某个学位的专业被展开的消息监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class OnSchollMajorViewListener implements ViewSchoolMajorListener {

		@Override
		public void onItemOpened(int id) {
			GoOutDebug.e(TAG, "onItemOpened = " + id);
			Message msg = new Message();
			msg.what = MSG_MAJOR_OPENED;
			msg.arg1 = id;
			uiHandler.sendMessage(msg);
		}

		@Override
		public void onItemClosed(int id) {
			GoOutDebug.e(TAG, "onItemClosed = " + id);
		}
	}

	/**
	 * 监听下载专业信息
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-20
	 */
	private class MyGetSchoolMajorListener extends GetSchooMajorListener {

		private String degreeName;// 由于返回的json里没有degree名字，所以加此变量

		public MyGetSchoolMajorListener(String degreeName) {
			this.degreeName = degreeName;
		}

		@Override
		public void getSchooMajorFailed() {
			super.getSchooMajorFailed();
			GoOutDebug.e(TAG, "getSchooMajorFailed");
			uiHandler.sendEmptyMessage(MSG_LOAD_FAIL);
			degreesCount = 0;
		}

		@Override
		public void getSchooMajorFinished(SchoolMajorInfo majorInfo) {
			super.getSchooMajorFinished(majorInfo);

			degreesCount++;
			if (majorInfo != null) {
				GoOutDebug.e(TAG, "getSchooMajorFinished not null");
				majorInfo.setGroupName(degreeName);
				mArraySchoolMajorInfos.add(majorInfo);
				// if (mArraySchoolMajorInfos.size() == SchoolMainActivity.mSchool.getDegrees().length) {
				// // 获得的专业组数与学位数相等，则获得完全
				// uiHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
				// }
			}
			// else {
			// // 有一个失败，视为失败
			// GoOutDebug.e(TAG, "getSchooMajorFinished null");
			// uiHandler.sendEmptyMessage(MSG_LOAD_FAIL);
			// }

			if (degreesCount == SchoolMainActivity.mSchool.getDegrees().length) {
				// 获得完全
				if (mArraySchoolMajorInfos.size() == 0) {
					uiHandler.sendEmptyMessage(MSG_NULL);
				} else {
					uiHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
				}
			}
		}

	}
}
