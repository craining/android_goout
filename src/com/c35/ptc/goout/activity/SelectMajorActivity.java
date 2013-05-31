package com.c35.ptc.goout.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
import com.c35.ptc.goout.bean.Major;
import com.c35.ptc.goout.interfaces.GetSelectMajorListListener;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;

/**
 * 院校列表，按专业筛选
 * 
 * (尚未实现按首字母分组显示，服务器未返回拼音字段)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-4
 */
public class SelectMajorActivity extends Activity implements OnClickListener {

	private static final String TAG = "SelectMajorActivity";

	private LinearLayout layoutShowInfo;
	private Button btnReLoad;
	private TextView textShowInfo;// 显示加载中，或加载失败，或为空

	private ImageView imgBack;
	private ListView listviewMajor;
	private BaseAdapter adapterMajorList;
	// private ArrayList<Major> listMajors;

	private GetSelectMajorListListener mListener;// 调用服务器接口时的监听
	private GoOutController mController;

	private Handler uiHandler;
	private static final int MSG_GETMAJORLIST_FINISHED = 0x200;// 获取成功
	private static final int MSG_GETMAJORLIST_FAIL = 0x201;// 获取失败
	private static final int MSG_GETMAJORLIST_NULL = 0x202;// 获取为空
	private GoOutGlobal appGloble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_major);

		appGloble = (GoOutGlobal) getApplicationContext();
		mController = GoOutController.getInstance();
		mListener = new GetMajorListListener();
		uiHandler = new UiHandler();

		layoutShowInfo = (LinearLayout) findViewById(R.id.layout_select_major_loadinfo);
		textShowInfo = (TextView) findViewById(R.id.text_select_major_loadinfo);
		btnReLoad = (Button) findViewById(R.id.btn_select_major_reload);
		imgBack = (ImageView) findViewById(R.id.img_select_major_back);
		listviewMajor = (ListView) findViewById(R.id.listview_select_major);

		btnReLoad.setOnClickListener(this);
		imgBack.setOnClickListener(this);

		listviewMajor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				// 传值回去
				// GoOutDebug.e(TAG, "name=" + appGloble.getArraySelectMajor().get(arg2).getNameCn() + "id=" +
				// appGloble.getArraySelectMajor().get(arg2).getServerId());
				Bundle bundle = new Bundle();
				bundle.putString("DataMajorNameCn", appGloble.getArraySelectMajor().get(arg2).getNameCn());
				bundle.putInt("DataMajorServerId", appGloble.getArraySelectMajor().get(arg2).getServerId());
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
		// test
		// listMajors = new ArrayList<Major>();
		// listMajors = TestGoOut.getTestMajorDataList();
		// adapterMajorList = new MajorListAdapter(SelectMajorActivity.this, listMajors);
		// listviewMajor.setAdapter(adapterMajorList);

		getMajorList();
	}

	/**
	 * 加载专业列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-20
	 */
	private void getMajorList() {
		// 有则显示，无则获取
		if (appGloble.getArraySelectMajor() == null || appGloble.getArraySelectMajor().size() == 0) {
			// 网络状态
			if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
				textShowInfo.setText(R.string.loadnonetwork);
				btnReLoad.setVisibility(View.VISIBLE);
				return;
			} else {
				btnReLoad.setVisibility(View.GONE);
				textShowInfo.setText(R.string.select_major_loading);
				mController.getSelectMajorList(mListener);
			}
		} else {
			showMajorList();
		}
	}

	/**
	 * 显示列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private void showMajorList() {

		// listMajors = new ArrayList<Major>();
		// listMajors = Constants.arraySelectMajor;

		adapterMajorList = new MajorListAdapter(SelectMajorActivity.this, appGloble.getArraySelectMajor());
		listviewMajor.setAdapter(adapterMajorList);
		layoutShowInfo.setVisibility(View.GONE);
		listviewMajor.setVisibility(View.VISIBLE);
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
		case R.id.img_select_major_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_select_major_reload:
			// 重新加载
			getMajorList();
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
			case MSG_GETMAJORLIST_FAIL:
				textShowInfo.setText(R.string.select_major_loadfail);
				btnReLoad.setVisibility(View.VISIBLE);
				break;
			case MSG_GETMAJORLIST_FINISHED:
				showMajorList();
				break;
			case MSG_GETMAJORLIST_NULL:
				textShowInfo.setText(R.string.select_major_loadnull);
				btnReLoad.setVisibility(View.VISIBLE);
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
	private class GetMajorListListener extends GetSelectMajorListListener {

		@Override
		public void getSelectMajorListFailed() {
			super.getSelectMajorListFailed();
			uiHandler.sendEmptyMessage(MSG_GETMAJORLIST_FAIL);
		}

		@Override
		public void getSelectMajorListFinished(ArrayList<Major> majorList) {
			super.getSelectMajorListFinished(majorList);

			if (majorList != null && majorList.size() > 0) {
				ArrayList<Major> majorlist = new ArrayList<Major>();
				Major m = new Major();
				m.setNameCn(getString(R.string.select_major_all));
				m.setServerId(-1);
				majorlist.add(m);
				for (Major a : majorList) {
					majorlist.add(a);
				}
				// 列表不为空
				appGloble.setArraySelectMajor(majorlist);
				uiHandler.sendEmptyMessage(MSG_GETMAJORLIST_FINISHED);

			} else {
				// 列表为空
				uiHandler.sendEmptyMessage(MSG_GETMAJORLIST_NULL);
			}

		}

	}

	/**
	 * 专业列表适配
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class MajorListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Major> listMajors;

		public MajorListAdapter(Context context, List<Major> list) {
			this.inflater = LayoutInflater.from(context);
			this.listMajors = list;
		}

		@Override
		public int getCount() {
			return listMajors.size();
		}

		@Override
		public Object getItem(int position) {
			return listMajors.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				// GoOutDebug.e(TAG, "convertView = null");
				convertView = inflater.inflate(R.layout.listitem_select_major, null);
				holder = new ViewHolder();
				holder.textNameCn = (TextView) convertView.findViewById(R.id.text_listitem_major_select_namecn);
				holder.textSortKeyFirst = (TextView) convertView.findViewById(R.id.text_listitem_major_select_groupname);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textNameCn.setText(listMajors.get(position).getNameCn());
			// 是否显示区域(仅当属于改区域中的第一个国家时显示区域)
			if (listMajors.get(position).isFirstOfSortKey()) {
				holder.textSortKeyFirst.setText(getFirstLetterFromSortkey(listMajors.get(position).getSortKey()));
				holder.textSortKeyFirst.setVisibility(View.VISIBLE);
			} else {
				holder.textSortKeyFirst.setVisibility(View.GONE);
			}

			return convertView;
		}

		private class ViewHolder {

			TextView textNameCn;// 中文名
			TextView textSortKeyFirst;// 区域
		}

	}

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
}
