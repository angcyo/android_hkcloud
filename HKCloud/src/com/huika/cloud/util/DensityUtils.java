package com.huika.cloud.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 系统屏幕的一些操作<br>
 * <b>创建时间</b> 2014-8-14
 * 
 * @version 1.1
 */
public final class DensityUtils {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 */
	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取dialog宽度
	 */
	public static int getDialogW(Activity aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels - 100;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
		return w;
	}

	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenW(Activity aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth();
		return w;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getScreenH(Activity aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int h = dm.heightPixels;
		// int h = aty.getWindowManager().getDefaultDisplay().getHeight();
		return h;
	}
	
	
	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenW(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int w = dm.widthPixels;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth();
		return w;
	}
	
	/**
	 * 获取屏幕高度
	 */
	public static int getScreenH(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int h = dm.heightPixels;
		// int h = aty.getWindowManager().getDefaultDisplay().getHeight();
		return h;
	}
}