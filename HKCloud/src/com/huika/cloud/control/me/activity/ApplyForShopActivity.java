package com.huika.cloud.control.me.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.support.model.AccountDetailBean;
import com.huika.cloud.util.IdentifyUtil;
import com.huika.cloud.util.PhoneUtil;
import com.huika.cloud.views.ClearableEditText;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：申请开店
 * @author ht
 * @date 2015-12-4 上午10:28:18
 */
public class ApplyForShopActivity extends RdpBaseActivity {
	private View mMasterView;
//	private WebView ads_webview;
//	private LinearLayout ads_container_ll;
	private EditText et_name;
	private EditText et_identify,et_phone,et_ver_code;
	private Button bt_get_ver_code,bt_confirm_open_shop;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("申请开店");
		ClearableEditText clearableEditText=null;
		mMasterView = addMasterView(R.layout.apply_for_shop);
		initView();
		initListener();
		
	}

	private void initListener() {
		bt_get_ver_code.setOnClickListener(this);
		bt_confirm_open_shop.setOnClickListener(this);
	}

	private void initView() {
//		ads_container_ll = (LinearLayout) mMasterView.findViewById(R.id.ads_container_ll);
//		ads_webview = (WebView) mMasterView.findViewById(R.id.ads_webview);
		et_name = (EditText) mMasterView.findViewById(R.id.et_name);
		et_identify = (EditText) mMasterView.findViewById(R.id.et_identify);
		et_phone = (EditText) mMasterView.findViewById(R.id.et_phone);
		et_ver_code = (EditText) mMasterView.findViewById(R.id.et_verification_code);
		bt_get_ver_code = (Button) mMasterView.findViewById(R.id.bt_get_ver_code);
		bt_confirm_open_shop = (Button) mMasterView.findViewById(R.id.bt_confirm_open_shop);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_confirm_open_shop:
			//确认开店
			if(TextUtils.isEmpty(et_name.getText().toString())){
				ToastMsg.showToastMsg(mApplication, "请输入用户名");
				return;
			}
			if(TextUtils.isEmpty(et_identify.getText().toString())||!new IdentifyUtil().verify(et_identify.getText().toString())){
				ToastMsg.showToastMsg(mApplication, "请输入正确的身份证号码");
				return ;
			}
			if(TextUtils.isEmpty(et_phone.getText().toString())||PhoneUtil.IsPhoneStr(et_phone.getText().toString())){
				ToastMsg.showToastMsg(mApplication, "请输入正确的手机号码");
			}
			
			break;
		case R.id.bt_get_ver_code:
			//获取验证码
			Type typeOfResult=new TypeToken<AccountDetailBean>() {
			}.getType();;
			RdpNetCommand rdpNetCommand = new RdpNetCommand(mApplication, typeOfResult);
			rdpNetCommand.setServerApiUrl("");
			rdpNetCommand.clearConditions();
			rdpNetCommand.execute();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result,
			Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		//保存验证码
		
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}
	
}
