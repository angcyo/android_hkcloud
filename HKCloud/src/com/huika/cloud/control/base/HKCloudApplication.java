package com.huika.cloud.control.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;

import com.huika.cloud.config.BroadcastConstants;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.db.areainfo.AreaInfoDaoMaster;
import com.huika.cloud.support.model.UserModel;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpAppInitialize;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;

/**
 * @description：全局类
 * @author shicm
 * @date 2015-11-17 下午3:11:19
 */
public class HKCloudApplication extends RdpApplication {
	public static final String MERCHANTID = "402881e8461795c201461795c2e90000";
	public static AreaInfoDaoMaster mAreaInfoDaoMaster;
	private static HKCloudApplication mInstance;
	private UserModel mUser;
	private BroadcastReceiver receiverExitSystem = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			HKCloudApplication.this.onTerminate();
		}
	};

	public static HKCloudApplication getInstance() {
		return mInstance;
	}

	public static AreaInfoDaoMaster getAreaInfoDaoMaster() {
		if (mAreaInfoDaoMaster == null) {
			synchronized (HKCloudApplication.class) {
				if (mAreaInfoDaoMaster == null) {
					String path = AreaInfoDaoMaster.createOrUpdate(getInstance());
					SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
					mAreaInfoDaoMaster = new AreaInfoDaoMaster(db);
				}
			}
		}
		return mAreaInfoDaoMaster;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
		IntentFilter exit_system_filter = new IntentFilter();
		exit_system_filter.addAction(BroadcastConstants.ACTION_EXIT_SYSTEM);
		registerReceiver(receiverExitSystem, exit_system_filter);
	}

	@Override
	protected RdpAppInitialize getAppInitialize() {
		return new HKCloudAppInitialize();
	}

	@Override
	public boolean isLogin(boolean jumpToLoginActivity) {
		if (mCurrUser == null) {
			if (jumpToLoginActivity) {
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			return false;
		} else {
			return true;
		}
	}

	public String getMemberId(boolean jumpToLoginActivity) {
		if (isLogin(jumpToLoginActivity)) {
			return mCurrUser.memberId;
		} else {
			return "";
		}
	}

	public void setCurrUser(UserModel user) {
		this.mUser = user;
	}

	public UserModel getUserModel() {
		if (mUser == null) { return new UserModel(); }
		return mUser;
	}

}
