package com.huika.cloud.util;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @description：分享
 * @author shicm
 * @date 2015-11-23 上午10:48:26
 */
public class ShareUtils {
	public static ShareUtils getInstantiation() {
		return new ShareUtils();
	}

	/** 分享 */
	public void ShareMall(Activity mContext, View View, String commodityID, String title, String shareStr, String img) {
		String product = "http://www.huikamall.com/home/shopping/" + commodityID + ".html";
		HKCloudShareTool mHkShareSdkTool = new HKCloudShareTool(mContext);
		mHkShareSdkTool.showDefaultSharePop(View, img, title, shareStr, product);
	}
}
