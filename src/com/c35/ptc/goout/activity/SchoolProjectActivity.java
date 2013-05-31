package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.interfaces.GetProjectListListener;
import com.c35.ptc.goout.listviewloader.ProjectListAdapter;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;

/**
 * 院校中的项目推荐页面
 * 
 * * (此页暂时将加载失败、无网络时的“重试”按钮去掉,因为此页)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
public class SchoolProjectActivity extends Activity implements OnClickListener {

	private static final String TAG = "SchoolProjectActivity";

	private ImageView imgBack;
	private Button btnFav;// 收藏或取消收藏

	private TextView textShowInfo;// 加载中、加载完成、加载失败、网络问题等
	private LinearLayout layoutShowInfo;// textShowInfo的容器，以后可往里面加图片啥的
	private Button btnReload;
	private ListView listviewProjects;

	private ProjectListAdapter adapterProjectListOfSchool;

	private GetProjectListListener mListener;// 调用服务器接口时的监听
	private GoOutController mController;

	private Handler uiHandler;
	private static final int MSG_GETPROJECTLIST_FINISHED = 0x400;// 获取成功
	private static final int MSG_GETPROJECTLIST_FAIL = 0x401;// 获取失败
	private static final int MSG_GETPROJECTLIST_NULL = 0x402;// 获取为空

	private GoOutGlobal appGloble;// 全局对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_school_project);

		appGloble = (GoOutGlobal) getApplicationContext();

		mController = GoOutController.getInstance();
		mListener = new GetProjectListOfSchoolListListener();
		uiHandler = new UiHandler();

		imgBack = (ImageView) findViewById(R.id.img_schoolproject_back);
		btnFav = (Button) findViewById(R.id.btn_schoolproject_fav);
		textShowInfo = (TextView) findViewById(R.id.text_schoolproject_load);
		btnReload = (Button) findViewById(R.id.btn_schoolproject_reload);
		layoutShowInfo = (LinearLayout) findViewById(R.id.layout_schoolproject_load);
		listviewProjects = (ListView) findViewById(R.id.listview_schoolproject);

		imgBack.setOnClickListener(this);
		btnFav.setOnClickListener(this);
		btnReload.setOnClickListener(this);

		listviewProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(SchoolProjectActivity.this, ProjectInfoActivity.class);
				i.putExtra("projectServerId", appGloble.getArrayProjectsOfSchool().get(position).getServerId());
				i.putExtra("projectName", appGloble.getArrayProjectsOfSchool().get(position).getName());
				i.putExtra("publisherServerId", appGloble.getArrayProjectsOfSchool().get(position).getPublisherServerId());
				i.putExtra("publisherType", appGloble.getArrayProjectsOfSchool().get(position).getPublisherType());
				startActivity(i);
			}

		});

		// 暂时注释
		// 滚动时不加载图片
		// listviewProjects.setOnScrollListener(new AbsListView.OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// switch (scrollState) {
		// case OnScrollListener.SCROLL_STATE_FLING:
		// adapterProjectListOfSchool.setFlagBusy(true);
		// // 此段代码说明见{@link MainSchoolActivity#listSchoolSortLetter.setOnScrollListener()}
		// if ((view.getLastVisiblePosition() == (view.getCount() - 1)) || (view.getFirstVisiblePosition() ==
		// 0)) {
		// adapterProjectListOfSchool.setFlagBusy(false);
		// }
		// break;
		// case OnScrollListener.SCROLL_STATE_IDLE:
		// adapterProjectListOfSchool.setFlagBusy(false);
		// break;
		// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// adapterProjectListOfSchool.setFlagBusy(false);
		// break;
		// default:
		// break;
		// }
		// adapterProjectListOfSchool.notifyDataSetChanged();
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
	 * 获得项目列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void getData() {

		if (GoOutTest.test) {
			// test
			appGloble.setArrayProjectsOfSchool(GoOutTest.getTestProjectListData());
			showProjectList();
		} else {
			// 有则显示，无则获取
			if (appGloble.getArrayProjectsOfSchool() == null || appGloble.getArrayProjectsOfSchool().size() == 0) {

				// 判断网络状态
				if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					textShowInfo.setText(R.string.loadnonetwork);
					btnReload.setVisibility(View.VISIBLE);
					return;
				} else {
					textShowInfo.setText(R.string.projectlist_loading);
					btnReload.setVisibility(View.GONE);
					mController.getPorjectListOfSchool(SchoolMainActivity.mSchool.getServerId(), mListener);
				}
			} else {
				showProjectList();
			}
		}

	}

	/**
	 * 显示项目列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void showProjectList() {

		layoutShowInfo.setVisibility(View.GONE);
		adapterProjectListOfSchool = new ProjectListAdapter(SchoolProjectActivity.this, appGloble.getArrayProjectsOfSchool());
		listviewProjects.setAdapter(adapterProjectListOfSchool);
		listviewProjects.setVisibility(View.VISIBLE);
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
		getData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GoOutDebug.e(TAG, "onDestroy");
		// 销毁院校项目列表缓存
		appGloble.setArrayProjectsOfSchool(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_schoolproject_back:
			finish();
			break;
		case R.id.btn_schoolproject_fav:
			// 收藏或取消收藏
			if (SchoolMainActivity.ctrlFavOpera(SchoolProjectActivity.this, mController)) {
				ctrlFavButtonText();
			}
			break;
		case R.id.btn_schoolproject_reload:
			// 重新加载
			getData();
			break;
		default:
			break;
		}

	}

	/**
	 * HANDLER接收器，处理ui
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
			case MSG_GETPROJECTLIST_FAIL:
				textShowInfo.setText(R.string.projectlist_loadfail);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_GETPROJECTLIST_FINISHED:
				showProjectList();
				break;
			case MSG_GETPROJECTLIST_NULL:
				textShowInfo.setText(R.string.projectlist_null);
				btnReload.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 获取国家列表的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-14
	 */
	private class GetProjectListOfSchoolListListener extends GetProjectListListener {

		@Override
		public void getProjectListFailed() {
			super.getProjectListFailed();
			uiHandler.sendEmptyMessage(MSG_GETPROJECTLIST_FAIL);
			GoOutDebug.v(TAG, "getSelectCountryListFailed");
		}

		@Override
		public void getProjectListFinished(ArrayList<Project> projects) {
			super.getProjectListFinished(projects);
			if (projects != null && projects.size() > 0) {
				// 加载完成
				appGloble.setArrayProjectsOfSchool(projects);
				uiHandler.sendEmptyMessage(MSG_GETPROJECTLIST_FINISHED);
				GoOutDebug.v(TAG, "getSelectCountryListFinished not null");
			} else {
				// 为空
				uiHandler.sendEmptyMessage(MSG_GETPROJECTLIST_NULL);
				GoOutDebug.v(TAG, "getSelectCountryListFinished null");
			}
		}
	}
}
