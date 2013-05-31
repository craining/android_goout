package com.c35.ptc.goout.util;

import java.util.ArrayList;

import android.content.Context;

import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.bean.RecentlyConsult;
import com.c35.ptc.goout.interfaces.UploadProjectConsultRecordListener;

/**
 * 用于检查上传失败的数据，并尝试重新上传
 * 
 * （包括联系记录 (后期可能有收藏记录), 因为拨打电话可能是在离线的情况下进行的）
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-29
 */
public class CheckUploadFailedDataUtil {

	private static final String TAG = "CheckUploadFailedDataUtil";

	private Context mContext;

	public CheckUploadFailedDataUtil(Context con) {
		this.mContext = con;
	}

	/**
	 * 检查各个未上传成功的记录，并上传
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-29
	 */
	public void check() {
		if (NetworkUtil.isNetworkAvailable(mContext)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// 检查联系记录
					checkRecentlyCommunicate();
				}
			}).start();
		}

	}

	/**
	 * 检查联系记录
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-29
	 */
	private void checkRecentlyCommunicate() {

		ArrayList<RecentlyConsult> recentlys = new ArrayList<RecentlyConsult>();
		final GoOutController controller = GoOutController.getInstance();
		recentlys = controller.getRecentlyCommunicateListUploadFailed(mContext);
		if (recentlys != null && recentlys.size() > 0) {
			GoOutDebug.e(TAG, "have upload failed record, try to reload!!!!");
			for (RecentlyConsult record : recentlys) {
				// 上传联系记录
				controller.uploadOneConsultRecork(record.getProjectId(), record.getCallRuntime(), record.getCallTime(), PhoneUtil.getPhoneSimNumber(mContext), PhoneUtil.getPhoneImsi(mContext), PhoneUtil.getPhoneImei(mContext), record.getCalledNumber(), record.getCalledType(), record.getCalledID(), new UploadProjectConsultRecordListener() {

					@Override
					public void uploadOneConsultRecorkFailed(long callStratTime) {
						super.uploadOneConsultRecorkFailed(callStratTime);
						GoOutDebug.e(TAG, "uploadOneConsultRecorkFailed!, Upload record failed， update upload state in local db.");
						// 上传失败，更新本地联系记录的上传状态
						controller.updateUploadStateOfConsultRecord(mContext, callStratTime, 0);
					}

					@Override
					public void uploadOneConsultRecorkFinished(long callStratTime) {
						super.uploadOneConsultRecorkFinished(callStratTime);
						GoOutDebug.e(TAG, "uploadOneConsultRecorkFinished!!!!!!!!!!!!!!!!!!!!!!!!!");
					}

				});
			}
		} else {
			GoOutDebug.e(TAG, "not have upload failed record, no need to try to reload!!!!");
		}

	}

}
