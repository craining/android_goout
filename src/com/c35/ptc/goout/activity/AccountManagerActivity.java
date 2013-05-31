package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.thirdaccounts.ThirdConfigConstants;
import com.c35.ptc.goout.util.AccountUtil;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 我的账户页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-15
 */
public class AccountManagerActivity extends Activity implements OnClickListener {

	private ImageView imgBack;
	private LinearLayout layoutAccountInfo;
	private TextView textAccountName;

	// 绑定手机
	private TextView textAccountMobile;
	private LinearLayout btnAccountMobileBind;
	private TextView textAccountMobileBind;
	// 绑定微博
	private TextView textSinaWeiBoName;
	private LinearLayout btnSinaWeiBoBind;
	private TextView textSinaWeiBoBind;
	private ImageView imgSinaWeiBoBind;
	// 绑定腾讯微博
	private TextView textQqWeiBoName;
	private LinearLayout btnQqWeiBoBind;
	private TextView textQqWeiBoBind;
	private ImageView imgQqWeiBoBind;
	// 绑定人人
	private TextView textRenrenName;
	private LinearLayout btnRenrenBind;
	private TextView textRenrenBind;
	private ImageView imgRenrenBind;

	private Button btnLogInOut;

	private GoOutController mController;
	private GoOutGlobal mGlobal;
	private boolean islogin;// 区分登录还是退出

	private ProgressDialog mpDialog = null;// 等待进度框

	private Handler uiHandler;
	private static final int MSG_ACCOUNT_EXIT_SUCCESS = 0x120;// 退出成功
	private static final int MSG_ACCOUNT_EXIT_FAIL = 0x121;// 退出失败

	private static final int REQUEST_CODE = 2;

	private boolean bindSina = false;
	private boolean bindRenren = false;
	private boolean bindQQweibo = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_manager);

		mController = GoOutController.getInstance();
		mGlobal = (GoOutGlobal) getApplicationContext();
		uiHandler = new UiHandler();

		imgBack = (ImageView) findViewById(R.id.img_accountmanager_back);
		layoutAccountInfo = (LinearLayout) findViewById(R.id.layout_accountmanager_accounts);
		textAccountName = (TextView) findViewById(R.id.text_accountmanager_accountname);
		textAccountMobile = (TextView) findViewById(R.id.text_accountmanager_mobilenum);

		btnAccountMobileBind = (LinearLayout) findViewById(R.id.layout_accountmanager_mobileadd);
		textAccountMobileBind = (TextView) findViewById(R.id.text_accountmanager_mobileadd);

		textSinaWeiBoName = (TextView) findViewById(R.id.text_accountmanager_sinaweibo_name);
		btnSinaWeiBoBind = (LinearLayout) findViewById(R.id.layout_accountmanager_sinaadd);
		textSinaWeiBoBind = (TextView) findViewById(R.id.text_accountmanager_sinaadd);
		imgSinaWeiBoBind = (ImageView) findViewById(R.id.img_accountmanager_sinaadd);

		textQqWeiBoName = (TextView) findViewById(R.id.text_accountmanager_qqweibo_name);
		btnQqWeiBoBind = (LinearLayout) findViewById(R.id.layout_accountmanager_qqweiboadd);
		textQqWeiBoBind = (TextView) findViewById(R.id.text_accountmanager_qqweiboadd);
		imgQqWeiBoBind = (ImageView) findViewById(R.id.img_accountmanager_qqweiboadd);

		textRenrenName = (TextView) findViewById(R.id.text_accountmanager_renren_name);
		btnRenrenBind = (LinearLayout) findViewById(R.id.layout_accountmanager_renrenadd);
		textRenrenBind = (TextView) findViewById(R.id.text_accountmanager_renrenadd);
		imgRenrenBind = (ImageView) findViewById(R.id.img_accountmanager_renrenadd);

		btnLogInOut = (Button) findViewById(R.id.btn_accountmanager_loginout);

		imgBack.setOnClickListener(this);
		btnAccountMobileBind.setOnClickListener(this);
		btnSinaWeiBoBind.setOnClickListener(this);
		btnQqWeiBoBind.setOnClickListener(this);
		btnRenrenBind.setOnClickListener(this);
		btnLogInOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_accountmanager_back:
			finish();
			break;
		case R.id.layout_accountmanager_mobileadd:
			// 绑定手机号
			Intent i = new Intent(AccountManagerActivity.this, AccountRegisterActivity.class);
			i.putExtra("type", AccountRegisterActivity.TYPE_BIND_MOBILE);
			startActivity(i);
			break;
		case R.id.layout_accountmanager_sinaadd:
			// 绑定新浪微博
			bindSina();

			break;
		case R.id.layout_accountmanager_qqweiboadd:
			// 绑定QQ微博
			bindQQweibo();
			break;
		case R.id.layout_accountmanager_renrenadd:
			// 绑定人人
			bindRenRen();
			break;
		case R.id.btn_accountmanager_loginout:

			if (islogin) {
				// 若是登录
				if (!NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					Toast.makeText(AccountManagerActivity.this, R.string.loadnonetwork_toast, Toast.LENGTH_SHORT).show();
				} else {
					startActivity(new Intent(AccountManagerActivity.this, AccountLoginActivity.class));
				}
			} else {
				// 删除账户
				doExit();
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 绑定人人账户
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void bindRenRen() {
		if (bindRenren) {
			// 解绑
			if (mController.deleteAccountToken(AccountManagerActivity.this, Account.TYPE_RENREN)) {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_fail, Toast.LENGTH_SHORT).show();
			}
			refreshThirdAccountBindState();

		} else {
			ThirdAuthActivity.startOauthActivity(AccountManagerActivity.this, ThirdConfigConstants.Renren_Authorize2, ThirdConfigConstants.PLATFORM_RENREN, ThirdConfigConstants.THIRD_BIND, REQUEST_CODE);
		}
	}

	/**
	 * 绑定腾讯微博账号
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void bindQQweibo() {
		if (bindQQweibo) {
			// 解绑
			if (mController.deleteAccountToken(AccountManagerActivity.this, Account.TYPE_QQWEIBO)) {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_fail, Toast.LENGTH_SHORT).show();
			}
			refreshThirdAccountBindState();
		} else {
			ThirdAuthActivity.startOauthActivity(AccountManagerActivity.this, ThirdConfigConstants.QQWEIBO_Authorize_2, ThirdConfigConstants.PLATFORM_QQWEIBO, ThirdConfigConstants.THIRD_BIND, REQUEST_CODE);
		}
	}

	/**
	 * 绑定新浪微博账号
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void bindSina() {
		if (bindSina) {
			// 解绑
			if (mController.deleteAccountToken(AccountManagerActivity.this, Account.TYPE_SINA)) {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_success, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(AccountManagerActivity.this, R.string.unbind_fail, Toast.LENGTH_SHORT).show();
			}
			refreshThirdAccountBindState();

		} else {
			// 绑定
			ThirdAuthActivity.startOauthActivity(AccountManagerActivity.this, ThirdConfigConstants.Sina_Authorize2, ThirdConfigConstants.PLATFORM_SINA, ThirdConfigConstants.THIRD_BIND, REQUEST_CODE);
		}
	}

	/**
	 * 刷新绑定状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	private void refreshThirdAccountBindState() {
		GoOutDebug.e("refreshThirdAccountBindState", "refreshThirdAccountBindState!!!");

		// 初始化，都为未绑定，
		bindSina = false;
		bindRenren = false;
		bindQQweibo = false;
		textSinaWeiBoName.setText("");
		imgSinaWeiBoBind.setImageResource(R.drawable.ic_bind_none);
		textSinaWeiBoBind.setText(R.string.item_account_add);

		textRenrenName.setText("");
		imgRenrenBind.setImageResource(R.drawable.ic_bind_none);
		textRenrenBind.setText(R.string.item_account_add);

		textQqWeiBoName.setText("");
		imgQqWeiBoBind.setImageResource(R.drawable.ic_bind_none);
		textQqWeiBoBind.setText(R.string.item_account_add);

		// 有绑定的改变状态

		ArrayList<Account> accounts = AccountUtil.getAccountList(AccountManagerActivity.this);
		if (accounts != null && accounts.size() > 0) {
			for (Account account : accounts) {
				if (account.getAccountType().equals(Account.TYPE_SINA)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindSina = true;
						textSinaWeiBoName.setText(account.getAccountName());
						imgSinaWeiBoBind.setImageResource(R.drawable.ic_bind);
						textSinaWeiBoBind.setText(R.string.item_account_remove);
					}
				} else if (account.getAccountType().equals(Account.TYPE_RENREN)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindRenren = true;
						textRenrenName.setText(account.getAccountName());
						imgRenrenBind.setImageResource(R.drawable.ic_bind);
						textRenrenBind.setText(R.string.item_account_remove);
					}
				} else if (account.getAccountType().equals(Account.TYPE_QQWEIBO)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindQQweibo = true;
						textQqWeiBoName.setText(account.getAccountName());
						imgQqWeiBoBind.setImageResource(R.drawable.ic_bind);
						textQqWeiBoBind.setText(R.string.item_account_remove);
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAccountUI();
	}

	/**
	 * 刷新账号信息
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	private void updateAccountUI() {
		AccountUtil.getNowLoginAccount(AccountManagerActivity.this);
		Account acc = mGlobal.getAccount();
		if (acc == null) {
			btnLogInOut.setText(R.string.login);
			btnLogInOut.setBackgroundResource(R.drawable.btn_login_selector);
			islogin = true;
			layoutAccountInfo.setVisibility(View.GONE);
			((RelativeLayout) findViewById(R.id.layout_accountmanager_mobile)).setVisibility(View.GONE);
		} else {
			btnLogInOut.setText(R.string.exit);
			btnLogInOut.setBackgroundResource(R.drawable.btn_logout_selector);
			islogin = false;
			layoutAccountInfo.setVisibility(View.VISIBLE);
			((RelativeLayout) findViewById(R.id.layout_accountmanager_mobile)).setVisibility(View.VISIBLE);
			if (StringUtil.isNull(acc.getBindMobile())) {
				// 若没绑定手机号，则只能是第三方账户
				textAccountName.setText(acc.getAccountName());
				textAccountMobile.setText(R.string.item_account_notadd);
				btnAccountMobileBind.setVisibility(View.VISIBLE);
			} else {
				if (StringUtil.isNull(acc.getAccountName())) {
					textAccountName.setText(acc.getBindMobile());
				} else {
					textAccountName.setText(acc.getAccountName());
				}

				String mobile = acc.getBindMobile();
				StringBuilder bindMobile = new StringBuilder(mobile.substring(0, 3));
				bindMobile.append("****").append(mobile.substring(7, 11));

				textAccountMobile.setText(bindMobile.toString());
				btnAccountMobileBind.setVisibility(View.GONE);
			}
		}

		refreshThirdAccountBindState();
	}

	/**
	 * 退出账户
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	private void doExit() {
		mpDialog = ProgressDialog.show(AccountManagerActivity.this, null, getString(R.string.exit_ing), true);
		mpDialog.setCancelable(false);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					// TODO 清账户相关的数据?

					// 退出账户，若是第三方账户，不解绑
					if (mController.exitAccount(AccountManagerActivity.this, mGlobal.getAccount().getLocalId())) {
						uiHandler.sendEmptyMessage(MSG_ACCOUNT_EXIT_SUCCESS);
					} else {
						uiHandler.sendEmptyMessage(MSG_ACCOUNT_EXIT_FAIL);
					}

				} catch (Exception e) {
					e.printStackTrace();
					uiHandler.sendEmptyMessage(MSG_ACCOUNT_EXIT_FAIL);
				}

			}
		}).start();
	}

	private class UiHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_ACCOUNT_EXIT_SUCCESS:
				updateAccountUI();
				mpDialog.dismiss();
				Toast.makeText(AccountManagerActivity.this, R.string.exit_success, Toast.LENGTH_SHORT).show();
				break;
			case MSG_ACCOUNT_EXIT_FAIL:
				mpDialog.dismiss();
				Toast.makeText(AccountManagerActivity.this, R.string.exit_fail, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 授权页的返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 绑定成功了

		}
	}

}
