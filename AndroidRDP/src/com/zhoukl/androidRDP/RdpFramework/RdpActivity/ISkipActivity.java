package com.zhoukl.androidRDP.RdpFramework.RdpActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @description：跳转的接口
 * @date 2015-11-5 上午10:16:09
 */
public interface ISkipActivity {
	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	void skipActivity(Activity aty, Class<?> cls);

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	void skipActivity(Activity aty, Intent it);

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	void skipActivity(Activity aty, Class<?> cls, Bundle extras);

	/**
	 * show a @param(cls)，but can't finish activity
	 */
	void showActivity(Activity aty, Class<?> cls);

	/**
	 * show a @param(cls)，but can't finish activity
	 */
	void showActivity(Activity aty, Intent it);

	/**
	 * show a @param(cls)，but can't finish activity
	 */
	void showActivity(Activity aty, Class<?> cls, Bundle extras);
}
