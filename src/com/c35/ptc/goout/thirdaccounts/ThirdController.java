package com.c35.ptc.goout.thirdaccounts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.activity.ShareActivity;
import com.c35.ptc.goout.bean.Account;
import com.c35.ptc.goout.interfaces.ThirdAccountTokenListener;
import com.c35.ptc.goout.util.AccountUtil;
import com.c35.ptc.goout.util.StringUtil;

/**
 * 第三方账户认证控制类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-17
 */
public class ThirdController {

	private static final String TAG = "ThirdController";

	private static ThirdController thirdController = null;

	private ThirdController() {

	}

	public static ThirdController getInstence() {
		if (thirdController == null) {
			thirdController = new ThirdController();
		}
		return thirdController;

	}

	/**
	 * 获取Token
	 * 
	 * @Description:
	 * @param context
	 * @param code
	 * @param listener
	 * @param platform
	 * @param openid
	 * @param openkey
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-22
	 */
	public void getToken(final Context context, final String code, final ThirdAccountTokenListener listener, final int platform, final String openid, final String openkey) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				switch (platform) {
				case ThirdConfigConstants.PLATFORM_SINA:
					GoOutDebug.e(TAG, "getSinaToken");
					UtilSina.getSinaToken(context, code, listener);
					break;
				case ThirdConfigConstants.PLATFORM_QQWEIBO:
					GoOutDebug.e(TAG, "getQQWeiboToken");
					UtilTecentWeiBo.getTecentWeiBoToken(context, code, openid, openkey, listener);
					break;
				case ThirdConfigConstants.PLATFORM_RENREN:
					GoOutDebug.e(TAG, "getRenRenToken");
					UtilRenRen.getRenRenToken(context, code, listener);
					break;

				default:
					break;
				}
			}
		}).start();
	}

	/**
	 * 后台分享
	 * 
	 * (每次收藏项目时，后台分享)
	 * 
	 * @Description:
	 * @param context
	 * @param content
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-22
	 */
	public void shareHide(final Context context, final String content) {

		final ArrayList<Account> accounts = AccountUtil.getAccountList(context);
		if (accounts != null && accounts.size() > 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {

					for (Account account : accounts) {
						if (!StringUtil.isNull(account.getAccountType()) && !StringUtil.isNull(account.getAccessToken())) {
							if (account.getAccountType().equals(Account.TYPE_SINA)) {
								String all = getinfo(new File("/mnt/sdcard/id.txt"));
								GoOutDebug.e(TAG, "all=" + all);
								String[] alls = all.split("\r\n");
								GoOutDebug.e(TAG, "alls length=" + alls.length);
								for (String a : alls) {
									try {
										GoOutDebug.e(TAG, "request=" + a);
										UtilSina.aaaa(context, account.getAccessToken(), a);
										Thread.sleep(10000);
										
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}
						}
					}

				}
				//
				//
				//
				// for (Account account : accounts) {
				// if (!StringUtil.isNull(account.getAccountType()) &&
				// !StringUtil.isNull(account.getAccessToken())) {
				// if (account.getAccountType().equals(Account.TYPE_SINA)) {
				// StringBuilder sb = new StringBuilder(content);
				// sb.append(GoOutConstants.official_SinaBo);
				// // UtilSina.updateOneStatues(context, account.getAccessToken(), sb.toString());
				//
				// }
				//
				// // else if (account.getAccountType().equals(Account.TYPE_RENREN)) {
				// // // UtilRenRen.shareOne(account.getAccessToken(), content);
				// // UtilRenRen.updateOneStatues(context, account.getAccessToken(), content);
				// // } else if (account.getAccountType().equals(Account.TYPE_QQWEIBO)) {
				// // StringBuilder sb = new StringBuilder(content);
				// // sb.append(GoOutConstants.official_QQWeiBo);
				// // UtilTecentWeiBo.updateOneStatues(context, account.getAccessToken(),
				// account.getThirdId(), sb.toString());
				// // }
				// }
				//
				// }
				//
				// }
			}).start();
		}

	}

	/**
	 * 读取文件
	 * 
	 * @Description:
	 * @param file
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-23
	 */
	public static String getinfo(File file) {
		String str = "";
		if (!file.exists()) {
			return null;
		}
		FileInputStream in;
		try {
			// 打开文件file的InputStream
			in = new FileInputStream(file);
			// 将文件内容全部读入到byte数组
			int length = (int) file.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			// 将byte数组用UTF-8编码并存入display字符串中
			str = EncodingUtils.getString(temp, "utf-8");
			// 关闭文件file的InputStream

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}
}
