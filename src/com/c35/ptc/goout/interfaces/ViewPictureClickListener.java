package com.c35.ptc.goout.interfaces;

/**
 * 院校照片展示控件的回调
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public interface ViewPictureClickListener {

	public void onPictureShowListItemClickedListener(int id);// 图片底部横向列表小图片被点中

	public void onPictureShowMainImageClickedListener(int id, String picName);// 正在展示的大图片被点击了

	public void onPictureShowResume();// 图片UIadd完成
}
