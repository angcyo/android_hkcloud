package com.huika.cloud.util;

import com.huika.cloud.control.base.HKCloudApplication;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * @description：PreferHelper
 * @author samy
 * @date 2014年9月17日 下午6:30:24
 */
public class PreferHelper {
	public static final String NAME = "hkCloud_preference";
	private static SharedPreferences sp;
	private static SharedPreferences.Editor editor;
	private static RdpApplication mApplication; 
	private static PreferHelper mInstance;
	/**
	 * 字段区
	 */
	/** 登录 */
	public static final String KEY_LOGIN_PHONE = "key_login_phone";
	public static final String KEY_LOGIN_PWD = "key_login_pwd";

	/** 定位 */
	public static final String CURRENT_LATITUDE = "Latitude";
	public static final String CURRENT_LONGITUDE = "Longitude";
	public static final String ADDRESS_LOCATION = "address_location";
	/** 版本更新 */
	public static final String LAST_VERSION_CHECK_TIME = "lastVersionCheckTime";
	public static final String LAST_CHECK_VERSION = "lastCheckVersion";
	/**最近的搜索词*/
	public static final String LAST_SEARCH_KEYWORD = "lastSearchKeyword";
	/**一元购最近的搜索词*/
	public static final String ONE_PIECE_LAST_SEARCH_KEYWORD = "onePieceLastSearchKeyword";
	/**商品分类更新时间*/
	public static final String PRODUCT_CATEGORY_UPDATETIME = "productCategoryUpdatetime";
	/**银行卡列表是否展开*/
	public static final String MY_BANKLIST_ISSHOWN = "myBankListShown";
	public static final String MY_BANKLIST_SHOWN_INDEX = "myBankListShownIndex";
	/**是否登录成功*/
	public static final String HAD_LOGIN = "had_login";

	private PreferHelper() {
		 sp = HKCloudApplication.getInstance().getSharedPreferences(NAME, 0);
	     editor = sp.edit();
	}

	public static void initialize(RdpApplication app) {
	    mApplication = app;
	    sp = mApplication.getSharedPreferences(NAME, 0);
        editor = sp.edit();
	}
	

	// 增加了双重判断
	public static PreferHelper getInstance() {
		if (null == mInstance) {
			synchronized (PreferHelper.class) {
				if (null == mInstance) mInstance = new PreferHelper();
			}
		}
		return mInstance;
	}

	
	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public  int getInt(String key) {
        return getInt(key, -1);
    }

    public  int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

	public  long getLong(String key) {
		return sp.getLong(key, 1);
	}

	public  boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	/**
	 * @description：移除特定的
	 * @date 2014年11月5日 下午4:30:08
	 */
	public static void remove(String name) {
		editor.remove(name);
		editor.commit();
	}

	/**
	 * 保存登录相关信息
	 * @param phone
	 * @param pwd
	 * @author FAN 创建于Dec 1, 2014
	 */
	public static void saveLoginInfo(String phone, String pwd) {
		editor.putString(KEY_LOGIN_PHONE, phone);
		editor.putString(KEY_LOGIN_PWD, pwd);
		editor.commit();
	}
}
