package com.huika.cloud.control.me.activity;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
/**
 * @description：我的优惠券
 * @author ht
 * @date 2015-12-4 上午10:21:51
 */
public class MyCouponsCountActivity extends RdpBaseActivity {
	@Override
	protected void initActivity() {
		super.initActivity();
		addMasterView(R.layout.me_coupons_count);
	}
	
}
