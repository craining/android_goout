package com.c35.ptc.goout.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewEllipsizeTextListener;

/**
 * 重写TextView，用于支持3.0以下版本两行以上的TextView末尾显示省略号
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2012-12-6
 */

public class EllipsizeTextView extends TextView {

	private static final String TAG = "EllipsizeTextView";

	private static final String ELLIPSIS = "...";// 省略号
	private ViewEllipsizeTextListener ellipsizeListeners;

	private boolean isStale;// 是否需要对文字进行处理，目前仅在超过2行时进行处理
	private boolean programmaticChange;
	private boolean isEllipsized;
	private String fullText;
	private int lines = 3;
	private float lineSpacingMultiplier = 1.0f;
	private float lineAdditionalVerticalPadding = 0.0f;
	private int titleStrLength; // 标题的长度，用于区分标题和内容的不同样式
	private int titleSize = 22;// 标题的文字大小
	private int contentSize = 20;// 内容的大小

	// (另外可抽取出标题和内容的颜色进行设置)

	public EllipsizeTextView(Context context) {
		super(context);
	}

	public EllipsizeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EllipsizeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置标题文字的大小，如“简介：”
	 * 
	 * @Description:
	 * @param titleSize
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	/**
	 * 设置内容文字的大小，如简介的内容
	 * 
	 * @Description:
	 * @param contentSize
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
	}

	/**
	 * 设置标题长度
	 * 
	 * @Description:
	 * @param titleStrLength
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setTitleStrLength(int titleStrLength) {
		this.titleStrLength = titleStrLength;
	}

	/**
	 * 设置最大行数
	 */
	public void setShowLines(int lines) {
		// this.setTextSize(20);
		if (lines <= 2) {
			isStale = false;
			this.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
			this.setLines(lines);
		} else {
			GoOutDebug.e(TAG, "setLines............................" + lines);
			isStale = true;
			this.lines = lines;
			// this.setMaxLines(lines);
			this.setEllipsize(null);// 不设置末尾省略号，用代码实现
		}

		// // 预览行数为0-2行时不做处理，当设置为3行时，开始处理末尾要显示的省略号
		// switch (maxLines) {
		// case PREVIEW_LINE_ONE:
		//
		// super.setSingleLine(true);
		//
		// break;
		// case PREVIEW_LINE_TWO:
		// isStale = false;
		// super.setSingleLine(false);
		// super.setMaxLines(2);
		// super.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
		// break;
		// case PREVIEW_LINE_THREE:
		//
		// break;
		//
		// default:
		// break;
		// }
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		this.lineAdditionalVerticalPadding = add;
		this.lineSpacingMultiplier = mult;
		super.setLineSpacing(add, mult);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before, int after) {
		super.onTextChanged(text, start, before, after);
		if (!programmaticChange && isStale) {
			fullText = text.toString();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isStale) {
			GoOutDebug.e(TAG, "EllipsizeText  onDrow............................");
			resetText();// 如果超过3行预览，则进行处理
		}
		super.onDraw(canvas);
	}

	/**
	 * 超过两行时对文字进行处理
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-6
	 */
	private void resetText() {
		// GoOutDebug.v("EllipsizeText", "EllipsizeText  resetText............................");
		// int maxLines = getMaxLines();

		String workingText = fullText;
		// GoOutDebug.v("EllipsizeText", "pre fullText =" + fullText);
		boolean ellipsized = false;
		Layout layout = createWorkingLayout(workingText);
		if (layout.getLineCount() > lines) {
			workingText = fullText.substring(0, (layout.getLineEnd(lines - 1) - 1)).trim();
			Layout layout2 = createWorkingLayout(workingText + ELLIPSIS);
			while (layout2.getLineCount() > lines && (workingText.length() - 1) > 0) {
				workingText = workingText.substring(0, workingText.length() - 1);
				layout2 = createWorkingLayout(workingText + ELLIPSIS);
			}
			ellipsized = true;
		}

		int allLength = workingText.length();
		if (allLength >= titleStrLength) {
			GoOutDebug.e(TAG, "allLength >= titleStrLength");
			if (allLength < fullText.length()) {
				// 超过的话加“...”
				workingText = workingText + ELLIPSIS;
				if (ellipsizeListeners != null) {
					ellipsizeListeners.onEllipsizeShow(true);
				}
			} else {
				if (ellipsizeListeners != null) {
					ellipsizeListeners.onEllipsizeShow(false);
				}
			}
			programmaticChange = true;
			try {
				// 对处理后的text设置显示样式，此处为标题黑色大，内容灰色小。

				SpannableStringBuilder spannable = new SpannableStringBuilder(workingText);

				ColorStateList titleColor = getResources().getColorStateList(R.color.black);// 标题的颜色
				ColorStateList contentColor = getResources().getColorStateList(R.color.dimgrey);// 内容的颜色

				TextAppearanceSpan titleAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, titleSize, titleColor, titleColor);// 标题加粗，较大
				TextAppearanceSpan contentAppearance = new TextAppearanceSpan(null, Typeface.NORMAL, contentSize, contentColor, contentColor);// 内容正常，较小

				spannable.setSpan(titleAppearance, 0, titleStrLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置标题样式
				if (allLength > titleStrLength) {
					spannable.setSpan(contentAppearance, titleStrLength, allLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置内容样式
				}
				setText(spannable);
			} finally {
				programmaticChange = false;
			}
		}

		isStale = false;
		if (ellipsized != isEllipsized) {
			isEllipsized = ellipsized;
		}
	}

	/**
	 * 返回一个布局
	 * 
	 * @Description:
	 * @param workingText
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private Layout createWorkingLayout(String workingText) {
		return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(), Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
		/**
		 * StaticLayout 参数含义：
		 * 
		 * 1.字符串子资源
		 * 
		 * 2 .画笔对象
		 * 
		 * 3.layout的宽度，字符串超出宽度时自动换行。
		 * 
		 * 4.layout的样式，有ALIGN_CENTER， ALIGN_NORMAL， ALIGN_OPPOSITE 三种。
		 * 
		 * 5.相对行间距，相对字体大小，1.5f表示行间距为1.5倍的字体高度。
		 * 
		 * 6.相对行间距，0表示0个像素。
		 * 
		 * 实际行间距等于这两者的和。
		 * 
		 * 7.还不知道是什么意思，参数名是boolean includepad。
		 * 
		 * 需要指出的是这个layout是默认画在Canvas的(0,0)点的，如果需要调整位置只能在draw之前移Canvas的起始坐标 canvas.translate(x,y);
		 */
	}

	/**
	 * 设置监听器
	 * 
	 * @Description:
	 * @param listener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-25
	 */
	public void setEllipsizeListener(ViewEllipsizeTextListener listener) {
		this.ellipsizeListeners = listener;
	}

}
