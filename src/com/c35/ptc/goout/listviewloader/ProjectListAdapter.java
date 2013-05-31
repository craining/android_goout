package com.c35.ptc.goout.listviewloader;

import java.util.ArrayList;

import android.content.Context;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Project;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 项目列表适配
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-12
 */
public class ProjectListAdapter extends BaseAdapter {

	private static final String TAG = "ProjectListAdapter";
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	private ImageLoader mImageLoader;
	// private int mCount;
	private Context mContext;
	// private String[] urlArrays;
	private ArrayList<Project> listProject;

	public ProjectListAdapter(Context context, ArrayList<Project> listProject) {
		this.mContext = context;
		this.listProject = listProject;
		mImageLoader = new ImageLoader(context);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	@Override
	public int getCount() {
		return listProject.size();
	}

	@Override
	public Object getItem(int position) {
		return listProject.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_main_project, null);
			viewHolder = new ViewHolder();
			viewHolder.imgIco = (ImageView) convertView.findViewById(R.id.img_listitem_project_ic);
			viewHolder.textName = (TextView) convertView.findViewById(R.id.text_listitem_project_name);
			viewHolder.textGpaLanguage = (TextView) convertView.findViewById(R.id.text_listitem_project_gpa_laguage);
			viewHolder.textDegreeCountry = (TextView) convertView.findViewById(R.id.text_listitem_project_degree_country);
			viewHolder.textTuiTion = (TextView) convertView.findViewById(R.id.text_listitem_project_tuition);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!mBusy && !StringUtil.isNull(listProject.get(position).getLogoName())) {
			String url = "";
			url = ImageUrlUtil.getProjectPhotoUrl(listProject.get(position).getLogoName(), listProject.get(position).getServerId(), ImageUrlUtil.PHOTO_TYPE_S);
			GoOutDebug.e(TAG, "url=" + url);
			GoOutDebug.e(TAG, "mProject.getLogoName()=" + listProject.get(position).getLogoName());
			mImageLoader.DisplayImage(url, viewHolder.imgIco, false);
		}

		Project project = listProject.get(position);

		viewHolder.textName.setText(project.getName());
		StringBuilder sb1 = new StringBuilder();
		// 如果GPA为空，则不显示
		if (!StringUtil.isNull(project.getGpa())) {
			sb1.append(mContext.getString(R.string.projectinfo_gpa)).append(project.getGpa()).append("  ");
		}
		// 如果语言为空，则不显示
		if (!StringUtil.isNull(project.getLanguage())) {
			sb1.append(mContext.getString(R.string.projectinfo_language)).append(project.getLanguage());
		}
		// 如果GPA和语言都为空，则不显示此行
		if (!StringUtil.isNull(sb1.toString())) {
			viewHolder.textGpaLanguage.setText(sb1.toString());
			viewHolder.textGpaLanguage.setVisibility(View.VISIBLE);
		} else {
			// GoOutDebug.e(TAG, "ONE NULL HIDE!!! name = " + project.getName());
			viewHolder.textGpaLanguage.setVisibility(View.GONE);
		}

		StringBuilder sb2 = new StringBuilder();
		// 如果Degree为空，则不显示
		if (!StringUtil.isNull(project.getDegree())) {
			sb2.append(project.getDegree()).append("  ");
		}
		// 如果国家为空，则不显示
		if (!StringUtil.isNull(project.getCountry())) {
			sb2.append(project.getCountry());
		}
		// 如果degree和国家都为空，则不显示此行
		if (!StringUtil.isNull(sb2.toString())) {
			viewHolder.textDegreeCountry.setText(sb2.toString());
			viewHolder.textDegreeCountry.setVisibility(View.VISIBLE);
		} else {
			// GoOutDebug.e(TAG, "TWO NULL HIDE!!! name = " + project.getName());
			viewHolder.textDegreeCountry.setVisibility(View.GONE);
		}
		// 学费
		if (listProject.get(position).getTuition() != 0) {
			viewHolder.textTuiTion.setVisibility(View.VISIBLE);
			viewHolder.textTuiTion.setText(listProject.get(position).getTuitionUnit() + listProject.get(position).getTuition() + "/" + listProject.get(position).getTuitionTimeUnit());
		} else {
			viewHolder.textTuiTion.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {

		ImageView imgIco;
		TextView textName;
		TextView textGpaLanguage;
		TextView textDegreeCountry;
		TextView textTuiTion;
	}
}
