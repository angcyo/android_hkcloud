package com.huika.cloud.control.safeaccount.activity;

import java.lang.reflect.Type;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.TimeCount;
import com.huika.cloud.views.ClearableEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：绑订的手机号码来修改登录密码
 * @author shicm
 * @date 2015-11-10 上午9:39:01
 */
public class UpPhoneLoginPwdActivity extends RdpBaseActivity {
	@ViewInject(R.id.phone_tv_update_pay_pwd)
	private TextView tvPhone;
	@ViewInject(R.id.tv_code_pwd)
	private TextView sendPhoneCode;
	@ViewInject(R.id.update_pwd_edit_code)
	private ClearableEditText mEdtPhoneCode;
	@ViewInject(R.id.phone_new_pwd_edit)
	private ClearableEditText mEdtNewPwd;
	@ViewInject(R.id.phone_update_btn_login)
	private Button mBtnSure;
	private View mViews;
	private TimeCount timeCount = null;
     @Override
    protected void initActivity() {
    	super.initActivity();
    	setFuncTitle(getString(R.string.update_login_pwd_title));
    	mViews = addMasterView(R.layout.update_phone_login_pwd);
    	RdpAnnotationUtil.inject(this);
    	tvPhone.setText("你的手机：" + HKCloudApplication.getInstance().getUserModel().getAccount());
    }
	
	@OnClick({R.id.phone_update_btn_login,R.id.tv_code_pwd})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.phone_update_btn_login: // 确定
				showToastMsg("确定");
				executePayPwd();
				break;
			case R.id.tv_code_pwd:  // 请求验证码
				showToastMsg("请求验证码");
				executeCode();
				break;
			default:
				break;
		}
	}
	
	/**
	 * @description：请求验证码
	 * @author shicm
	 * @date 2015-11-17 下午2:05:54
	 */
	private void executeCode() {
		UserModel mUser = HKCloudApplication.getInstance().getUserModel();
		Type codeResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand codePwdCommand = new RdpNetCommand(this, codeResult);
		codePwdCommand.setOnCommandSuccessedListener(this);
		codePwdCommand.setOnCommandFailedListener(this);
		codePwdCommand.setServerApiUrl(UrlConstant.APP_GETSERVER_SMS); //网络链接地址接口
		codePwdCommand.clearConditions(); 
		codePwdCommand.setCondition("merchantId",HKCloudApplication.MERCHANTID);
		codePwdCommand.setCondition("type", 4);
		codePwdCommand.setCondition("phone", HKCloudApplication.getInstance().getUserModel().getAccount());
		codePwdCommand.execute();
	}

	
	/**
	 * @description：修改密码
	 * @author shicm
	 * @date 2015-11-17 下午2:06:07
	 */
	private void executePayPwd() {
		UserModel mUser = HKCloudApplication.getInstance().getUserModel();
		Type typeOfResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand updatePwdCommand = new RdpNetCommand(this, typeOfResult);
		updatePwdCommand.setOnCommandSuccessedListener(this);
		updatePwdCommand.setOnCommandFailedListener(this);
		updatePwdCommand.setServerApiUrl(UrlConstant.USER_CHANGEPASSWORD); //网络链接地址接口
		updatePwdCommand.clearConditions(); 
		updatePwdCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		updatePwdCommand.setCondition("memberId", mUser.getMemberId());
		updatePwdCommand.setCondition("oldPassWord", "");
		updatePwdCommand.setCondition("passWord", mEdtNewPwd.getText().toString());
		updatePwdCommand.setCondition("type", 1); // 1登陆密码 2交易密码 3设置交易密码
		updatePwdCommand.setCondition("validateCode", mEdtPhoneCode.getText().toString());
		updatePwdCommand.execute();
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		if (result.getUrl().endsWith(UrlConstant.APP_GETSERVER_SMS)) {
			showToastMsg(result.getMsg());
			 if (timeCount == null) timeCount = new TimeCount(sendPhoneCode);
	            timeCount.start();
		} else {
			
		}
	}
}
