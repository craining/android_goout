package com.c35.ptc.goout.interfaces;

/**
 * 项目详情页图片展示控件回调
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public interface ViewPictureFlipperListener {

	public void onPictureClickedListener(int id);// 图片被点击了

	public void onPictureChangedListener(int id);// 图片切换了

	public void onPictureLongClickedListener(int id);// 图片被长按了

	public void onPictureScrolling(boolean scrolling);// 正在滑动

	public void onPictureScrollingStoped(boolean stop);// 滑动结束
}
