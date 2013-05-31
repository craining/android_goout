package com.c35.ptc.goout.activity;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.TimeUtil;

/**
 * 含Tab选项卡的院校页面
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */

public class SchoolMainActivity extends ActivityGroup implements OnClickListener {

	private LinearLayout bodyView;// 显示不同的页面
	// 底部选项卡
	private LinearLayout tabInfo;
	private LinearLayout tabMajor;
	private LinearLayout tabProject;
	private LinearLayout tabPic;

	public static boolean boolIsFav;// 是否已收藏
	public static School mSchool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_school_main);

		bodyView = (LinearLayout) findViewById(R.id.tab_layout_school_mainbody);
		tabInfo = (LinearLayout) findViewById(R.id.tab_school_info);
		tabMajor = (LinearLayout) findViewById(R.id.tab_school_major);
		tabProject = (LinearLayout) findViewById(R.id.tab_school_project);
		tabPic = (LinearLayout) findViewById(R.id.tab_school_pic);

		tabInfo.setOnClickListener(this);
		tabMajor.setOnClickListener(this);
		tabProject.setOnClickListener(this);
		tabPic.setOnClickListener(this);

		mSchool = new School();

		Bundle b = getIntent().getExtras();
		if (b != null && b.containsKey("schoolServerId")) {

			mSchool.setServerId(b.getInt("schoolServerId"));
			mSchool.setNameCn(b.getString("schoolNameCn"));
			mSchool.setNameEn(b.getString("schoolNameEn"));
			mSchool.setLogoName(b.getString("schoolLogoName"));

			showInfoPage();
		} else {
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_school_info:
			showInfoPage();
			break;
		case R.id.tab_school_major:
			showMajorPage();
			break;
		case R.id.tab_school_project:
			showProjectPage();
			break;
		case R.id.tab_school_pic:
			showPicPage();
			break;

		default:
			break;
		}
	}

	/**
	 * 显示简介页面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showInfoPage() {
		tabInfo.setBackgroundResource(R.drawable.tabschool_bg_selected);
		tabMajor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabProject.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabPic.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("info", new Intent(SchoolMainActivity.this, SchoolIntroActivity.class)).getDecorView());
	}

	/**
	 * 显示专业页面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showMajorPage() {
		tabInfo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabMajor.setBackgroundResource(R.drawable.tabschool_bg_selected);
		tabProject.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabPic.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("major", new Intent(SchoolMainActivity.this, SchoolMajorActivity.class)).getDecorView());
	}

	/**
	 * 显示项目推荐页面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showProjectPage() {
		tabInfo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabMajor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabProject.setBackgroundResource(R.drawable.tabschool_bg_selected);
		tabPic.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("project", new Intent(SchoolMainActivity.this, SchoolProjectActivity.class)).getDecorView());
	}

	/**
	 * 显示照片页面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showPicPage() {
		tabInfo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabMajor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabProject.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabPic.setBackgroundResource(R.drawable.tabschool_bg_selected);
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("pic", new Intent(SchoolMainActivity.this, SchoolPicActivity.class)).getDecorView());
	}

	/**
	 * 收藏或取消收藏
	 * 
	 * @Description:
	 * @param con
	 * @param controller
	 * @return 操作成功返回true
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public static boolean ctrlFavOpera(Context con, GoOutController controller) {
		boolean operaResult = false;
		// 收藏或取消收藏
		if (SchoolMainActivity.boolIsFav) {
			// 取消收藏(数据库操作)
			// operaResult = controller.unFavOneSchool(con, SchoolMainActivity.mSchool.getServerId());
			operaResult = controller.unFavOneSchool(con, mSchool);
		} else {
			// 收藏(数据库操作)
			// operaResult = controller.favOneSchool(con, SchoolMainActivity.mSchool.getServerId(),
			// TimeUtil.getCurrentTimeMillis());
			operaResult = controller.favOneSchool(con, mSchool, TimeUtil.getCurrentTimeMillis());
		}
		if (operaResult) {
			boolIsFav = !boolIsFav;// 操作成功后才能设置
			Toast.makeText(con, R.string.doopera_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(con, R.string.doopera_fail, Toast.LENGTH_SHORT).show();
		}
		return operaResult;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
