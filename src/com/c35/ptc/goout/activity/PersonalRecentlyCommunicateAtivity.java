package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.RecentlyConsult;
import com.c35.ptc.goout.listviewloader.ConsultListAdapter;

/**
 * 最近联系
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class PersonalRecentlyCommunicateAtivity extends Activity implements OnClickListener {

	private static final String TAG = "PersonalRecentlyCommunicateAtivity";

	private ImageView imgBack;
	private LinearLayout layoutInfo;// 显示尚未联系过等
	private ListView listviewConsultants;
	private ArrayList<RecentlyConsult> listConsultants;
	private ConsultListAdapter adapterConsultants;

	private GoOutController mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal_recently_communicate);

		mController = GoOutController.getInstance();
		imgBack = (ImageView) findViewById(R.id.img_recently_communicate_back);
		listviewConsultants = (ListView) findViewById(R.id.listview_recently_communicate);
		layoutInfo = (LinearLayout) findViewById(R.id.layout_recently_communicate_info);

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

		listviewConsultants.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 跳转到顾问详情页
				Intent i = new Intent(PersonalRecentlyCommunicateAtivity.this, ConsultantInfoActivity.class);
				i.putExtra("projectServerId", listConsultants.get(position).getProjectId());
				i.putExtra("calledNumber", listConsultants.get(position).getCalledNumber());
				// GoOutDebug.e(TAG, "send calledNumber=" + listConsultants.get(position).getCalledNumber());
				i.putExtra("calledType", listConsultants.get(position).getCalledType());
				i.putExtra("consultantServerId", listConsultants.get(position).getCalledID());
				i.putExtra("calledLogoName", listConsultants.get(position).getCalledLogoName());
				i.putExtra("calledServerArea", listConsultants.get(position).getCalledServerArea());
				i.putExtra("calledDescription", listConsultants.get(position).getCalledDescription());
				i.putExtra("calledName", listConsultants.get(position).getCalledName());
				i.putExtra("calledPublisherProjectCount", -1);// 因为是本地读取的最近联系人，其发布的项目数不准确，无须存储
				startActivity(i);
			}

		});

		// // // 滚动时不加载图片
		// listviewConsultants.setOnScrollListener(new AbsListView.OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// switch (scrollState) {
		// case OnScrollListener.SCROLL_STATE_FLING:
		// adapterConsultants.setFlagBusy(true);
		// // 此段代码说明见{@link MainSchoolActivity#listSchoolSortLetter.setOnScrollListener()}
		// if ((view.getLastVisiblePosition() == (view.getCount() - 1)) || (view.getFirstVisiblePosition() ==
		// 0)) {
		// adapterConsultants.setFlagBusy(false);
		// }
		// break;
		// case OnScrollListener.SCROLL_STATE_IDLE:
		// adapterConsultants.setFlagBusy(false);
		// break;
		// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// adapterConsultants.setFlagBusy(false);
		// break;
		// default:
		// break;
		// }
		// adapterConsultants.notifyDataSetChanged();
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
		// totalItemCount) {
		// }
		// });
	}

	/**
	 * 显示最近联系记录
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	private void showList() {
		listConsultants = new ArrayList<RecentlyConsult>();
		listConsultants = mController.getRecentlyCommunicateListAll(getApplicationContext());
		if (listConsultants != null && listConsultants.size() > 0) {
			layoutInfo.setVisibility(View.GONE);
			listviewConsultants.setVisibility(View.VISIBLE);
			adapterConsultants = new ConsultListAdapter(PersonalRecentlyCommunicateAtivity.this, listConsultants);
			listviewConsultants.setAdapter(adapterConsultants);
		} else {
			layoutInfo.setVisibility(View.VISIBLE);
			listviewConsultants.setVisibility(View.GONE);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_recently_communicate_back:
			finish();
			break;

		default:
			break;
		}

	}
}
