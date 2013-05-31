package com.c35.ptc.goout.view;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewLetterListListener;

/**
 * 自定义view，字母快速检索
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */

public class LetterListView extends View {

	private static final String TAG = "LetterListView";

	private ViewLetterListListener onTouchingLetterChangedListener;
	private String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;
	private Paint paint = new Paint();
	private boolean showBkg = false;// 是否按下字母列表
	private String colorOnLayoutSelected = "#20000000"; // 字母列表按下时的颜色（此处仅改透明度）
	private String colorOnLetterSelected = "#FFFFFF"; // 字母被选中时的颜色

	private Rect rectTemp = new Rect();
	private int staturesBarHeight = 38;// 默认状态栏高度，用到时需重新获取
	private int viewTopYonScreen;

	private Bitmap selectedLetterBg;// 选中时字母背景

	private int letterTextSize = 18;// 字体的大小

	public LetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		selectedLetterBg = readBitMap(R.drawable.bg_sort_letter_selected);
	}

	public LetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		selectedLetterBg = readBitMap(R.drawable.bg_sort_letter_selected);
	}

	public LetterListView(Context context) {
		super(context);
		selectedLetterBg = readBitMap(R.drawable.bg_sort_letter_selected);
	}

	/**
	 * 设置字母列表按下时的背景色
	 * 
	 * @Description:
	 * @param colorOnLayoutSelected
	 *            如："#40000000"，可包含透明度
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public void setColorOnLayoutSelected(String colorOnLayoutSelected) {
		this.colorOnLayoutSelected = colorOnLayoutSelected;
	}

	/**
	 * 设置字母被选中的字体颜色
	 * 
	 * @Description:
	 * @param colorOnLetterSelected
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-5
	 */
	public void setColorOnLetterSelected(String colorOnLetterSelected) {
		this.colorOnLetterSelected = colorOnLetterSelected;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画按下字母条时的背景，显示按下效果
		if (showBkg) {
			canvas.drawColor(Color.parseColor(colorOnLayoutSelected));
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / items.length;
		GoOutDebug.e(TAG, "singleHeight=" + singleHeight);
		GoOutDebug.e(TAG, "width=" + width);
		GoOutDebug.e(TAG, "letterTextSize=" + letterTextSize);
		for (int i = 0; i < items.length; i++) {
			paint.setTextSize(letterTextSize);// 设置字体大小
			paint.setColor(Color.GRAY);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			float xPos = width / 2 - paint.measureText(items[i]) / 2;
			// GoOutDebug.e(TAG, "xPos=" + xPos);
			float yPos = singleHeight * i + singleHeight;
			// 字母条上的字母被选中时的效果
			if (i == choose) {
				paint.setColor(Color.parseColor(colorOnLetterSelected));
				paint.setFakeBoldText(true);
				if (selectedLetterBg != null) {
					canvas.drawBitmap(selectedLetterBg, xPos - 8, yPos - singleHeight, paint);
				}

			}
			// canvas.drawRect(0, singleHeight * i, width, yPos, paint);
			paint.setFakeBoldText(false);
			canvas.drawText(items[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		final int action = event.getAction();
		final float y = event.getY();
		final float x = event.getX();
		final int oldChoose = choose;
		final int c = (int) (y / getHeight() * items.length);
		final int width = getWidth();

		// 获得状态栏高度
		getWindowVisibleDisplayFrame(rectTemp);
		staturesBarHeight = rectTemp.top;
		// GoOutDebug.e(TAG, " layoutSortLetter.getWindowVisibleDisplayFrame.TOP =" + rectTemp.top);
		// 获得view所在屏幕的y坐标
		getGlobalVisibleRect(rectTemp);
		viewTopYonScreen = rectTemp.top;
		// GoOutDebug.e(TAG, " layoutSortLetter.getGlobalVisibleRect.TOP =" + rectTemp.top);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			GoOutDebug.e(TAG, "MotionEvent.ACTION_DOWN");
			showBkg = true;
			if (oldChoose != c && onTouchingLetterChangedListener != null) {
				if (c > -1 && c < items.length) {
					onTouchingLetterChangedListener.onTouchingLetterChanged(items[c], y + viewTopYonScreen - staturesBarHeight, x, width);
					choose = c;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			GoOutDebug.e(TAG, "MotionEvent.ACTION_MOVE");
			if (oldChoose != c && onTouchingLetterChangedListener != null) {
				if (c > -1 && c < items.length) {
					onTouchingLetterChangedListener.onTouchingLetterChanged(items[c], y + viewTopYonScreen - staturesBarHeight, x, width);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			GoOutDebug.e(TAG, "MotionEvent.ACTION_UP");
			showBkg = false;
			choose = -1;
			if(onTouchingLetterChangedListener!=null) {
				onTouchingLetterChangedListener.onTouchingLetterEnd();
			}
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * 设置监听器
	 * 
	 * @Description:
	 * @param onTouchingLetterChangedListener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setOnTouchingLetterChangedListener(ViewLetterListListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @Description:
	 * @param resId
	 * @return
	 * @see: 考虑是否输出特定大小的bitmap，铺满字母的整个背景
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-29
	 */
	public Bitmap readBitMap(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}
