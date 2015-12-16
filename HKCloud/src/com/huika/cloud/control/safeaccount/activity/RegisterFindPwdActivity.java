package com.huika.cloud.control.safeaccount.activity;


import java.lang.reflect.Type;

import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.main.activity.MainActivity;
import com.huika.cloud.support.event.MainPagerChangeEvent;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.PhoneUtil;
import com.huika.cloud.util.PreferHelper;
import com.huika.cloud.util.TimeCount;
import com.huika.cloud.views.ClearableEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpModel.BaseUserBean;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import de.greenrobot.event.EventBus;

/**
 * @description：注册和找回密码界面页面
 * @author shicm
 * @date 2015-11-9 上午11:19:49
 */
public class RegisterFindPwdActivity extends RdpBaseActivity {
	public static final String INP_REGITSER = "REGISTER"; 
	private boolean isRegiter = false;  // 判断是否是注册和找回密码，true是注册，false找回密码
	private View mView; 
	@ViewInject(R.id.edit_phone)
	private ClearableEditText mEdtPhone; // 手机号码
	@ViewInject(R.id.edit_code)
	private ClearableEditText mEdtCode; // 验证码
	@ViewInject(R.id.pwd_edit)
	private EditText mEdtPwd; // 手机号码
	@ViewInject(R.id.tv_code)
	private TextView tv_code; // 获取验证码的按钮
	@ViewInject(R.id.cb_showpw)
	private CheckBox cb_showpw; // 是否展示密码
	@ViewInject(R.id.cb_protocol)
	private CheckBox cb_protocol; // 是否展示密码
	
	@ViewInject(R.id.tv_protocol)
	private TextView tv_protocol; // 用户协议
	@ViewInject(R.id.btn_comfirm)
	private Button mBtnConfirm; // 确认按钮
	private RdpNetCommand mRdpnetRequest;
	
	private TimeCount timeCount = null;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		isRegiter = getIntent().getBooleanExtra(INP_REGITSER, false);
	    setFuncTitle(getString(isRegiter ? R.string.register_title : R.string.find_pwd_title));
		mView = addMasterView(R.layout.register_findpwd_hkclound);
		RdpAnnotationUtil.inject(this);
		mEdtCode.setEnabled(false);
		cb_protocol.setVisibility(isRegiter ? View.VISIBLE: View.GONE);
		cb_showpw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mEdtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				} else {
					mEdtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}

			}
		});
	}

//	private void initView() {
//		mEdtPhone = (ClearableEditText) mView.findViewById(R.id.edit_phone);
//		mEdtCode = (ClearableEditText) mView.findViewById(R.id.edit_code);
//		mEdtPwd = (EditText) mView.findViewById(R.id.pwd_edit);
//		tv_code = (TextView) mView.findViewById(R.id.tv_code);
//		cb_showpw = (CheckBox)mView.findViewById(R.id.cb_showpw);
//		cb_protocol = (CheckBox)mView.findViewById(R.id.cb_protocol);
//		mBtnConfirm = (Button)mView.findViewById(R.id.btn_comfirm);
//		tv_protocol = (TextView) mView.findViewById(R.id.tv_protocol);
//	
//	}

//	private void initLister() {
//		
//		tv_code.setOnClickListener(this);
//		tv_protocol.setOnClickListener(this);
//		mBtnConfirm.setOnClickListener(this);
//	}
	
	@OnClick({R.id.btn_comfirm,R.id.tv_protocol, R.id.tv_code})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_comfirm:
				if(!PhoneUtil.IsPhoneStr(mEdtPhone.getText().toString())) {
					showToastMsg("请填写正确的手机号码");
					return;
				} 
				if (TextUtils.isEmpty(mEdtCode.getText().toString()) ) {
					showToastMsg("请获取验证码并填写");
					return;
				}
				if (TextUtils.isEmpty(mEdtPwd.getText().toString()) ) {
					showToastMsg("请填写密码");
					return;
				}
				if (!cb_protocol.isChecked()) {
					showToastMsg("请选择用户协议");
					return;
				}
				showLoadingDialog("");
				executeRequest();
				break;
			case R.id.tv_protocol:
				showToastMsg("用户协议");
				break;
			case R.id.tv_code:
				String phone = mEdtPhone.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					showToastMsg("手机号码不能为空");
					return;
				} else if (!PhoneUtil.IsPhoneStr(phone)) {
					showToastMsg("请填写正确的手机号码");
					return;
				}
				executeCode();
				break;
			default:
				break;
		}
	}

	/**
	 * @description：请求验证码
	 * @author shicm
	 * @date 2015-11-9 下午5:09:34
	 */
	private void executeCode() {
		showLoadingDialog("");
		Type codeResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand mCodeRequest = new RdpNetCommand(this, codeResult);
		mCodeRequest.setOnCommandSuccessedListener(this);
		mCodeRequest.setOnCommandFailedListener(this);
		mCodeRequest.setServerApiUrl(UrlConstant.APP_GETSERVER_SMS); //网络链接地址接口
		mCodeRequest.clearConditions(); 
		mCodeRequest.setCondition("phone", mEdtPhone.getText().toString());
		mCodeRequest.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		mCodeRequest.setCondition("type", isRegiter? 1 : 4); // 1:注册、2：修改支付密码 3:提现 4:找回密码
		mCodeRequest.execute();
	}

	private void executeRequest() {
		Type typeOfResult;
		if (isRegiter) {
			typeOfResult = new TypeToken<UserModel>() {
			}.getType();
		} else {
			typeOfResult = new TypeToken<String>() {
			}.getType();
		}
		mRdpnetRequest = new RdpNetCommand(this, typeOfResult);
		mRdpnetRequest.setOnCommandSuccessedListener(this);
		mRdpnetRequest.setOnCommandFailedListener(this);
		mRdpnetRequest.setServerApiUrl(isRegiter? UrlConstant.USER_REGISTER : UrlConstant.USER_FINDPASSWORD); //网络链接地址接口
		mRdpnetRequest.clearConditions(); 
		mRdpnetRequest.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		mRdpnetRequest.setCondition("phone", mEdtPhone.getText().toString());
		mRdpnetRequest.setCondition("validateCode", mEdtCode.getText().toString());
		mRdpnetRequest.setCondition("password", mEdtPwd.getText().toString());
		mRdpnetRequest.execute();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		dismissLoadingDialog();
		if (result.getUrl().equals(UrlConstant.APP_GETSERVER_SMS)) {
			mEdtCode.setEnabled(true);
			showToastMsg(result.getMsg());
			 if (timeCount == null) timeCount = new TimeCount(tv_code);
	            timeCount.start();
		} else if (result.getUrl().equals(UrlConstant.USER_REGISTER)) {
			UserModel mUser = (UserModel) data;
			PreferHelper.getInstance().saveLoginInfo(mUser.getAccount(), mEdtPwd.getText().toString());
			HKCloudApplication.getInstance().setCurrUser(mUser);
			EventBus.getDefault().post(new MainPagerChangeEvent(0));
			skipActivity(this, MainActivity.class);
		}  else {
			finish();
		}
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		dismissLoadingDialog();
		/*if (result.getUrl().equals(UrlConstant.REGISTER_SMS) || result.getUrl().equals(UrlConstant.USER_GETFINDPASSWORDSMSCODE)
				|| result.getUrl().equals(UrlConstant.USER_FINDPASSWORD)) {
		}*/
		showToastMsg(result.getMsg());
	}
}
