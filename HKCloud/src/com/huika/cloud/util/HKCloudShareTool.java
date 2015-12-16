package com.huika.cloud.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpUtils.NetUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.utils.OnekeyShareTool;
import cn.sharesdk.onekeyshare.view.MySharePopWindows;
import cn.sharesdk.onekeyshare.view.MySharePopWindows.GriViewOnImtermClick;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @description：惠云分享工具
 * @author shicm
 * @date 2015-11-23 上午10:46:38
 */
public class HKCloudShareTool implements GriViewOnImtermClick {
	/** 右边的按钮 分享 */
	private Activity mContext;
	private String imagePath = ""; // 图片logo地址
	private String shareTitle = "";
	private String shareStr = "";
	private String shareUrl = ""; // 分享的地址
	private MySharePopWindows popWindows;

	public HKCloudShareTool(Activity mContext) {
		this.mContext = mContext;
	}

	public void showDefaultSharePop(View v, String imagePath, String shareTitle, String shareStr, String shareUrl) {
		this.imagePath = imagePath;
		this.shareTitle = shareTitle;
		this.shareStr = shareStr;
		this.shareUrl = shareUrl;
		String[] titles = new String[] {mContext.getString(R.string.wechat), mContext.getString(R.string.wechatmoments), 
				mContext.getString(R.string.qq), mContext.getString(R.string.qzone)};
		String[] platforms = new String[] {Wechat.NAME,WechatMoments.NAME, QQ.NAME, QZone.NAME};
		Drawable[] resImgs = new Drawable[] {mContext.getResources().getDrawable(R.drawable.pm_logo_wechat),
				 mContext.getResources().getDrawable(R.drawable.pm_logo_wechatmoments), mContext.getResources().getDrawable(R.drawable.pm_logo_qq),
				mContext.getResources().getDrawable(R.drawable.pm_logo_qzone)};
		popWindows = new MySharePopWindows(mContext, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, titles, platforms, resImgs, this);
		if (TextUtils.isEmpty(shareUrl)) {
			ToastMsg.showToastMsg(mContext, "网页地址获取失败");
			return;
		}
		if (NetUtil.isNetworkAvailable(mContext)) {
			popWindows.showPop(v);
		} else {
			ToastMsg.showToastMsg(mContext, "无网络，请先联网");
		}
	}

	@Override
	public void onGvOnItem(String title) {

		if (TextUtils.isEmpty(shareTitle)) {
			shareTitle = shareUrl;
		}
		String shareTitleWXMember = shareTitle;

		if (!shareTitleWXMember.equals(shareStr)) {
			shareTitleWXMember = shareTitleWXMember + "。" + shareStr;
		}

		if (TextUtils.isEmpty(shareStr)) {
			shareStr = shareTitle;
		}

		if (TextUtils.isEmpty(shareTitleWXMember)) {
			shareTitleWXMember = shareUrl;
		}
		if (TextUtils.isEmpty(imagePath)) {
			imagePath = ""; // 固定的地址
		}
		ShareSDK.initSDK(mContext);
		OnekeyShareTool.initAndShowShare(mContext, shareTitle, shareTitleWXMember, shareUrl, shareStr, shareUrl, imagePath, "惠卡", null, true, title, null);
	}

}
