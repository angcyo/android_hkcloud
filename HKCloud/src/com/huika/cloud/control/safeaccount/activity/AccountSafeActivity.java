package com.huika.cloud.control.safeaccount.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.me.activity.BindBankActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：账户安全页面
 * @author shicm
 * @date 2015-11-9 下午5:47:18
 */
public class AccountSafeActivity extends RdpBaseActivity {
	
	@ViewInject(R.id.up_login_pwd_rl)
	private RelativeLayout up_login_pwd_rl;
	@ViewInject(R.id.up_paypwd_rl)
	private RelativeLayout up_paypwd_rl;
	@ViewInject(R.id.account_auth_rl)
	private RelativeLayout account_auth_rl;
	@ViewInject(R.id.account_auth_info_ll)
	private LinearLayout auth_info_ll;
	@ViewInject(R.id.tv_name_account)
	private TextView tv_name_account;
	@ViewInject(R.id.tv_uppay_pwd)
	private TextView tv_uppay_pwd;
	@ViewInject(R.id.tv_card_account)
	private TextView tv_card_account;
	private boolean isNameAuth;
	private View mView;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle(R.string.account_safe_title);
		isNameAuth = HKCloudApplication.getInstance().getUserModel().getRealNameAuthentication() == 1;
		mView = addMasterView(R.layout.account_safe);
		RdpAnnotationUtil.inject(this);
		tv_uppay_pwd.setText(HKCloudApplication.getInstance().getUserModel().getTransPassword() == 1 ? "修改支付密码" : "设置支付密码");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 判断是都实名认证
		account_auth_rl.setVisibility(isNameAuth ? View.GONE : View.VISIBLE);
		account_auth_rl.setEnabled(!isNameAuth);
		mView.findViewById(R.id.auth_info_view).setVisibility(isNameAuth ? View.GONE : View.VISIBLE);
		auth_info_ll.setVisibility(isNameAuth ? View.VISIBLE : View.GONE);
		if (isNameAuth) {
			tv_name_account.setText("姓名：" + HKCloudApplication.getInstance().getUserModel().getRealName());
			tv_card_account.setText("身份证号：" + HKCloudApplication.getInstance().getUserModel().getIdNumber());
		}
	}
    @OnClick({R.id.up_login_pwd_rl,R.id.up_paypwd_rl,R.id.account_auth_rl})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.up_login_pwd_rl:
				showActivity(this, UpLoginPwdActivity.class);
				break;
			case R.id.up_paypwd_rl:
				if (HKCloudApplication.getInstance().getUserModel().getTransPassword() == 1) {
					Bundle mBundle = new Bundle();
					mBundle.putInt(UpPayPwdActivity.INP_UPDATE_PAY_PASSWORD, 1);
					showActivity(this, UpPayPwdActivity.class,mBundle);
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt(BindBankActivity.INP_TYPE, 1);
					showActivity(this, BindBankActivity.class, bundle);
				}
				break;
			case R.id.account_auth_rl:
				showActivity(this, AuthenticationActivity.class);
				break;
			default:
				break;
		}
	}
}
