package com.c35.ptc.goout.util;

import java.util.ArrayList;

import android.content.Context;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutGlobal;
import com.c35.ptc.goout.bean.Account;

/**
 * 账户相关类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-18
 */
public class AccountUtil {

	/**
	 * 获得当前登录的账户
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-18
	 */
	public static Account getNowLoginAccount(Context con) {
		Account account = null;
		GoOutController controller = GoOutController.getInstance();
		account = controller.getNowLoginAccount(con);
		GoOutGlobal global = (GoOutGlobal) con.getApplicationContext();
		global.setAccount(account);
		return account;
	}

	/**
	 * 获得所有账户
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-19
	 */
	public static ArrayList<Account> getAccountList(Context con) {
		ArrayList<Account> accounts = null;
		GoOutController controller = GoOutController.getInstance();
		accounts = controller.getLocalAccounts(con);
		return accounts;

	}

	// /**
	// * 账户是否绑定了
	// * @Description:
	// * @param con
	// * @param type
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-4-19
	// */
	// public static boolean isThirdAccountBind(Context con, String type) {
	// boolean result = false;
	// Account account = null;
	// GoOutController controller = GoOutController.getInstance();
	// account = controller.getLocalAccountInfo(con, type);
	// if (account != null) {
	// }
	//
	// return result;
	// }
}
