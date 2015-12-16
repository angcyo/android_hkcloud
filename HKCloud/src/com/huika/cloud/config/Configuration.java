package com.huika.cloud.config;

import com.huika.cloud.util.PreferHelper;

/**
 * @description：服务器环境的配置
 * @author shicm
 * @date 2015-11-17 上午10:10:16
 */
public class Configuration {
	// webip配置s // 正式环境
	public static final String WEB_RELEASE_SERVER_DOMAIN = "http://192.168.50.3:8083/hxlmpro-api";
	public static boolean IS_LOG = true;// 发布时改为false,不打LOG
	public static String DEBUG_TAG = "HKCLOUD";

	// 预生产环境
	public static final String WEB_RELEASE_TEST_SERVER_DOMAIN = "http://192.168.50.3:8083/hxlmpro-api";

	// 测试环境
	public static final String WEB_TEST_SERVER_DOMAIN = "http://192.168.50.3:8083/hxlmpro-api";

	public static final String BASE_WEB_SERVER = getWebServerDomain();

	/**
	  * @Description:获取web服务端配置路径
	  */
	private static final String getWebServerDomain() {
		if (isReleaseEnvironment()) {
			return WEB_RELEASE_SERVER_DOMAIN;
		} else if (isReleaseTestEnvironment()) {
			return WEB_RELEASE_TEST_SERVER_DOMAIN;
		} else {
			return WEB_TEST_SERVER_DOMAIN;
		}
	}

	public static final boolean isReleaseEnvironment() {
		return PreferHelper.getInstance().getBoolean(Constant.IS_RELEASE_ENVIRONMENT, true);
	}

	private static final boolean isReleaseTestEnvironment() {
		return PreferHelper.getInstance().getBoolean(Constant.IS_RELEASE_TEST_ENVIRONMENT, false);
	}

	private static final boolean isWebTestEnvironment() {
		return PreferHelper.getInstance().getBoolean(Constant.IS_WEB_TEST_ENVIRONMENT, false);
	}
}
