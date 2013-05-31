package com.c35.ptc.goout.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.c35.ptc.goout.R;

/**
 * 有关于UI上的Util
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public class ViewUtil {

	/**
	 * 返回一个TextView的样式，整个TextView分两部分，开头的标题为黑色较大，之后的内容灰色较小
	 * 
	 * (标题最大)
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public static SpannableStringBuilder getStyleBlackTitleGreyContentBig(Context con, String title, String content) {
		String workingText = title + content;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList titleColor = con.getResources().getColorStateList(R.color.black);// 标题的颜色
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.dimgrey);// 内容的颜色

		TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_BIG_TITLE), titleColor, titleColor);// 标题加粗，较大
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_BIG_CONTENT), contentColor, contentColor);// 内容正常，较小

		spannable.setSpan(titleAppearance, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置标题样式
		if (title.length() < workingText.length()) {// 排除内容为空的情况
			spannable.setSpan(contentAppearance, title.length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
		}
		return spannable;
	}

	/**
	 * 返回一个TextView的样式，整个TextView分两部分，开头的标题为黑色较大，之后的内容灰色较小
	 * 
	 * (标题次大)
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public static SpannableStringBuilder getStyleBlackTitleGreyContentMiddle(Context con, String title, String content) {
		String workingText = title + content;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList titleColor = con.getResources().getColorStateList(R.color.black);// 标题的颜色
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.dimgrey);// 内容的颜色

		TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_TITLE), titleColor, titleColor);// 标题加粗，较大
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), contentColor, contentColor);// 内容正常，较小

		spannable.setSpan(titleAppearance, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置标题样式
		if (title.length() < workingText.length()) {// 排除内容为空的情况
			spannable.setSpan(contentAppearance, title.length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
		}
		return spannable;
	}

	/**
	 * 返回一个TextView的样式，整个TextView分两部分，开头的标题为黑色较大，之后的内容灰色较小
	 * 
	 * （标题不大）
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public static SpannableStringBuilder getStyleBlackTitleGreyContentSmall(Context con, String title, String content) {
		String workingText = title + content;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList titleColor = con.getResources().getColorStateList(R.color.black);// 标题的颜色
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.dimgrey);// 内容的颜色

		TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_SMALL_TITLE), titleColor, titleColor);// 标题加粗，较大
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_SMALL_CONTENT), contentColor, contentColor);// 内容正常，较小

		spannable.setSpan(titleAppearance, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置标题样式
		if (title.length() < workingText.length()) {// 排除内容为空的情况
			spannable.setSpan(contentAppearance, title.length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
		}
		return spannable;
	}

	/**
	 * 返回一个TextView的样式，整个TextView分两部分，开头的标题为黑色较大，之后的内容橙色较小
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public static SpannableStringBuilder getStyleBlackTitleOrangeContent(Context con, String title, String content) {
		String workingText = title + content;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList titleColor = con.getResources().getColorStateList(R.color.black);// 标题的颜色
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.orange_text);// 内容的颜色

		TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_TITLE), titleColor, titleColor);// 标题加粗，较大
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), contentColor, contentColor);// 内容橙色，较小

		spannable.setSpan(titleAppearance, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置标题样式
		if (title.length() < workingText.length()) {// 排除内容为空的情况
			spannable.setSpan(contentAppearance, title.length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
		}
		return spannable;
	}

	/**
	 * 返回一个TextView的样式，整个TextView分三个部分，开头的标题为黑色较大，之后的内容橙色较小，最后部分也为黑色较大
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	public static SpannableStringBuilder getStyleBlackHeadOrangeContentBlackTail(Context con, String head, String content, String tail) {

		// 若没有已发布留学数
		if (StringUtil.isNull(tail)) {
			SpannableStringBuilder spannable = new SpannableStringBuilder(head);
			ColorStateList headTailColor = con.getResources().getColorStateList(R.color.black);
			TextAppearanceSpan headTailAppearance1 = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), headTailColor, headTailColor);
			spannable.setSpan(headTailAppearance1, 0, head.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			return spannable;
		}

		if (StringUtil.isNull(content)) {
			content = " 0 ";
		}

		String workingText = head + content + tail;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList headTailColor = con.getResources().getColorStateList(R.color.black);
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.orange_text);

		// 此处需加两个相同的 headTailAppearance，否则仅有一个有效
		TextAppearanceSpan headTailAppearance1 = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), headTailColor, headTailColor);
		TextAppearanceSpan headTailAppearance2 = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), headTailColor, headTailColor);
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MIDDLE_CONTENT), contentColor, contentColor);

		spannable.setSpan(headTailAppearance1, 0, head.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(contentAppearance, head.length(), (content + head).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(headTailAppearance2, (content + head).length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spannable;
	}

	/**
	 * 返回一个TextView的样式，用于显示院校的某个专业
	 * 
	 * @Description:
	 * @param con
	 * @param title
	 * @param content
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public static SpannableStringBuilder getStyleSchoolMajor(Context con, String cn, String en) {
		String workingText = cn + "\r\n" + en;
		SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

		ColorStateList titleColor = con.getResources().getColorStateList(R.color.school_major_cn);// 中文颜色
		ColorStateList contentColor = con.getResources().getColorStateList(R.color.school_major_en);// 英文颜色

		TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MAJOR_CN), titleColor, titleColor);// 标题加粗，较大
		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MAJOR_EN), contentColor, contentColor);// 内容橙色，较小

		spannable.setSpan(titleAppearance, 0, cn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置中文样式
		spannable.setSpan(contentAppearance, cn.length(), workingText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置英文样式
		// if (title.length() < workingText.length()) {// 排除内容为空的情况
		// spannable.setSpan(contentAppearance, title.length(), workingText.length(),
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
		// }
		return spannable;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @Description:
	 * @param context
	 * @param v
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	public static void hideKeyboard(Context context, View v) {
		// 隐藏软件盘
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 获得EditText错误提示的文字颜色风格，4.0上默认白色，背景也是白色，所以更改文字颜色
	 * @Description:
	 * @param con
	 * @param error
	 * @return
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	public static SpannableStringBuilder getErrorTextStyle(Context con, String error) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(error);

		ColorStateList contentColor = con.getResources().getColorStateList(R.color.orange_red);

		TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, con.getResources().getDimensionPixelSize(R.dimen.TEXT_SIZE_MAJOR_EN), contentColor, contentColor);// 内容橙色，较小

		spannable.setSpan(contentAppearance, 0, error.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置
		return spannable;
	}

}
