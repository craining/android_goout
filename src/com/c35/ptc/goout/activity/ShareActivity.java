package com.c35.ptc.goout.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.thirdaccounts.ThirdConfigConstants;
import com.c35.ptc.goout.thirdaccounts.UtilRenRen;
import com.c35.ptc.goout.thirdaccounts.UtilSina;
import com.c35.ptc.goout.thirdaccounts.UtilTecentWeiBo;
import com.c35.ptc.goout.util.AccountUtil;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 项目分享页
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-16
 */
public class ShareActivity extends Activity implements OnClickListener {

	private static final String TAG = "ShareActivity";

	private ImageView imgBack;
	private Button btnSend;
	private EditText editContent;
	private TextView textLeft;
	private ImageView imgSina;
	private ImageView imgRenren;
	private ImageView imgQqweibo;

	private boolean bindSina = false;
	private boolean bindRenren = false;
	private boolean bindQQweibo = false;

	private boolean selectSina = false;
	private boolean selectRenren = false;
	private boolean selectQQweibo = false;

	private String sinaToken;
	private String renrenToken;
	private String qqWeiBoToken;
	private String qqWeiBoUserId;

	private boolean shareSinaSuccess;
	private boolean shareRenrenSuccess;
	private boolean shareQQweiboSuccess;

	private static final int BIND_SINA = 1;
	private static final int BIND_RENREN = 2;
	private static final int BIND_QQWEIBO = 3;

	// 我在**看到*项目名称*，项目国家，学历，项目金额，点击查看+应用下载地址，@本应用微博@发布者微博
	// private String projectName;
	// private String projectCountry;
	// private String projectDegree;
	// private String projectTuition
	private String shareContent;

	private ProgressDialog mpDialog = null;// 等待进度框

	private Handler handler;
	private static final int MSG_SHARE_FINIFH = 0x321;// 分享完成

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);

		handler = new UIHandler();

		imgBack = (ImageView) findViewById(R.id.img_share_back);
		btnSend = (Button) findViewById(R.id.btn_share_send);
		editContent = (EditText) findViewById(R.id.edit_share_content);
		textLeft = (TextView) findViewById(R.id.text_share_left);
		imgSina = (ImageView) findViewById(R.id.img_share_sinaweibo);
		imgRenren = (ImageView) findViewById(R.id.img_share_renrenweibo);
		imgQqweibo = (ImageView) findViewById(R.id.img_share_qqweibo);

		imgBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		imgSina.setOnClickListener(this);
		imgRenren.setOnClickListener(this);
		imgQqweibo.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			// projectName = b.getString("pName");
			// projectCountry = b.getString("pCountry");
			// projectDegree = b.getString("pDegree");
			// projectTuition = b.getString("pTuition");
			shareContent = b.getString("shareContent");
		}

		editContent.addTextChangedListener(new TextWatcher() {

			// private CharSequence temp;
			private boolean isEdit = true;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// temp = s;
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				textLeft.setText("" + (GoOutConstants.SHARE_MAX_LENGTH - editContent.length()));

				selectionStart = editContent.getSelectionStart();
				selectionEnd = editContent.getSelectionEnd();
				if (editContent.length() > GoOutConstants.SHARE_MAX_LENGTH) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					editContent.setText(s);
					editContent.setSelection(tempSelection);
				}
			}

		});

		// String shareContent = StringUtil.getShareProjectContent(ShareActivity.this, projectName,
		// projectCountry, projectTuition, projectDegree);
		if (shareContent.length() > GoOutConstants.SHARE_MAX_LENGTH) {
			// 默认分享的内容大于最大字符
			// 暂时不会出现

		} else {
			textLeft.setText((GoOutConstants.SHARE_MAX_LENGTH - shareContent.length()) + "");
			editContent.setText(shareContent);
			editContent.setSelection(shareContent.length());
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkBindState();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_share_back:
			finish();

			break;
		case R.id.btn_share_send:
			// 分享项目

			if (TextUtils.isEmpty(editContent.getText())) {
				Toast.makeText(ShareActivity.this, R.string.share_null, Toast.LENGTH_SHORT).show();
			} else {

				final String text = editContent.getText().toString();
				mpDialog = ProgressDialog.show(ShareActivity.this, null, getString(R.string.share_wait), true);
				mpDialog.setCancelable(false);

				new Thread(new Runnable() {

					@Override
					public void run() {

						if (selectSina) {
							StringBuilder sb1 = new StringBuilder(text);
							sb1.append(GoOutConstants.official_SinaBo);
//							shareSinaSuccess = UtilSina.updateOneStatues(ShareActivity.this, sinaToken, sb1.toString());
						}
						if (selectQQweibo) {
							StringBuilder sb2 = new StringBuilder(text);
							sb2.append(GoOutConstants.official_QQWeiBo);
							shareQQweiboSuccess = UtilTecentWeiBo.updateOneStatues(ShareActivity.this, qqWeiBoToken, qqWeiBoUserId, sb2.toString());
						}
						if (selectRenren) {
							// shareRenrenSuccess = UtilRenRen.shareOne(renrenToken, text);
							shareRenrenSuccess = UtilRenRen.updateOneStatues(ShareActivity.this, renrenToken, text);
						}

						handler.sendEmptyMessage(MSG_SHARE_FINIFH);
					}
				}).start();
			}
			break;
		case R.id.img_share_sinaweibo:
			if (!bindSina) {
				showBindAlertDlg(BIND_SINA);

			} else {
				if (selectSina) {
					// 取消选中
					selectSina = false;
				} else {
					// 选中
					selectSina = true;
				}
				updateView();
			}
			break;
		case R.id.img_share_qqweibo:
			if (!bindQQweibo) {
				showBindAlertDlg(BIND_QQWEIBO);
			} else {
				if (selectQQweibo) {
					// 取消选中
					selectQQweibo = false;
				} else {
					// 选中
					selectQQweibo = true;
				}
				updateView();
			}
			break;

		case R.id.img_share_renrenweibo:
			if (!bindRenren) {
				showBindAlertDlg(BIND_RENREN);
			} else {
				if (selectRenren) {
					// 取消选中
					selectRenren = false;
				} else {
					// 选中
					selectRenren = true;
				}
				updateView();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 判断绑定状态
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-25
	 */
	private void checkBindState() {
		GoOutDebug.e(TAG, "checkBindState!!!");

		// 初始化，都为未绑定，
		bindSina = false;
		bindRenren = false;
		bindQQweibo = false;

		// 有绑定的改变状态

		ArrayList<Account> accounts = AccountUtil.getAccountList(ShareActivity.this);
		if (accounts != null && accounts.size() > 0) {
			for (Account account : accounts) {
				if (account.getAccountType().equals(Account.TYPE_SINA)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindSina = true;
						selectSina = true;
						sinaToken = account.getAccessToken();
					}
				} else if (account.getAccountType().equals(Account.TYPE_RENREN)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindRenren = true;
						selectRenren = true;
						renrenToken = account.getAccessToken();
					}
				} else if (account.getAccountType().equals(Account.TYPE_QQWEIBO)) {
					if (!StringUtil.isNull(account.getAccessToken())) {
						bindQQweibo = true;
						selectQQweibo = true;
						qqWeiBoToken = account.getAccessToken();
						qqWeiBoUserId = account.getThirdId();
					}
				}
			}
		}
		updateView();
	}

	/**
	 * 更新ui
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-25
	 */
	private void updateView() {
		imgSina.setImageResource(R.drawable.logo_sinaweibo_grey);
		imgRenren.setImageResource(R.drawable.logo_renren_grey);
		imgQqweibo.setImageResource(R.drawable.logo_qqweibo_grey);

		if (bindSina) {
			if (selectSina) {
				imgSina.setImageResource(R.drawable.logo_sinaweibo);
			}
		}

		if (bindQQweibo) {
			if (selectQQweibo) {
				imgQqweibo.setImageResource(R.drawable.logo_qqweibo);
			}
		}
		if (bindRenren) {
			if (selectRenren) {
				imgRenren.setImageResource(R.drawable.logo_renren);
			}
		}
	}

	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SHARE_FINIFH:
				mpDialog.dismiss();

				boolean allSuccess = true;

				StringBuilder sb = new StringBuilder(getString(R.string.share_fail_top));

				if (selectQQweibo) {
					if (!shareQQweiboSuccess) {
						sb.append(getString(R.string.account_qqweibo2));
						allSuccess = false;
					}
				}

				if (selectRenren) {
					if (!shareRenrenSuccess) {
						sb.append(" ").append(getString(R.string.account_renren2));
						allSuccess = false;
					}
				}
				if (selectSina) {
					if (!shareSinaSuccess) {
						sb.append(" ").append(getString(R.string.account_sinaweibo2));
						allSuccess = false;
					}
				}

				if (allSuccess) {
					Toast.makeText(ShareActivity.this, R.string.share_success, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(ShareActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
					checkBindState();// 若有token过期，则刷新绑定状态
				}
				break;

			default:
				break;
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}

	/**
	 * 是否进行绑定
	 * @Description:
	 * @param bindwho
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-4-25
	 */
	private void showBindAlertDlg(final int bindwho) {
		AlertDialog.Builder builder = new Builder(ShareActivity.this);
		builder.setMessage(R.string.share_alert);

		builder.setTitle(R.string.alert_str);

		builder.setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (bindwho) {
				case BIND_SINA:
					ThirdAuthActivity.startOauthActivity(ShareActivity.this, ThirdConfigConstants.Sina_Authorize2, ThirdConfigConstants.PLATFORM_SINA, ThirdConfigConstants.THIRD_BIND, 1);
					break;
				case BIND_QQWEIBO:
					ThirdAuthActivity.startOauthActivity(ShareActivity.this, ThirdConfigConstants.QQWEIBO_Authorize_2, ThirdConfigConstants.PLATFORM_QQWEIBO, ThirdConfigConstants.THIRD_BIND, 1);
					break;
				case BIND_RENREN:
					ThirdAuthActivity.startOauthActivity(ShareActivity.this, ThirdConfigConstants.Renren_Authorize2, ThirdConfigConstants.PLATFORM_RENREN, ThirdConfigConstants.THIRD_BIND, 1);

					break;

				default:
					break;
				}
			}
		});
		builder.setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.create().show();

	}
}
