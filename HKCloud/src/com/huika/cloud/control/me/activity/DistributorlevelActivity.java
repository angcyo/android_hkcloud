package com.huika.cloud.control.me.activity;

import android.view.View;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * @description：分销商等级与权益
 * @author ht
 * @date 2015-12-4 上午10:25:36
 */
public class DistributorlevelActivity extends RdpBaseActivity {
	private View mMasterView;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("分销商等级与权益");
		mMasterView = addMasterView(R.layout.distributor_level);
	}
}
