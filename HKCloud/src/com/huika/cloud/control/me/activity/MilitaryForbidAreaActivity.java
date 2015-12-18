package com.huika.cloud.control.me.activity;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.util.PreferHelper;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * @author zhoukl
 * @description：军事禁区，一般用户请绕行，否则后果自负， 嘿嘿……
 * @date 2014年10月29日 上午11:30:45
 */
public class MilitaryForbidAreaActivity extends RdpBaseActivity {
	private View mView;
	private RadioGroup rgEnvironment;
	private RadioButton mrbRelease, mrbReleaseTest, mrbTest, onLineRbTest;
	private TextView tvServerDomain, tvIMServerDomain;
	private Button btnOk, btnCancel;
	private boolean mIsReleaseEnv;// 正式环境
	private boolean mIsOnlineEnv;// 测试用的线上环境 9005
	private boolean mIsPreReleaseEnv;// 预生产环境

	protected void initActivity() {
		mView = addMasterView(R.layout.military_forbid_area_activity);
		initView();
	}

	private void initView() {
		mrbRelease = (RadioButton) mView.findViewById(R.id.rbRelease);
		mrbReleaseTest = (RadioButton) mView.findViewById(R.id.rbReleaseTest);
		mrbTest = (RadioButton) mView.findViewById(R.id.rbTest);
		onLineRbTest = (RadioButton) mView.findViewById(R.id.online_rbTest);
		btnOk = (Button) mView.findViewById(R.id.btnOk);
		btnCancel = (Button) mView.findViewById(R.id.btnCancel);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		tvServerDomain = (TextView) mView.findViewById(R.id.tvServerDomain);
		tvIMServerDomain = (TextView) mView.findViewById(R.id.tvIMServerDomain);

		rgEnvironment = (RadioGroup) mView.findViewById(R.id.rgEnvironment);
		rgEnvironment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.rbRelease:
						tvServerDomain.setText(UrlConstant.RELEASE_SERVER_DOMAIN);
						break;
					case R.id.rbReleaseTest:
						tvServerDomain.setText(UrlConstant.RELEASE_TEST_SERVER_DOMAIN);
						break;
					case R.id.rbTest:
						tvServerDomain.setText(UrlConstant.TEST_SERVER_DOMAIN);
						break;
				// case R.id.online_rbTest:
				// tvServerDomain.setText(UrlConstant.ONLINE_SERVER_DOMAIN);
				// break;
				}
			}
		});

		// 默认正式环境
		mIsReleaseEnv = PreferHelper.getInstance().getBoolean(Constant.IS_RELEASE_ENVIRONMENT, true);
		mIsPreReleaseEnv = PreferHelper.getInstance().getBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, false);
		mIsOnlineEnv = PreferHelper.getInstance().getBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, false);
		if (mIsReleaseEnv) {
			mrbRelease.setChecked(true);
		} else if (mIsPreReleaseEnv) {
			mrbReleaseTest.setChecked(true);
		} else if (mIsOnlineEnv) {
			onLineRbTest.setChecked(true);
		} else {
			mrbTest.setChecked(true);
		}
	}

	protected void switchVersionEnvironment() {
		// TODO: 显示测试版本，发布版本的开关
		if (mrbRelease.isChecked()) {
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_ENVIRONMENT, true);
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, false);
		} else if (mrbReleaseTest.isChecked()) {
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, true);
			PreferHelper.getInstance().setBoolean(Constant.IS_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, false);
		} else if (mrbTest.isChecked()) {
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_TEST_ENVIRONMENT, true);
			PreferHelper.getInstance().setBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, false);
		} else if (onLineRbTest.isChecked()) {
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_TEST_ENVIRONMENT, false);
			PreferHelper.getInstance().setBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, true);
		}
		/** 发送下线通知给服务器 */
		showLoadingDialog("正在切换版本环境...");
		loadingDialog.setCancelable(false);
		// 强制清空版本信息， 促使系统重新处理【yfc_res.zip】包
		getSharedPreferences("init", 0).edit().putInt("version_name_int", 0).commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnOk:
				switchVersionEnvironment();
				break;
			case R.id.btnCancel:
				finish();
		}
	}
}
