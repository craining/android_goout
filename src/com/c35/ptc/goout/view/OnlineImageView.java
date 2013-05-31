package com.c35.ptc.goout.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.interfaces.ViewOnlineImageListener;
import com.c35.ptc.goout.util.FileManager;

/**
 * 自定义加载网络图片的View
 * 
 * @author ZGY
 * 
 */
public class OnlineImageView extends LinearLayout {

	// private String IMAGES_STORE_PATH = "/mnt/sdcard/.goout/images/";//图片缓存目录
	private static final String TAG = "OnlineImageView";

	private ImageView imgShow;// 显示图片
	private TextView textPercent;// 显示加载百分比
	private ProgressBar processBar;// 显示进度
	private String imageUrl;
	private Context mContext;

	private ViewOnlineImageListener onlineImageViewlistener;

	private Handler uiHandler = new UiHandler();
	// private static final int MSG_UPDATE_PROGRESS = 0x500;// 更新进度
	private static final int MSG_LOAD_IMAGE_START = 0x501;// 开始加载
	private static final int MSG_LOAD_IMAGE_END = 0x502;// 结束加载
	private static final int MSG_LOAD_IMAGE_FAIL = 0x503;// 加载失败

	/**
	 * 不同尺寸的图标
	 */
	private static final int[] SIZE_SCHOOL_PHOTO_LOGO = { 77, 57 };// 院校图片缩略图
	// private static final int[] SIZE_SCHOOL_PHOTO_LOGO = { 60, 50 };// 院校图片缩略图
	private static final int[] SIZE_SCHOOL_LOGO = { 56, 56 };// (院校LOGO图片)
	private static final int[] SIZE_SCHOOL_PHOTO = { 427, 355 };// (院校图片)
	private static final int[] SIZE_PROJECT_PHOTO = { 480, 285 };// (项目图片)
	private static final int[] SIZE_PUBLISHER_LOGO = { 69, 69 };// (发布者logo)

	/**
	 * 不同大小的加载比例文字
	 */
	private static final int TEXTSIZE_LOADING_SMALL = 12;
	private static final int TEXTSIZE_LOADING_BIG = 30;

	public static final int PIC_PROJECT_PHOTO = 1;// (项目图片)
	public static final int PIC_PUBLISHER_LOGO = 2;// (发布者logo)
	public static final int PIC_SCHOOL_LOGO = 3;// (院校LOGO图片)
	public static final int PIC_SCHOOL_PHOTO = 4;// (院校图片)
	public static final int PIC_SCHOOL_PHOTO_LOGO = 5;// 院校图片缩略图

	private int[] showPicSize = { 77, 57 };// 要显示的图片大小

	private boolean boolRoundCorner = false;// 圆角与否

	private int[] imageViewSize;//ImageView的大小，用于圆角图片的显示

	public OnlineImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OnlineImageView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	private void init(Context context) {
		if (mContext == null) {
			mContext = context;
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			ViewGroup main = (ViewGroup) inflater.inflate(R.layout.imageview_online, null);
			imgShow = (ImageView) main.findViewById(R.id.img_imageonline);
			textPercent = (TextView) main.findViewById(R.id.text_imageonline);
			processBar = (ProgressBar) main.findViewById(R.id.process_imageonline);

			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			main.measure(w, h);
			imageViewSize = new int[] { main.getMeasuredWidth(), main.getMeasuredHeight() };
			GoOutDebug.e(TAG, "imageViewSize w = " + imageViewSize[0] + " imageViewSize h = " + imageViewSize[1]);

			this.addView(main);
		}
	}

	/**
	 * 填充图片
	 * 
	 * @Description:
	 * @param url
	 * @param picSize
	 *            大图 {@link #PIC_SEZE_HIGH} <br/>
	 *            小图 {@link #PIC_SEZE_SMALL} <br/>
	 *            中图{@link #PIC_SEZE_MIDDLE}
	 * @param isRoundCorner
	 *            (是否是圆角图片)
	 * @param fitXy
	 *            (拉伸与否)
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-15
	 */
	public void setImageViewUrl(String url, int picSize, boolean isRoundCorner, ImageView.ScaleType scaleType) {
		if (mContext == null) {
			return;
		}
		this.boolRoundCorner = isRoundCorner;

		if (url != null && url.length() > 0) {
			imgShow.setScaleType(scaleType);
			// 根据图片大小，选择加载时不同尺寸的默认背景图片
			switch (picSize) {
			case PIC_PROJECT_PHOTO:
				showPicSize = SIZE_PROJECT_PHOTO;
				processBar.setBackgroundResource(R.drawable.online_image_loading_project_photo);
				textPercent.setTextSize(TEXTSIZE_LOADING_BIG);
				break;
			case PIC_PUBLISHER_LOGO:
				showPicSize = SIZE_PUBLISHER_LOGO;
				processBar.setBackgroundResource(R.drawable.online_image_loading_publisher_logo);
				textPercent.setTextSize(TEXTSIZE_LOADING_SMALL);
				break;
			case PIC_SCHOOL_LOGO:
				showPicSize = SIZE_SCHOOL_LOGO;
				processBar.setBackgroundResource(R.drawable.online_image_loading_school_logo);
				textPercent.setTextSize(TEXTSIZE_LOADING_SMALL);
				break;
			case PIC_SCHOOL_PHOTO:
				showPicSize = SIZE_SCHOOL_PHOTO;
				processBar.setBackgroundResource(R.drawable.online_image_loading_school_photo);
				textPercent.setTextSize(TEXTSIZE_LOADING_BIG);
				break;
			case PIC_SCHOOL_PHOTO_LOGO:
				showPicSize = SIZE_SCHOOL_PHOTO_LOGO;
				GoOutDebug.e(TAG, "url=" + url + "   sizex=" + showPicSize[0] + "  sizeY=" + showPicSize[1]);
				processBar.setBackgroundResource(R.drawable.online_image_loading_school_photo);
				textPercent.setTextSize(TEXTSIZE_LOADING_SMALL);
				break;
			default:
				break;
			}

			imageUrl = url;
			loadFromServerOrLocal();
		}
	}

	// /**测试图片时用的
	// */
	// public void setImageViewResource(int src, int picSize, boolean isRoundCorner) {
	// if (mContext == null) {
	// return;
	// }
	// imgShow.setImageResource(src);
	// imgShow.setVisibility(View.VISIBLE);
	// textPercent.setVisibility(View.GONE);
	// processBar.setVisibility(View.GONE);
	// }

	/**
	 * 判断是否下载图片，则直接显示
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-7
	 */
	private void loadFromServerOrLocal() {
		GoOutDebug.e(TAG, "show url=" + imageUrl);
		// 若已经下载下来了，则直接显示，否则下载
		File imageFile = new File(getFileCacheDir() + imageUrl.hashCode());
		if (imageFile.exists()) {
			GoOutDebug.e(TAG, "Image is already exist");
			LoadFromLocal(imageFile);
		} else {
			GoOutDebug.v(TAG, "load image url: " + imageUrl);
			LoadFromServer();
		}
		// test
		// LoadImage(imageUrl);
	}

	/**
	 * 加载图片
	 * 
	 * @Description:
	 * @param url
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	private void LoadFromServer() {
		new Thread() {

			public void run() {
				// 开始加载
				Message msg2 = new Message();
				msg2.what = MSG_LOAD_IMAGE_START;
				uiHandler.sendMessage(msg2);

				boolean isLoad = loadImageFromUrl(mContext, imageUrl);

				if (isLoad) {
					LoadFromLocal(new File(getFileCacheDir() + imageUrl.hashCode()));
				} else {
					uiHandler.sendEmptyMessage(MSG_LOAD_IMAGE_FAIL);
					GoOutDebug.e(TAG, "Load server Image Fail:" + imageUrl.toString());
				}
			}
		}.start();
	}

	/**
	 * 加载本地图片
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-28
	 */
	private void LoadFromLocal(File imageFile) {
		Bitmap bitmap = null;
		// bitmap = BitmapFactory.decodeFile(imageFile.toString());
		bitmap = readBitmapAutoSize(imageFile.toString(), showPicSize[0], showPicSize[1]);

		GoOutDebug.v(TAG, "show image path: " + imageFile.toString());
		// 加载结束
		if (bitmap != null) {

			if (boolRoundCorner) {
				bitmap = getRoundedCornerBitmap(bitmap, imageViewSize[0], imageViewSize[0]);
			}

			if (bitmap != null) {
				Message msg = new Message();
				msg.what = MSG_LOAD_IMAGE_END;
				msg.obj = bitmap;
				uiHandler.sendMessage(msg);
			} else {
				uiHandler.sendEmptyMessage(MSG_LOAD_IMAGE_FAIL);
				GoOutDebug.e(TAG, "Load local Image Null:" + imageFile.toString());
			}
		} else {
			//还是空，删除本地文件！
			imageFile.delete();
			uiHandler.sendEmptyMessage(MSG_LOAD_IMAGE_FAIL);
			GoOutDebug.e(TAG, "Load local Image Null:" + imageFile.toString());
		}
	}

	/**
	 * handler接收器，改变ui
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-15
	 */
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_LOAD_IMAGE_FAIL:
				// 加载失败，将加载进度去掉
				
				imgShow.setVisibility(View.GONE);
				processBar.setVisibility(View.VISIBLE);
				processBar.setProgress(0);
				textPercent.setVisibility(View.GONE);
				if (onlineImageViewlistener != null) {
					onlineImageViewlistener.OnImageLoadFail(imageUrl);
				}
				break;
			case MSG_LOAD_IMAGE_START:
				// 开始加载
				// uiHandler.removeMessages(MSG_LOAD_IMAGE_START);
				GoOutDebug.v(TAG, "reset");
				textPercent.setVisibility(View.VISIBLE);
				processBar.setVisibility(View.VISIBLE);
				processBar.setProgress(0);
				imgShow.setVisibility(View.GONE);
				textPercent.setText("0%");
				if (onlineImageViewlistener != null) {
					onlineImageViewlistener.OnImageLoadStart(imageUrl);
				}
				break;
			case MSG_LOAD_IMAGE_END:
				// 加载结束
				// uiHandler.removeMessages(MSG_LOAD_IMAGE_END);
				Bitmap bitmap = (Bitmap) msg.obj;
				if (bitmap != null) {
					GoOutDebug.v(TAG, "setImageBitmap NOT null");
					imgShow.setImageBitmap(bitmap);
					imgShow.setVisibility(View.VISIBLE);
					textPercent.setVisibility(View.GONE);
					processBar.setVisibility(View.GONE);
					if (onlineImageViewlistener != null) {
						onlineImageViewlistener.OnImageLoadSuccess(imageUrl);
					}
				} else {
					uiHandler.sendEmptyMessage(MSG_LOAD_IMAGE_FAIL);
					// textPercent.setText("load fail");
					if (onlineImageViewlistener != null) {
						onlineImageViewlistener.OnImageLoadFail(imageUrl);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 更新加载进度显示
	 */
	private Handler percentHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what > 0) {
				if (msg.what == 100) {
					textPercent.setVisibility(View.GONE);
				} else {
					processBar.setProgress(msg.what);
					textPercent.setText(msg.what + "%");
				}
			}

		}
	};

	/**
	 * 下载图片
	 * 
	 * @Description:
	 * @param context
	 * @param imageUrl
	 * @param saveUrl
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	private boolean loadImageFromUrl(Context context, String imageUrl) {

		InputStream is = null;

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(imageUrl);
		HttpResponse response;
		try {
			response = client.execute(get);

			HttpEntity entity = response.getEntity();
			float length = entity.getContentLength();

			is = entity.getContent();
			// GoOutDebug.v(TAG, "loadImageFromUrl");
			FileOutputStream fos = null;
			if (is != null) {
				GoOutDebug.v(TAG, "save image path: " + getFileCacheDir() + imageUrl.hashCode());
				fos = new FileOutputStream(getFileCacheDir() + imageUrl.hashCode());
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
				int newPercent = 0, oldPercent = 0;
				while ((ch = is.read(buf)) != -1) {
					fos.write(buf, 0, ch);
					count += ch;
					newPercent = (int) (count * 100 / length);
					if (newPercent > oldPercent) {
						percentHandler.sendEmptyMessage(newPercent);
					}
					oldPercent = newPercent;
				}
			}
			fos.flush();
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnlineImageViewListener(ViewOnlineImageListener listener) {
		this.onlineImageViewlistener = listener;
	}

	// /**
	// * 设置显示加载比例文字的大小
	// *
	// * @param size
	// */
	// public void setTextSizePixel(int size) {
	// if (textPercent != null) {
	// textPercent.setTextSize(size);
	// }
	// }

	/**
	 * 获得缓存图片的路径
	 * 
	 * @Description:
	 * @param context
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-6
	 */
	private String getFileCacheDir() {
		return FileManager.getSaveFilePath(mContext);
	}

 

	/**
	 * 读取本地图片的bitmap
	 * 
	 * @Description:
	 * @param filePath
	 * @param outWidth
	 * @param outHeight
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public static Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth, outHeight);
			return BitmapFactory.decodeStream(bs, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static BitmapFactory.Options setBitmapOption(String file, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;

		GoOutDebug.e(TAG, "outWidth=" + outWidth + "  outHeight=" + outHeight);

		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;

		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	/**
	 * 获得圆角图片
	 * 
	 * @Description:
	 * @param bitmap
	 * @param roundPx
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// 若读取图片的宽度或高度小于ImageView的宽度或高度，则对图片进行放大
		if (w < width || h < height) {
			Matrix matrix = new Matrix();
			matrix.postScale((float) width / w, (float) height / h); // 长和宽放大缩小的比例
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		// GoOutDebug.e(TAG, "w = " + output.getWidth() + "   h = " + output.getHeight());
		// 创建一个新的bitmap，然后在bitmap里创建一个圆角画布，将之前的图片画在里面。
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, 10, 10, paint);// 圆角平滑度
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
