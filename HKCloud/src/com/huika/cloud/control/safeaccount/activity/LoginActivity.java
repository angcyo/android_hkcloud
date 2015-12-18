package com.huika.cloud.control.safeaccount.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.support.event.LoginEvent;
import com.huika.cloud.views.ClearableEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import de.greenrobot.event.EventBus;

/**
 * @description：登录界面
 * @author shicm
 * @date 2015-11-5 下午5:37:13
 */
public class LoginActivity extends RdpBaseActivity {
	public static final String INP_WHERE = "TO_WHERE";
	private View mLoginView;
	@ViewInject(R.id.login_edit_name)
	private ClearableEditText mEdtPhone;
	@ViewInject(R.id.login_edit_pwd)
	private EditText mEdtPwd;
	@ViewInject(R.id.btn_login)
	private Button mBtnSure;
	@ViewInject(R.id.tv_findPwd)
	private TextView mFindPwdTv;
	@ViewInject(R.id.cb_show_login_pwd)
	private CheckBox cb_showpw; // 是否展示密码

	private String phone;
	private String pwd;
	private String version;

	private String toWhereClass = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initdatas();
	}

	private void initdatas() {
		toWhereClass = getIntent().getStringExtra("INP_WHERE");
	}

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle(R.string.login_str_titel);
		addRightFuncTextView("注册", new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle mBundle = new Bundle();
				mBundle.putBoolean(RegisterFindPwdActivity.INP_REGITSER, true);
				showActivity(LoginActivity.this, RegisterFindPwdActivity.class, mBundle);
			}
		}, 1);
		mLoginView = addMasterView(R.layout.login_hkclound);
		RdpAnnotationUtil.inject(this);
		EventBus.getDefault().register(this);
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

    @OnClick({R.id.btn_login,R.id.tv_findPwd})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_login:
				if (TextUtils.isEmpty(mEdtPhone.getText().toString())) {
					showToastMsg(R.string.login_username_hint);
					return;
				} else if (TextUtils.isEmpty(mEdtPwd.getText().toString())) {
					showToastMsg(R.string.login_pwd);
					return;
				}
				showLoadingDialog("");
				phone = mEdtPhone.getText().toString();
				pwd = mEdtPwd.getText().toString();
				closeInput();
				/*
				 * if (null != getCurrentVersionInfo()) {
				 * version = getCurrentVersionInfo().versionName;
				 * }
				 */
				LoginHelper.getInstance(this).executeLoginRequest(phone, pwd);
				break;
			case R.id.tv_findPwd:
				showActivity(LoginActivity.this, RegisterFindPwdActivity.class);
				break;
			default:
				break;
		}
	}

	public void closeInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && this.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(LoginEvent event) {
		dismissLoadingDialog();
		if (event.isSuceess) {
		//TODO 登录成功以后跳转到对应的activity
//			Intent toIntent = getIntent();
//			if (TextUtils.isEmpty(toWhereClass)) {
//				toIntent.setClass(this, MainActivity.class);
//				EventBus.getDefault().post(new MainPagerChangeEvent(0));
//			} else {
//				try {
//					toIntent.setClass(this, Class.forName(toWhereClass));
//				} catch (ClassNotFoundException e) {
//					toIntent.setClass(this, MainActivity.class);
//					EventBus.getDefault().post(new MainPagerChangeEvent(0));
//				}
//			}
//			toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(toIntent);
			finish();
		} else {
			// 原因不为空并且非自动登陆才展示原因
			if (!TextUtils.isEmpty(event.msg)) {
				showToastMsg(event.msg);
			}
		}
	}

}
