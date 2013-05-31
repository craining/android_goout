package com.c35.ptc.goout.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.AccountLoginListener;
import com.c35.ptc.goout.thirdaccounts.ThirdConfigConstants;
import com.c35.ptc.goout.util.MD5Util;

/**
 * 没有账户时的登录页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-15
 */
public class AccountLoginActivity extends Activity implements OnClickListener {

	private static final String TAG = "AccountLoginActivity";

	private ImageView imgBack;
	private Button btnRegister;
	private EditText editName;
	private EditText editPwd;
	private Button btnLogin;
	private LinearLayout layoutLoginSinaWeibo;
	private LinearLayout layoutLoginQqWeibo;
	private LinearLayout layoutLoginRenrenWeibo;

	private GoOutController mController;

	private Handler uiHandler;
	private static final int MSG_LOGIN_SUCCESS = 0x333;// 登录成功
	private static final int MSG_LOGIN_FAIL = 0x335;// 登录失败
	private ProgressDialog mpDialog = null;// 等待框

	private static final int REAUEST_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_login);

		mController = GoOutController.getInstance();
		uiHandler = new UiHandler();

		imgBack = (ImageView) findViewById(R.id.img_accountlogin_back);
		btnRegister = (Button) findViewById(R.id.btn_accountlogin_register);
		editName = (EditText) findViewById(R.id.edit_accountlogin_name);
		btnLogin = (Button) findViewById(R.id.btn_accountlogin_login);
		editPwd = (EditText) findViewById(R.id.edit_accountlogin_pwd);
		layoutLoginSinaWeibo = (LinearLayout) findViewById(R.id.layout_accountlogin_sina);
		layoutLoginQqWeibo = (LinearLayout) findViewById(R.id.layout_accountlogin_qqweibo);
		layoutLoginRenrenWeibo = (LinearLayout) findViewById(R.id.layout_accountlogin_renren);

		imgBack.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		layoutLoginSinaWeibo.setOnClickListener(this);
		layoutLoginQqWeibo.setOnClickListener(this);
		layoutLoginRenrenWeibo.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_accountlogin_back:
			finish();
			break;
		case R.id.btn_accountlogin_register:
			// 注册页
			Intent i = new Intent(AccountLoginActivity.this, AccountRegisterActivity.class);
			i.putExtra("type", AccountRegisterActivity.TYPE_REGISTER);
			startActivity(i);
			finish();
			break;
		case R.id.btn_accountlogin_login:
			// 登录
			if (!TextUtils.isEmpty(editPwd.getText()) && !TextUtils.isEmpty(editName.getText().toString())) {
				mpDialog = ProgressDialog.show(AccountLoginActivity.this, null, getString(R.string.login_wait_hint), true);
				mpDialog.setCancelable(false);
				mController.loginAccount(MD5Util.getMD5String(editPwd.getText().toString()), editName.getText().toString(), new LoginAccountListener());
			} else {
				Toast.makeText(AccountLoginActivity.this, R.string.login_null, Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.layout_accountlogin_sina:
			ThirdAuthActivity.startOauthActivity(AccountLoginActivity.this, ThirdConfigConstants.Sina_Authorize2, ThirdConfigConstants.PLATFORM_SINA, ThirdConfigConstants.THIRD_LOGIN, REAUEST_CODE);
			break;
		case R.id.layout_accountlogin_qqweibo:
			ThirdAuthActivity.startOauthActivity(AccountLoginActivity.this, ThirdConfigConstants.QQWEIBO_Authorize_2, ThirdConfigConstants.PLATFORM_QQWEIBO, ThirdConfigConstants.THIRD_LOGIN, REAUEST_CODE);
			break;
		case R.id.layout_accountlogin_renren:
			ThirdAuthActivity.startOauthActivity(AccountLoginActivity.this, ThirdConfigConstants.Renren_Authorize2, ThirdConfigConstants.PLATFORM_RENREN, ThirdConfigConstants.THIRD_LOGIN, REAUEST_CODE);
			break;
		default:
			break;
		}
	}

	/**
	 * Handler
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-18
	 */
	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_LOGIN_SUCCESS:
				mpDialog.dismiss();
				Toast.makeText(AccountLoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
				finish();
				break;
			case MSG_LOGIN_FAIL:
				mpDialog.dismiss();
				Toast.makeText(AccountLoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 登录账号的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-18
	 */
	private class LoginAccountListener extends AccountLoginListener {

		@Override
		public void accountLoginSuccess(Account account) {
			super.accountLoginSuccess(account);
			GoOutDebug.e(TAG, "accountLoginSuccess");
			account.setIsLogin(Account.ACCOUNT_IS_LOGIN);

			mController.insertAccount(AccountLoginActivity.this, account);
			uiHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
		}

		@Override
		public void accountLoginFail() {
			super.accountLoginFail();
			GoOutDebug.e(TAG, "accountLoginFail");
			uiHandler.sendEmptyMessage(MSG_LOGIN_FAIL);
		}

	}

	/**
	 * 授权页的返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 登录成功了，自动退出登录页
			finish();
		}
	}

}
