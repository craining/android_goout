package com.c35.ptc.goout.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.AccountRegListener;
import com.c35.ptc.goout.interfaces.AccountThirdLoginListener;
import com.c35.ptc.goout.interfaces.ThirdAccountTokenListener;
import com.c35.ptc.goout.thirdaccounts.ThirdConfigConstants;
import com.c35.ptc.goout.thirdaccounts.ThirdController;
import com.c35.ptc.goout.util.StringUtil;

public class ThirdAuthActivity extends Activity {

	private static final String TAG = "ThirdAuthActivity";

	private WebView webView;

	private int platform = -1;

	private Handler mHandler;
	public static final int OAUTH_SUCCESS = 0x222;
	public static final int OAUTH_FAIL = 0x223;
	public static final int ACCOUNT_LOGIN_SUCCESS = 0x225;
	public static final int ACCOUNT_LOGIN_FAIL = 0x226;
	public static final int ACCOUNT_REG_SUCCESS = 0x227;
	public static final int ACCOUNT_REG_FAIL = 0x228;

	private int way = ThirdConfigConstants.THIRD_BIND;// 1从注册或登录页来的,0从绑定来的

	private GoOutController mController;

	private Account mAccountWithTokenInfo;// 获取token后，account属性在此保存

	private ProgressDialog mpDialog = null;// 等待框

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_thirdauth);

		mHandler = new AuthHandler();
		mController = GoOutController.getInstance();

		webView = (WebView) this.findViewById(R.id.webView);
		// webView.getSettings().setJavaScriptEnabled(true);
		// webView.getSettings().setLoadsImagesAutomatically(true);
		// webView.getSettings().setSupportZoom(true);
		// webView.getSettings().setBuiltInZoomControls(true);
		// webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存

		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.requestFocus();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
//		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// shouldOverrideUrlLoading

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				ThirdAuthActivity.this.setProgress(newProgress * 100);
			}
		});

		Bundle extras = getIntent().getExtras();
		String url = "";
		if (extras != null) {
			if (extras.containsKey("url")) {
				platform = extras.getInt("platform");
				way = extras.getInt("way");
				url = extras.getString("url");
				webView.loadUrl(url);

				// Map<String,String> map=new HashMap<String,String>();
				// map.put("User-Agent","Android");
				// webView.loadUrl(url,map);
			}
		}
		// webView.addJavascriptInterface(new JavaScriptInterface(), "Methods");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, final String url, Bitmap favicon) {
				GoOutDebug.e("onPageStarted", "url= " + url);

				switch (platform) {
				case ThirdConfigConstants.PLATFORM_SINA:
					if (url.contains("code=")) {
						String code = url.substring(url.indexOf("code=") + 5, url.length());
						ThirdController tC = ThirdController.getInstence();
						tC.getToken(ThirdAuthActivity.this, code, new ThirdTokenListener(), ThirdConfigConstants.PLATFORM_SINA, null, null);

					}
					break;
				case ThirdConfigConstants.PLATFORM_RENREN:
					if (url.contains("code=")) {
						String code = url.substring(url.indexOf("code=") + 5, url.length());
						ThirdController tC = ThirdController.getInstence();
						tC.getToken(ThirdAuthActivity.this, code, new ThirdTokenListener(), ThirdConfigConstants.PLATFORM_RENREN, null, null);
					}
					break;
				case ThirdConfigConstants.PLATFORM_QQWEIBO:
					if (url.contains("code=") && url.contains("&openid=") && url.contains("&openkey=")) {
						String code = url.substring(url.indexOf("code=") + 5, url.indexOf("&openid="));
						String openid = url.substring(url.indexOf("&openid=") + 8, url.indexOf("&openkey="));
						String openkey = url.substring(url.indexOf("&openkey=") + 9, url.length());
						if (!StringUtil.isNull(code)) {
							ThirdController tC = ThirdController.getInstence();
							tC.getToken(ThirdAuthActivity.this, code, new ThirdTokenListener(), ThirdConfigConstants.PLATFORM_QQWEIBO, openid, openkey);
						} else {
							mHandler.sendEmptyMessage(OAUTH_FAIL);
						}
					}
					break;
				default:
					break;
				}
			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
				GoOutDebug.e(TAG, "onReceivedSslError!!!!");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// view.loadUrl("javascript:window.Methods.getHTML('<div>'+document.getElementById('oauth_pin').innerHTML+'</div>');");
				super.onPageFinished(view, url);
			}

			// @Override
			// public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// // view.loadUrl(url);
			// return true;
			// // return super.shouldOverrideUrlLoading(view, url);
			// }

			// @Override
			// public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// // TODO Auto-generated method stub
			// return super.shouldOverrideKeyEvent(view, event);
			// }

		});
	}

	/**
	 * 跳到当前页
	 * 
	 * @Description:
	 * @param context
	 * @param url
	 * @param platform
	 * @param way
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public static void startOauthActivity(Activity context, String url, int platform, int way, int requestCode) {
		Intent intent2 = new Intent();
		Bundle extras2 = new Bundle();
		extras2.putString("url", url);
		extras2.putInt("platform", platform);
		extras2.putInt("way", way);
		intent2.setClass(context, ThirdAuthActivity.class);
		intent2.putExtras(extras2);
		context.startActivityForResult(intent2, requestCode);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			// if (webView.canGoBack()) {
			// webView.goBack();
			// return true;
			// }
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Handler
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-19
	 */
	private class AuthHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case OAUTH_SUCCESS:
				afterOathSuccess();
				break;
			case OAUTH_FAIL:
				switch (way) {
				case ThirdConfigConstants.THIRD_BIND:
					Toast.makeText(ThirdAuthActivity.this, R.string.bind_fail, Toast.LENGTH_SHORT).show();
					break;
				case ThirdConfigConstants.THIRD_LOGIN:
					Toast.makeText(ThirdAuthActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
					break;
				case ThirdConfigConstants.THIRD_REG:
					Toast.makeText(ThirdAuthActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
				setResult(RESULT_CANCELED);
				finish();
				break;

			case ACCOUNT_LOGIN_SUCCESS:
				Toast.makeText(ThirdAuthActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
				break;
			case ACCOUNT_LOGIN_FAIL:
				// GoOutDebug.e(TAG, "ACCOUNT_LOGIN_FAIL");
				Toast.makeText(ThirdAuthActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				finish();
				break;
			case ACCOUNT_REG_SUCCESS:
				Toast.makeText(ThirdAuthActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
				break;
			case ACCOUNT_REG_FAIL:
				// 注册失败
				int errorCode = msg.arg1;
				// GoOutDebug.e(TAG, "errorCode=" + errorCode);
				if (errorCode == GoOutConstants.ERROR_CODE_CODE_ERROR) {
					Toast.makeText(ThirdAuthActivity.this, R.string.register_code_error, Toast.LENGTH_SHORT).show();
				} else if (errorCode == GoOutConstants.ERROR_CODE_MOBILE_ALREADY_BIND) {
					Toast.makeText(ThirdAuthActivity.this, R.string.register_same_mobile, Toast.LENGTH_SHORT).show();
				} else if (errorCode == GoOutConstants.ERROR_CODE_THIRD_ALREADY_REGISTER) {
					Toast.makeText(ThirdAuthActivity.this, R.string.register_same_third, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ThirdAuthActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
				}
				setResult(RESULT_CANCELED);
				finish();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 获取token成功后的操作，
	 * 
	 * @Description:
	 * @param way
	 *            绑定、登录、还是注册
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	private void afterOathSuccess() {
		switch (way) {
		case ThirdConfigConstants.THIRD_BIND:
			// 绑定成功
			// 入库
			if (mController.insertAccount(ThirdAuthActivity.this, mAccountWithTokenInfo)) {
				Toast.makeText(ThirdAuthActivity.this, R.string.bind_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ThirdAuthActivity.this, R.string.bind_fail, Toast.LENGTH_SHORT).show();
			}
			setResult(RESULT_OK);
			finish();
			break;
		case ThirdConfigConstants.THIRD_LOGIN:
			// 走登录
			// 弹窗
			// GoOutDebug.e(TAG, "ThirdConfigConstants.THIRD_LOGIN");
			mpDialog = ProgressDialog.show(ThirdAuthActivity.this, null, getString(R.string.login_wait_hint), true, false);
			mController.loginAccountThird(mAccountWithTokenInfo.getThirdId(), mAccountWithTokenInfo.getAccountType(), new LoginAccountThirdListener());
			break;

		case ThirdConfigConstants.THIRD_REG:
			// 走注册
			// 弹窗
			mpDialog = ProgressDialog.show(ThirdAuthActivity.this, null, getString(R.string.register_wait_hint), true, false);
			mController.regAccountThird(mAccountWithTokenInfo.getThirdId(), mAccountWithTokenInfo.getAccountType(), mAccountWithTokenInfo.getAccountName(), new RegAccountThirdListener());
			break;

		default:
			break;
		}
	}

	/**
	 * 账户注册监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-18
	 */
	private class RegAccountThirdListener extends AccountRegListener {

		@Override
		public void regAccountFinished(Account account) {
			super.regAccountFinished(account);
			// 注册成功
			account.setIsLogin(Account.ACCOUNT_IS_LOGIN);
			saveAccountInfo(account, true);
		}

		@Override
		public void regAccountFailed(int errorCode) {
			super.regAccountFailed(errorCode);
			Message msg = new Message();
			msg.what = ACCOUNT_REG_FAIL;
			msg.arg1 = errorCode;
			GoOutDebug.e(TAG, "errorCode=" + errorCode);
			mHandler.sendMessage(msg);
		}

	}

	/**
	 * 第三方账户都登录时的响应
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-19
	 */
	private class LoginAccountThirdListener extends AccountThirdLoginListener {

		@Override
		public void accountLoginSuccess(Account account) {
			super.accountLoginSuccess(account);
			// 登录成功
			account.setIsLogin(Account.ACCOUNT_IS_LOGIN);
			saveAccountInfo(account, false);
		}

		@Override
		public void accountLoginFail() {
			super.accountLoginFail();
			mHandler.sendEmptyMessage(ACCOUNT_LOGIN_FAIL);
		}

	}

	/**
	 * 获取token的响应
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-18
	 */
	private class ThirdTokenListener extends ThirdAccountTokenListener {

		@Override
		public void getTokenSuccess(Account account) {
			super.getTokenSuccess(account);
			mAccountWithTokenInfo = new Account();
			mAccountWithTokenInfo = account;
			mHandler.sendEmptyMessage(OAUTH_SUCCESS);
		}

		@Override
		public void getTokenFail() {
			super.getTokenFail();

			mHandler.sendEmptyMessage(OAUTH_FAIL);

		}
	}

	/**
	 * 保存账户信息（入库）
	 * 
	 * @Description:
	 * @param account
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	private void saveAccountInfo(Account account, boolean isReg) {

		mAccountWithTokenInfo.setIsLogin(account.getIsLogin());
		mAccountWithTokenInfo.setUid(account.getUid());
		mAccountWithTokenInfo.setBindMobile(account.getBindMobile());
		// 入库
		if (mController.insertAccount(ThirdAuthActivity.this, mAccountWithTokenInfo)) {
			mHandler.sendEmptyMessage(isReg ? ACCOUNT_REG_SUCCESS : ACCOUNT_LOGIN_SUCCESS);
		} else {
			mHandler.sendEmptyMessage(isReg ? ACCOUNT_REG_FAIL : ACCOUNT_LOGIN_FAIL);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if ((mpDialog != null) && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			// webView.destroy();
			clearCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除缓存
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-22
	 */
	private void clearCache() {

		// WebView cookies清理：

		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		CookieManager.getInstance().removeSessionCookie();
		//
		// // 另外,清理cache 和历史记录的方法：
		// webView.clearCache(true);
		// webView.clearHistory();

		// File file = CacheManager.getCacheFileBaseDir();
		// if (file != null && file.exists() && file.isDirectory()) {
		// for (File item : file.listFiles()) {
		// item.delete();
		// }
		// file.delete();
		// }
		//
		// deleteDatabase("webview.db");
		// deleteDatabase("webviewCache.db");
	}
}
