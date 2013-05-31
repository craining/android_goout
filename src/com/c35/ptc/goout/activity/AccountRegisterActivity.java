package com.c35.ptc.goout.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.AccountBindPhoneListener;
import com.c35.ptc.goout.interfaces.AccountMarkPhoneListener;
import com.c35.ptc.goout.interfaces.AccountRegListener;
import com.c35.ptc.goout.interfaces.SendCodeListener;
import com.c35.ptc.goout.thirdaccounts.ThirdConfigConstants;
import com.c35.ptc.goout.util.MD5Util;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.ViewUtil;

/**
 * 注册站内账户页
 * 
 * 站外账户绑定手机页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-15
 */
public class AccountRegisterActivity extends Activity implements OnClickListener {

	private static final String TAG = "AccountRegisterActivity";

	private Button btnLoginReg;// （注册时为“登录”；绑定手机号时为“注册”）
	private TextView textTitle;
	private ImageView imgBack;
	private EditText editTel;
	// private ImageView imgTelIc;
	private Button btnGetCode;

	private RelativeLayout layoutAfterGet;
	// private ImageView imgCodeIc;
	private EditText editCode;
	private Button btnCodeOk;

	private LinearLayout layoutRegisterFirst;// 设置密码之前的ui
	private LinearLayout layoutRegisterSecond;// 设置密码的ui
	private EditText editPwd;
	private Button btnSetPwd;
	private LinearLayout layoutRegisterOthres;// 其它注册方式的ui
	private LinearLayout layoutSinaWeibo;
	private LinearLayout layoutRenren;
	private LinearLayout layoutQQweibo;

	private static final int LEFT_TIME = 60;// 倒计时时间
	private int timeLeft = 0;// 倒计时
	private Handler uiHandler;
	private static final int MSG_TIME_TICK = 0x111;// 按钮的倒计时更新
	private static final int MSG_REQUEST_CODE_FINISH = 0x112;// 请求验证码成功
	private static final int MSG_REQUEST_CODE_FAIL = 0x113;// 请求验证码失败
	private static final int MSG_REG_SUCCESS = 0x115;// 注册成功
	private static final int MSG_REG_FAIL = 0x116;// 注册失败
	private static final int MSG_GET_CODE_FROM_MSG = 0x117;// 短信获取到验证码
	private static final int MSG_BIND_SUCCESS = 0x118;// 短信获取到验证码
	private static final int MSG_BIND_FAIL = 0x119;// 短信获取到验证码
	private static final int MSG_MARK_SUCCESS = 0x121;// 短信获取到验证码
	private static final int MSG_MARK_FAIL = 0x122;// 短信获取到验证码

	public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private SmsReceiver smsReceiver;
	private boolean canReceive = false;// 是否拦截验证码

	public static final int TYPE_REGISTER = 1;// 注册站内账户
	public static final int TYPE_BIND_MOBILE = 2;// 站外账户绑定手机

	private boolean mIsRegister = true;// 注册账户,否则为绑定手机

	private GoOutController mController;

	private ProgressDialog mpDialog = null;// 注册等待进度框

	private String nowCode = "";// 当前的验证码
	private String nowMobile = "";// 当前手机号

	private static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_register);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			switch (b.getInt("type")) {
			case TYPE_REGISTER:
				mIsRegister = true;
				break;
			case TYPE_BIND_MOBILE:
				mIsRegister = false;
				break;

			default:
				break;
			}
		}

		uiHandler = new UiHandler();
		smsReceiver = new SmsReceiver();
		mController = GoOutController.getInstance();

		// 监听电话
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SMS_ACTION);
		intentFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(smsReceiver, intentFilter);

		textTitle = (TextView) findViewById(R.id.text_accountregister_title);
		btnLoginReg = (Button) findViewById(R.id.btn_accountregister_login);
		imgBack = (ImageView) findViewById(R.id.img_accountregister_back);
		editTel = (EditText) findViewById(R.id.edit_accountregister_tel);
		// imgTelIc = (ImageView) findViewById(R.id.img_accountregister_telic);
		btnGetCode = (Button) findViewById(R.id.btn_accountregister_getcode);
		layoutAfterGet = (RelativeLayout) findViewById(R.id.layout_accountregister_afterget);
		// imgCodeIc = (ImageView) findViewById(R.id.img_accountregister_codeic);
		editCode = (EditText) findViewById(R.id.edit_accountregister_code);
		btnCodeOk = (Button) findViewById(R.id.btn_accountregister_codeok);

		layoutRegisterFirst = (LinearLayout) findViewById(R.id.layout_accountregitster_first);
		layoutRegisterSecond = (LinearLayout) findViewById(R.id.layout_accountregitster_second);
		layoutRegisterOthres = (LinearLayout) findViewById(R.id.layout_accountregister_others);
		layoutRenren = (LinearLayout) findViewById(R.id.layout_accountregister_renren);
		layoutQQweibo = (LinearLayout) findViewById(R.id.layout_accountregister_qqweibo);
		layoutSinaWeibo = (LinearLayout) findViewById(R.id.layout_accountregister_sina);

		editPwd = (EditText) findViewById(R.id.edit_accountregister_pwd);
		btnSetPwd = (Button) findViewById(R.id.btn_accountregister_pwd_set);

		layoutRenren.setOnClickListener(this);
		layoutQQweibo.setOnClickListener(this);
		layoutSinaWeibo.setOnClickListener(this);
		btnLoginReg.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		btnGetCode.setOnClickListener(this);
		btnCodeOk.setOnClickListener(this);
		btnSetPwd.setOnClickListener(this);

		if (mIsRegister) {
			// 注册账号
			// btnLoginReg.setText(R.string.login);
			btnLoginReg.setVisibility(View.GONE);
			textTitle.setText(R.string.register_title);
		} else {
			// 绑定手机
			btnLoginReg.setVisibility(View.VISIBLE);
			btnLoginReg.setText(R.string.register_title);
			textTitle.setText(R.string.register_mobile);
		}

		// test
		// btnGetCode.setEnabled(true);

		editTel.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				GoOutDebug.v(TAG, "onTextChanged");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				GoOutDebug.v(TAG, "beforeTextChanged");

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 此处注意：需将edittext的长度设置成12位，若为11位的话，setError后，再次按数字，则错误提示消失。
				GoOutDebug.v(TAG, "afterTextChanged");
				if (!TextUtils.isEmpty(editTel.getText())) {
					String text = editTel.getText().toString();
					if (text.length() == 11) {
						if (StringUtil.isPhoneNumberFormat(text)) {
							btnGetCode.setEnabled(true);
							return;
						} else {
							// 提示电话号码不合法
							editTel.setError(ViewUtil.getErrorTextStyle(getApplicationContext(), getString(R.string.register_number_error)));
						}
					} else if (text.length() == 12) {
						editTel.setText(text.substring(0, 11));
						editTel.setSelection(11);
						btnGetCode.setEnabled(true);
						return;
					}
				}
				btnGetCode.setEnabled(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_accountregister_login:

			if (mIsRegister) {
				// 登录页
				finish();
				startActivity(new Intent(AccountRegisterActivity.this, AccountLoginActivity.class));
			} else {
				// 改为注册
				finish();
				Intent i = new Intent(AccountRegisterActivity.this, AccountRegisterActivity.class);
				i.putExtra("type", TYPE_REGISTER);
				startActivity(i);
			}
			break;
		case R.id.img_accountregister_back:
			finish();
			break;
		case R.id.btn_accountregister_getcode:
			// 请求验证码
			requestCode();
			break;
		case R.id.btn_accountregister_codeok:
			// 输入完成验证码
			// 如果正确
			if (!TextUtils.isEmpty(editCode.getText())) {
				if (editCode.getText().toString().equals(nowCode)) {
					codeGot();
				} else {
					// 验证码不匹配
					Toast.makeText(AccountRegisterActivity.this, R.string.register_inputcode_error, Toast.LENGTH_SHORT).show();
				}
			} else {
				// 未输入验证码
				Toast.makeText(AccountRegisterActivity.this, R.string.register_inputcode_null, Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btn_accountregister_pwd_set:
			// 设置密码，进行注册
			registerWithPwd();
			break;

		case R.id.layout_accountregister_renren:
			// 人人
			ThirdAuthActivity.startOauthActivity(AccountRegisterActivity.this, ThirdConfigConstants.Renren_Authorize2, ThirdConfigConstants.PLATFORM_RENREN, ThirdConfigConstants.THIRD_REG, REQUEST_CODE);
			break;
		case R.id.layout_accountregister_qqweibo:
			// QQ微博
			ThirdAuthActivity.startOauthActivity(AccountRegisterActivity.this, ThirdConfigConstants.QQWEIBO_Authorize_2, ThirdConfigConstants.PLATFORM_QQWEIBO, ThirdConfigConstants.THIRD_REG, REQUEST_CODE);
			break;
		case R.id.layout_accountregister_sina:
			// 新浪微博
			ThirdAuthActivity.startOauthActivity(AccountRegisterActivity.this, ThirdConfigConstants.Sina_Authorize2, ThirdConfigConstants.PLATFORM_SINA, ThirdConfigConstants.THIRD_REG, REQUEST_CODE);
			break;

		default:
			break;
		}

	}

	/**
	 * 请求验证码
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void requestCode() {
		editCode.setText("");
		// editTel.setEnabled(false);//请求验证码时，号码不可变
		// 获取验证码
		ViewUtil.hideKeyboard(AccountRegisterActivity.this, editTel);// 隐藏键盘
		mController.getCode(editTel.getText().toString(), new RequestSendCodeListener());
		// 启动重复倒计时
		canReceive = true;
		layoutAfterGet.setVisibility(View.VISIBLE);
		timeLeft = LEFT_TIME;
		StringBuilder sb = new StringBuilder();
		sb.append("（").append(timeLeft).append("）").append(getString(R.string.register_recendcode));
		btnGetCode.setText(sb.toString());
		btnGetCode.setTextColor(getResources().getColor(R.color.grey));
		uiHandler.sendEmptyMessageDelayed(MSG_TIME_TICK, 1000);
		// 灰显
		btnGetCode.setBackgroundResource(R.drawable.btn_register_d);// 后续改为背景图片
		btnGetCode.setClickable(false);
	}

	/**
	 * 设置完成密码进行注册
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void registerWithPwd() {
		if (!TextUtils.isEmpty(editPwd.getText())) {
			if (editPwd.getText().toString().length() < 6 || editPwd.getText().toString().length() > 12) {
				editPwd.setError(ViewUtil.getErrorTextStyle(AccountRegisterActivity.this, getString(R.string.register_pwd_error)));
			} else {
				// 等待进度框
				mpDialog = ProgressDialog.show(AccountRegisterActivity.this, null, getString(R.string.register_wait_hint), true);
				mpDialog.setCancelable(false);
				nowMobile = editTel.getText().toString();
				mController.regAccount(editCode.getText().toString(), MD5Util.getMD5String(editPwd.getText().toString()), nowMobile, new RegisterAccountListener());
				ViewUtil.hideKeyboard(AccountRegisterActivity.this, editPwd);
			}
		} else {
			// 密码为空
			Toast.makeText(AccountRegisterActivity.this, R.string.register_pwd_null, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取到验证码且匹配，之后提交手机信息,提交成功后验证
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	private void codeGot() {

		// 等待进度框
		mpDialog = ProgressDialog.show(AccountRegisterActivity.this, null, getString(R.string.register_wait_code_hint), true);
		mpDialog.setCancelable(false);

		// 验证码匹配
		// mark phone
		mController.markPhone(editTel.getText().toString(), PhoneUtil.getDeviceInfo(getApplicationContext()), PhoneUtil.getPhoneImei(getApplicationContext()), PhoneUtil.getPhoneImsi(getApplicationContext()), new MarkPhoneListener());
	}

	/**
	 * 验证码匹配，并上传设备信息成功后的操作
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	private void afterMark() {

		if (mIsRegister) {
			// 是注册账户，直接进入密码设置页
			showPwdSet();
		} else {
			// 是绑定手机号
			mController.bindPhone(editTel.getText().toString(), nowCode, GoOutGlobal.getInstance().getAccount().getUid(), new BindPhoneListener());
		}
	}

	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_TIME_TICK:
				// 倒计时
				uiHandler.removeMessages(MSG_TIME_TICK);
				if (timeLeft > 1) {
					timeLeft--;
					StringBuilder sb = new StringBuilder();
					sb.append("（").append(timeLeft).append("）").append(getString(R.string.register_recendcode));
					btnGetCode.setText(sb.toString());
					uiHandler.sendEmptyMessageDelayed(MSG_TIME_TICK, 1000);
				} else {
					// 灰显
					btnGetCode.setText(R.string.register_recendcode);
					btnGetCode.setBackgroundResource(R.drawable.btn_register_selector);
					btnGetCode.setClickable(true);
					btnGetCode.setTextColor(getResources().getColor(R.color.white));
				}

				break;

			case MSG_REQUEST_CODE_FINISH:
				// 请求验证码成功

				break;

			case MSG_REQUEST_CODE_FAIL:
				// 请求验证码失败
				requestCodeError(msg);
				break;

			case MSG_REG_SUCCESS:
				// 注册成功
				Toast.makeText(AccountRegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
				finish();
				break;

			case MSG_REG_FAIL:
				// 注册失败
				registerError(msg);
				dismissDlg();
				break;
			case MSG_GET_CODE_FROM_MSG:
				// 短信拦截到验证码
				editCode.setText(nowCode);
				codeGot();
				break;

			case MSG_BIND_SUCCESS:
				// 绑定成功
				Toast.makeText(AccountRegisterActivity.this, R.string.bind_success, Toast.LENGTH_SHORT).show();
				break;
			case MSG_BIND_FAIL:
				// 绑定失败
				bindError(msg);
				dismissDlg();
				break;

			case MSG_MARK_SUCCESS:
				afterMark();
				break;
			case MSG_MARK_FAIL:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_error_unknow, Toast.LENGTH_SHORT).show();
				dismissDlg();
				break;
			default:
				break;
			}
		}

		/**
		 * 绑定失败
		 * 
		 * @Description:
		 * @param msg
		 * @see:
		 * @since:
		 * @author: zhuanggy
		 * @date:2013-4-24
		 */
		private void bindError(Message msg) {
			switch (msg.arg1) {
			case GoOutConstants.ERROR_CODE_CODE_ERROR:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_code_error, Toast.LENGTH_SHORT).show();
				break;
			case GoOutConstants.ERROR_CODE_MOBILE_ALREADY_BIND:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_same_mobile, Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(AccountRegisterActivity.this, R.string.bind_fail, Toast.LENGTH_SHORT).show();
				break;
			}
		}

		/**
		 * 注册失败
		 * 
		 * @Description:
		 * @param msg
		 * @see:
		 * @since:
		 * @author: zhuanggy
		 * @date:2013-4-24
		 */
		private void registerError(Message msg) {
			switch (msg.arg1) {
			case GoOutConstants.ERROR_CODE_CODE_ERROR:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_code_error, Toast.LENGTH_SHORT).show();
				break;
			case GoOutConstants.ERROR_CODE_MOBILE_ALREADY_BIND:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_same_mobile, Toast.LENGTH_SHORT).show();
				break;
			case GoOutConstants.ERROR_CODE_THIRD_ALREADY_REGISTER:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_same_third, Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
				break;
			}
		}

		/**
		 * 请求验证码失败
		 * 
		 * @Description:
		 * @param msg
		 * @see:
		 * @since:
		 * @author: zhuanggy
		 * @date:2013-4-24
		 */
		private void requestCodeError(Message msg) {
			timeLeft = 0;//重复验证码按钮恢复
			switch (msg.arg1) {
			case GoOutConstants.ERROR_CODE_MOBILE_ALREADY_BIND:
				// Toast.makeText(AccountRegisterActivity.this, R.string.register_same_mobile,
				// Toast.LENGTH_SHORT).show();
				editTel.setError(ViewUtil.getErrorTextStyle(getApplicationContext(), getString(R.string.register_same_mobile)));
				break;

			default:
				Toast.makeText(AccountRegisterActivity.this, R.string.register_request_code_fail, Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

	/**
	 * 验证通过
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-17
	 */
	private void showPwdSet() {
		layoutRegisterFirst.setVisibility(View.GONE);
		layoutRegisterSecond.setVisibility(View.VISIBLE);
		layoutRegisterOthres.setVisibility(View.GONE);
		editPwd.requestFocus();
		dismissDlg();
	}

	/**
	 * 接收短信，用于监听验证码，并自动进入到下一步
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-16
	 */
	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			GoOutDebug.e(TAG, "SmsReceiver!!!!");

			if (canReceive && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
				SmsMessage[] msg = null;
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] pdusObj = (Object[]) bundle.get("pdus");
					msg = new SmsMessage[pdusObj.length];
					int mmm = pdusObj.length;
					for (int i = 0; i < mmm; i++)
						msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
				}

				String msgTxt = "";
				int msgLength = msg.length;
				for (int i = 0; i < msgLength; i++) {
					msgTxt += msg[i].getMessageBody();
				}

				// 获得发信人号码
				String getFromNum = "";
				for (SmsMessage currMsg : msg) {
					getFromNum = currMsg.getDisplayOriginatingAddress();
				}
				GoOutDebug.e(TAG, "from=" + getFromNum + " content=" + msgTxt);
				// 拦截到验证码短信
				if (getFromNum.contains(GoOutConstants.PHONE_NUMBER_SERVER)) {
					// Toast.makeText(AccountRegisterActivity.this, msgTxt, Toast.LENGTH_SHORT).show();
					if (msgTxt.contains(nowCode)) {
						// 包含验证码
						uiHandler.sendEmptyMessage(MSG_GET_CODE_FROM_MSG);
					}
				}
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		dismissDlg();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

	/**
	 * 请求发送验证码的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-17
	 */
	private class RequestSendCodeListener extends SendCodeListener {

		@Override
		public void sendCodeRequestFinish(String code) {
			super.sendCodeRequestFinish(code);
			GoOutDebug.e(TAG, "sendCodeRequestFinish");
			nowCode = code;
			uiHandler.sendEmptyMessage(MSG_REQUEST_CODE_FINISH);
		}

		@Override
		public void sendCodeRequestFail(int errorCode) {
			super.sendCodeRequestFail(errorCode);
			GoOutDebug.e(TAG, "sendCodeRequestFail");
			Message msg = new Message();
			msg.what = MSG_REQUEST_CODE_FAIL;
			msg.arg1 = errorCode;
			uiHandler.sendMessage(msg);
		}

	}

	/**
	 * 注册账号的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-17
	 */
	private class RegisterAccountListener extends AccountRegListener {

		@Override
		public void regAccountFinished(Account account) {
			super.regAccountFinished(account);

			// 注册成功,入库，退出
			// TODO 入库（稍后考虑密码的加密）
			account.setIsLogin(Account.ACCOUNT_IS_LOGIN);
			account.setBindMobile(nowMobile);

			mController.insertAccount(AccountRegisterActivity.this, account);
			uiHandler.sendEmptyMessage(MSG_REG_SUCCESS);
		}

		@Override
		public void regAccountFailed(int errorCode) {
			super.regAccountFailed(errorCode);
			// 注册失败
			Message msg = new Message();
			msg.what = MSG_REG_FAIL;
			msg.arg1 = errorCode;
			uiHandler.sendMessage(msg);
		}

	}

	/**
	 * 绑定手机号的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-19
	 */
	private class BindPhoneListener extends AccountBindPhoneListener {

		@Override
		public void bindSuccess() {
			super.bindSuccess();
			GoOutDebug.e(TAG, "bindSuccess");
			uiHandler.sendEmptyMessage(MSG_BIND_SUCCESS);
		}

		@Override
		public void bindFail(int errorCode) {
			super.bindFail(errorCode);
			GoOutDebug.e(TAG, "bindFail errorCode=" + errorCode);

			Message msg = new Message();
			msg.what = MSG_BIND_FAIL;
			msg.arg1 = errorCode;
			uiHandler.sendMessage(msg);
		}

	}

	/**
	 * 记录手机信息的监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-4-19
	 */
	private class MarkPhoneListener extends AccountMarkPhoneListener {

		@Override
		public void accountMarkFinished(boolean success) {
			super.accountMarkFinished(success);
			GoOutDebug.e(TAG, "accountMarkFinished");
			if (success) {
				uiHandler.sendEmptyMessage(MSG_MARK_SUCCESS);
			} else {
				uiHandler.sendEmptyMessage(MSG_MARK_FAIL);
			}

		}

		@Override
		public void accountMarkFail() {
			super.accountMarkFail();
			GoOutDebug.e(TAG, "accountMarkFail");
			uiHandler.sendEmptyMessage(MSG_MARK_FAIL);
		}

	}

	private void dismissDlg() {
		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}

	/**
	 * 授权页的返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 注册成功了，自动退出注册页
			finish();
		}
	}
}
