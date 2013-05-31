package com.c35.ptc.goout.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Country;
import com.c35.ptc.goout.interfaces.GetSelectCountryListListener;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;

/**
 * 院校列表按国建筛选
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-4
 */
public class SelectCountryActivity extends Activity implements OnClickListener {

	private static final String TAG = "SelectCountryActivity";

	private ImageView imgBack;
	private ListView listviewCountry;
	private LinearLayout layoutShowInfo;
	private Button btnReload;
	private TextView textShowInfo;// 显示加载中、加载失败、空等信息

	private BaseAdapter adapterListCountry;
	// private ArrayList<Country> listCountries;// 考虑是否使用此数组?

	private GetSelectCountryListListener mListener;// 调用服务器接口时的监听
	private GoOutController mController;

	private Handler uiHandler;
	private static final int MSG_GETCOUNTRYLIST_FINISHED = 0x900;// 获取成功
	private static final int MSG_GETCOUNTRYLIST_FAIL = 0x901;// 获取失败
	private static final int MSG_GETCOUNTRYLIST_NULL = 0x902;// 获取为空

	private GoOutGlobal appGloble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_country);

		appGloble = (GoOutGlobal) getApplicationContext();
		mController = GoOutController.getInstance();
		mListener = new GetCountryListListener();
		uiHandler = new UiHandler();

		layoutShowInfo = (LinearLayout) findViewById(R.id.layout_select_country_loadinfo);
		textShowInfo = (TextView) findViewById(R.id.text_select_country_loadinfo);
		btnReload = (Button) findViewById(R.id.btn_select_country_reload);
		imgBack = (ImageView) findViewById(R.id.img_select_country_back);
		listviewCountry = (ListView) findViewById(R.id.listview_select_country);

		btnReload.setOnClickListener(this);
		imgBack.setOnClickListener(this);

		listviewCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				// 传值回去
				Bundle bundle = new Bundle();
				bundle.putString("DataCountryNameCn", appGloble.getArraySelectCountry().get(arg2).getNameCn());
				bundle.putString("DataCountryNameEn", appGloble.getArraySelectCountry().get(arg2).getNameEn());
				bundle.putInt("DataCountryServerId", appGloble.getArraySelectCountry().get(arg2).getServerId());
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});

		getCountryList();

	}

	/**
	 * 获得国家列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void getCountryList() {
		// 有则显示，无则获取国家列表

		// 先看全局变量里有木有
		ArrayList<Country> countries = new ArrayList<Country>();
		countries = appGloble.getArraySelectCountry();

		if (countries != null && countries.size() > 0) {
			showCountryList();
		} else {
			// // 再看本地数据库有木有
			// countries = mController.getCountries(SelectCountryActivity.this);
			// if (countries != null && countries.size() > 0) {
			// appGloble.setArraySelectCountry(countries);
			// showCountryList();
			// } else {
			// 再向服务器请求
			// 加载服务器数据
			// 判断网络状态
			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				textShowInfo.setText(R.string.loadnonetwork);
				btnReload.setVisibility(View.VISIBLE);
				return;
			} else {
				btnReload.setVisibility(View.GONE);
				textShowInfo.setText(R.string.select_country_loading);
				mController.getSelectCountryList(mListener);
			}

			// }
		}

	}

	/**
	 * 显示
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void showCountryList() {
		// listCountries = new ArrayList<Country>();
		// listCountries = TestGoOut.getTestCountryDataList();
		layoutShowInfo.setVisibility(View.GONE);
		adapterListCountry = new CoutryListAdapter(SelectCountryActivity.this, appGloble.getArraySelectCountry());
		listviewCountry.setAdapter(adapterListCountry);
		listviewCountry.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_select_country_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_select_country_reload:
			// 重新加载
			getCountryList();
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
			case MSG_GETCOUNTRYLIST_FAIL:
				textShowInfo.setText(R.string.select_country_loadfail);
				btnReload.setVisibility(View.VISIBLE);
				break;
			case MSG_GETCOUNTRYLIST_FINISHED:
				showCountryList();
				break;
			case MSG_GETCOUNTRYLIST_NULL:
				textShowInfo.setText(R.string.select_country_loadnull);
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
	private class GetCountryListListener extends GetSelectCountryListListener {

		@Override
		public void getSelectCountryListFailed() {
			super.getSelectCountryListFailed();
			// 加载失败
			uiHandler.sendEmptyMessage(MSG_GETCOUNTRYLIST_FAIL);
			GoOutDebug.v(TAG, "getSelectCountryListFailed");
		}

		@Override
		public void getSelectCountryListFinished(ArrayList<Country> countrylist) {
			super.getSelectCountryListFinished(countrylist);
			if (countrylist != null && countrylist.size() > 0) {
				// 加载完成
				appGloble.setArraySelectCountry(countrylist);
				uiHandler.sendEmptyMessage(MSG_GETCOUNTRYLIST_FINISHED);
				GoOutDebug.v(TAG, "getSelectCountryListFinished not null");
			} else {
				// 为空
				uiHandler.sendEmptyMessage(MSG_GETCOUNTRYLIST_NULL);
				GoOutDebug.v(TAG, "getSelectCountryListFinished null");
			}
		}
	}

	/**
	 * 国家的列表适配
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class CoutryListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Country> listCountries;

		public CoutryListAdapter(Context context, List<Country> list) {
			this.inflater = LayoutInflater.from(context);
			this.listCountries = list;
		}

		@Override
		public int getCount() {
			return listCountries.size();
		}

		@Override
		public Object getItem(int position) {
			return listCountries.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				//GoOutDebug.v(TAG, "convertView = null");
				convertView = inflater.inflate(R.layout.listitem_select_country, null);
				holder = new ViewHolder();
				holder.textNameCn = (TextView) convertView.findViewById(R.id.text_listitem_country_select_namecn);
				holder.textArea = (TextView) convertView.findViewById(R.id.text_listitem_country_select_area);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textNameCn.setText(listCountries.get(position).getNameCn());
			// 是否显示区域(仅当属于改区域中的第一个国家时显示区域)
			if (listCountries.get(position).isFirstOfArea()) {
				holder.textArea.setText(listCountries.get(position).getArea());
				holder.textArea.setVisibility(View.VISIBLE);
			} else {
				holder.textArea.setVisibility(View.GONE);
			}

			return convertView;
		}

		private class ViewHolder {

			TextView textNameCn;// 中文名
			TextView textArea;// 区域
		}

	}

}
