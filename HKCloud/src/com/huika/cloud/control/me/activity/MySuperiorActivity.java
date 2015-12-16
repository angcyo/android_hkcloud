package com.huika.cloud.control.me.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：我的上级
 * @author ht
 * @date 2015-12-4 上午10:23:33
 */
public class MySuperiorActivity extends RdpBaseActivity {
	private View mMasterView;
	@ViewInject(R.id.iv_Superior_user)
	private ImageView mImgPhoto;
	@ViewInject(R.id.tv_user_name)
	private TextView tv_user_name;
	
	
    private long mPreClickTime;
	private int mCount;
	private static final int SWITCH_VERSION_CLK_NUM = 3;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("我的上级");
		mMasterView = addMasterView(R.layout.my_superior);
		RdpAnnotationUtil.inject(this);
	}
	
	@OnClick({R.id.iv_Superior_user,R.id.tv_user_name})
	@Override
	public void onClick(View v) {
		 long now = 0;
		switch (v.getId()) {
			case R.id.iv_Superior_user:
				now = System.currentTimeMillis();
		        if (now - mPreClickTime < 300) {
		          mCount++;
		        } else {
		          mCount = 1;
		        }
		        mPreClickTime = now;
				break;
			case R.id.tv_user_name:
				  now = System.currentTimeMillis();
			        if (mCount >= SWITCH_VERSION_CLK_NUM && now - mPreClickTime < 500) {
			          showActivity(this, MilitaryForbidAreaActivity.class);
			        }
				break;

			default:
				break;
		}
		super.onClick(v);
	}
}
