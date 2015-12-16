package com.huika.cloud.control.me.activity;

import android.view.View;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * @description：会员等级权益
 * @author ht
 * @date 2015-12-4 上午10:24:55
 */
public class MemberLevelActivity extends RdpBaseActivity {
	private View mMasterView;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("会员权益");
		mMasterView = addMasterView(R.layout.member_level);
	}
}
