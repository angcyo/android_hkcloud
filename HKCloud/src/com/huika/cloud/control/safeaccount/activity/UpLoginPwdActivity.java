package com.huika.cloud.control.safeaccount.activity;

import java.lang.reflect.Type;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.PreferHelper;
import com.huika.cloud.views.ClearableEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpModel.BaseUserBean;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：修改登录密码
 * @author shicm
 * @date 2015-11-10 上午9:38:12
 */
public class UpLoginPwdActivity extends RdpBaseActivity {
	@ViewInject(R.id.up_old_pwd_edit)
	private ClearableEditText mEdtOldPwd;
	@ViewInject(R.id.up_new_pwd_edit)
	private ClearableEditText mEdtNewPwd;
	@ViewInject(R.id.up_pwd_btn_login)
	private Button mBtnSure;
	@ViewInject(R.id.tv_up_pwd_tophone)
	private TextView mTvPhone;
	
	private View mViews;
	private UserModel mUser;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle(R.string.update_pwd_title);
		mViews = addMasterView(R.layout.update_login_pwd);
		RdpAnnotationUtil.inject(this);
		mTvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); // 加下划线
	}


	@OnClick({R.id.up_pwd_btn_login,R.id.tv_up_pwd_tophone})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.up_pwd_btn_login:
				String oldPwd = mEdtOldPwd.getText().toString();
				String newPwd = mEdtNewPwd.getText().toString();
				if (TextUtils.isEmpty(oldPwd)) {
					showToastMsg("请输入原密码");
					return;
				}
				if (oldPwd.length() < 8) {
					showToastMsg("原密码不得低于8位");
					return;
				}
				if (TextUtils.isEmpty(newPwd)) {
					showToastMsg("请输入原密码");
					return;
				}
				if (newPwd.length() < 8) {
					showToastMsg("新密码不得低于8位");
					return;
				}
				executeUpdatePwd();
				break;
			case R.id.tv_up_pwd_tophone:
				showActivity(this, UpPhoneLoginPwdActivity.class);
				break;
			default:
				break;
		}
	}

	/**
	 * @description：修改密码网络请求
	 * @author shicm
	 * @date 2015-11-10 上午11:40:32
	 */
	private void executeUpdatePwd() {
		showLoadingDialog("");
		mUser = HKCloudApplication.getInstance().getUserModel();
		Type typeOfResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand updatePwdCommand = new RdpNetCommand(this, typeOfResult);
		updatePwdCommand.setOnCommandSuccessedListener(this);
		updatePwdCommand.setOnCommandFailedListener(this);
		updatePwdCommand.setServerApiUrl(UrlConstant.USER_CHANGEPASSWORD); // 修改登录密码
		updatePwdCommand.clearConditions();
		updatePwdCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		updatePwdCommand.setCondition("memberId", mUser.getMemberId());
		updatePwdCommand.setCondition("oldPassWord", mEdtOldPwd.getText().toString());
		updatePwdCommand.setCondition("passWord", mEdtNewPwd.getText().toString());
		updatePwdCommand.setCondition("type", 1); // 1登陆密码 2交易密码 3设置交易密码
		updatePwdCommand.setCondition("validateCode", "");
		updatePwdCommand.execute();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		dismissLoadingDialog();
		showToastMsg(result.getMsg());
		if (result.getCode() == 1) {
			PreferHelper.getInstance().saveLoginInfo(mUser.getAccount(), mEdtNewPwd.getText().toString());
			finish();
		}
	}
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		dismissLoadingDialog();
		showToastMsg(result.getMsg());
	}

}
