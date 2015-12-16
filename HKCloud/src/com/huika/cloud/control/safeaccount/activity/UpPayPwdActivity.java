package com.huika.cloud.control.safeaccount.activity;

import java.lang.reflect.Type;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.me.activity.BindBankActivity;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.MMAlertDialog;
import com.huika.cloud.util.MMAlertDialog.DialogOnItemClickListener;
import com.huika.cloud.util.TimeCount;
import com.huika.cloud.views.ClearableEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：修改支付密码第一步验证登录密码验证
 * @author shicm
 * @date 2015-11-10 上午9:55:12
 */
public class UpPayPwdActivity extends RdpBaseActivity implements DialogOnItemClickListener{
	public static final String INP_UPDATE_PAY_PASSWORD = "UpdatePayPwd";
	private int updatePwdType = 0; 
	@ViewInject(R.id.up_pay_pwd_edit)
	private ClearableEditText up_pay_pwd_edit;
	@ViewInject(R.id.up_pay_pwd_edit_code)
	private ClearableEditText up_pay_pwd_edit_code;
	@ViewInject(R.id.tv_code_pay_pwd)
	private TextView tv_code_pay_pwd;
	@ViewInject(R.id.update_pay_pwd_btn)
	private Button mbtnSure;
	@ViewInject(R.id.phone_tv_update_Pwd)
	private TextView phone_tv_update_Pwd;
	private View mView;
	private TimeCount timeCount = null;

	@Override
	protected void initActivity() {
		initDatas();
		super.initActivity();
		setFuncTitle(updatePwdType == 0 ?  R.string.update_pay_pwd_title_find : R.string.update_pay_pwd_title_second);
		mView = addMasterView(R.layout.update_pay_pwd);
		RdpAnnotationUtil.inject(this);
		phone_tv_update_Pwd.setText("你的手机:" + HKCloudApplication.getInstance().getUserModel().getAccount());
		up_pay_pwd_edit.setHint(updatePwdType == 0 ? getString(R.string.update_input_login_pwd) : getString(R.string.update_input_pay_pwd));
	}

	private void initDatas() {
		updatePwdType = getIntent().getIntExtra(INP_UPDATE_PAY_PASSWORD, 0);
	}

	@OnClick({R.id.update_pay_pwd_btn,R.id.tv_code_pay_pwd})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) { // 验证登录密码
			case R.id.update_pay_pwd_btn:
				/**验证码**/
				if (TextUtils.isEmpty(up_pay_pwd_edit_code.getText().toString())) {
					showToastMsg("验证码不能为空");
					return;
				} else if (TextUtils.isEmpty(up_pay_pwd_edit.getText().toString())) {
					showToastMsg("登录密码不能为空");
					return;
				}
				Dialog dialog = MMAlertDialog.createUpPwdDialog(this, this);
				dialog.show();
				break;
			case R.id.tv_code_pay_pwd:
				showLoadingDialog("");
				executeLoginPwd();
				break;
			default:
				break;
		}
	}

	/**
	 * @description：
	 * @author shicm
	 * @date 2015-12-7 下午4:22:14
	 */
	private void executeUpdatePwd(String pwd) {
		showLoadingDialog(""); // 确认按钮
		Type typeOfResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand updatePwdCommand = new RdpNetCommand(this, typeOfResult);
		updatePwdCommand.setOnCommandSuccessedListener(this);
		updatePwdCommand.setOnCommandFailedListener(this);
		updatePwdCommand.setServerApiUrl(UrlConstant.USER_CHANGEPASSWORD); // 修改支付密码
		updatePwdCommand.clearConditions();
		updatePwdCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		updatePwdCommand.setCondition("memberId", HKCloudApplication.getInstance().getMemberId(false));
		updatePwdCommand.setCondition("type", updatePwdType == 0 ? 5 : 2);
		updatePwdCommand.setCondition("oldPassWord", up_pay_pwd_edit.getText().toString());
		updatePwdCommand.setCondition("password", pwd);
		updatePwdCommand.setCondition("validateCode", up_pay_pwd_edit.getText().toString());
		updatePwdCommand.execute();
	}

	// 验证登录密码
	private void executeLoginPwd() {
		UserModel mUser = HKCloudApplication.getInstance().getUserModel();
		Type typeOfResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand updatePwdCommand = new RdpNetCommand(this, typeOfResult);
		updatePwdCommand.setOnCommandSuccessedListener(this);
		updatePwdCommand.setOnCommandFailedListener(this);
		updatePwdCommand.setServerApiUrl(UrlConstant.APP_GETSERVER_SMS); // 网络链接地址接口
		updatePwdCommand.clearConditions();
		updatePwdCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		updatePwdCommand.setCondition("type", 2);
		updatePwdCommand.setCondition("phone", HKCloudApplication.getInstance().getUserModel().getAccount());
		updatePwdCommand.execute();
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		dismissLoadingDialog();
		if (result.getUrl().equals(UrlConstant.APP_GETSERVER_SMS)) {
			showToastMsg(result.getMsg());
			if (timeCount == null)
				timeCount = new TimeCount(tv_code_pay_pwd);
			timeCount.start();
		} else if (result.getUrl().equals(UrlConstant.USER_CHANGEPASSWORD)) {
			showToastMsg(result.getMsg());
			finish();
		}
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		dismissLoadingDialog();
	}
	@Override
	public void onItemClickListener(View v, int position) {
		
	}

	@Override
	public void onDialogDismiss(String psw) {
		executeUpdatePwd(psw);
	}
}
