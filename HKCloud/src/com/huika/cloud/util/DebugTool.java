package com.huika.cloud.util;

import com.huika.cloud.config.Configuration;

import android.util.Log;


public class DebugTool {
	/**
	 * 方法概述：仿C，打印调试信息
	 * 
	 * @description 方法详细描述：
	 * @author 刘成伟（wwwlllll@126.com）
	 * @param @param paramString
	 * @return void
	 * @throws
	 * @Title: DebugTool.java
	 * @Package com.huika.huixin.utils
	 * @date 2014-5-23 下午1:58:17
	 */
	public static void print(String paramString) {
		if (Configuration.IS_LOG) {
			StackTraceElement stack = (new Throwable()).getStackTrace()[1];
			StringBuilder builder = new StringBuilder();
			builder.append("行号:" + stack.getLineNumber());
			builder.append("<->方法:" + stack.getMethodName());
			builder.append("<->文件:" + stack.getFileName());
			builder.append("<->信息:" + paramString);
			Log.i(Configuration.DEBUG_TAG, builder.toString());
		}
	}

	/**
	 * 打印调试信息.
	 */
	public static void debug(String msg) {
		if (Configuration.IS_LOG) {
			Log.d(Configuration.DEBUG_TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (Configuration.IS_LOG) {
			Log.d(tag, msg);
		}
	}

	/**
	 * 打印警告信息.
	 */
	public static void warn(String msg) {
		if (Configuration.IS_LOG) {
			Log.w(Configuration.DEBUG_TAG, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (Configuration.IS_LOG) {
			Log.w(tag, msg);
		}
	}

	/**
	 * 打印提示信息.
	 * 
	 * @param msg
	 */
	public static void info(String msg) {
		if (Configuration.IS_LOG) {
			Log.i(Configuration.DEBUG_TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (Configuration.IS_LOG) {
			Log.i(tag, msg);
		}
	}

	/**
	 * 打印错误信息.
	 */
	public static void error(String msg, Exception e) {
		if (Configuration.IS_LOG) {
			Log.e(Configuration.DEBUG_TAG, msg, e);
		}
	}

	public static void e(String msg, Exception e) {
		if (Configuration.IS_LOG) {
			Log.e(Configuration.DEBUG_TAG, msg, e);
		}
	}

	public static void v(String tag, String msg) {
		if (Configuration.IS_LOG) {
			Log.v(tag, msg);
		}
	}

}
