package com.c35.ptc.goout.activity;

import com.c35.ptc.goout.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

/**
 * 检查更新
 * 
 * (v1版暂时不用)
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-2-28
 */

public class PersonalCheckUpdateActivity extends Activity implements OnClickListener {

	private ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal_check_update);
		imgBack = (ImageView) findViewById(R.id.img_check_update_back);

		imgBack.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_check_update_back:
			finish();
			break;

		default:
			break;
		}

	}

}
