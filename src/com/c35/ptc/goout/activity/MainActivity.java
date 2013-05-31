package com.c35.ptc.goout.activity;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.c35.ptc.goout.R;
import com.c35.ptc.goout.util.InitUtil;

/**
 * 含tab选项卡的主页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
@SuppressLint("NewApi")
public class MainActivity extends ActivityGroup implements OnClickListener {

	private LinearLayout bodyView;// 显示不同的页面
	// 底部选项卡
	private LinearLayout tabStart;
	private LinearLayout tabSchool;
	private LinearLayout tabPersonal;
	private ImageView imgTabStart;
	private ImageView imgTabSchool;
	private ImageView imgTabPersonal;

	private LinearLayout layoutMainContent;
	private LinearLayout layoutTransition;
	private ImageView imgTransition;
	private Animation animationOut;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		layoutMainContent = (LinearLayout) findViewById(R.id.layout_main_content);
		bodyView = (LinearLayout) findViewById(R.id.tab_layout_mainbody);
		tabStart = (LinearLayout) findViewById(R.id.tab_start);
		tabSchool = (LinearLayout) findViewById(R.id.tab_school);
		tabPersonal = (LinearLayout) findViewById(R.id.tab_personal);
		layoutTransition = (LinearLayout) findViewById(R.id.layout_transition);
		imgTransition = (ImageView) findViewById(R.id.img_transition);
		imgTabStart = (ImageView) findViewById(R.id.img_tab_start);
		imgTabSchool = (ImageView) findViewById(R.id.img_tab_school);
		imgTabPersonal = (ImageView) findViewById(R.id.img_tab_personal);

		tabStart.setOnClickListener(this);
		tabSchool.setOnClickListener(this);
		tabPersonal.setOnClickListener(this);

		showTransiton();

		InitUtil.InitFileDir();// 初始化文件目录

		showStartPage();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_start:
			showStartPage();
			break;
		case R.id.tab_school:
			showSchoolPage();
			break;
		case R.id.tab_personal:
			showPersonalPage();
			break;

		default:
			break;
		}

	}

	/**
	 * 显示首页
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showStartPage() {
		tabStart.setBackgroundResource(R.drawable.tabmain_bg_selected);
		imgTabStart.setImageResource(R.drawable.tabmain_ico_start_p);
		imgTabSchool.setImageResource(R.drawable.tabmain_ico_school_n);
		imgTabPersonal.setImageResource(R.drawable.tabmain_ico_personal_n);
		tabSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabPersonal.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("start", new Intent(MainActivity.this, MainStartActivity.class)).getDecorView());
	}

	/**
	 * 显示院校页
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showSchoolPage() {
		tabStart.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabSchool.setBackgroundResource(R.drawable.tabmain_bg_selected);
		tabPersonal.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		imgTabStart.setImageResource(R.drawable.tabmain_ico_start_n);
		imgTabSchool.setImageResource(R.drawable.tabmain_ico_school_p);
		imgTabPersonal.setImageResource(R.drawable.tabmain_ico_personal_n);
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("school", new Intent(MainActivity.this, MainSchoolActivity.class)).getDecorView());
	}

	/**
	 * 显示个人页面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showPersonalPage() {
		tabStart.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		tabPersonal.setBackgroundResource(R.drawable.tabmain_bg_selected);
		imgTabStart.setImageResource(R.drawable.tabmain_ico_start_n);
		imgTabSchool.setImageResource(R.drawable.tabmain_ico_school_n);
		imgTabPersonal.setImageResource(R.drawable.tabmain_ico_personal_p);
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("personal", new Intent(MainActivity.this, MainPersonalActivity.class)).getDecorView());
	}

	/**
	 * 显示欢迎界面
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void showTransiton() {

		animationOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
		animationOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layoutTransition.setVisibility(View.GONE);
				layoutMainContent.setVisibility(View.VISIBLE);
			}
		});
		imgTransition.setAnimation(animationOut);
	}
}
