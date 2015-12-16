package com.zhoukl.androidRDP.RdpFramework.RdpApp;

import android.os.Environment;


public class RdpConstant {
	// 正式、测试环境的切换功能
	public static final String IS_RELEASE_ENVIRONMENT = "IS_RELEASE_ENVIRONMENT";
	public static final String IS_HIDE_LOGOFF_BUTTON = "IS_HIDE_LOGOFF_BUTTON";
	public static final String IS_RELEASE_TEST_ENVIRONMENT = "IS_RELEASE_TEST_ENVIRONMENT";

	public static final int FLAG_NOTIFICATION_VALUE = 0;

	public static final String ENVIRONMENT_FLAG = getEnvironmentFlag();
	public static final String HOMEDIR = getHomeDIR();
	public static final String IMAGE_DIR = "/images/";
	public static final String VOICE_DIR = "/voices/";
	// 临时缓存目录
	public static final String TEMP_CACHE_DIR = HOMEDIR + "/temp/";
	
    private static final String getHomeDIR() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + RdpConfiguration.APP_NAME + ENVIRONMENT_FLAG;
    }


    /*************************************** 广播相关的常量  ******************************************************/
    
    public static final String BROADCAST_TAG = getBroadCastTag();    
    
    /**登录,或者需要监听用户信息变更的广播*/
    public static final String BROADCAST_LOGIN = BROADCAST_TAG + "LOGIN";
    /**登出*/
    public static final String BROADCAST_LOGOUT = BROADCAST_TAG + "LOGOUT";


    /** 数据库的名称 **/
	public static final String DATABASE_NAME = getDatabaseName();
	private static final String getDatabaseName() {
		return RdpConfiguration.APP_NAME + ENVIRONMENT_FLAG + ".db";
	}
	
	private static final String getEnvironmentFlag() {
        if (1 == 1) {
            return "release";
        } else if (2 == 2) {
            return "debug";
        } else {
            return "test";
        }
    }

	protected static String getBroadCastTag() {
	    return "com.zhoukl.androidRDP.";
	}
	 public static final String PROPERTY_LISTVIEW_EMPTY_RES_ID = "ro.property.listview.empty.res.id";
	 public static final String PROPERTY_LISTVIEW_ERROR_RES_ID = "ro.property.listview.error.res.id";
}
