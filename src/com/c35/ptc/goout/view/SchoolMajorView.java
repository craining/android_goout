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

import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.SchoolMajorInfo;
import com.c35.ptc.goout.interfaces.ViewSchoolMajorListener;
import com.c35.ptc.goout.util.ViewUtil;

/**
 * 院校专业中，可展开的条目
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-13
 */
public class SchoolMajorView extends LinearLayout {

	private static final String TAG = "SchoolMajorView";
	private ViewSchoolMajorListener viewSchoolMajorListener;
	private Context mContext;
	private LinearLayout layoutInfo;
	private RelativeLayout layoutTitle;
	private ImageView imgCtrl;
	private ViewGroup main;
	private boolean isOpen;// 展开状态
	private int id = -1;

	public SchoolMajorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initViews();
	}

	public SchoolMajorView(Context context) {
		super(context);
		this.mContext = context;
		initViews();
	}

	/**
	 * 初始化ui
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void initViews() {

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		main = (ViewGroup) inflater.inflate(R.layout.school_major, null);
		imgCtrl = (ImageView) main.findViewById(R.id.img_schoolmajor_updown);
		layoutTitle = (RelativeLayout) main.findViewById(R.id.layout_schoolmajor_item_title);
		layoutInfo = (LinearLayout) main.findViewById(R.id.layout_schoolmajor_info);
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
		isOpen = open;
		openOrClose();
	}

	/**
	 * 显示学位及以下的专业信息
	 * 
	 * @Description:
	 * @param info
	 * @param id
	 *            当前专业分组的id
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setContentView(SchoolMajorInfo info, int id) {
		if (mContext == null || info == null) {
			return;
		}
		this.id = id;
		((TextView) main.findViewById(R.id.text_schoolmajor_degree)).setText(info.getGroupName());
		// 将专业数组转换成一个string显示的textview上，专业间用空行隔开
		String[] majorsCn = info.getMajorsCn();
		String[] majorsEn = info.getMajorsEn();
		String all = "";
		if (majorsCn != null && majorsCn.length > 0) {

			TextView[] textViews = new TextView[majorsCn.length];

			for (int i = 0; i < majorsCn.length; i++) {
				textViews[i] = new TextView(mContext);
				textViews[i].setPadding(0, 5, 0, 5);
				textViews[i].setText(ViewUtil.getStyleSchoolMajor(mContext, majorsCn[i], majorsEn[i]));
				// if (i != majorsCn.length - 1) {
				// all += majorsCn[i] + "\r\n\r\n";
				// } else {
				// all += majorsCn[i];//最后一个不再加空行
				// }
			}
			// for (String major : majors) {
			// all += major + "\r\n\r\n";
			// }
			LinearLayout layout = (LinearLayout) main.findViewById(R.id.layout_schoolmajor_info_content);
			for(TextView t : textViews) {
				layout.addView(t);
			}
			
		}

		// if (id == 0) {
		// // ((View) main.findViewById(R.id.view_schoolmajor_line)).setVisibility(View.GONE);// 第一个条目不显示分隔线
		// ((LinearLayout)
		// main.findViewById(R.id.layout_schoolmajor_item)).setBackgroundResource(R.drawable.bg_roundcorner_top);//
		// 第一个条目顶部圆角
		// ((RelativeLayout)
		// main.findViewById(R.id.layout_schoolmajor_item_title)).setBackgroundResource(R.drawable.bg_roundcorner_top);//
		// 第一个条目顶部圆角
		// }

		this.addView(main);
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
	public void setViewSchoolMajorListener(ViewSchoolMajorListener listener) {

		this.viewSchoolMajorListener = listener;
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
		if (isOpen) {
			if (viewSchoolMajorListener != null) {
				viewSchoolMajorListener.onItemOpened(id);
			}
			layoutInfo.setVisibility(View.VISIBLE);
			imgCtrl.setImageResource(R.drawable.ic_arrow_up);

		} else {
			if (viewSchoolMajorListener != null) {
				viewSchoolMajorListener.onItemClosed(id);
			}
			viewSchoolMajorListener.onItemClosed(id);
			layoutInfo.setVisibility(View.GONE);
			imgCtrl.setImageResource(R.drawable.ic_arrow_down);
		}
	}

}
