package com.c35.ptc.goout.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c35.ptc.goout.GoOutConstants;
import com.c35.ptc.goout.GoOutDebug;
import com.c35.ptc.goout.GoOutController;
import com.c35.ptc.goout.R;
import com.c35.ptc.goout.bean.RecentlyConsult;
import com.c35.ptc.goout.interfaces.OutGoingCallListener;
import com.c35.ptc.goout.interfaces.UploadProjectConsultRecordListener;
import com.c35.ptc.goout.receiver.OutGoingCall;
import com.c35.ptc.goout.util.ImageUrlUtil;
import com.c35.ptc.goout.util.NetworkUtil;
import com.c35.ptc.goout.util.PhoneUtil;
import com.c35.ptc.goout.util.StringUtil;
import com.c35.ptc.goout.util.ViewUtil;
import com.c35.ptc.goout.view.OnlineImageView;

/**
 * 顾问详情页
 * 
 * (目前此页仅用于显示从项目详情页和最近联系页传递过来的信息)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-3-5
 */
public class ConsultantInfoActivity extends Activity implements OnClickListener {

	private static final String TAG = "ConsultantInfoActivity";

	private ImageView imgBack;
	private TextView textTitle;
	private TextView textName;
	private TextView textTel;
	private TextView textServerArea;// 顾问服务区域
	private TextView textDescription;// 顾问所属部门
	private TextView textCounts;// 发布信息的条数，注意样式(数目为橙色，其它为黑色)
	private OnlineImageView onlineImgIc;// 头像
	private LinearLayout layoutCall;// 咨询顾问
	private RelativeLayout layoutCounts;// 发布信息的条数控件

	private OutGoingCall outCallListener;//

	private RecentlyConsult mConsultRecord;

	private GoOutController mController;
	private int publishProjectCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consultantinfo);

		outCallListener = new OutGoingCall(ConsultantInfoActivity.this);
		mController = GoOutController.getInstance();

		imgBack = (ImageView) findViewById(R.id.img_consultantinfo_back);
		textTitle = (TextView) findViewById(R.id.text_consultantinfo_title);
		onlineImgIc = (OnlineImageView) findViewById(R.id.onlineimg_consultantinfo_ic);
		textName = (TextView) findViewById(R.id.text_consultantinfo_name);
		textTel = (TextView) findViewById(R.id.text_consultantinfo_tel);
		textServerArea = (TextView) findViewById(R.id.text_consultantinfo_serverarea);
		textDescription = (TextView) findViewById(R.id.text_consultantinfo_description);
		textCounts = (TextView) findViewById(R.id.text_consultantinfo_projectcounts);
		layoutCall = (LinearLayout) findViewById(R.id.btn_consutantinfo_docall);
		layoutCounts = (RelativeLayout) findViewById(R.id.layout_consultantinfo_projectcounts);

		imgBack.setOnClickListener(this);
		layoutCall.setOnClickListener(this);
		layoutCounts.setOnClickListener(this);

		// 获得传递过来的数据，显示
		Bundle b = getIntent().getExtras();
		if (b != null) {

			try {
				mConsultRecord = new RecentlyConsult();

				mConsultRecord.setCalledType(b.getInt("calledType"));
				mConsultRecord.setCalledNumber(b.getString("calledNumber"));
				// GoOutDebug.e(TAG, "recive calledNumber=" + b.getString("calledNumber"));
				mConsultRecord.setProjectId(b.getInt("projectServerId"));
				mConsultRecord.setCalledID(b.getInt("consultantServerId"));
				mConsultRecord.setCalledDescription(b.getString("calledDescription"));
				mConsultRecord.setCalledServerArea(b.getString("calledServerArea"));
				mConsultRecord.setCalledLogoName(b.getString("calledLogoName"));
				mConsultRecord.setCalledName(b.getString("calledName"));
				publishProjectCount = b.getInt("calledPublisherProjectCount");
			} catch (Exception e) {
				e.printStackTrace();
				finish();
			}

		} else {
			finish();
		}

		outCallListener.setOutCallListener(new CallOutListener());
		outCallListener.regist();// 注册通话状态监听器
		showInfo();
	}

	/**
	 * 显示详情
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-13
	 */
	private void showInfo() {

		// test
		// textCounts.setText(ViewUtil.getStyleBlackHeadOrangeContentBlackTail(getApplicationContext(),
		// getString(R.string.consultantinfo_counts_head), " 23 ",
		// getString(R.string.consultantinfo_counts_tail)));

		if (mConsultRecord != null) {

			// 若没有电话，则不显示咨询顾问按钮
			if (StringUtil.isNull(mConsultRecord.getCalledNumber())) {
				layoutCall.setVisibility(View.GONE);
			}
			// 若是中介，则标题显示中介信息，若是顾问则标题显示顾问信息
			if (mConsultRecord.getCalledType() == GoOutConstants.TYPE_CONSULTANT) {
				textTitle.setText(R.string.consultantinfo_title_adviser);
			} else {
				textTitle.setText(R.string.consultantinfo_title_intermediary);
			}

			// logo
			if (StringUtil.isNull(mConsultRecord.getCalledLogoName())) {
				onlineImgIc.setVisibility(View.GONE);
			} else {
				onlineImgIc.setVisibility(View.VISIBLE);
				onlineImgIc.setImageViewUrl(ImageUrlUtil.getPublisherLogoUrl(mConsultRecord.getCalledLogoName()), OnlineImageView.PIC_PUBLISHER_LOGO, false, ImageView.ScaleType.FIT_XY);
			}
			// 名字
			if (StringUtil.isNull(mConsultRecord.getCalledName())) {
				textName.setVisibility(View.GONE);
			} else {
				textName.setVisibility(View.VISIBLE);
				textName.setText(mConsultRecord.getCalledName());
			}
			// 电话
			if (StringUtil.isNull(mConsultRecord.getCalledNumber())) {
				textTel.setVisibility(View.GONE);
			} else {
				textTel.setVisibility(View.VISIBLE);
				textTel.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), getString(R.string.consultant_tel), mConsultRecord.getCalledNumber()));
			}

			// 服务区域
			if (StringUtil.isNull(mConsultRecord.getCalledServerArea())) {
				textServerArea.setVisibility(View.GONE);
			} else {
				textServerArea.setVisibility(View.VISIBLE);
				textServerArea.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), getString(R.string.consultant_serverarea), mConsultRecord.getCalledServerArea()));
			}
			// 描述
			if (StringUtil.isNull(mConsultRecord.getCalledDescription())) {
				textDescription.setVisibility(View.GONE);
			} else {
				textDescription.setVisibility(View.VISIBLE);
				textDescription.setText(ViewUtil.getStyleBlackTitleGreyContentSmall(getApplicationContext(), mConsultRecord.getCalledDescription(), ""));
			}

			// 发布项目数据
			if (publishProjectCount == -1) {
				textCounts.setText(ViewUtil.getStyleBlackHeadOrangeContentBlackTail(getApplicationContext(), getString(R.string.consultantinfo_counts_null), null, null));
			} else {
				textCounts.setText(ViewUtil.getStyleBlackHeadOrangeContentBlackTail(getApplicationContext(), getString(R.string.consultantinfo_counts_head), " " + publishProjectCount + " ", getString(R.string.consultantinfo_counts_tail)));
			}
		} else {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		outCallListener.unRegist();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_consultantinfo_back:
			finish();
			break;
		case R.id.btn_consutantinfo_docall:
			// 咨询顾问
			// GoOutDebug.e(TAG, "tel:" + mConsultRecord.getCalledNumber());
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mConsultRecord.getCalledNumber()));
			startActivity(intent);
			break;
		case R.id.layout_consultantinfo_projectcounts:
			// 查看发布的项目
			Intent i = new Intent(ConsultantInfoActivity.this, PublisherProjectActivity.class);
			i.putExtra("publisherId", mConsultRecord.getCalledID());
			i.putExtra("publisherType", mConsultRecord.getCalledType());
			startActivity(i);

			break;
		default:
			break;
		}

	}

	/**
	 * 上传通话记录
	 * 
	 * (后期与ProjectInfoActivity里的合并)
	 * 
	 * @Description:
	 * @param startTime
	 * @param callRunTime
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-19
	 */
	private void saveAndUploadCallRecord(long startTime, int callRunTime) {
		if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
			// 保存联系记录
			mController.insertConsultRecord(ConsultantInfoActivity.this, mConsultRecord.getProjectId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ConsultantInfoActivity.this), PhoneUtil.getPhoneImsi(ConsultantInfoActivity.this), PhoneUtil.getPhoneImei(ConsultantInfoActivity.this), mConsultRecord.getCalledNumber(), mConsultRecord.getCalledType(), mConsultRecord.getCalledID(), 1, mConsultRecord.getCalledName(), mConsultRecord.getCalledLogoName(), mConsultRecord.getCalledServerArea(), mConsultRecord.getCalledDescription());

			// 上传联系记录
			mController.uploadOneConsultRecork(mConsultRecord.getProjectId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ConsultantInfoActivity.this), PhoneUtil.getPhoneImsi(ConsultantInfoActivity.this), PhoneUtil.getPhoneImei(ConsultantInfoActivity.this), mConsultRecord.getCalledNumber(), mConsultRecord.getCalledType(), mConsultRecord.getCalledID(), new UploadProjectConsultRecordListener() {

				@Override
				public void uploadOneConsultRecorkFailed(long callStartTime) {
					super.uploadOneConsultRecorkFailed(callStartTime);
					// 上传失败，更新本地联系记录的上传状态
					mController.updateUploadStateOfConsultRecord(ConsultantInfoActivity.this, callStartTime, 0);
				}

				@Override
				public void uploadOneConsultRecorkFinished(long callStartTime) {
					super.uploadOneConsultRecorkFinished(callStartTime);
				}

			});
		} else {
			// 保存联系记录
			mController.insertConsultRecord(ConsultantInfoActivity.this, mConsultRecord.getProjectId(), callRunTime, startTime, PhoneUtil.getPhoneSimNumber(ConsultantInfoActivity.this), PhoneUtil.getPhoneImsi(ConsultantInfoActivity.this), PhoneUtil.getPhoneImei(ConsultantInfoActivity.this), mConsultRecord.getCalledNumber(), mConsultRecord.getCalledType(), mConsultRecord.getCalledID(), 0, mConsultRecord.getCalledName(), mConsultRecord.getCalledLogoName(), mConsultRecord.getCalledServerArea(), mConsultRecord.getCalledDescription());
		}

	}

	/**
	 * 获得联系记录相关，呼出电话时的电话状态
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-3-19
	 */
	private class CallOutListener extends OutGoingCallListener {

		@Override
		public void onCallOutGoing(String num) {
			super.onCallOutGoing(num);
			GoOutDebug.e(TAG, "onCallOutGoing=" + num);
		}

		@Override
		public void onCallIdle(String num, long statTime, int callRunTime) {
			super.onCallIdle(num, statTime, callRunTime);
			GoOutDebug.e(TAG, "onCallIdle 空闲，挂断 number=" + num + "callRunTime=" + callRunTime);

			if (callRunTime > 0) {
				saveAndUploadCallRecord(statTime, callRunTime);
			}
		}

		@Override
		public void onCallOffHook(String num) {
			super.onCallOffHook(num);
			GoOutDebug.e(TAG, "onCallOffHook 摘机 number=" + num);
		}

	}

	/********************* TODO 若从最近联系列表过来的，需要更新下已发布的项目数，若logo不一样，顺便更新logo（考虑后期添加） *******************************************/

}
