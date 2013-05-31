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
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.interfaces.SyncFavoriteProjectListener;
import com.c35.ptc.goout.listviewloader.ProjectListAdapter;
import com.c35.ptc.goout.logic.SyncFavoriteProjectLogic;

/**
 * 项目收藏
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */
public class FavouriteProjectAcitivty extends Activity implements OnClickListener,
		SyncFavoriteProjectListener {

	private ImageView imgBack;
	private Button btnRefresh;
	private LinearLayout layoutInfo;// 尚未收藏项目等
	private ListView listviewProjects;
	private ArrayList<Project> listProjects;
	private ProjectListAdapter adapterProjects;

	private GoOutController mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favourite_project);

		SyncFavoriteProjectLogic.getInstance().addListener(this);

		mController = GoOutController.getInstance();

		imgBack = (ImageView) findViewById(R.id.img_favourite_project_back);
		btnRefresh = (Button) findViewById(R.id.btn_favourite_project_refresh);
		layoutInfo = (LinearLayout) findViewById(R.id.layout_favourite_project_info);
		listviewProjects = (ListView) findViewById(R.id.listview_favourite_project);

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

		listviewProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(FavouriteProjectAcitivty.this, ProjectInfoActivity.class);
				i.putExtra("projectServerId", listProjects.get(position).getServerId());
				i.putExtra("projectName", listProjects.get(position).getName());
				i.putExtra("publisherServerId", listProjects.get(position).getPublisherServerId());
				i.putExtra("publisherType", listProjects.get(position).getPublisherType());
				i.putExtra("logoName", listProjects.get(position).getLogoName());
				startActivity(i);
			}

		});

	}

	/**
	 * 获得本地数据，显示ui。
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-18
	 */
	private void showList() {
		listProjects = new ArrayList<Project>();
		listProjects = mController.getFavProjects(getApplicationContext());
		if (listProjects != null && listProjects.size() > 0) {
			layoutInfo.setVisibility(View.GONE);
			listviewProjects.setVisibility(View.VISIBLE);
			adapterProjects = new ProjectListAdapter(FavouriteProjectAcitivty.this, listProjects);
			listviewProjects.setAdapter(adapterProjects);
		} else {
			layoutInfo.setVisibility(View.VISIBLE);
			listviewProjects.setVisibility(View.GONE);
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
		SyncFavoriteProjectLogic.getInstance().removeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_favourite_project_back:
			finish();
			break;
		case R.id.btn_favourite_project_refresh:
			SyncFavoriteProjectLogic.getInstance().sync();
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
