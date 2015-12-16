package com.huika.cloud.control.me.activity;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
/**
 * @description：我邀请的会员
 * @author ht
 * @date 2015-12-4 上午10:25:18
 */
public class MeInviteMembersActivity extends RdpBaseActivity {
	@Override
	protected void initActivity() {
		super.initActivity();
		addMasterView(R.layout.me_invite_members);
	}
}
