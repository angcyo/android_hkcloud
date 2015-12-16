package com.huika.cloud.control.me.activity;

import android.view.View;
import android.widget.Button;

import com.huika.cloud.R;
import com.huika.cloud.views.ClearableEditText;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * 绑定微信公众号
 * @description：
 * @author ht
 * @date 2015-12-4 上午10:27:07
 */
public class BindWXPublicNum extends RdpBaseActivity {
	private View mMasterView;
	private ClearableEditText et_name;
	private ClearableEditText et_original_id;
	private ClearableEditText et_wx_num;
	private ClearableEditText et_token;
	private ClearableEditText et_app_id;
	private ClearableEditText et_secret;
	private Button bt_confirm;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("绑定微信公众号");
		mMasterView = addMasterView(R.layout.bind_wx_layout);
		initView();
		bt_confirm.setOnClickListener(this);
	}

	private void initView() {
		et_name = (ClearableEditText) mMasterView.findViewById(R.id.et_name);
		et_original_id = (ClearableEditText) mMasterView.findViewById(R.id.et_original_id);
		et_wx_num = (ClearableEditText) mMasterView.findViewById(R.id.et_wx_num);
		et_token = (ClearableEditText) mMasterView.findViewById(R.id.et_token);
		et_app_id = (ClearableEditText) mMasterView.findViewById(R.id.et_app_id);
		et_secret = (ClearableEditText) mMasterView.findViewById(R.id.et_secret);
		bt_confirm = (Button) mMasterView.findViewById(R.id.bt_confirm);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_confirm:
				
				break;
			default:
				super.onClick(v);
				break;
		}
	}
}
