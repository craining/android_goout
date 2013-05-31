package com.c35.ptc.goout.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.interfaces.OutGoingCallListener;
import com.c35.ptc.goout.util.TimeUtil;

/**
 * 接收电话状态
 * 
 * 抽取出此类，并加回调，供各个含咨询顾问的页面使用
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-19
 */
public class OutGoingCall {

	private static final String TAG = "OutGoingCall";

	private TelephonyManager mTm;
	private PhoneCallListenner mPl;
	private BroadcastReceiver mBr;
	private Context mContext;

	private String mStrCallNum;

	private boolean regist;// 是否监听状态

	private OutGoingCallListener callOutListener;

	private long startTime = -1;// 开始拨打的时间

	public OutGoingCall(Context con) {
		this.mContext = con;
		mTm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
		mPl = new PhoneCallListenner();
		mBr = new PhoneCallReceiver();
	}

	/**
	 * 注册广播接收器
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void regist() {
		if (mContext != null && mBr != null) {
			GoOutDebug.e(TAG, "regist out call listener");
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
			filter.addAction("android.intent.action.PHONE_STATE");
			filter.setPriority(Integer.MAX_VALUE);
			mContext.registerReceiver(mBr, filter);
			regist = true;
		}
	}

	/**
	 * 注册广播接收器
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void unRegist() {
		if (mContext != null && mBr != null) {
			GoOutDebug.e(TAG, "Unregist out call listener");
			try {
				mContext.unregisterReceiver(mBr);
				regist = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置监听器
	 * 
	 * @Description:
	 * @param callOutListener
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	public void setOutCallListener(OutGoingCallListener callOutListener) {
		this.callOutListener = callOutListener;
	}

	/**
	 * 通话状态监听
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-19
	 */
	private class PhoneCallListenner extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state 当前状态 incomingNumber,貌似没有去电的API
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:
				// 挂断时间：
				GoOutDebug.i(TAG, "电话空闲");
				GoOutDebug.i(TAG, "callOutListener=null?" + (callOutListener == null ? "true" : "false"));
				GoOutDebug.i(TAG, "regist" + (regist ? "true" : "false"));

				if (callOutListener != null && regist) {
					// 延迟操作，防止系统存储数据延迟造成无法正确读取到刚刚拨打的记录
					new Handler().postDelayed(new Runnable() {

						public void run() {
							int durTime = getCallDurTime();
							GoOutDebug.i(TAG, "durTime=" + durTime);
							if (durTime > 0) {
								callOutListener.onCallIdle(mStrCallNum, startTime, durTime);
							}
							// long endTime = TimeUtil.getCurrentTimeMillis();
							// if (startTime > 0) {
							// callOutListener.onCallIdle(mStrCallNum, startTime, (int) ((endTime - startTime)
							// /
							// 1000));
							// } else {
							// callOutListener.onCallIdle(mStrCallNum, startTime, -1);
							// }
						}
					}, 1000);

				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				GoOutDebug.i(TAG, "摘机状态");
				// (去电时对方接听状态尚无法得到，电话一呼出，直接进入此状态)
				if (callOutListener != null && regist) {
					callOutListener.onCallOffHook(mStrCallNum);
					startTime = TimeUtil.getCurrentTimeMillis();
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// GoOutDebug.i(TAG, "来电: " + incomingNumber);
				break;
			}

			mTm.listen(mPl, PhoneStateListener.LISTEN_NONE);
		}

	}

	/**
	 * 呼出电话接收器
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-19
	 */
	private class PhoneCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				// GoOutDebug.v(TAG, "去电：" + getResultData());
				if (callOutListener != null && regist) {
					mStrCallNum = getResultData();
					callOutListener.onCallOutGoing(mStrCallNum);
				}
			} else {
				mTm.listen(mPl, PhoneStateListener.LISTEN_CALL_STATE);
			}

		}

	}

	/**
	 * 获得刚刚的通话时间
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-25
	 */
	private int getCallDurTime() {
		int durTime = 0;
		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = null;
		try {
			// 按数据库里的顺序查询，第一条即为刚刚拨出的记录
			cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[] { CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION }, Calls.NUMBER + "=?", new String[] { mStrCallNum }, CallLog.Calls.DEFAULT_SORT_ORDER);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				// GoOutDebug.e(TAG, "callType" + cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
				// GoOutDebug.e(TAG, "callDate" +
				// TimeUtil.longToDateTimeString(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))));
				// GoOutDebug.e(TAG, "callDur" +
				// cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));

				if (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == 2) {
					durTime = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return durTime;

	}

}
