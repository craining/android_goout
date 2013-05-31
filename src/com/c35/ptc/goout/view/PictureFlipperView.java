package com.c35.ptc.goout.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewPictureFlipperListener;

/**
 * 项目详情顶部图片查看效果(方案1)横向滑动的View
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-1
 */
public class PictureFlipperView extends LinearLayout {

	private ViewPictureFlipperListener pictureFlipperViewListener;
	private ViewPager viewPager;
	private ArrayList<View> listPictureViews;
	private ImageView[] imagePointViews;
	private Context mContext;

	public PictureFlipperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public PictureFlipperView(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 设置各个图片显示
	 * 
	 * @Description:
	 * @param imagesUrl
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setContentView(String[] imagesUrl) {

		if (mContext == null || !(imagesUrl != null && imagesUrl.length > 0)) {
			return;
		}

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listPictureViews = new ArrayList<View>();

		// //测试不同尺寸的图片
		// OnlineImageView layout;
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m1, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		//
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m2, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m3, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m4, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m5, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m6, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m7, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		// layout = new OnlineImageView(mContext);
		// layout.setImageViewResource(R.drawable.m8, OnlineImageView.PIC_SEZE_MIDDLE, false);
		// listPictureViews.add(layout);
		OnlineImageView ImageViewLayout;
		for (int id = 0; id < imagesUrl.length; id++) {
			final int idTemp = id;
			ImageViewLayout = new OnlineImageView(mContext);
			ImageViewLayout.setImageViewUrl(imagesUrl[id], OnlineImageView.PIC_PROJECT_PHOTO, false, ImageView.ScaleType.FIT_XY);

			// 设置图片点击的监听
			ImageViewLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (pictureFlipperViewListener != null) {
						pictureFlipperViewListener.onPictureClickedListener(idTemp);
					}
				}
			});

			ImageViewLayout.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (pictureFlipperViewListener != null) {
						pictureFlipperViewListener.onPictureLongClickedListener(idTemp);
					}

					return false;
				}
			});

			listPictureViews.add(ImageViewLayout);
		}

		imagePointViews = new ImageView[listPictureViews.size()];
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.pictures_flippershow, null);
		ViewGroup group = (ViewGroup) main.findViewById(R.id.viewgroup_flippershow_pointimages);
		// group.setBackgroundResource(R.drawable.ic_launcher);
		viewPager = (ViewPager) main.findViewById(R.id.viewpager_flippershow);

		// 设置图片底部的点
		LayoutParams pams = new LayoutParams(12, 12);
		pams.setMargins(5, 0, 5, 0);
		pams.gravity = Gravity.CENTER_VERTICAL;
		ImageView imagePointView;
		for (int i = 0; i < listPictureViews.size(); i++) {
			imagePointView = new ImageView(mContext);
			imagePointView.setLayoutParams(pams);
			imagePointViews[i] = imagePointView;
			group.addView(imagePointView);
		}

		setBackgroundPointImage(0);// 默认进入程序后第一张图片被选中;

		this.addView(main);

		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyListener());

	}

	/**
	 * 设置切换监听
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-1
	 */
	public void setOnPictureClickedListener(ViewPictureFlipperListener listener) {
		this.pictureFlipperViewListener = listener;
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// return listPictureViews.size();//不循环往复滑动
			// 仅当图片多余1张时可循环滑动
			if (imagePointViews.length == 1) {
				return 1;
			} else {
				return 1000;// 循环往复滑动1000次
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(listPictureViews.get(arg1 % listPictureViews.size()));
			// GoOutDebug.e("destroyItem", "removeView = " + arg1);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			if (((ViewPager) arg0).getChildCount() == listPictureViews.size()) {
				((ViewPager) arg0).removeView(listPictureViews.get(arg1 % listPictureViews.size()));
			}
			((ViewPager) arg0).addView(listPictureViews.get(arg1 % listPictureViews.size()), 0);

			return listPictureViews.get(arg1 % listPictureViews.size());
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			GoOutDebug.e("restoreState", "restoreState = ");
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	/**
	 * 页面切换监听器
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// 滚动后
			// GoOutDebug.e("picflipper", "onPageScrollStateChanged");
			if (pictureFlipperViewListener != null) {
				pictureFlipperViewListener.onPictureScrollingStoped(true);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 滚动中
			// GoOutDebug.e("picflipper", "onPageScrolled");
			if (pictureFlipperViewListener != null) {
				pictureFlipperViewListener.onPictureScrolling(true);
			}

		}

		@Override
		public void onPageSelected(int arg0) {
			setBackgroundPointImage(arg0);
			if (pictureFlipperViewListener != null) {
				pictureFlipperViewListener.onPictureChangedListener(arg0 % listPictureViews.size());
			}

		}
	}

	/**
	 * 当图片切换时，设置底部原点的变化
	 * 
	 * @Description:
	 * @param id
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-1
	 */
	private void setBackgroundPointImage(int id) {
		id = id % imagePointViews.length;
		imagePointViews[id].setBackgroundResource(R.drawable.pictures_filpper_dot_selected);
		// imagePointViews[id].setImageResource(R.drawable.pictures_filpper_dot_selected);
		for (int i = 0; i < imagePointViews.length; i++) {
			if (id != i) {
				// imagePointViews[id].setImageResource(R.drawable.pictures_filpper_dot_unselected);
				imagePointViews[i].setBackgroundResource(R.drawable.pictures_filpper_dot_unselected);
			}
		}
	}

	/**
	 * 
	 * @Description:
	 * @param allow
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	public void disallowInterceptTouchEvent(boolean allow) {
		viewPager.requestDisallowInterceptTouchEvent(allow);
	}

}
