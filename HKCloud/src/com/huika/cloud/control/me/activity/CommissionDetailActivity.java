package com.huika.cloud.control.me.activity;

import android.view.View;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

/**
 * 
 * @description：分佣金明细
 * @author ht
 * @date 2015-12-4 上午10:26:27
 */
public class CommissionDetailActivity extends RdpBaseActivity {
	private View mMasterView;
	private RdpListView commission_lv;

	@Override
	protected void initActivity() {
		super.initActivity();
		mMasterView = addMasterView(R.layout.commission_lv_layout);
		commission_lv = (RdpListView) mMasterView.findViewById(R.id.rlv);
	}
}
