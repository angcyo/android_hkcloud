package com.zhoukl.androidRDP.RdpUtils;

import java.util.HashMap;
import java.util.Locale;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @description：
 * @author zhoukl(67073753@qq.com) 
 * @date 2015-8-31 上午10:38:01
 */
public class RdpLanguageUtil {
	public static String LANG_AUTO = "auto";
	private static HashMap<String, Locale> mLocaleMap;
	private static Locale mDefaultLocale = Locale.SIMPLIFIED_CHINESE;

	public static void initialize(HashMap<String, Locale> localeMap) {
	    mLocaleMap = localeMap;
	}
	
    public static void initialize(HashMap<String, Locale> localeMap, Locale defaultLocale) {
        initialize(localeMap);
        mDefaultLocale = defaultLocale;
    }

    public static String getSystemLanguage(Context context) {
        return Locale.getDefault().getLanguage();
    }

	/**
	 * 欢迎界面也需要调用此方法设置初始语言
	 *  author:zwy
	 */
	public static void setLanguage(Context context, String language) {
		Resources resources = context.getResources();
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
		if (mLocaleMap == null) {
            config.locale = mDefaultLocale;
		} else {
    		if (mLocaleMap.containsKey(language)) {
    		    config.locale = mLocaleMap.get(language);
    		} else {
    		    config.locale = mDefaultLocale;
    		}
		}
		resources.updateConfiguration(config, dm);
	}

}
