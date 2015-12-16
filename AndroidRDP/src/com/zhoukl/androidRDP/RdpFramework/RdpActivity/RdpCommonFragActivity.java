package com.zhoukl.androidRDP.RdpFramework.RdpActivity;

import android.os.Bundle;

import com.zhoukl.androidRDP.R;

/**
 * @description：普通绑定单个的fragment
 * @author shicm
 * @date 2015-10-23 下午1:51:22
 */
public abstract class RdpCommonFragActivity extends RdpActivity {

	public static final String INP_RESID = "resourceId";
	private int resourceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		resourceId = getIntent().getIntExtra(INP_RESID, R.layout.rdp_base_activity);
		setContentView(resourceId);
	}

}
