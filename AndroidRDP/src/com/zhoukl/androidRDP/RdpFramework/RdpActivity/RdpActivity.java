package com.zhoukl.androidRDP.RdpFramework.RdpActivity;

import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpAppProperties;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * @description：所有Activity的基类，用于项目生命周期的管理等 ,预留
 * @author zhoukl
 * @date 2015年1月19日 上午11:09:46
 */
public class RdpActivity extends FragmentActivity{
    RdpAppProperties mRdpAppProperties;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		mRdpAppProperties = RdpApplication.mRdpAppProperties;
	}

}
