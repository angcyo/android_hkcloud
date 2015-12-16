package com.huika.cloud.control.me.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

/**
 * @description：我的团队
 * @author ht
 * @date 2015-12-4 上午10:23:48
 */
public class MyTeamActivity extends RdpBaseActivity {
	private View mMasterView;
	private RdpListView rlv;
	private ImageView iv_user;
	private ImageView iv_goto_my_invite;
	private TextView direct_invite_num;
	private TextView my_team_num;
	private RelativeLayout rl_all;
	private RelativeLayout rl_team_num;
	private RelativeLayout rl_consume_num;

	boolean isSelectedAll=true;
	private View view_all;
	private View view_team;
	private View view_consume;
	private boolean isSelectedTeam=false;
	private boolean isSelectedConsume=false;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("个人中心");
		mMasterView = addMasterView(R.layout.my_team_layout);
		initView();
		initListener();
	}

	private void initListener() {
		iv_goto_my_invite.setOnClickListener(this);
		rl_all.setOnClickListener(this);
		rl_team_num.setOnClickListener(this);
		rl_consume_num.setOnClickListener(this);
	}

	private void initView() {
		iv_user = (ImageView) mMasterView.findViewById(R.id.iv_user);
		iv_goto_my_invite = (ImageView) mMasterView.findViewById(R.id.iv_goto_my_invite);
		direct_invite_num = (TextView) mMasterView.findViewById(R.id.direct_invite_num);
		my_team_num = (TextView) mMasterView.findViewById(R.id.my_team_num);
		rl_all = (RelativeLayout) mMasterView.findViewById(R.id.rl_all);
		rl_team_num = (RelativeLayout) mMasterView.findViewById(R.id.rl_team_num);
		rl_consume_num = (RelativeLayout) mMasterView.findViewById(R.id.rl_consume_num);
		rlv = (RdpListView) mMasterView.findViewById(R.id.rlv);
		view_all = mMasterView.findViewById(R.id.view_all);
		view_team = mMasterView.findViewById(R.id.view_team);
		view_consume = mMasterView.findViewById(R.id.view_consume);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_all:
				if(!isSelectedAll){
					isSelectedAll=true;
					isSelectedTeam = false;
					isSelectedConsume = false;
					view_all.setVisibility(View.VISIBLE);
					view_consume.setVisibility(View.GONE);
					view_team.setVisibility(View.GONE);
				}
				break;
			case R.id.rl_team_num:
				if(!isSelectedTeam){
					isSelectedAll=false;
					isSelectedTeam =true;
					isSelectedConsume = false;
					view_all.setVisibility(View.GONE);
					view_consume.setVisibility(View.GONE);
					view_team.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.rl_consume_num:
				if (!isSelectedConsume) {
					isSelectedAll=false;
					isSelectedTeam = false;
					isSelectedConsume = true;
					view_all.setVisibility(View.GONE);
					view_consume.setVisibility(View.VISIBLE);
					view_team.setVisibility(View.GONE);
				}
				break;
			case R.id.iv_goto_my_invite:
				ToastMsg.showToastMsg(mApplication, "跳转到我邀请的会员界面");
				break;
			default:
				super.onClick(v);
				break;
		}
	}
}
