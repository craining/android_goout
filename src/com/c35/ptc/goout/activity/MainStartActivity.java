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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.GoOutTest;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.interfaces.GetProjectListListener;
import com.c35.ptc.goout.interfaces.GetSelectCountryListListener;
import com.c35.ptc.goout.listviewloader.ProjectListAdapter;
import com.c35.ptc.goout.util.CheckUploadFailedDataUtil;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;

/**
 * 主页中的首页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
public class MainStartActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainStartActivity";

	// 过滤项目“研究生”、“本科”、“中学”、“游学”
	private LinearLayout layoutFilterHighSchool;
	private LinearLayout layoutFilterBachelor;
	private LinearLayout layoutFilterMaster;
	private LinearLayout layoutFilterDoctor;
	private LinearLayout layoutFilterPreparatory;

	private ListView listviewProjects;// 项目列表
	private ProjectListAdapter listProjectsAdapter;// 列表适配

	private LinearLayout layoutProjectLoad;// 尚无项目时显示的布局
	private TextView textProjectLoad;// 尚无项目时显示的文字
	private Button btnProjectReload;// 重新加载

	private GetProjectListListener mListener;// 调用服务器接口时的监听
	private GoOutController mController;

	private RelativeLayout layoutFliterCountry;
	private TextView textFliterCountry;// 顶部按钮选国家
	private static final int REQUEST_CODE_COUNTRYSELECT = 4;// 选择国家请求

	private Handler handlerMainStart;
	private static final int MSG_UPDATE_PROJECT_GETFIAL = 0x600;// 获取项目列表失败
	private static final int MSG_UPDATE_PROJECT_GETFINISH = 0x601;// 获取项目列表完成
	private static final int MSG_UPDATE_PROJECT_NULL = 0x602;// 获取项目列表为空
	private static final int MSG_GET_COUNTRY_FINISH = 0x603;// 加载国家列表完成
	private static final int MSG_GET_COUNTRY_FAIL = 0x604;// 加载国家列表失败

	/**
	 * 当前过滤项
	 */
	private boolean isFliterHighSchool;
	private boolean isFliterBachelor;
	private boolean isFliterMaster;
	private boolean isFliterDoctor;
	private boolean isFliterPreparatory;

	private GoOutGlobal appGloble;// 全局变量的使用对象

	private ArrayList<Project> showProjects;// 当前显示的项目列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_start);

		appGloble = (GoOutGlobal) getApplicationContext();

		mListener = new ProjectListListener();
		mController = GoOutController.getInstance();
		handlerMainStart = new HandlerMainStart();

		layoutFilterHighSchool = (LinearLayout) findViewById(R.id.layout_main_start_filter_highschool);
		layoutFilterBachelor = (LinearLayout) findViewById(R.id.layout_main_start_filter_bachelor);
		layoutFilterMaster = (LinearLayout) findViewById(R.id.layout_main_start_filter_master);
		layoutFilterDoctor = (LinearLayout) findViewById(R.id.layout_main_start_filter_doctor);
		layoutFilterPreparatory = (LinearLayout) findViewById(R.id.layout_main_start_filter_preparatory);

		listviewProjects = (ListView) findViewById(R.id.listview_main_start);
		layoutProjectLoad = (LinearLayout) findViewById(R.id.layout_projectlist_load);
		textProjectLoad = (TextView) findViewById(R.id.text_projcectlist_load);
		btnProjectReload = (Button) findViewById(R.id.btn_projectlist_reload);
		layoutFliterCountry = (RelativeLayout) findViewById(R.id.layout_startpage_flitercountry);
		textFliterCountry = (TextView) findViewById(R.id.text_start_filtercountry);

		initViewsListener();

		// 先获得要展示的国家id,再获得项目列表
		getCountryShow();

		// test interfaces of server
		// TestGoOut.testInterfacesOfServer(MainStartActivity.this);
		// TestGoOut.testTimeUtil();

		// 失败的上传记录重新上传
		CheckUploadFailedDataUtil check = new CheckUploadFailedDataUtil(getApplicationContext());
		check.check();
	}

	/**
	 * 初始化监听
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	private void initViewsListener() {
		listviewProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				Intent i = new Intent(MainStartActivity.this, ProjectInfoActivity.class);
				i.putExtra("projectServerId", showProjects.get(arg2).getServerId());
				i.putExtra("projectName", showProjects.get(arg2).getName()); // 判断是否为空?
				i.putExtra("publisherServerId", showProjects.get(arg2).getPublisherServerId());
				i.putExtra("publisherType", showProjects.get(arg2).getPublisherType());
				i.putExtra("logoName", showProjects.get(arg2).getLogoName());
				// GoOutDebug.e(TAG, "publisherServerId=" + showProjects.get(arg2).getPublisherServerId() +
				// "publisherType=" + showProjects.get(arg2).getPublisherType());
				startActivity(i);
			}
		});

		layoutFliterCountry.setOnClickListener(this);
		btnProjectReload.setOnClickListener(this);
		layoutFilterHighSchool.setOnClickListener(this);
		layoutFilterBachelor.setOnClickListener(this);
		layoutFilterMaster.setOnClickListener(this);
		layoutFilterDoctor.setOnClickListener(this);
		layoutFilterPreparatory.setOnClickListener(this);

		// 暂时注释
		// listviewProjects.setOnScrollListener(new AbsListView.OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// switch (scrollState) {
		// case OnScrollListener.SCROLL_STATE_FLING:
		// listProjectsAdapter.setFlagBusy(true);
		// //此段代码说明见{@link MainSchoolActivity#listSchoolSortLetter.setOnScrollListener()}
		// if ((view.getLastVisiblePosition() == (view.getCount() - 1)) || (view.getFirstVisiblePosition() ==
		// 0)) {
		// listProjectsAdapter.setFlagBusy(false);
		// }
		// break;
		// case OnScrollListener.SCROLL_STATE_IDLE:
		// listProjectsAdapter.setFlagBusy(false);
		// break;
		// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// listProjectsAdapter.setFlagBusy(false);
		// break;
		// default:
		// break;
		// }
		// listProjectsAdapter.notifyDataSetChanged();
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
		// totalItemCount) {
		// }
		// });

	}

	/**
	 * 获得项目数据
	 * 
	 * @Description:
	 * @param reLoad
	 *            是否重新从服务器加载，即“重试"按钮
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-25
	 */
	private void getProjectsData(boolean reLoad) {
		if (appGloble.getmCountryProjectList() == null) {
			getCountryShow();
			return;
		}
		loadStartViews();
		textFliterCountry.setText(appGloble.getmCountryProjectList().getNameCn());// 显示当前过滤的国家
		if (GoOutTest.test) {
			// test
			appGloble.setArrayProjects(GoOutTest.getTestProjectListData());
			showAll();
		} else {
			// 有则显示，无则网络加载
			if (appGloble.getArrayProjects() != null && appGloble.getArrayProjects().size() > 0 && !reLoad) {
				// 有
				showAll();
			} else {
				// 无
				// 判断网络状态
				if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					textProjectLoad.setText(R.string.loadnonetwork);
					btnProjectReload.setVisibility(View.VISIBLE);
					return;
				} else {
					btnProjectReload.setVisibility(View.GONE);
					textProjectLoad.setText(R.string.projectlist_loading);
					mController.getPorjectList(appGloble.getmCountryProjectList().getServerId(), mListener);
				}
			}
		}

	}

	/**
	 * 加载前初始化ui
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	private void loadStartViews() {
		layoutProjectLoad.setVisibility(View.VISIBLE);
		listviewProjects.setVisibility(View.GONE);
	}

	/**
	 * 刷新列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void showProjectList() {

		if (showProjects != null && showProjects.size() > 0) {
			layoutProjectLoad.setVisibility(View.GONE);
			listProjectsAdapter = new ProjectListAdapter(MainStartActivity.this, showProjects);
			listviewProjects.setAdapter(listProjectsAdapter);
			listviewProjects.setVisibility(View.VISIBLE);
		} else {
			handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_NULL);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GoOutDebug.e(TAG, "onDestroy");
		appGloble.setArrayProjects(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_main_start_filter_highschool:
			// 过滤高中生
			fliterHighSchool();
			break;
		case R.id.layout_main_start_filter_bachelor:
			// 过滤本科
			fliterBachelor();
			break;
		case R.id.layout_main_start_filter_master:
			// 过滤硕士
			fliterMaster();
			break;
		case R.id.layout_main_start_filter_doctor:
			// 过滤博士
			fliterDoctor();
			break;
		case R.id.layout_main_start_filter_preparatory:
			// 过滤预科
			fliterPreparatory();
			break;

		case R.id.layout_startpage_flitercountry:
			// 选国家
			startActivityForResult(new Intent(MainStartActivity.this, SelectCountryActivity.class), REQUEST_CODE_COUNTRYSELECT);
			break;
		case R.id.btn_projectlist_reload:
			// 重新加载
			getProjectsData(true);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_COUNTRYSELECT:
			if (resultCode == RESULT_OK) {
				// 选择国家回来，进行按国家过滤
				if (data != null) {
					Bundle b = data.getExtras();

					if (b != null) {
						int preShowId = appGloble.getmCountryProjectList().getServerId();
						int toShowId = b.getInt("DataCountryServerId");
						if (preShowId != toShowId) {
							Country c = new Country();
							c.setServerId(toShowId);
							c.setNameCn(b.getString("DataCountryNameCn"));
							c.setNameEn(b.getString("DataCountryNameEn"));
							appGloble.setmCountryProjectList(c);
							appGloble.setArrayProjects(null);
							fliterCountry();
						}
					}

					Toast.makeText(MainStartActivity.this, b.getString("DataCountryNameCn"), Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 过滤高中
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterHighSchool() {

		if (!isFliterHighSchool) {
			isFliterHighSchool = true;
			isFliterBachelor = false;
			isFliterMaster = false;
			isFliterDoctor = false;
			isFliterPreparatory = false;

			layoutFilterBachelor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterMaster.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterDoctor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterPreparatory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterHighSchool.setBackgroundResource(R.drawable.type_bg_selected);
			showProjects = new ArrayList<Project>();
			if (appGloble.getArrayProjects() != null) {
				showProjects = new ArrayList<Project>();
				for (Project p : appGloble.getArrayProjects()) {
					if (p.getDegree().equals(GoOutConstants.DEGREE_HIGHSCHOOL)) {
						showProjects.add(p);
					}
				}
			}
			showProjectList();
		} else {
			isFliterHighSchool = false;
			layoutFilterHighSchool.setBackgroundResource(getResources().getColor(android.R.color.transparent));
			showAll();
		}

	}

	/**
	 * 过滤本科
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterBachelor() {
		if (!isFliterBachelor) {
			isFliterHighSchool = false;
			isFliterBachelor = true;
			isFliterMaster = false;
			isFliterDoctor = false;
			isFliterPreparatory = false;
			layoutFilterHighSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterMaster.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterDoctor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterPreparatory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterBachelor.setBackgroundResource(R.drawable.type_bg_selected);

			if (appGloble.getArrayProjects() != null) {
				showProjects = new ArrayList<Project>();
				for (Project p : appGloble.getArrayProjects()) {
					if (p.getDegree().equals(GoOutConstants.DEGREE_BACHELOR)) {
						showProjects.add(p);
					}
				}
				showProjectList();
			}
		} else {
			layoutFilterBachelor.setBackgroundResource(getResources().getColor(android.R.color.transparent));
			isFliterBachelor = false;
			showAll();
		}

	}

	/**
	 * 过滤硕士
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterMaster() {
		if (!isFliterMaster) {
			isFliterHighSchool = false;
			isFliterBachelor = false;
			isFliterMaster = true;
			isFliterDoctor = false;
			isFliterPreparatory = false;
			layoutFilterBachelor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterHighSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterDoctor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterPreparatory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterMaster.setBackgroundResource(R.drawable.type_bg_selected);

			if (appGloble.getArrayProjects() != null) {
				showProjects = new ArrayList<Project>();
				for (Project p : appGloble.getArrayProjects()) {
					if (p.getDegree().equals(GoOutConstants.DEGREE_MASTER)) {
						showProjects.add(p);
					}
				}
				showProjectList();
			}
		} else {
			isFliterMaster = false;
			layoutFilterMaster.setBackgroundResource(getResources().getColor(android.R.color.transparent));
			showAll();
		}

	}

	/**
	 * 过滤博士
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterDoctor() {
		if (!isFliterDoctor) {
			isFliterHighSchool = false;
			isFliterBachelor = false;
			isFliterMaster = false;
			isFliterDoctor = true;
			isFliterPreparatory = false;
			layoutFilterBachelor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterMaster.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterHighSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterPreparatory.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterDoctor.setBackgroundResource(R.drawable.type_bg_selected);
			if (appGloble.getArrayProjects() != null) {
				showProjects = new ArrayList<Project>();

				for (Project p : appGloble.getArrayProjects()) {
					if (p.getDegree().equals(GoOutConstants.DEGREE_DOCTOR)) {
						showProjects.add(p);
					}
				}
				showProjectList();
			}

		} else {
			isFliterDoctor = false;
			layoutFilterDoctor.setBackgroundResource(getResources().getColor(android.R.color.transparent));
			showAll();
		}

	}

	/**
	 * 过滤预科
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterPreparatory() {
		if (!isFliterPreparatory) {
			isFliterHighSchool = false;
			isFliterBachelor = false;
			isFliterMaster = false;
			isFliterDoctor = false;
			isFliterPreparatory = true;
			layoutFilterBachelor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterMaster.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterDoctor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterHighSchool.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			layoutFilterPreparatory.setBackgroundResource(R.drawable.type_bg_selected);
			if (appGloble.getArrayProjects() != null) {
				showProjects = new ArrayList<Project>();

				for (Project p : appGloble.getArrayProjects()) {
					if (p.getDegree().equals(GoOutConstants.DEGREE_PREPARATORY)) {
						showProjects.add(p);
					}
				}
				showProjectList();
			}
		} else {
			isFliterPreparatory = false;
			layoutFilterPreparatory.setBackgroundResource(getResources().getColor(android.R.color.transparent));
			showAll();
		}

	}

	/**
	 * 获得某国家的项目列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void fliterCountry() {
		getProjectsData(false);
	}

	/**
	 * 显示所有项目
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void showAll() {
		showProjects = new ArrayList<Project>();
		showProjects = appGloble.getArrayProjects();
		showProjectList();

	}

	/**
	 * 过滤专业或显示全部
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-25
	 */
	private void doFliterDegree() {
		GoOutDebug.e(TAG, "Do fliter!!!!");
		if (isFliterHighSchool) {
			isFliterHighSchool = false;
			fliterHighSchool();
			return;
		}
		if (isFliterBachelor) {
			isFliterBachelor = false;
			fliterBachelor();
			return;
		}
		if (isFliterMaster) {
			isFliterMaster = false;
			fliterMaster();
			return;
		}
		if (isFliterDoctor) {
			isFliterDoctor = false;
			fliterDoctor();
			return;
		}
		if (isFliterPreparatory) {
			isFliterPreparatory = false;
			fliterPreparatory();
			return;
		}
		showAll();
	}

	/**
	 * 先获得国家
	 * 
	 * TODO 考虑放到线程里
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	private void getCountryShow() {

		if (GoOutTest.test) {
			appGloble.setArraySelectCountry(GoOutTest.getCountryList());
		}

		// 先看全局变量里有木有
		ArrayList<Country> countries = new ArrayList<Country>();
		countries = appGloble.getArraySelectCountry();
		if (countries != null && countries.size() > 0) {
			// 缓存有数据
			// 获得本地存储的国家id？
			// 没有取默认美国
			if (appGloble.getmCountryProjectList() == null) {
				for (Country c : countries) {
					if (c.getNameCn().equals(GoOutConstants.DEFAULT_COUNTRY_OF_PROJECTLIST)) {
						GoOutDebug.e(TAG, "new mShowCountry ");
						// Country country = new Country();
						// country.setNameCn(c.getNameCn());
						// country.setServerId(c.getServerId());
						// mShowCountry = c;
						appGloble.setmCountryProjectList(c);
						break;
					}
				}
				getProjectsData(true);// 重新从服务器加载
			} else {
				getProjectsData(false);
			}

		}
		// 国家列表不入库，暂时注释
		else {
			// // 再看本地数据库有木有
			// countries = mController.getCountries(MainStartActivity.this);
			// if (countries != null && countries.size() > 0) {
			// appGloble.setArraySelectCountry(countries);
			// // 本地有数据
			// // 获得本地存储的国家id？
			// // 没有取默认美国
			// for (Country c : countries) {
			// if (c.getNameCn().equals("美国")) {
			// mShowCountry = c;
			// break;
			// }
			// }
			// getProjectsData();
			// }
			// else {
			// 再向服务器请求
			// 加载服务器数据
			// 判断网络状态
			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				textProjectLoad.setText(R.string.loadnonetwork);
				btnProjectReload.setVisibility(View.VISIBLE);
				return;
			} else {
				btnProjectReload.setVisibility(View.GONE);
				textProjectLoad.setText(R.string.projectlist_loading);
				mController.getSelectCountryList(new GetCountryListListener());
			}

			// }
		}

	}

	/**
	 * 调用服务器取项目列表时的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-6
	 */
	private class ProjectListListener extends GetProjectListListener {

		@Override
		public void getProjectListFailed() {
			// 获取失败
			super.getProjectListFailed();
			GoOutDebug.e(TAG, "获取失败");
			handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_GETFIAL);
		}

		@Override
		public void getProjectListFinished(ArrayList<Project> projects) {
			// 获取成功
			super.getProjectListFinished(projects);
			GoOutDebug.e(TAG, "获取成功！！！！！！！！！！！！！！！！！！！！！");

			if (projects != null && projects.size() > 0) {
				appGloble.setArrayProjects(projects);
				handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_GETFINISH);
			} else {
				handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_NULL);
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
	private class GetCountryListListener extends GetSelectCountryListListener {

		@Override
		public void getSelectCountryListFailed() {
			super.getSelectCountryListFailed();
			// 加载失败
			handlerMainStart.sendEmptyMessage(MSG_GET_COUNTRY_FAIL);
			GoOutDebug.v(TAG, "getSelectCountryListFailed");
		}

		@Override
		public void getSelectCountryListFinished(ArrayList<Country> countrylist) {
			super.getSelectCountryListFinished(countrylist);
			if (countrylist != null && countrylist.size() > 0) {
				// 加载完成
				appGloble.setArraySelectCountry(countrylist);
				GoOutDebug.v(TAG, "getSelectCountryListFinished not null");
				// 暂不存储
				// mController.insertCountriesData(MainStartActivity.this, countrylist);// 将从服务器上获得的国家数据存储到本地
				handlerMainStart.sendEmptyMessage(MSG_GET_COUNTRY_FINISH);
			} else {
				// 为空
				handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_GETFIAL);
				GoOutDebug.v(TAG, "getSelectCountryListFinished null");
			}
		}
	}

	/**
	 * Handler 接收器
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class HandlerMainStart extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_UPDATE_PROJECT_GETFIAL:
				// 获取失败
				textProjectLoad.setText(R.string.projectlist_loadfail);
				btnProjectReload.setVisibility(View.VISIBLE);
				break;
			case MSG_UPDATE_PROJECT_GETFINISH:
				// 获取完成
				doFliterDegree();
				break;
			case MSG_UPDATE_PROJECT_NULL:
				layoutProjectLoad.setVisibility(View.VISIBLE);
				textProjectLoad.setText(R.string.projectlist_null);
				btnProjectReload.setVisibility(View.VISIBLE);
				listviewProjects.setVisibility(View.GONE);
				break;
			case MSG_GET_COUNTRY_FINISH:
				// 国家列表加载出来了，选择要展示哪国的项目
				getCountryShow();
				break;
			case MSG_GET_COUNTRY_FAIL:
				// 国家列表加载失败
				handlerMainStart.sendEmptyMessage(MSG_UPDATE_PROJECT_GETFIAL);
				// layoutFliterCountry.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	}

}
