package com.c35.ptc.goout.listviewloader;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.School;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.ServerUtil;

/**
 * 院校列表适配
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-12
 */
public class SchoolListAdapter extends BaseAdapter {

	private static final String TAG = "MainSchoolListAdapter";
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	private ImageLoader mImageLoader;
	// private int mCount;
	private Context mContext;
	// private String[] urlArrays;
	private ArrayList<School> listSchool;

	public SchoolListAdapter(Context context, ArrayList<School> listSchool) {
		this.mContext = context;
		this.listSchool = listSchool;
		mImageLoader = new ImageLoader(context);

	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	@Override
	public int getCount() {
		return listSchool.size();
	}

	@Override
	public Object getItem(int position) {
		return listSchool.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_main_school, null);
			viewHolder = new ViewHolder();
			viewHolder.imgIco = (ImageView) convertView.findViewById(R.id.img_listitem_school_ic);
			viewHolder.textNameCn = (TextView) convertView.findViewById(R.id.text_listitem_school_namecn);
			viewHolder.textNameEn = (TextView) convertView.findViewById(R.id.text_listitem_school_nameen);
			viewHolder.textRanking = (TextView) convertView.findViewById(R.id.text_listitem_school_ranking);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = "";
		// url = listSchool.get(position).getSchoolLogoUrl();
		url = ImageUrlUtil.getSchoolLogoUrl(listSchool.get(position).getLogoName());
		GoOutDebug.e(TAG, "URL=" + url);
		viewHolder.imgIco.setImageResource(R.drawable.online_image_loading_school_logo);

		if (!mBusy) {
			mImageLoader.DisplayImage(url, viewHolder.imgIco, false);
		}

		viewHolder.textNameCn.setText(listSchool.get(position).getNameCn());
		viewHolder.textNameEn.setText(listSchool.get(position).getNameEn());
		viewHolder.textRanking.setText(listSchool.get(position).getRanking() + "");

		return convertView;
	}

	static class ViewHolder {

		ImageView imgIco;
		TextView textNameCn;
		TextView textNameEn;
		TextView textRanking;
	}

}