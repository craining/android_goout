package com.c35.ptc.goout.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.SchoolDegreeInfo;
import com.c35.ptc.goout.interfaces.ViewSchoolDegreeListener;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.ViewUtil;

/**
 * 院校学位中，可展开的条目
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-13
 */
public class SchoolDegreeView extends LinearLayout {

	private static final String TAG = "SchoolDegreeView";
	private ViewSchoolDegreeListener viewSchoolDegreeListener;
	private Context mContext;
	private LinearLayout layoutInfo;
	private ImageView imgCtrl;
	private RelativeLayout layoutTitle;
	private ViewGroup main;
	private boolean isOpen;// 展开状态
	private int id = -1;
	private boolean allNull = true;// 是否是没有详细信息

	public SchoolDegreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initViews();
	}

	public SchoolDegreeView(Context context) {
		super(context);
		this.mContext = context;
		initViews();
	}

	/**
	 * 初始化ui
	 */
	private void initViews() {

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		main = (ViewGroup) inflater.inflate(R.layout.school_degree, null);
		imgCtrl = (ImageView) main.findViewById(R.id.img_schooldegree_updown);
		layoutTitle = (RelativeLayout) main.findViewById(R.id.layout_schooldegree_title);
		layoutInfo = (LinearLayout) main.findViewById(R.id.layout_schooldegree_info);
		layoutTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpen = !isOpen;
				openOrClose();
			}
		});
	}

	/**
	 * 开关view
	 * 
	 * @Description:
	 * @param open
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	public void setOpenClose(boolean open) {
		if (!allNull) {
			isOpen = open;
			openOrClose();
		}
	}

	/**
	 * 显示学位信息
	 * 
	 * @Description:
	 * @param info
	 * @param id
	 *            当前学位的id
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setContentView(SchoolDegreeInfo info, int id) {
		if (mContext == null || info == null) {
			GoOutDebug.e(TAG, "NULL ERROR");
			return;
		}

		this.id = id;
		allNull = true;

		((TextView) main.findViewById(R.id.text_schooldegree)).setText(info.getDegreeName());

		if (StringUtil.isNull(info.getEducationRequire())) {
			((TextView) main.findViewById(R.id.text_schooldegree_education)).setVisibility(View.GONE);
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_education)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintro_educationrequire), info.getEducationRequire()));
			allNull = false;
		}

		if (StringUtil.isNull(info.getGpa())) {
			((TextView) main.findViewById(R.id.text_schooldegree_gpa)).setVisibility(View.GONE);
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_gpa)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintro_gpa), info.getGpa()));
			allNull = false;
		}

		// 语言要求，无要求不显示
		if (StringUtil.isNull(info.getLanguage())) {
			((TextView) main.findViewById(R.id.text_schooldegree_language)).setVisibility(View.GONE);
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_language)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintrlanguage), info.getLanguage()));
			allNull = false;
		}

		// 截止日期们
		String[] endTimes = info.getEndTimes();
		if (endTimes != null && endTimes.length > 0) {
			String endTimesStr = "";
			for (String endTime : endTimes) {
				endTimesStr = endTimesStr + endTime + "、";
			}
			((TextView) main.findViewById(R.id.text_schooldegree_endtime)).setVisibility(View.VISIBLE);
			((TextView) main.findViewById(R.id.text_schooldegree_endtime)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintro_endtime), endTimesStr));
			allNull = false;
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_endtime)).setVisibility(View.GONE);
		}

		// 学费(3000美元/年)
		if (info.getTuituon() > 0) {
			((TextView) main.findViewById(R.id.text_schooldegree_tuition)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintro_tuition), info.getTuituon() + info.getTuituonUnit() + "/" + info.getTuituonTimeUnit()));
			allNull = false;
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_tuition)).setVisibility(View.GONE);
		}

		// 奖学金有无
		if (info.getScholarShip() == 0 || info.getScholarShip() == 1) {
			((TextView) main.findViewById(R.id.text_schooldegree_scholarship)).setText(ViewUtil.getStyleBlackTitleGreyContentSmall(mContext, mContext.getString(R.string.schoolintro_scholarship), info.getScholarShip() == 0 ? mContext.getString(R.string.schoolintro_scholarship_have) : mContext.getString(R.string.schoolintro_scholarship_none)));
			allNull = false;
		} else {
			((TextView) main.findViewById(R.id.text_schooldegree_scholarship)).setVisibility(View.GONE);
		}

		if (allNull) {
			// 所有都为空
			((LinearLayout) main.findViewById(R.id.layout_schooldegree_info)).setVisibility(View.GONE);
			((ImageView) main.findViewById(R.id.img_schooldegree_updown)).setVisibility(View.GONE);
		}

		this.addView(main);

		// 第一个学位不显示顶部line
		if (id == 0) {
			((View) main.findViewById(R.id.view_schooldegree_line)).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 开关view
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void openOrClose() {
		if (!allNull) {
			if (isOpen) {
				if (viewSchoolDegreeListener != null) {
					viewSchoolDegreeListener.onItemOpened(id);
				}

				layoutInfo.setVisibility(View.VISIBLE);
				imgCtrl.setImageResource(R.drawable.ic_arrow_up);

			} else {
				if (viewSchoolDegreeListener != null) {
					viewSchoolDegreeListener.onItemClosed(id);
				}

				layoutInfo.setVisibility(View.GONE);
				imgCtrl.setImageResource(R.drawable.ic_arrow_down);
			}
		}

	}

	/**
	 * 设置展开与关闭的监听
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	public void setViewSchoolDegreeListener(ViewSchoolDegreeListener listener) {

		this.viewSchoolDegreeListener = listener;
	}
}
