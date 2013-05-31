package com.c35.ptc.goout.activity;

import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewPictureClickListener;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.view.OnlineImageView;
import com.c35.ptc.goout.view.PictureClickView;
import com.c35.ptc.goout.view.ScrollViewExtend;

/**
 * 院校中的照片展示页面
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */

public class SchoolPicActivity extends Activity implements OnClickListener {

	private static final String TAG = "SchoolPicActivity";

	private ImageView imgBack;

	private PictureClickView picClicView;// 大图+底部若干张小图的控件
	private View layoutTop;
	private OnlineImageView imgSchoolPic;// 顶部图片
	private TextView textSchoolNameCn;
	private TextView textSchoolNameEn;
	private TextView textPicisLoading;// 图片正在加载的展示，主要是读取本地图片，然后显示到界面的上的过程
	private Handler uiHandler;
	private static final int MSG_PIC_CLICKVIEW_ONRESUME = 0x800;// picClickViewsetContentView完成

	private Button btnFav;// 收藏或取消收藏
	private GoOutController mController;

	private ScrollViewExtend scrollView;
	private RelativeLayout layoutMain;

	private boolean picsShowing = false;// 若未显示照片，则尝试重新显示。（之前断网，没有显示，再次来到此页，自动显示；ps：若每次都重新显示，效率较低。）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_school_pic);

		uiHandler = new UiHandler();
		mController = GoOutController.getInstance();

		imgBack = (ImageView) findViewById(R.id.img_schoolpic_back);
		btnFav = (Button) findViewById(R.id.btn_schoolpic_fav);
		picClicView = (PictureClickView) findViewById(R.id.picclickview_schoolpic);
		// layoutPicClicView = (LinearLayout) findViewById(R.id.layout_schoolpic_images_body);
		layoutTop = (View) findViewById(R.id.layout_schoolpic_top);
		imgSchoolPic = (OnlineImageView) findViewById(R.id.onlineimg_schoolpic_ic);
		textSchoolNameCn = (TextView) findViewById(R.id.text_schoolpic_namecn);
		textSchoolNameEn = (TextView) findViewById(R.id.text_schoolpic_nameen);
		textPicisLoading = (TextView) findViewById(R.id.text_schoolpic_loading);

		layoutMain = (RelativeLayout) findViewById(R.id.layout_schoolpic_main);
		scrollView = (ScrollViewExtend) findViewById(R.id.scroll_schoolpic);

		btnFav.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		showInfo();
		ViewTreeObserver vto = layoutTop.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			public boolean onPreDraw() {
				// int width = layoutTop.getMeasuredWidth();
				// Log.e("onPreDraw", "layoutTop.getMeasuredWidth()=" + layoutTop.getMeasuredWidth());
				showImages();
				return true;
			}
		});

	}

	/**
	 * 显示信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void showInfo() {
		// imgSchoolPic.setImageViewUrl(SchoolMainActivity.mSchool.getSchoolLogoUrl(),
		// OnlineImageView.PIC_SEZE_SMALL, false);
		imgSchoolPic.setImageViewUrl(ImageUrlUtil.getSchoolLogoUrl(SchoolMainActivity.mSchool.getLogoName()), OnlineImageView.PIC_SCHOOL_LOGO, false, ImageView.ScaleType.FIT_XY);
		textSchoolNameCn.setText(SchoolMainActivity.mSchool.getNameCn());
		textSchoolNameEn.setText(SchoolMainActivity.mSchool.getNameEn());
	}

	/**
	 * 加载图片
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-28
	 */
	private void showImages() {
		// 如果图片未展示，尝试重新加载
		if (!picsShowing) {
			picsShowing = true;

			// int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			// int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			//
			// layoutTop.measure(w, h);
			final int allWidth = layoutTop.getMeasuredWidth();
			GoOutDebug.v(TAG, "layoutTop.getMeasuredWidth()=" + allWidth);
			GoOutDebug.v(TAG, "layoutTop.getWidth()=" + layoutTop.getWidth());
			GoOutDebug.v(TAG, "layoutTop.getLeft()=" + layoutTop.getLeft());
			GoOutDebug.v(TAG, "layoutTop.getRight()=" + layoutTop.getRight());

			if (GoOutTest.test) {
				picClicView.setOnPictureClickedListener(new MainPictureClickViewListener());
				picClicView.setContentView(GoOutTest.testImagesUrl, "", 0, allWidth);
			} else {
				final String[] picNames = SchoolMainActivity.mSchool.getPicsNames();
				if (picNames != null && picNames.length > 0) {
					GoOutDebug.e(TAG, "showImages!!!!");
					textPicisLoading.setText(R.string.schoolpic_loading);

					// test,延迟操作，防止页面卡顿
					new Handler().postDelayed(new Runnable() {

						public void run() {

							// TODO 目前服务器上有重复数据，需要滤重
							HashSet set = new HashSet();
							set.addAll(Arrays.asList(picNames));
							String[] strs2 = (String[]) set.toArray(new String[0]);

							picClicView.setOnPictureClickedListener(new MainPictureClickViewListener());
							picClicView.setContentView(strs2, SchoolMainActivity.mSchool.getCountryName(), SchoolMainActivity.mSchool.getServerId(), allWidth);

						}
					}, 50);

				} else {
					// picsShowing = false;
					textPicisLoading.setText(R.string.schoolpic_null);
				}
			}

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
		if (SchoolMainActivity.boolIsFav) {
			btnFav.setText(R.string.projectinfo_title_btn_unfav);// 取消收藏
		} else {
			btnFav.setText(R.string.projectinfo_title_btn_fav);// 收藏
		}

	}

	/**
	 * 控制ScrollView滚动到底部
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-27
	 */
	private void scrollToBottom() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// GoOutDebug.e(TAG, "ScrollY: " + scrollView.getScrollY());
				int off = layoutMain.getMeasuredHeight() - scrollView.getHeight();
				if (off > 0) {
					scrollView.scrollTo(0, off);
				}
				// GoOutDebug.e(TAG, "ScrollY: " + scrollView.getScrollY());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		ctrlFavButtonText();
		if (SchoolMainActivity.mSchool.getPicsNames() == null || SchoolMainActivity.mSchool.getPicsNames().length == 0) {
			picsShowing = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_schoolpic_back:
			finish();
			break;
		case R.id.btn_schoolpic_fav:
			// 收藏或取消收藏
			if (SchoolMainActivity.ctrlFavOpera(SchoolPicActivity.this, mController)) {
				ctrlFavButtonText();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * handler接收器
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_PIC_CLICKVIEW_ONRESUME:
				textPicisLoading.setVisibility(View.GONE);
				picClicView.setVisibility(View.VISIBLE);
				scrollToBottom();// 整个ScrollView自动滚动到底部
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 图片查看view的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-5
	 */
	private class MainPictureClickViewListener implements ViewPictureClickListener {

		@Override
		public void onPictureShowListItemClickedListener(int id) {
			// 底部的某个小图被点击了
			GoOutDebug.e(TAG, "onPictureShowListItemClicked  !");
		}

		@Override
		public void onPictureShowMainImageClickedListener(int id, String picName) {
			// 当前展示的大图被点击了
			GoOutDebug.e(TAG, "onPictureShowMainImageClicked !");
		}

		@Override
		public void onPictureShowResume() {
			// ui加载完成
			uiHandler.sendEmptyMessage(MSG_PIC_CLICKVIEW_ONRESUME);
		}

	}
}
