package com.c35.ptc.goout.interfaces;

/**
 * 字母检索控件的回调
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public interface ViewLetterListListener {

	public void onTouchingLetterEnd();// 字母检索结束

	public void onTouchingLetterChanged(String s, float y, float x, float width);// 字母检索中
}
