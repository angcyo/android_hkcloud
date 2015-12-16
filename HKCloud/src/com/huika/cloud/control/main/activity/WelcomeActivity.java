package com.huika.cloud.control.main.activity;

import java.io.File;

import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.util.PreferHelper;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpSplashActivity;


import android.content.SharedPreferences;
import android.widget.ImageView;


/**
 * @description：启动界面效果
 * @author samy
 * @date 2014年9月16日 上午11:25:04
 */
public class WelcomeActivity extends RdpSplashActivity {

	private SharedPreferences.Editor edit;
	private SharedPreferences sp;

	@Override
	protected void initActivity() {
		// TODO Auto-generated method stub
		super.initActivity();
		sp = getSharedPreferences(PreferHelper.NAME, 0);
		edit = sp.edit();
		initView();
	}
	@Override
	protected void setRootBackground(ImageView view) {
		view.setBackgroundResource(R.drawable.welcome_bg);
		animationDuration = 2000;
	}
    @Override
	protected void initWidget() {
    	super.initWidget();
		// 自动登录
		if (LoginHelper.getInstance(HKCloudApplication.getInstance()).isAutoLogin()) {
			String phone = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PHONE);
			String pwd = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
			LoginHelper.getInstance(HKCloudApplication.getInstance()).executeLoginRequest(phone, pwd);
		}
	}

	/**
	 * 重写父类的redirectTo方法，没有检查版本控制
	 */
	@Override
	protected void redirectTo() {
		forwardStep();
	}

	/**
	 * 下一步跳转
	 *
	 * @author FAN 创建于Dec 1, 2014
	 */
	private void forwardStep() {

		if (firstsInstall()) {// 首次安装
//			skipActivity(this, GuideViewAct.class); // 首次安装
			skipActivity(this, MainActivity.class);
		} else {
			skipActivity(this, MainActivity.class);
		}
	}

	@Override
	protected boolean firstsInstall() {
		File files = getFilesDir();
		File installFile = new File(files, "install");

		int newVC = 0;
		try {
			newVC = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (Exception e) {
		}

		boolean firstInstall = installFile.exists();
		if (!firstInstall) {// 文件夹不存在，则表示初次安装
			installFile.mkdirs();
			try {
				new File(installFile, newVC + "").createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			edit.putBoolean(Constant.IS_FIRST_INSTALL, true);
			edit.commit();
			return true;
		} else {
			String[] fs = installFile.list();
			if (fs == null || fs.length == 0) {// 上一个版本为空
				try {
					new File(installFile, newVC + "").createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			String lastV = fs[0];
			if (newVC > Integer.parseInt(lastV)) {
				try {
					new File(installFile, newVC + "").createNewFile();
					for (String vf : fs) {
						File temp = new File(installFile, vf);
						if (temp.exists()) temp.delete();
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			edit.putBoolean(Constant.IS_FIRST_INSTALL, false);
			edit.commit();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}