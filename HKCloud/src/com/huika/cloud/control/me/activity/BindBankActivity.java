package com.huika.cloud.control.me.activity;

import java.lang.reflect.Type;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.activity.UpPayPwdActivity;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.views.passwordview.GridPasswordView;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
/**
 * @description：设置支付密码 验证支付密码
 * @author ht
 * @date 2015-11-24 下午3:27:30
 */
public class BindBankActivity extends RdpBaseActivity {
	private static final String FIRST_STEP = "first_step";
	public static final String INP_TYPE = "PWDTYPE";  // 0 验证 1 设置 2、修改
	public static final int RESULT_CODE = 1;
	private View mMasterView;
	private GridPasswordView gpwv;
	private BroadcastReceiver receiver;
	private int transPassword;
	private TextView tv_setting;
	private TextView tv_test;
	private Button goto_bindcard_two;
	private Button bt_confirm;
	private int type  = 0; // // 0 验证 1 设置 2、修改
	private TextView tv_update;
	private Button bt_confirm_update;
	private RdpNetCommand payWordCommond;
	private UserModel mUser;
	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				BindBankActivity.this.finish();
			}
		};
		IntentFilter filter=new IntentFilter(Constant.FINISH_ACTIVITY);
		registerReceiver(receiver, filter);
		mUser = HKCloudApplication.getInstance().getUserModel();
		
		Type typeOfResult=new TypeToken<String>(){}.getType();
		payWordCommond = new RdpNetCommand(mApplication, typeOfResult);
		payWordCommond.setServerApiUrl(UrlConstant.USER_CHANGEPASSWORD);
		payWordCommond.setOnCommandSuccessedListener(this);
		payWordCommond.setOnCommandFailedListener(this);
	}
	
	@Override
	protected void initActivity() {
		super.initActivity();
		type = getIntent().getIntExtra(INP_TYPE, 0);
		mMasterView = addMasterView(R.layout.bind_card_first);
		gpwv = (GridPasswordView) mMasterView.findViewById(R.id.gpwv);
		tv_setting = (TextView) mMasterView.findViewById(R.id.tv_setting);
		tv_test = (TextView) mMasterView.findViewById(R.id.tv_test);
		tv_update = (TextView) mMasterView.findViewById(R.id.tv_update);
		goto_bindcard_two = (Button) mMasterView.findViewById(R.id.goto_bindcard_two);
		bt_confirm = (Button) mMasterView.findViewById(R.id.bt_confirm);
		bt_confirm_update = (Button) mMasterView.findViewById(R.id.bt_confirm_update);
		if(type==1){
			//设置支付密码
			setFuncTitle("设置支付密码");
			tv_setting.setVisibility(View.VISIBLE);
			tv_test.setVisibility(View.GONE);
			tv_update.setVisibility(View.GONE);
			goto_bindcard_two.setVisibility(View.GONE);
			bt_confirm.setVisibility(View.VISIBLE);
			bt_confirm_update.setVisibility(View.GONE);
		}else if(type==0){
			//验证支付密码
			setFuncTitle("绑定银行卡");
			tv_setting.setVisibility(View.GONE);
			tv_test.setVisibility(View.VISIBLE);
			tv_update.setVisibility(View.GONE);
			goto_bindcard_two.setVisibility(View.VISIBLE);
			bt_confirm.setVisibility(View.GONE);
			bt_confirm_update.setVisibility(View.GONE);
		}else if(type==2){
			//修改支付密码
			setFuncTitle("修改支付密码");
			tv_setting.setVisibility(View.GONE);
			tv_test.setVisibility(View.GONE);
			tv_update.setVisibility(View.VISIBLE);
			goto_bindcard_two.setVisibility(View.GONE);
			bt_confirm.setVisibility(View.GONE);
			bt_confirm_update.setVisibility(View.VISIBLE);
		}
		bt_confirm.setOnClickListener(this);
		goto_bindcard_two.setOnClickListener(this);
		bt_confirm_update.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_bindcard_two:
			if(gpwv.getPassWord()!=null&&!TextUtils.isEmpty(gpwv.getPassWord())){
				startActivity(new Intent(mApplication, BindCardStepTwoActivity.class));
			}else{
				ToastMsg.showToastMsg(mApplication, "请输入支付密码");
			}
			break;
		case R.id.bt_confirm:
			if(gpwv.getPassWord()!=null&&!TextUtils.isEmpty(gpwv.getPassWord())){
				//TODO 设置支付密码
				changePswRequest("402881e8461795c201461795c2e90000",mUser.getMemberId(),gpwv.getPassWord(),"","3","");
			}else{
				ToastMsg.showToastMsg(mApplication, "请设置支付密码");
			}
			break;
		case R.id.bt_confirm_update:
			if(gpwv.getPassWord()!=null&&!TextUtils.isEmpty(gpwv.getPassWord())){
				//TODO 修改支付密码

				Intent intent=new Intent(mApplication, UpPayPwdActivity.class);
				startActivity(intent);

			}else{
				ToastMsg.showToastMsg(mApplication, "请输入新的支付密码");
			}
			break;
		default:
			super.onClick(v);
			break;
		}
	}
	
	private void changePswRequest(String merchantId,String memberId,String password,String oldPassword,String type,String validateCode) {
		payWordCommond.clearConditions();
		payWordCommond.setOnCommandFailedListener(this);
		payWordCommond.setOnCommandSuccessedListener(this);
		payWordCommond.setCondition("merchantId",merchantId);
		payWordCommond.setCondition("memberId", memberId);
		payWordCommond.setCondition("password",password);
		payWordCommond.setCondition("oldPassword",oldPassword);
		payWordCommond.setCondition("type", type);
		payWordCommond.setCondition("validateCode", validateCode);
		payWordCommond.execute();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		setResult(RESULT_CODE);
		mUser.setTransPassword(1);
		finish();
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}
}
