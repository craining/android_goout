package com.c35.ptc.goout.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.interfaces.GetSchoolListListener;
import com.c35.ptc.goout.interfaces.GetSelectCountryListListener;
import com.c35.ptc.goout.interfaces.ViewLetterListListener;
import com.c35.ptc.goout.listviewloader.SchoolListAdapter;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.view.LetterListView;

/**
 * 主页中的院校页面
 * 
 * (本地数据库暂时去掉)
 * 
 * （名次排序的列表暂时隐藏掉了）
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
public class MainSchoolActivity extends Activity implements OnClickListener, ViewLetterListListener {

	private static final String TAG = "MainSchoolActivity";

	// 正在加载院校列表的提示
	private LinearLayout layoutLoadInfo;
	private Button btnReload;
	private TextView textLoadInfo;

	private LinearLayout layoutSchoolListBody;// 显示院校列表，与上一个TextView互斥显示

	private TextView textSortRank;// 按名次排序
	private TextView textSortLetter;// 按字母排序

	// 名次顺序的布局
	// private LinearLayout layoutSortRank;
	// private ListView listSchoolSortRank;// 排名的列表
	// private SchoolListAdapter adapterListSortRank;

	// 字母顺序的布局
	private RelativeLayout layoutSortLetter;
	private ListView listSchoolSortLetter;// 字母排序的列表
	private SchoolListAdapter adapterListSortLetter;

	// 字母检索相关
	private WindowManager.LayoutParams layoutOverlay;// 字母浮动窗布局
	private TextView textLetterOverlay;// 字母浮动窗里的TextView
	private WindowManager windowManager;
	private LetterListView letterListView;// 右侧字母列表
	private HashMap<String, Integer> letterIndexer;// 从列表中抽取首字母和位置
	private TextView textSortLetterTitle;// 显示当前滚到的字母
	private int letterFloatViewWidth = 140;// 字母弹窗的宽度
	private int letterFloatViewHight = 140; // 字母弹窗的高度

	// Handler
	private MainSchoolHandler schoolHandler;
	private static final int MSG_LETTER_OVERLAY_HIDE = 0x555;// 隐藏字母浮动窗
	private static final int MSG_GETSCHOOLLIST_FINISHED = 0x405;// 获取成功
	private static final int MSG_GETSCHOOLLIST_FAIL = 0x406;// 获取失败
	private static final int MSG_GETSCHOOLLIST_NULL = 0x407;// 获取为空
	private static final int MSG_GET_COUNTRY_FINISH = 0x408;// 获取国家成功
	private static final int MSG_GET_COUNTRY_FAIL = 0x409;// 获取国家失败
	private static final int MSG_NO_NETWORK = 0x410;// 网络异常
	private static final int MSG_UPDATE_FLITER_COUNTRY = 0x411;// 更新当前正在过滤的国家按钮
	private static final int MSG_SHOW_LIST = 0x412;// 显示列表
	private static final int MSG_GETSCHOOLLOST_FROM_SERVER = 0x413;// 从服务器加载院校列表，（提示文字增加正在从服务器加载）

	private RelativeLayout btnFliterCountry;
	private TextView textFliterCountry;
	private ImageView imgFliterArrow;
	private Button btnFilterMajor;// 右上角专业过滤按钮

	private static final int REQUEST_CODE_COUNTRYSELECT = 500;// 选择国家请求
	private static final int REQUEST_CODE_MAJORSELECT = 501;// 选择专业请求

	private GoOutController mController;

	private GoOutGlobal appGloble;

	// 过滤时使用
	// private Country mShowCountry;
	private Major mShowMajor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_school);
		// 初始化
		appGloble = (GoOutGlobal) getApplicationContext();
		schoolHandler = new MainSchoolHandler();
		mController = GoOutController.getInstance();

		layoutSchoolListBody = (LinearLayout) findViewById(R.id.layout_school_listbody);
		layoutLoadInfo = (LinearLayout) findViewById(R.id.layout_schoollist_loadinfo);
		textLoadInfo = (TextView) findViewById(R.id.text_schoollist_loadinfo);
		btnReload = (Button) findViewById(R.id.btn_schoollist_reload);
		textSortRank = (TextView) findViewById(R.id.text_school_sortrank);
		textSortLetter = (TextView) findViewById(R.id.text_school_sortletter);

		// layoutSortRank = (LinearLayout) findViewById(R.id.layout_school_sort_rank);
		// listSchoolSortRank = (ListView) findViewById(R.id.listview_school_sort_rank);
		layoutSortLetter = (RelativeLayout) findViewById(R.id.layout_school_sort_letter);
		listSchoolSortLetter = (ListView) findViewById(R.id.listview_school_sort_letter);
		letterListView = (LetterListView) findViewById(R.id.letterview_school_sort_letter);
		textSortLetterTitle = (TextView) findViewById(R.id.text_school_sort_letter_titile);

		btnFliterCountry = (RelativeLayout) findViewById(R.id.layout_school_flitercountry);
		textFliterCountry = (TextView) findViewById(R.id.text_school_flitercountry);
		btnFilterMajor = (Button) findViewById(R.id.btn_school_major);
		imgFliterArrow = (ImageView) findViewById(R.id.img_school_flitercountry_arrow);
		initLetterOverlay();// 初始化字母弹窗
		initListener();

		updateSchoolList();

	}

	/**
	 * 初始化监听qi
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void initListener() {
		btnReload.setOnClickListener(this);
		textSortRank.setOnClickListener(this);
		textSortLetter.setOnClickListener(this);
		letterListView.setOnTouchingLetterChangedListener(this);
		btnFliterCountry.setOnClickListener(this);
		btnFilterMajor.setOnClickListener(this);

		// 名次排序的列表的点击监听
		// listSchoolSortRank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		// // 跳转院校页
		// Intent i = new Intent(MainSchoolActivity.this, SchoolMainActivity.class);
		// i.putExtra("schoolServerId", appGloble.getArraySchoolsSortedByRank().get(arg2).getServerId());
		// i.putExtra("schoolNameCn", appGloble.getArraySchoolsSortedByRank().get(arg2).getNameCn());
		// i.putExtra("schoolNameEn", appGloble.getArraySchoolsSortedByRank().get(arg2).getNameEn());
		// i.putExtra("schoolName", appGloble.getArraySchoolsSortedByRank().get(arg2).getLogoName());
		// startActivity(i);
		// }
		// });

		// 字母排序的列表的点击监听
		listSchoolSortLetter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				// 跳转院校页
				Intent i = new Intent(MainSchoolActivity.this, SchoolMainActivity.class);
				i.putExtra("schoolServerId", appGloble.getArraySchoolsSortedByLetter().get(arg2).getServerId());
				i.putExtra("schoolNameCn", appGloble.getArraySchoolsSortedByLetter().get(arg2).getNameCn());
				i.putExtra("schoolNameEn", appGloble.getArraySchoolsSortedByLetter().get(arg2).getNameEn());
				i.putExtra("schoolLogoName", appGloble.getArraySchoolsSortedByLetter().get(arg2).getLogoName());
				startActivity(i);
			}
		});
		// 字母排序的列表滚动的监听
		// 滚动时不加载图片
		listSchoolSortLetter.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					adapterListSortLetter.setFlagBusy(true);

					// 两种情况，在listview最顶部，继续拖动listview往下滚动，listview不会继续向上滚动了，此情况最后触发SCROLL_STATE_FLING事件，图片不加载
					// 另一种情况类似，在listview最底部，继续往上拖动，松开，listview不能继续往上滚，最后触发SCROLL_STATE_FLING
					// 因此在此处判断是否是在顶部或底部
					if ((view.getLastVisiblePosition() == (view.getCount() - 1)) || (view.getFirstVisiblePosition() == 0)) {
						adapterListSortLetter.setFlagBusy(false);
					}
					GoOutDebug.e(TAG, "SCROLL_STATE_FLING");
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					adapterListSortLetter.setFlagBusy(false);
					GoOutDebug.e(TAG, "SCROLL_STATE_IDLE");
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					adapterListSortLetter.setFlagBusy(false);
					GoOutDebug.e(TAG, "SCROLL_STATE_TOUCH_SCROLL");
					break;
				default:
					break;
				}
				adapterListSortLetter.notifyDataSetChanged();
				GoOutDebug.e(TAG, "onScrollStateChanged");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				GoOutDebug.e(TAG, "onScroll");
				// 显示当前列表滚动到的字母
				if (appGloble.getArraySchoolsSortedByLetter() != null && appGloble.getArraySchoolsSortedByLetter().size() > firstVisibleItem) {
					textSortLetterTitle.setText(getFirstLetterFromSortkey(appGloble.getArraySchoolsSortedByLetter().get(firstVisibleItem).getSortKey()));
				}
			}
		});
	}

	/**
	 * 先获得国家
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-21
	 */
	private void updateSchoolList() {

		if (mShowMajor == null) {
			startLoadFliterCountry();
		} else {
			startLoadFliterMajor();
		}

		// 开启线程，加载

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (appGloble.getmCountrySchoolList() == null) {
					// 先看全局变量里有木有
					ArrayList<Country> countries = new ArrayList<Country>();
					countries = appGloble.getArraySelectCountry();
					if (countries != null && countries.size() > 0) {
						// 缓存有数据
						// 获得本地存储的国家id？
						// 没有取默认美国
						for (Country c : countries) {
							if (c.getNameCn().equals(GoOutConstants.DEFAULT_COUNTRY_OF_SCHOOLLIST)) {
								// mShowCountry = new Country();
								// mShowCountry.setNameCn(c.getNameCn());
								// mShowCountry.setServerId(c.getServerId());
								appGloble.setmCountrySchoolList(c);
								break;
							}
						}
						getSchoolsData();
					} else {
						// // 再看本地数据库有木有
						// countries = mController.getCountries(MainSchoolActivity.this);
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
						// getSchoolsDataFromLocal();
						// } else {
						// 再向服务器请求
						// 加载服务器数据
						if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
							schoolHandler.sendEmptyMessage(MSG_NO_NETWORK);
						} else {
							mController.getSelectCountryList(new GetCountryListListener());
						}

						// }
					}
				} else {
					getSchoolsData();
				}

			}
		}).start();

	}

	/**
	 * 从本地获得数据并显示(本地没有则从服务器上获取)
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void getSchoolsData() {

		schoolHandler.sendEmptyMessage(MSG_UPDATE_FLITER_COUNTRY);
		if (GoOutTest.test) {
			GoOutTest.getTestSchoolListData(MainSchoolActivity.this);// 存库
		}

		// 由于暂不存库，咱不读取库
		// TableSchool local = new TableSchool(MainSchoolActivity.this);
		// local.open();
		// // 按国家（可含专业）来取
		// // Global.arraySchoolsSortedByRank = local.getSchoolListAsRankSort();
		// appGloble.setArraySchoolsSortedByLetter(local.getSchoolListAsLetterSort(mShowCountry.getServerId()));
		// local.close();

		if (appGloble.getArraySchoolsSortedByLetter() != null && appGloble.getArraySchoolsSortedByLetter().size() > 0) {

			schoolHandler.sendEmptyMessage(MSG_SHOW_LIST);

			// // 将当前国家所有院校列表得到了，再判断是否过滤专业
			// if (mShowMajor == null) {
			// GoOutDebug.e(TAG, "不需过滤专业，直接显示");
			// // 数据不为空,且所有专业，则直接显示
			// schoolHandler.sendEmptyMessage(MSG_SHOW_LIST);
			// } else {
			// // 需要过滤某专业，获取某专业的院校
			// GoOutDebug.e(TAG, "需要过滤专业，从网络加载");
			// getCountryMajorSchoolDataFromServer();
			// }
		} else {
			// // 数据为空，获取该国家所有院校
			// getCountrySchoolDataFromServer();

			getCountryMajorSchoolDataFromServer();

		}

	}

	/**
	 * 开始加载某国家时的UI状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	private void startLoadFliterCountry() {
		GoOutDebug.e(TAG, "Load start!");
		// 隐藏专业过滤按钮，隐藏排序按钮，显示加载中等信息的布局，其中隐藏重新加载按钮
		layoutSchoolListBody.setVisibility(View.GONE);// 隐藏包含列表的布局
		btnReload.setVisibility(View.GONE);
		textLoadInfo.setText(R.string.schoollist_loading);// 正在加载
		layoutLoadInfo.setVisibility(View.VISIBLE);
		btnFilterMajor.setText(R.string.filter_major);// 选专业
		// 过滤国家的过程中暂不允许过滤
		// imgFliterArrow.setVisibility(View.GONE);
		// btnFliterCountry.setEnabled(false);
	}

	/**
	 * 过滤某专业时的UI状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	private void startLoadFliterMajor() {
		GoOutDebug.e(TAG, "Load start!");
		layoutSchoolListBody.setVisibility(View.GONE);// 隐藏包含列表的布局
		btnReload.setVisibility(View.GONE);
		textLoadInfo.setText(R.string.schoollist_loading);// 正在加载
		layoutLoadInfo.setVisibility(View.VISIBLE);

		btnFilterMajor.setText(mShowMajor.getNameCn());// 某专业
		// btnFilterMajor.setOnClickListener(this);
		// // 加载过程中不可过滤国家和专业
		// btnFilterMajor.setVisibility(View.GONE);
		// imgFliterArrow.setVisibility(View.GONE);

		// btnFliterCountry.setEnabled(false);
	}

	/**
	 * 加载结束时,且不为空的ui状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	private void endLoad() {
		GoOutDebug.e(TAG, "Load end!");
		layoutLoadInfo.setVisibility(View.GONE);
		layoutSchoolListBody.setVisibility(View.VISIBLE);
		// 可过滤国家和专业
		// btnFilterMajor.setVisibility(View.VISIBLE);
		// if (mShowMajor != null) {
		// btnFilterMajor.setText(mShowMajor.getNameCn());
		// } else {
		// btnFilterMajor.setText(R.string.filter_major);// 选专业
		// }
		imgFliterArrow.setVisibility(View.VISIBLE);
		btnFliterCountry.setEnabled(true);
	}

	/**
	 * 加载结束时，为空的ui状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-22
	 */
	private void endLoadNull() {
		GoOutDebug.e(TAG, "Load end but null!");

		ArrayList<School> schools = new ArrayList<School>();
		appGloble.setArraySchoolsSortedByLetter(schools);
		textLoadInfo.setText(R.string.schoollist_null);
		btnReload.setVisibility(View.VISIBLE);

		// 可过滤国家和专业
		// btnFilterMajor.setVisibility(View.VISIBLE);
		// if (mShowMajor != null) {
		// btnFilterMajor.setText(mShowMajor.getNameCn());
		// }
		imgFliterArrow.setVisibility(View.VISIBLE);
		btnFliterCountry.setEnabled(true);
	}

	/**
	 * 从服务器上获取某国家的全部数据
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void getCountrySchoolDataFromServer() {

		// 判断网络状态
		if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
			schoolHandler.sendEmptyMessage(MSG_NO_NETWORK);
			return;
		} else {
			schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLOST_FROM_SERVER);
			mController.getSchoolList(appGloble.getmCountrySchoolList().getServerId(), -1, new MainGetSchoolListListener(appGloble.getmCountrySchoolList().getServerId(), -1));
		}
	}

	/**
	 * 从服务器上获取某国家的某个专业的院校数据
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void getCountryMajorSchoolDataFromServer() {

		// 判断网络状态
		if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
			schoolHandler.sendEmptyMessage(MSG_NO_NETWORK);
			return;
		} else {
			// schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLOST_FROM_SERVER);
			int majorId = (mShowMajor == null) ? -1 : mShowMajor.getServerId();// -1表示所有专业
			mController.getSchoolList(appGloble.getmCountrySchoolList().getServerId(), majorId, new MainGetSchoolListListener(appGloble.getmCountrySchoolList().getServerId(), majorId));
		}
	}

	// /**
	// * 显示排名顺序
	// *
	// * @Description:
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-2-27
	// */
	// private void showRankSort() {
	// textSortRank.setBackgroundColor(getResources().getColor(R.color.bg_sort_selected));
	// textSortLetter.setBackgroundColor(getResources().getColor(R.color.bg_sort_unselected));
	// layoutSortRank.setVisibility(View.VISIBLE);
	// layoutSortLetter.setVisibility(View.GONE);
	//
	// // 填充名次排序的列表
	// // if (Global.arraySchoolsSortedByRank != null && Global.arraySchoolsSortedByRank.size() > 0) {
	// adapterListSortRank = new SchoolListAdapter(MainSchoolActivity.this,
	// appGloble.getArraySchoolsSortedByLetter());
	// listSchoolSortRank.setAdapter(adapterListSortRank);
	// endLoad();
	// // } else {
	// // textLoading.setText(R.string.schoollist_null);
	// // }
	//
	// }

	/**
	 * 显示首字母排序的列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-27
	 */
	private void showLetterSort() {
		textSortRank.setBackgroundColor(getResources().getColor(R.color.bg_sort_unselected));
		textSortLetter.setBackgroundColor(getResources().getColor(R.color.bg_sort_selected));
		// layoutSortRank.setVisibility(View.GONE);
		layoutSortLetter.setVisibility(View.VISIBLE);

		// // 填充字母排序的列表
		// if (Global.arraySchoolsSortedByLetter != null && Global.arraySchoolsSortedByLetter.size() > 0) {
		// 获得列表的字母信息
		// 将A之前的和Z之后的项放到 schoolsTemp 中，再将总列表
		letterIndexer = new HashMap<String, Integer>();
		List<School> schoolsAll = new ArrayList<School>();
		schoolsAll = appGloble.getArraySchoolsSortedByLetter();
		List<School> schoolsTemp = new ArrayList<School>();

		int count = schoolsAll.size();
		int tempId = 0;
		GoOutDebug.e(TAG, "count=" + count);
		for (int i = 0; i < count; i++) {
			String currentStr = getFirstLetterFromSortkey(schoolsAll.get(i).getSortKey());
			String previewStr = (i - 1) >= 0 ? getFirstLetterFromSortkey(schoolsAll.get(i - 1).getSortKey()) : "null";// 假设第一项之前的sortKey为"null"

			if (currentStr.equals("#")) {
				// GoOutDebug.e(TAG, "currentStr=" + currentStr + " i=" + i);
				schoolsTemp.add(schoolsAll.get(i));
			} else {
				// GoOutDebug.e(TAG, "currentStr=" + currentStr + " tempId=" + tempId);
				if (!previewStr.equals(currentStr)) {
					letterIndexer.put(currentStr, i);
					// GoOutDebug.e(TAG, "currentStr=" + currentStr + " value=" + i);
				}
				tempId++;
			}
			// GoOutDebug.e(TAG, "currentStr=" + currentStr + "   previewStr=" + previewStr + "   id=" + i);
		}
		if (schoolsTemp.size() > 0) {
			// GoOutDebug.e(TAG, "insert # items tempId=" + tempId);
			letterIndexer.put("#", tempId);
			for (School s : schoolsTemp) {
				schoolsAll.remove(s);
			}
			for (School s : schoolsTemp) {
				schoolsAll.add(s);
			}
		}
		appGloble.setArraySchoolsSortedByLetter((ArrayList<School>) schoolsAll);

		adapterListSortLetter = new SchoolListAdapter(MainSchoolActivity.this, (ArrayList<School>) schoolsAll);
		// adapterListSortLetter.
		listSchoolSortLetter.setAdapter(adapterListSortLetter);
		textSortLetterTitle.setText(getFirstLetterFromSortkey(appGloble.getArraySchoolsSortedByLetter().get(0).getSortKey()));
		// 是否显示当前列表项数
		if (GoOutTest.testShowSchoolSize) {
			Toast.makeText(MainSchoolActivity.this, "Size:" + count, Toast.LENGTH_SHORT).show();
		}

		endLoad();
		// } else {
		// textLoading.setText(R.string.schoollist_null);
		// }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_school_sortrank:
			// 名次排序
			// showRankSort();
			break;
		case R.id.text_school_sortletter:
			// 字母排序
			showLetterSort();
			break;

		case R.id.layout_school_flitercountry:
			GoOutDebug.e(TAG, "layout_school_flitercountry is clicked!");
			// 选择国家
			startActivityForResult(new Intent(MainSchoolActivity.this, SelectCountryActivity.class), REQUEST_CODE_COUNTRYSELECT);

			break;

		case R.id.btn_school_major:
			// 选择专业
			startActivityForResult(new Intent(MainSchoolActivity.this, SelectMajorActivity.class), REQUEST_CODE_MAJORSELECT);
			break;
		case R.id.btn_schoollist_reload:
			// 重新加载
			updateSchoolList();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// // 未显示列表时，则尝试加载(先本地后服务器)
		// if (!layoutSchoolListBody.isShown()) {
		// getSchoolsDataFromLocal();// 获取本地数据，显示
		// }

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyLetterOverlay();// 移除字母弹窗
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null && resultCode == RESULT_OK) {
			Bundle b = data.getExtras();
			if (b != null) {
				switch (requestCode) {
				case REQUEST_CODE_COUNTRYSELECT: {
					// 选择国家回来，进行按国家过滤(先判断之前显示的是否与选择后要显示的一样)
					int preShowId = appGloble.getmCountrySchoolList().getServerId();
					Country c = new Country();
					int countryId = b.getInt("DataCountryServerId");
					if (preShowId != countryId) {
						c.setServerId(countryId);
						c.setNameCn(b.getString("DataCountryNameCn"));
						c.setNameEn(b.getString("DataCountryNameEn"));
						appGloble.setmCountrySchoolList(c);
						appGloble.setArraySchoolsSortedByLetter(null);// 刷新前将缓存列表置空
						updateSchoolList();
					}

					Toast.makeText(MainSchoolActivity.this, b.getString("DataCountryNameCn"), Toast.LENGTH_SHORT).show();
				}
					break;
				case REQUEST_CODE_MAJORSELECT: {
					// 选择专业回来，按专业过滤(先判断之前显示的是否与选择后要显示的一样)
					int majorId = b.getInt("DataMajorServerId");
					int preShowId = -1;
					if (mShowMajor != null) {
						preShowId = mShowMajor.getServerId();
					}
					if (preShowId != majorId) {
						if (majorId == -1) {
							GoOutDebug.e(TAG, "fliter all major！！");
							mShowMajor = null;
						} else {
							mShowMajor = new Major();
							mShowMajor.setNameCn(b.getString("DataMajorNameCn"));
							mShowMajor.setServerId(majorId);
						}
						appGloble.setArraySchoolsSortedByLetter(null);// 刷新前将缓存列表置空
						updateSchoolList();
					}

					Toast.makeText(MainSchoolActivity.this, b.getString("DataMajorNameCn"), Toast.LENGTH_SHORT).show();
				}
				default:
					break;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	private class MainSchoolHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_LETTER_OVERLAY_HIDE:
				textLetterOverlay.setVisibility(View.GONE);
				break;
			case MSG_GETSCHOOLLIST_FAIL:
				textLoadInfo.setText(R.string.schoollist_fail);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_GETSCHOOLLIST_FINISHED:
				// updateSchoolList();
				showLetterSort();
				break;
			case MSG_GETSCHOOLLIST_NULL:
				endLoadNull();
				break;
			case MSG_GET_COUNTRY_FAIL:
				// 国家列表加载失败
				schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_FAIL);
				// btnFliterCountry.setVisibility(View.GONE);
				break;
			case MSG_GET_COUNTRY_FINISH:
				// 国家列表加载完成
				GoOutDebug.e(TAG, "showLetterSort!!!!!!");
				updateSchoolList();

				break;
			case MSG_NO_NETWORK:
				// 无网络提示
				textLoadInfo.setText(R.string.loadnonetwork);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_UPDATE_FLITER_COUNTRY:
				textFliterCountry.setText(appGloble.getmCountrySchoolList().getNameCn());// 显示当前过滤的国家
				break;
			case MSG_SHOW_LIST:
				// 显示列表
				GoOutDebug.e(TAG, "showLetterSort!!!!!!");
				showLetterSort();
				// showRankSort();
				break;
			case MSG_GETSCHOOLLOST_FROM_SERVER:
				textLoadInfo.setText(getString(R.string.schoollist_loading) + "\r\n\r\n" + getString(R.string.load_slow_tip));// 正在从服务器加载数据
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
	private class MainGetSchoolListListener extends GetSchoolListListener {

		private int countryId;
		private int majorId;

		public MainGetSchoolListListener(int countryId, int majorId) {
			this.countryId = countryId;
			this.majorId = majorId;
		}

		@Override
		public void getSchoolListFailed() {
			super.getSchoolListFailed();
			// 加载失败
			schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_FAIL);
			GoOutDebug.e(TAG, "getSchoolListFailed");
		}

		@Override
		public void getSchoolListFinished(ArrayList<School> schools) {
			super.getSchoolListFinished(schools);
			// 加载完成

			if (schools != null && schools.size() > 0) {
				GoOutDebug.v(TAG, "getSchoolListFinished not null");

				// 当且仅当返回的数据为当前要显示的国家的院校数据时才可显示，防止连续过滤不同的国家
				if (appGloble.getmCountrySchoolList().getServerId() == countryId) {
					// 获得的国家列表
					// 处理并显示
					Collections.sort(schools);
					appGloble.setArraySchoolsSortedByLetter(schools);
					schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_FINISHED);

					// if (majorId == -1) {
					// // 某国家下全部院校，入库，（暂不存库）
					// mController.insertSchools(MainSchoolActivity.this, countryId, schools);
					// schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_FINISHED);
					// } else {
					// // 某国家下，某专业的院校
					// // 对当前列表进行过滤(不必存库)
					// // 保持当前列表顺序不变
					// // 当前列表肯定有数据
					// ArrayList<School> isShowing = appGloble.getArraySchoolsSortedByLetter();
					//
					// for (School s : isShowing) {
					// if (!schools.contains(s)) {
					// isShowing.remove(s);
					// }
					// }
					// }
				}
			} else {
				schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_NULL);
				GoOutDebug.v(TAG, "getSchoolListFinished null");
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
			schoolHandler.sendEmptyMessage(MSG_GET_COUNTRY_FAIL);
			GoOutDebug.v(TAG, "getSelectCountryListFailed");
		}

		@Override
		public void getSelectCountryListFinished(ArrayList<Country> countrylist) {
			super.getSelectCountryListFinished(countrylist);
			if (countrylist != null && countrylist.size() > 0) {
				// 加载完成
				appGloble.setArraySelectCountry(countrylist);
				GoOutDebug.v(TAG, "getSelectCountryListFinished not null");
				// mController.insertCountriesData(MainSchoolActivity.this, countrylist);// 将从服务器上获得的国家数据存储到本地
				schoolHandler.sendEmptyMessage(MSG_GET_COUNTRY_FINISH);
			} else {
				// 为空
				schoolHandler.sendEmptyMessage(MSG_GETSCHOOLLIST_FAIL);
				GoOutDebug.v(TAG, "getSelectCountryListFinished null");
			}
		}
	}

	/******************** 字母排序相关 *********************/

	/**
	 * 从sortkey中获得第一个字母
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-28
	 */
	private String getFirstLetterFromSortkey(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);

		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	/**
	 * 初始化字母检索相关的view
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-28
	 */
	private void initLetterOverlay() {
		// 获取弹窗大小
		DisplayMetrics metric = getResources().getDisplayMetrics();
		letterFloatViewHight = (int) (metric.widthPixels * 0.3);
		letterFloatViewWidth = letterFloatViewHight;

		LayoutInflater inflater = LayoutInflater.from(this);
		textLetterOverlay = (TextView) inflater.inflate(R.layout.text_schoolsortasletter, null);
		textLetterOverlay.setVisibility(View.INVISIBLE);// 默认显示，用于测试

		layoutOverlay = new WindowManager.LayoutParams();
		layoutOverlay.type = WindowManager.LayoutParams.TYPE_APPLICATION;
		layoutOverlay.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		layoutOverlay.format = PixelFormat.TRANSLUCENT;
		// layoutOverlay.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL; //居中显示
		layoutOverlay.gravity = Gravity.RIGHT | Gravity.TOP;// 靠右显示
		layoutOverlay.width = letterFloatViewWidth;
		layoutOverlay.height = letterFloatViewHight;
		// layoutOverlay.layoutAnimationParameters = ;
		windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(textLetterOverlay, layoutOverlay);
	}

	/**
	 * 移除字母弹窗
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-11
	 */
	private void destroyLetterOverlay() {
		if (windowManager != null && textLetterOverlay != null) {
			try {
				windowManager.removeView(textLetterOverlay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 结束字母检索
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-28
	 */
	@Override
	public void onTouchingLetterEnd() {
		schoolHandler.sendEmptyMessageDelayed(MSG_LETTER_OVERLAY_HIDE, 1000);
	}

	/**
	 * 字母检索中
	 * 
	 * @Description:
	 * @param s
	 * @param y
	 * @param x
	 * @param width
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-2-28
	 */
	@Override
	public void onTouchingLetterChanged(String s, float y, float x, float width) {

		textLetterOverlay.setText(s);
		textLetterOverlay.setVisibility(View.VISIBLE);
		// 设置浮动弹窗的位置，使其跟随手指上下滑动
		layoutOverlay.x = (int) width;
		layoutOverlay.y = (int) y - (letterFloatViewHight / 2);
		windowManager.updateViewLayout(textLetterOverlay, layoutOverlay);

		// GoOutDebug.e(TAG, "position=" + letterIndexer.get(s));

		if (letterIndexer != null && letterIndexer.get(s) != null) {
			int position = letterIndexer.get(s);
			listSchoolSortLetter.setSelection(position);
		}

	}
}
