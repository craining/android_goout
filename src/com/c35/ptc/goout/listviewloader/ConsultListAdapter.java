package com.c35.ptc.goout.listviewloader;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.RecentlyConsult;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.TimeUtil;
import com.c35.ptc.goout.util.ViewUtil;

/**
 * 顾问列表适配（最近联系）
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-12
 */
public class ConsultListAdapter extends BaseAdapter {

	private static final String TAG = "ConsultListAdapter";
	private boolean mBusy = false;
	// 从资源里读取的title
	private String telString;// “联系电话”
	private String serverAreaString;// “服务区域”
	private String callTime;// "联系时间"

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	private ImageLoader mImageLoader;
	// private int mCount;
	private Context mContext;
	// private String[] urlArrays;
	private ArrayList<RecentlyConsult> listConsultant;

	public ConsultListAdapter(Context context, ArrayList<RecentlyConsult> listConsults) {
		this.mContext = context;
		this.listConsultant = listConsults;
		mImageLoader = new ImageLoader(context);
		telString = mContext.getResources().getString(R.string.consultant_tel);
		serverAreaString = mContext.getResources().getString(R.string.consultant_serverarea);
		callTime = mContext.getResources().getString(R.string.consultant_call_time);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	@Override
	public int getCount() {
		return listConsultant.size();
	}

	@Override
	public Object getItem(int position) {
		return listConsultant.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_recently_communicate, null);
			viewHolder = new ViewHolder();
			viewHolder.imgIco = (ImageView) convertView.findViewById(R.id.img_listitemconsultant_ic);
			viewHolder.textName = (TextView) convertView.findViewById(R.id.text_listitemconsultant_name);
			viewHolder.textTel = (TextView) convertView.findViewById(R.id.text_listitemconsultant_tel);
			viewHolder.textServerArea = (TextView) convertView.findViewById(R.id.text_listitemconsultant_serverarea);
			viewHolder.textDescription = (TextView) convertView.findViewById(R.id.text_listitemconsultant_group);
			viewHolder.textCallTime = (TextView) convertView.findViewById(R.id.text_listitemconsultant_calltime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imgIco.setImageResource(R.drawable.online_image_loading_publisher_logo);
		if (!StringUtil.isNull(listConsultant.get(position).getCalledLogoName())) {
			String url = "";
			url = ImageUrlUtil.getPublisherLogoUrl(listConsultant.get(position).getCalledLogoName());
			if (!mBusy) {
				mImageLoader.DisplayImage(url, viewHolder.imgIco, false);
			}
		}

		viewHolder.textName.setText(listConsultant.get(position).getCalledName());
		viewHolder.textTel.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, telString, listConsultant.get(position).getCalledNumber()));

		// 服务区域
		if (StringUtil.isNull(listConsultant.get(position).getCalledServerArea())) {
			viewHolder.textServerArea.setVisibility(View.GONE);
		} else {
			viewHolder.textServerArea.setVisibility(View.VISIBLE);
			viewHolder.textServerArea.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, serverAreaString, listConsultant.get(position).getCalledServerArea()));
		}
		// 描述
		if (StringUtil.isNull(listConsultant.get(position).getCalledDescription())) {
			viewHolder.textDescription.setVisibility(View.GONE);
		} else {
			viewHolder.textDescription.setVisibility(View.VISIBLE);
			viewHolder.textDescription.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, listConsultant.get(position).getCalledDescription(), ""));
		}
		// 联系时间
		viewHolder.textCallTime.setVisibility(View.VISIBLE);
		viewHolder.textCallTime.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, callTime, TimeUtil.longToDateTimeString(listConsultant.get(position).getCallTime())));

		return convertView;
	}

	static class ViewHolder {

		ImageView imgIco;
		TextView textName;
		TextView textTel;
		TextView textServerArea;
		TextView textDescription;
		TextView textCallTime;// 拨打时间
	}
}
