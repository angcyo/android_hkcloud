package com.huika.cloud.control.me.activity;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

public class InviteMembersActivity extends RdpBaseActivity {
	@Override
	protected void initActivity() {
		super.initActivity();
		addMasterView(R.layout.invite_members);
	}
}
