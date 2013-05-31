package com.c35.ptc.goout.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewOnlineImageListener;
import com.c35.ptc.goout.interfaces.ViewPictureClickListener;
import com.c35.ptc.goout.util.ImageUrlUtil;

/**
 * 为院校的图片展示页做的一个图片展示的view
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public class PictureClickView extends LinearLayout {

	private static final String TAG = "PictureClickView";
	private ViewPictureClickListener pictureClickViewListener;
	private OnlineImageView[] imageItemViews;
	private int showingId;// 当前正在展示的图片
	private String[] imagesNames;// 图片的名称
	private String countryName;// 国家名称
	private int schoolId;// 院校id

	private OnlineImageView imgShow;// 展示的图片
	private LinearLayout layoutImagesList;// 小图片
	private HorizontalScrollView scrollView;// 滚动小图片的

	private Context mContext;

	private float touchStartX = 0;// 用于手指滑动切换图片显示

	private boolean isImageSelected = false;// 若未点击过小图，则第一张图加载完毕后默认显示第一张图

	public PictureClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public PictureClickView(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		GoOutDebug.v(TAG, "+++++++++++++++++++++++++++++++++++main width = " + getWidth());
		super.onDraw(canvas);
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
	public void setContentView(String[] imagesName, String countryName, int schoolId, int allwidth) {

		GoOutDebug.e(TAG, "allLength-------------------------=" + allwidth);
		if (mContext == null) {
			return;
		}

		this.imagesNames = imagesName;
		this.countryName = countryName;
		this.schoolId = schoolId;

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.pictures_clickshow, null);

		layoutImagesList = (LinearLayout) main.findViewById(R.id.viewgroup_clickshow_itemimages);
		imgShow = (OnlineImageView) main.findViewById(R.id.imgonline_clickshow_main);
		scrollView = (HorizontalScrollView) main.findViewById(R.id.scroll_clickshow_itemimages);

		imageItemViews = new OnlineImageView[imagesNames.length];

		// 设置图片底部的列表
		// 屏幕适配，计算出scrollView的高度，然后减去边距，即为图片的高度。
		// int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		// scrollView.measure(w, h);
		// this.measure(w, h);

		// LinearLayout mainLayout = (LinearLayout) main.findViewById(R.id.layout_imageonline_show_main);
		// mainLayout.measure(w, h);

		// int allSingleWidth = (int) ((allwidth - 2 * (allwidth / 45) - 2 * (scrollView.getPaddingRight())) /
		// 5);

		// Log.e(TAG, "this.getMeasureWidth=" + this.getMeasuredWidth() + "allSingleWidth=" + allSingleWidth);

		int imageShowHeight = (int) (allwidth * 0.9);
		int layoutKnotMarginTop = (int) (allwidth * 0.86);// 扣距离顶部的高度
		int layoutImageListHeight = (int) (allwidth * 0.2);// 底部小图所在layout的高度
		int layoutImageListPadingTB = allwidth / 30;

		// 动态设置大图高度
		LinearLayout.LayoutParams imgParamsShow = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, imageShowHeight);
		imgShow.setLayoutParams(imgParamsShow);
		// 动态设置底部小图所在layout的高度
		LinearLayout.LayoutParams imgParamsImageListLayout = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, layoutImageListHeight);
		scrollView.setLayoutParams(imgParamsImageListLayout);
		layoutImagesList.setPadding(0, layoutImageListPadingTB, 0, layoutImageListPadingTB);

		// 动态控制两个扣的显示位置
		RelativeLayout layoutKnont = (RelativeLayout) main.findViewById(R.id.layout_clickshow_knot);
		RelativeLayout.LayoutParams imgParamsKnot = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		imgParamsKnot.setMargins(0, layoutKnotMarginTop, 0, 0);
		layoutKnont.setLayoutParams(imgParamsKnot);

		// int imageHeight = layoutImagesList.getMeasuredHeight();// 得到图片的高度
		// int imageHeight = layoutImagesList.getMeasuredHeight() - layoutImagesList.getPaddingTop() -
		// layoutImagesList.getPaddingBottom() - borderWidth;// 得到图片的高度
		// Log.e(TAG, "scrollView.getPaddingRight()=" + scrollView.getPaddingRight());
		int allSingleWidth = (int) ((allwidth - 2 * (scrollView.getPaddingRight())) / 5.5);// 每个小图包含其边距所占的空间
		int width = (int) (allSingleWidth * 0.8);// 小图的宽度
		int height = (int) (allSingleWidth * 0.6);// 得到小图片的高度
		int margin = (int) (allSingleWidth * 0.1);// 小图间距的1/2
		int borderWidth = (int) (allSingleWidth * 0.04);// 小图被选中时的边缘的宽度

		Log.e(TAG, "layoutImagesList.getMeasuredHeight()=" + layoutImagesList.getMeasuredHeight() + "scrollView=PadingLR" + scrollView.getPaddingRight() + "layoutImagesList=PadingT" + layoutImageListPadingTB + "layoutImagesList=PadingB" + layoutImageListPadingTB + " imageHeight=" + height + "allSingleWidth=" + allSingleWidth);
		// Log.e(TAG, "width=" + width + "margin=" + margin + "all=" + (width + 2 * margin));
		LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(width, height);
		imgParams.setMargins(margin, 0, margin, 0);
		for (int i = 0; i < imagesNames.length; i++) {
			final int idTemp = i;
			final OnlineImageView imageItemView = new OnlineImageView(mContext);
			imageItemView.setLayoutParams(imgParams);
			imageItemView.setPadding(borderWidth, borderWidth, borderWidth, borderWidth);
			imageItemView.setClickable(true);
			imageItemView.setImageViewUrl(getSmallPicUrl(imagesNames[i]), OnlineImageView.PIC_SCHOOL_PHOTO_LOGO, true, ImageView.ScaleType.FIT_XY);
			// 设置图片点击的监听
			imageItemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					GoOutDebug.e(TAG, "clicked show image item:" + idTemp);
					isImageSelected = true;
					showImage(idTemp, true, false);
					// imageItemViews[idTemp].setImageViewUrl(mContext,
					// imageUrls[idTemp]);// 刷新与否？
					if (pictureClickViewListener != null) {
						pictureClickViewListener.onPictureShowListItemClickedListener(idTemp);
					}
				}
			});

			// 监听第一张小图的加载情况，若加载完成，且用户并没有点击其它小图时，则默认展示第一张图。

			imageItemView.setOnlineImageViewListener(new ViewOnlineImageListener() {

				@Override
				public void OnImageLoadSuccess(String imageUrl) {

					// 第一张图加载完成
					if (!isImageSelected && idTemp == 0) {
						showImage(0, true, false);
					}
				}

				@Override
				public void OnImageLoadStart(String imageUrl) {
				}

				@Override
				public void OnImageLoadFail(String imageUrl) {
					// TODO 由于服务器上有不存在的图片，加此监听，若加载失败，则不显示

					imageItemView.setVisibility(View.GONE);
				}
			});

			imageItemViews[i] = imageItemView;

			layoutImagesList.addView(imageItemView);
			// GoOutDebug.e(TAG, "imageItemViews[i] X=" + imageItemViews[i].getWidth());
		}

		this.addView(main);

		// 点击大图片的监听
		imgShow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pictureClickViewListener != null && imagesNames != null && showingId < imagesNames.length) {
					pictureClickViewListener.onPictureShowMainImageClickedListener(showingId, imagesNames[showingId]);
				}
			}
		});

		// 暂时不允许滑动大图查看
		// imgShow.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// // 按下
		// touchStartX = event.getX();
		// GoOutDebug.v(TAG, "startX= " + touchStartX);
		// break;
		// case MotionEvent.ACTION_UP:
		// // 抬起
		// float moveX = event.getX() - touchStartX;
		// GoOutDebug.v(TAG, "endX= " + event.getX());
		// GoOutDebug.v(TAG, "moveX= " + moveX);
		// if (moveX > 0 && ((moveX - 100) > 0)) {
		// // 右滑
		// showImage(showingId - 1, true, true);
		// } else if (moveX < 0 && ((moveX + 100) < 0)) {
		// // 左滑
		// showImage(showingId + 1, true, true);
		// }
		//
		// break;
		//
		// default:
		// break;
		// }
		//
		// return false;
		// }
		// });

		GoOutDebug.e(TAG, "setContent views end!!!!");
		if (pictureClickViewListener != null) {
			GoOutDebug.e(TAG, "setContent views end??????????");
			pictureClickViewListener.onPictureShowResume();
		}

	}

	/**
	 * 设置显示的图片
	 * 
	 * @Description:
	 * @param id
	 * @param imgShow
	 * @param showInImageList
	 *            若是点击底部列表出发的，则为true
	 * @param fliperImageShow
	 *            是否是通过滑动大图切换的
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public void showImage(int id, boolean showInImageList, boolean fliperImageShow) {

		GoOutDebug.v(TAG, "showImage=" + id);

		// // 可循环左右滑动
		// if (id < 0) {
		// id = imageUrls.length - 1;
		// } else {
		// id = id % imageUrls.length;
		// }

		// 不可循环滑动
		if (id >= 0 && id < imagesNames.length) {
			if (imagesNames != null && id < imagesNames.length) {

				imgShow.setImageViewUrl(getMiddlePicUrl(imagesNames[id]), OnlineImageView.PIC_SCHOOL_PHOTO, false, ImageView.ScaleType.FIT_XY);

				if (showInImageList) {
					showingId = id;
					setBackgroundItemImage(id, fliperImageShow);
				}
			}

		}
	}

	/**
	 * 底部图片被选中的状态
	 * 
	 * @Description:
	 * @param id
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-1
	 */
	private void setBackgroundItemImage(int id, boolean fliperImageShow) {
		imageItemViews[id].setBackgroundResource(R.drawable.bg_picshow_border_p);
		for (int i = 0; i < imageItemViews.length; i++) {
			if (id != i) {
				imageItemViews[i].setBackgroundResource(R.drawable.bg_picshow_border_n);
			}
		}

		// // 屏幕适配，计算出scrollView的高度，然后减去边距，即为图片的高度。
		// int w1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		// int h1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		// layoutImagesList.measure(w1, h1);
		// // GoOutDebug.e(TAG, "getMeasuredHeight=" + scrollView.getMeasuredHeight() + "  getPaddingTop=" +
		// // scrollView.getPaddingTop() + "  getPaddingBottom=" + scrollView.getPaddingBottom() +
		// // "  imageHeight=" + imageHeight);
		// GoOutDebug.e(TAG, "layoutImagesList : getMeasuredWidth=" + layoutImagesList.getMeasuredWidth());
		// GoOutDebug.e(TAG, "scrollView : getMeasuredWidth=" + scrollView.getMeasuredWidth());
		// GoOutDebug.e(TAG, "scrollView : getScrollX=" + scrollView.getScrollX());
		// GoOutDebug.e(TAG, "scrollView : getScrollY=" + scrollView.getScrollY());
		// GoOutDebug.e(TAG, "imageItemViews[id] : getMeasuredWidth=" +
		// imageItemViews[id].getMeasuredWidth());
		// GoOutDebug.e(TAG, "imageItemViews[id] : getRight=" + imageItemViews[id].getRight());
		// GoOutDebug.e(TAG, "imageItemViews[id] : geLeft=" + imageItemViews[id].getLeft());
		//
		// if (fliperImageShow) {
		// int showSmallImgRight = imageItemViews[id].getRight();
		// int showSmallImgLeft = imageItemViews[id].getLeft();
		// int scrollViewWidth = scrollView.getMeasuredWidth();
		// int scrollViewX = scrollView.getScrollX();
		// if (scrollViewWidth < (showSmallImgRight + 10)) {
		// scrollView.scrollTo(showSmallImgRight + 10, 0);
		// // Runnable s = new ScrollAnim();
		// // s.run();
		//
		// } else if ((showSmallImgLeft - 10) < scrollViewX) {
		// scrollView.scrollTo(showSmallImgLeft - 10, 0);
		// // Runnable s = new ScrollAnim();
		// // s.run();
		// }
		// }

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
	public void setOnPictureClickedListener(ViewPictureClickListener listener) {
		this.pictureClickViewListener = listener;
	}

	/**
	 * 获取小图url，供缩略图使用
	 * 
	 * @Description:
	 * @param picname
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-2
	 */
	private String getSmallPicUrl(String picname) {
		return ImageUrlUtil.getSchoolPhotoUrl(picname, countryName, schoolId, ImageUrlUtil.PHOTO_TYPE_S);
	}

	/**
	 * 获取大图，供展示图使用
	 * 
	 * @Description:
	 * @param picname
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-2
	 */
	private String getMiddlePicUrl(String picname) {
		return ImageUrlUtil.getSchoolPhotoUrl(picname, countryName, schoolId, ImageUrlUtil.PHOTO_TYPE_M);

	}
}
