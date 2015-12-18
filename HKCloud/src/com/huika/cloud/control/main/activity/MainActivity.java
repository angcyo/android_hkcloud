package com.huika.cloud.control.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @description：项目首页
 * @author shicm
 * @date 2015-11-5 上午11:53:43
 */
public class MainActivity extends RdpBaseActivity implements OnPageChangeListener, OnClickListener {
	private ViewPager mViewPager;
	private ArrayList<Fragment> mTabs = new ArrayList<Fragment>();
	private MainPagerAdapter mAdapter;
//	private Set<Integer> initFrags;

	private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
	private boolean isExit;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			isExit = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this);

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		initDatas();
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
//		initFrags = new HashSet<Integer>();
//		initFrags.add(0);
		onPageSelected(0);
		new RdpVersionManager(this, UrlConstant.SUPPORT_GETVERSIONINFO).checkVersion(true);

	}

	private void initDatas() {
		if (BuildConfig.DEBUG) {
			// mTabs.add(new MeFragment());
			mTabs.add(new HomeFragment());
		} else {
			mTabs.add(new MeFragment());
			// mTabs.add(new HomeFragment());
		}

		mTabs.add(new GoodsFragment());
		mTabs.add(new ShoppingCartFragment());
		mTabs.add(new MeFragment());
		mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mTabs);
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
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	;

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
//		if (!initFrags.contains(arg0)) {
//			initFrags.add(arg0);
//			Fragment faFragment = mTabs.get(arg0);
//			if (faFragment instanceof RdpBaseFragment)
//				((RdpBaseFragment)faFragment).initFristData();
//		}

	}

	/** 用EventBus，通知切换界面 */
	public void onEventMainThread(MainPagerChangeEvent event) {
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
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private class MainPagerAdapter extends PagerAdapter {
		// 记录是否是首次显示
		ArrayMap<Integer, Boolean> isFirstVisbles = new ArrayMap<Integer, Boolean>(5);
		private FragmentManager fm = null;
		private ArrayList<Fragment> fragmentList;

		public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
			this.fm = fm;
			this.fragmentList = fragmentList;
			for (int i = 0; i < 5; i++) {
				isFirstVisbles.put(i, false);
			}
		}


		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			if (isFirstVisbles.get(position) == false) {
				/* 这里是首次调用 */
				Fragment frag = fragmentList.get(position);
				isFirstVisbles.put(position, true);// 设置为不是首次
				if (frag instanceof RdpBaseFragment)
					((RdpBaseFragment) frag).initFristData();// 加载数据
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmentList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragmentList.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				if (position == 2) {
					Bundle bundle = new Bundle();
					bundle.putBoolean(ShoppingCartFragment.INP_BACK, true);
					mTabs.get(position).setArguments(bundle);
				}
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
				 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
				 */
				fm.executePendingTransactions();
			}
			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}
			return fragment.getView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(fragmentList.get(position).getView());
			object = null;
		}
	}

}
