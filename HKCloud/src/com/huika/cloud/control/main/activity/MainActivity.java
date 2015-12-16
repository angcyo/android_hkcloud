package com.huika.cloud.control.main.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.huika.cloud.BuildConfig;
import com.huika.cloud.R;
import com.huika.cloud.config.BroadcastConstants;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.cart.fragment.ShoppingCartFragment;
import com.huika.cloud.control.goods.fragment.GoodsFragment;
import com.huika.cloud.control.home.fragment.HomeFragment;
import com.huika.cloud.control.me.MeFragment;
import com.huika.cloud.support.event.MainPagerChangeEvent;
import com.huika.cloud.util.RdpVersionManager;
import com.huika.cloud.views.ChangeColorIconWithTextView;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import de.greenrobot.event.EventBus;

/**
 * @description：项目首页
 * @author shicm
 * @date 2015-11-5 上午11:53:43
 */
public class MainActivity extends RdpBaseActivity implements OnPageChangeListener, OnClickListener {
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;

	private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
	private boolean isExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this);

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		initDatas();
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		new RdpVersionManager(this, UrlConstant.SUPPORT_GETVERSIONINFO).checkVersion(true);

	}

	private void initDatas() {
		if (BuildConfig.DEBUG) {
//			mTabs.add(new MeFragment());
			mTabs.add(new HomeFragment());
		}else{
			mTabs.add(new MeFragment());
//			mTabs.add(new HomeFragment());
		}
	
		mTabs.add(new GoodsFragment());
		mTabs.add(new ShoppingCartFragment());
		mTabs.add(new MeFragment());

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				if (arg0 == 2) {
					Bundle bundle = new Bundle();
					bundle.putBoolean(ShoppingCartFragment.INP_BACK, true);
					mTabs.get(arg0).setArguments(bundle);
				}
				return mTabs.get(arg0);
			}
		};
		initTabIndicator();
	}

	private void initTabIndicator() {
		ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
		ChangeColorIconWithTextView four = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);
		mTabIndicator.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			isExit = false;
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isExit) {
				ToastMsg.showToastMsg(this, "再按一次退出应用");
				isExit = true;
				mHandler.sendEmptyMessageDelayed(0, 1000);
				return true;
			} else {
				sendBroadcast(new Intent(BroadcastConstants.ACTION_EXIT_SYSTEM));
			}
		}
		return super.onKeyDown(keyCode, event);
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (positionOffset > 0) {
			ChangeColorIconWithTextView left = mTabIndicator.get(position);
			ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	/** 用EventBus，通知切换界面 */
	public void onEventMainThread(MainPagerChangeEvent event) {
		// resetOtherTabs();
		// mTabIndicator.get(event.changePageNo).setIconAlpha(1.0f);
		// mViewPager.setCurrentItem(event.changePageNo, false);

		setCurrentItem(event.changePageNo);// hky 2015-12-14
	}

	private void setCurrentItem(int item) {
		resetOtherTabs();
		mTabIndicator.get(item).setIconAlpha(1.0f);
		mViewPager.setCurrentItem(item, false);
	}

	@Override
	public void onClick(View v) {

		setCurrentItem(Integer.valueOf((String) v.getTag()));// hky 2015-12-14
		

		// resetOtherTabs();
		//
		// switch (v.getId()) {
		// case R.id.id_indicator_one:
		// mTabIndicator.get(0).setIconAlpha(1.0f);
		// mViewPager.setCurrentItem(0, false);
		// break;
		// case R.id.id_indicator_two:
		// mTabIndicator.get(1).setIconAlpha(1.0f);
		// mViewPager.setCurrentItem(1, false);
		// break;
		// case R.id.id_indicator_three:
		// mTabIndicator.get(2).setIconAlpha(1.0f);
		// mViewPager.setCurrentItem(2, false);
		// break;
		// case R.id.id_indicator_four:
		// mTabIndicator.get(3).setIconAlpha(1.0f);
		// mViewPager.setCurrentItem(3, false);
		// break;
		//
		// }

	}

	/**
	 * 重置其他的Tab
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
