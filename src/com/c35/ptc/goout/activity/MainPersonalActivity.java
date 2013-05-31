package com.c35.ptc.goout.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.util.AccountUtil;
import com.c35.ptc.goout.util.FileUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 主页中的“我的”页面
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-27
 */
public class MainPersonalActivity extends Activity implements OnClickListener {

	private RelativeLayout layoutMainAccount;// 我的账户
	private TextView textAccountName;

	private RelativeLayout layoutFavouriteSchool;// 院校收藏
	private RelativeLayout layoutFavouriteProject;// 项目收藏
	private RelativeLayout layoutRecentlyCommunicate;// 最近联系

	private RelativeLayout layoutCheckUpdate;// 检测版本
	private RelativeLayout layoutClear;// 清除缓存
	private TextView textCahceSize;// 缓存文件大小

	private Handler uihandler;
	private static final int MSG_DELETE_FINISH = 0x101;// 缓存文件清除成功
	private static final int MSG_CHECK_UPDATE_FINISHED = 0x102;// 检测新版本
	private ProgressDialog mpDialog = null;// 等待进度框

	private GoOutGlobal global;

	private long cacheSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_personal);

		uihandler = new UIhandler();
		global = (GoOutGlobal) getApplicationContext();

		layoutMainAccount = (RelativeLayout) findViewById(R.id.layout_personal_account);
		textAccountName = (TextView) findViewById(R.id.text_personal_account_name);
		layoutFavouriteProject = (RelativeLayout) findViewById(R.id.layout_personal_favourite_project);
		layoutFavouriteSchool = (RelativeLayout) findViewById(R.id.layout_personal_favourite_school);
		layoutRecentlyCommunicate = (RelativeLayout) findViewById(R.id.layout_personal_recentlycommunicate);
		layoutCheckUpdate = (RelativeLayout) findViewById(R.id.layout_personal_checkupdate);
		layoutClear = (RelativeLayout) findViewById(R.id.layout_personal_clear);
		textCahceSize = (TextView) findViewById(R.id.text_personal_clear_size);

		layoutMainAccount.setOnClickListener(this);
		layoutFavouriteProject.setOnClickListener(this);
		layoutFavouriteSchool.setOnClickListener(this);
		layoutRecentlyCommunicate.setOnClickListener(this);
		layoutCheckUpdate.setOnClickListener(this);
		layoutClear.setOnClickListener(this);
		updateCacheFileSize();

	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshAccountName();

	}

	private void refreshAccountName() {
		AccountUtil.getNowLoginAccount(MainPersonalActivity.this);
		Account acc = global.getAccount();
		if (acc == null) {
			textAccountName.setText(R.string.item_account_null);
		} else {
			if (StringUtil.isNull(acc.getBindMobile())) {
				// 若没绑定手机号，则只能是第三方账户
				textAccountName.setText(acc.getAccountName());
			} else {
				// 若绑定了手机
				if (StringUtil.isNull(acc.getAccountName())) {
					textAccountName.setText(acc.getBindMobile());
				} else {
					textAccountName.setText(acc.getAccountName());
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_personal_account:
			// 我的账户，若无账户，跳转登录页
			startActivity(new Intent(MainPersonalActivity.this, AccountManagerActivity.class));
			break;
		case R.id.layout_personal_favourite_school:
			// 进入院校收藏
			startActivity(new Intent(MainPersonalActivity.this, FavouriteSchoolAcitivty.class));
			break;
		case R.id.layout_personal_recentlycommunicate:
			// 进入最近联系
			startActivity(new Intent(MainPersonalActivity.this, PersonalRecentlyCommunicateAtivity.class));
			break;
		case R.id.layout_personal_checkupdate:
			// 进入版本检测
			// 等待进度框
			mpDialog = ProgressDialog.show(MainPersonalActivity.this, null, getString(R.string.check_update_wait), true, false);

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						uihandler.sendEmptyMessage(MSG_CHECK_UPDATE_FINISHED);
					}

				}
			}).start();

			// startActivity(new Intent(MainPersonalActivity.this, PersonalCheckUpdateActivity.class));
			break;
		case R.id.layout_personal_favourite_project:
			// 进入项目收藏
			startActivity(new Intent(MainPersonalActivity.this, FavouriteProjectAcitivty.class));
			break;
		case R.id.layout_personal_clear:
			// 清除缓存
			showClearCacheDlg();
			break;
		default:
			break;
		}
	}

	/**
	 * 清除缓存对话框
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void showClearCacheDlg() {
		AlertDialog.Builder builder = new Builder(MainPersonalActivity.this);
		builder.setMessage(R.string.item_clear_dlgtext);

		builder.setTitle(R.string.alert_str);

		builder.setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				doClear();
			}
		});
		builder.setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.create().show();

		// GoOutDialog.Builder builder = new GoOutDialog.Builder(MainPersonalActivity.this);
		// builder.setTitle(getString(R.string.alert_str));
		// builder.setMessage(getString(R.string.item_clear_dlgtext));
		// builder.setPositiveButton(getString(R.string.ok_str), new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int id) {
		// doClear();
		// }
		// });
		// builder.setNegativeButton(getString(R.string.cancel_str), new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
	}

	private class UIhandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_DELETE_FINISH:
				Toast.makeText(MainPersonalActivity.this, R.string.catch_clear_success, Toast.LENGTH_SHORT).show();
				updateCacheFileSize();
				dismissDlg();
				break;

			case MSG_CHECK_UPDATE_FINISHED:
				dismissDlg();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 更新缓存文件大小
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	private void updateCacheFileSize() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (PhoneUtil.existSDcard()) {
					//
					final File catchPath = new File(GoOutConstants.IMAGES_STORE_SDCARD_PATH);
					FileUtil fileutil = new FileUtil();
					cacheSize = fileutil.getFileSize(catchPath);
					textCahceSize.setText(FileUtil.sizeLongToString(cacheSize));
				} else {
					textCahceSize.setText("未挂载存储卡");
					cacheSize = -1;
				}
			}
		}, 50);

	}

	/**
	 * 清除缓存
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	private void doClear() {

		if (PhoneUtil.existSDcard()) {
			// //
			final File catchPath = new File(GoOutConstants.IMAGES_STORE_SDCARD_PATH);
			// FileUtil fileutil = new FileUtil();
			if (cacheSize > 0) {

				// 等待进度框
				mpDialog = ProgressDialog.show(MainPersonalActivity.this, null, getString(R.string.catch_clearing), true);
				mpDialog.setCancelable(false);

				new Thread(new Runnable() {

					@Override
					public void run() {
						FileUtil.delFileDir(catchPath);
						uihandler.sendEmptyMessage(MSG_DELETE_FINISH);
					}
				}).start();

			} else {
				// 缓存文件不存在
				Toast.makeText(MainPersonalActivity.this, R.string.catch_notexist, Toast.LENGTH_SHORT).show();
			}

		} else {
			// 存储卡不存在
			Toast.makeText(MainPersonalActivity.this, R.string.sdcard_notexist, Toast.LENGTH_SHORT).show();
		}
	}

	private void dismissDlg() {
		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}
}
