package com.huika.cloud.control.me.fragment;

import java.lang.reflect.Type;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.control.me.activity.DisOrderDetailActivity;
import com.huika.cloud.support.model.MyTeamBean;
import com.huika.cloud.support.model.TeamBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

public class MyTeamFragment extends RdpBaseFragment implements OnTouchListener  {
	private View mMasterView;
	private RdpListView rlv;
	private ImageView iv_user;
	private ImageView iv_goto_my_invite;
	private TextView tv_user_name;
	private TextView tv_user_level;
	private TextView direct_invite_num;
	private TextView my_team_num;
	private RelativeLayout rl_all;
	private RelativeLayout rl_team_num;
	private RelativeLayout rl_consume_num;
	
	@Override
	protected void initFragment() {
		super.initFragment();
		mTitleBar.setVisibility(View.GONE);
		mMasterView = addMasterView(R.layout.my_team_layout);
		initView();
		initListener();
		//设置点击事件传递监听
		mMasterView.setOnTouchListener(this);
		adapter = new RdpDataListAdapter<TeamBean>(mApplication, R.layout.item_my_team);
		rlv.setAdapter(adapter);
		//获取团队信息
		Type typeOfResult=new TypeToken<MyTeamBean>(){}.getType();
		teamRequest = new RdpNetCommand(mApplication, typeOfResult);
		getMyTeamInfo(teamRequest,"memberId",0,0);
	}

	private void getMyTeamInfo(RdpNetCommand teamRequest, String memberId, int teamCountType, int consumeAmountType) {
		teamRequest.setServerApiUrl("请求我的团队信息");
		teamRequest.setOnCommandSuccessedListener(this);
		teamRequest.setOnCommandFailedListener(this);
		teamRequest.clearConditions();
		teamRequest.setCondition("memberId",memberId);
		teamRequest.setCondition("teamCountType",teamCountType);
		teamRequest.setCondition("consumeAmountType",consumeAmountType);
		showLoadingOverLay(rlv);
		teamRequest.execute();
	}
	
	private void initListener() {
		iv_goto_my_invite.setOnClickListener(this);
		rl_all.setOnClickListener(this);
		rl_team_num.setOnClickListener(this);
		rl_consume_num.setOnClickListener(this);
	}

	private void initView() {
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
	
	boolean isSelectedAll=true;
	private View view_all;
	private View view_team;
	private View view_consume;
	private boolean isSelectedTeam=false;
	private boolean isSelectedConsume=false;
	private RdpDataListAdapter<TeamBean> adapter;
	private RdpNetCommand teamRequest;
	
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
					getMyTeamInfo(teamRequest, "memberId", 0, 0);
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
					getMyTeamInfo(teamRequest, "memberId", 0, 0);
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
					getMyTeamInfo(teamRequest, "memberId", 0, 0);
				}
				break;
			case R.id.iv_goto_my_invite:
				ToastMsg.showToastMsg(mApplication, "跳转到我邀请的会员界面");
				startActivity(new Intent(mApplication, DisOrderDetailActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		//请求成功
		MyTeamBean myTeamBean=(MyTeamBean) data;
		direct_invite_num.setText(myTeamBean.getInviteNum()+"人");
		my_team_num.setText(myTeamBean.getTeamNum()+"人");
		adapter.setData(myTeamBean.getTeamArray());
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
