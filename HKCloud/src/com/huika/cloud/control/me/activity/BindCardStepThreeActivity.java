package com.huika.cloud.control.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.util.IdentifyUtil;
import com.huika.cloud.util.PhoneUtil;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

public class BindCardStepThreeActivity extends RdpBaseActivity {
	private View mMasterView;
	private ImageView bank_icon;
	private TextView tv_bank_num;
	private EditText et_card_user_name;
	private EditText et_card_user_num;
	private EditText et_card_user_phone;
	private Button bt_confirm_card;
	private String user_name;
	private String user_identity;
	private String user_phone;
	
	@Override
	protected void initActivity() {
		super.initActivity();
//		mTitleBar.setVisibility(View.GONE);
		mMasterView = addMasterView(R.layout.bind_card_three);
		initView();
		bt_confirm_card.setOnClickListener(this);
		Intent intent = getIntent();
		String card_type = intent.getStringExtra("card_type");
		String card_num = intent.getStringExtra("card_num");
		
		tv_bank_num.setText(card_type+"("+card_num+")");
	}
	
	private void initView() {
		bank_icon = (ImageView) mMasterView.findViewById(R.id.bank_icon);
		tv_bank_num = (TextView) mMasterView.findViewById(R.id.tv_bank_num);
		et_card_user_name = (EditText) mMasterView.findViewById(R.id.et_card_user_name);
		et_card_user_num = (EditText) mMasterView.findViewById(R.id.et_card_user_num);
		et_card_user_phone = (EditText) mMasterView.findViewById(R.id.et_card_user_phone);
		bt_confirm_card = (Button) mMasterView.findViewById(R.id.bt_confirm_card);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_confirm_card:
			user_name = et_card_user_name.getText().toString();
			user_identity = et_card_user_num.getText().toString();
			user_phone = et_card_user_phone.getText().toString();
			String formatIPNumber = PhoneUtil.formatIPNumber(user_phone, mApplication);
			if(TextUtils.isEmpty(user_name)){
				ToastMsg.showToastMsg(mApplication,"用户名不能为空");
				return;
			}
			if(!new IdentifyUtil().verify(user_identity)){
				ToastMsg.showToastMsg(mApplication, "请检查身份证号码");
				return;
			}
			if(!PhoneUtil.IsPhoneStr(formatIPNumber)){
				ToastMsg.showToastMsg(mApplication, "输入的电话号码有误");
				return;
			}
			//添加银行卡
			ToastMsg.showToastMsg(mApplication, "跳转到银行卡列表");
			BindCardStepThreeActivity.this.finish();
			//发送广播,关闭之前的界面
			Intent intent = new Intent();
	        intent.setAction(Constant.FINISH_ACTIVITY); 
	        sendBroadcast(intent); 
			startActivity(new Intent(mApplication, BankCardListActivity.class));
			break;
		default:
			break;
		}
	}
}
