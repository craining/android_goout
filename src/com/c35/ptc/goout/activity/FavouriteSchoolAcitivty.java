package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.interfaces.SyncFavoriteSchoolListener;
import com.c35.ptc.goout.listviewloader.SchoolListAdapter;
import com.c35.ptc.goout.logic.SyncFavoriteSchoolLogic;

/**
 * 院校收藏
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class FavouriteSchoolAcitivty extends Activity implements OnClickListener, SyncFavoriteSchoolListener {

	private ImageView imgBack;
	private LinearLayout layoutInfoShow;
	private ListView listviewSchools;
	private ArrayList<School> listSchools;
	private SchoolListAdapter adapterSchool;
	private GoOutController mController;

	private Button btnRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favourite_school);
		SyncFavoriteSchoolLogic.getInstance().addListener(this);
		mController = GoOutController.getInstance();
		btnRefresh = (Button) findViewById(R.id.btn_favourite_school_refresh);
		imgBack = (ImageView) findViewById(R.id.img_favourite_school_back);
		layoutInfoShow = (LinearLayout) findViewById(R.id.layout_favourite_school_info);
		listviewSchools = (ListView) findViewById(R.id.listview_favourite_school);

		initViewsListener();

	}

	/**
	 * 初始化控件的监听
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	private void initViewsListener() {
		imgBack.setOnClickListener(this);
		btnRefresh.setOnClickListener(this);

		listviewSchools.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(FavouriteSchoolAcitivty.this, SchoolMainActivity.class);
				i.putExtra("schoolServerId", listSchools.get(position).getServerId());
				i.putExtra("schoolNameCn", listSchools.get(position).getNameCn());
				i.putExtra("schoolNameEn", listSchools.get(position).getNameEn());
				i.putExtra("schoolLogoName", listSchools.get(position).getLogoName());
				startActivity(i);
			}

		});

	}

	/**
	 * 获得收藏的院校，并显示
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	private void showList() {
		listSchools = new ArrayList<School>();
		listSchools = mController.getFavSchools(getApplicationContext());
		if (listSchools != null && listSchools.size() > 0) {
			layoutInfoShow.setVisibility(View.GONE);
			listviewSchools.setVisibility(View.VISIBLE);
			adapterSchool = new SchoolListAdapter(FavouriteSchoolAcitivty.this, listSchools);
			listviewSchools.setAdapter(adapterSchool);
		} else {
			layoutInfoShow.setVisibility(View.VISIBLE);
			listviewSchools.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		showList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SyncFavoriteSchoolLogic.getInstance().removeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_favourite_school_back:
			finish();
			break;
		case R.id.btn_favourite_school_refresh:
			SyncFavoriteSchoolLogic.getInstance().sync();
			break;
		default:
			break;
		}

	}

	@Override
	public void syncResult(int result) {

		switch (result) {
		case SYNC_START:
			// 开始同步
			Toast.makeText(this, R.string.sycn_start, Toast.LENGTH_SHORT).show();
			break;
		case SYNC_ING:
			// 正在同步
			Toast.makeText(this, R.string.sycn_ing, Toast.LENGTH_SHORT).show();
			break;
		case SYNC_FAIL:
			// 同步失败
			Toast.makeText(this, R.string.sycn_fail, Toast.LENGTH_SHORT).show();
			break;
		case SYNC_NOT_LOGIN:
			// 未登录
			Toast.makeText(this, R.string.sycn_fail_notlogin, Toast.LENGTH_SHORT).show();
			break;
		case SYNC_SUCC:
			// 同步成功
			Toast.makeText(this, R.string.sycn_sucess, Toast.LENGTH_SHORT).show();
			showList();
			break;
		default:
			break;
		}

	}

}
