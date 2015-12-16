package com.huika.hfans.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.utils.OneKeyShareCons;
import cn.sharesdk.onekeyshare.utils.OnekeyShareTool;
import cn.sharesdk.onekeyshare.view.MySharePopWindows;
import cn.sharesdk.onekeyshare.view.MySharePopWindows.GriViewOnImtermClick;

import com.huika.hfans.R;

public class ShareSimple extends Activity implements GriViewOnImtermClick {

	private Button shareBtn;

	/** 分享用PopWindow */
	private MySharePopWindows popWindows;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_dome);
		shareBtn = (Button) findViewById(R.id.button1);
		// 获取默认的分享pop
		popWindows = MySharePopWindows.getDefaultSharePop(this, this);
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(getApplicationContext()))
					popWindows.showPop(v);
			}
		});
	}

	@Override
	public void onGvOnItem(String title) {
		// TODO Auto-generated method stub
		ShareSDK.initSDK(this);
		String shareTitle = "分享约电";
		String shareStr = "我在约电,期待与你相遇~";// 分享内容
		String imagePath = OneKeyShareCons.LOGO_URL;// 约电logo,没有图片默认用logo
		OnekeyShareTool.initAndShowShare(this, shareTitle,
				OneKeyShareCons.siteUrlStr, shareStr
						+ OneKeyShareCons.siteUrlStr,
				OneKeyShareCons.siteUrlStr, imagePath,
				OneKeyShareCons.LOGO_URL, null, true, title, null);
	}

}
