package com.huika.cloud.control.eshop.actvity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.CustomViewPager;
import android.support.v4.view.TabPageIndicator;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.huika.cloud.R;
import com.huika.cloud.control.eshop.fragment.MyOrderFragment;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * @description：我的订单列表
 * @author zhoukl
 * @date 2015-5-25 下午4:58:36
 */
public class MyOrderActivity extends RdpBaseActivity implements OnPageChangeListener { //, UserRefreshListener {
	public static final String IPN_ORDER_TYPE = "IPN_ORDER_TYPE";
	public static final int ORDER_TYPE_ALL = 0;
	public static final int ORDER_TYPE_WAIT_PAY = 1;
	public static final int ORDER_TYPE_WAIT_SEND = 2; // 目前没有用
	public static final int ORDER_TYPE_WAIT_RECEIVE = 3;
	public static final int ORDER_TYPE_WAIT_COMMENT = 4;
	private CustomViewPager pager;
	private TabPageIndicator page_indicator;
	// private Set<Integer> initFrags;
	private int mLastPage;

	@Override
	public void onBeforeInitActivity() {
		super.onBeforeInitActivity();
		
		View view = addMasterView(R.layout.my_order_activity);
	}

	@Override
	protected void initActivity() {
		super.initActivity();
//		setTitle("我的订单");
		setFuncTitle("我的订单");
//		findViewById(R.id.left).setOnClickListener(this);

		pager = (CustomViewPager) findViewById(R.id.pager);
		pager.setCanDragState(false);
		pager.setOffscreenPageLimit(4);

		page_indicator = (TabPageIndicator) findViewById(R.id.page_indicator);
		page_indicator.setWrapContent(true);
		page_indicator.setViewPageSmoothScroll(false);
		page_indicator.setOnPageChangeListener(this);
	}

	/**
	 *@author fan
	 *不知道为何usermodel在某些情况下为空了，所以废弃了checkLoginOrOther
	 */
	@Override
	protected void onResume() {
		super.onResume();
		/*UserModel userModel = GlobalApp.getInstance().getUserModel();
		if (userModel == null) {
			Intent login = new Intent(this, LoginActivity.class);
			login.putExtra(LoginActivity.TO_WHERE, getClass().getName());
			startActivity(login);
			finish();
		}
		else */if (pager.getAdapter() == null) {
			int initPage = getIntent().getIntExtra(IPN_ORDER_TYPE, ORDER_TYPE_ALL);
			mLastPage = initPage;
			// initFrags.add(mInitPage);
			MyOrderAdapter adapter = new MyOrderAdapter(getSupportFragmentManager(), initPage);
			pager.setAdapter(adapter);
			page_indicator.setViewPager(pager, initPage);
		} else {
			MyOrderFragment currentFrag = (MyOrderFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
			currentFrag.outReload();
		}
	}

	@Override
	public void onStop() {
		if (pager != null && pager.getAdapter() != null) {
			MyOrderFragment currentFrag = (MyOrderFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
			currentFrag.outStop();
		}
		super.onStop();
	}

//	@Override
//    public void onClick(View v) {
//        if (R.id.left == v.getId()) {
//            onBackPressed();
//            return;
//        }
//        super.onClick(v);
//    }

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int item) {
//		if (mInitPage == item) {
//			mInitPage = -1;
//			return;
//		}

		MyOrderFragment currentFrag = (MyOrderFragment) pager.getAdapter().instantiateItem(pager, mLastPage);
		currentFrag.outStop();

		// if(!initFrags.contains(item)){
		// initFrags.add(item);
		MyOrderFragment frag = (MyOrderFragment) pager.getAdapter().instantiateItem(pager, item);
		frag.show();
//		if (frag instanceof Rd.OnInitShowListener) {
//			((BaseFra.OnInitShowListener) frag).onInitShow();
//		}
		// }

		mLastPage = item;
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
	}

	/**
	 * @description：功能类型fragment页签的适配器
	 * @author zhoukl
	 * @date 2015-5-28 上午11:03:13
	 */
    private class MyOrderAdapter extends FragmentStatePagerAdapter {
        private int mInitPage = 0;
        private String[] titleStrs;
        //SparseArray<Fragment> mFragmentArray;

        public MyOrderAdapter(FragmentManager fm, int initPage) {
            super(fm);
            mInitPage = initPage;
            titleStrs = getResources().getStringArray(R.array.my_order_type);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new MyOrderFragment();
            Bundle args = new Bundle();
            args.putBoolean(MyOrderFragment.LOAD_INIT, mInitPage == position);
            args.putInt(MyOrderFragment.IPN_ORDER_TYPE, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return titleStrs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleStrs[position];
        }
    }

}
