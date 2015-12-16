package com.huika.cloud.control.cart.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.huika.cloud.R;
import com.huika.cloud.control.cart.fragment.ShoppingCartFragment;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;

/**
 * @description：购物车页面
 * @author shicm
 * @date 2015-11-12 下午3:54:48
 */
public class CartActivity extends RdpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.rltTitleBar).setVisibility(View.GONE);
		addMasterView(R.layout.product_cart_activity);
	    changeFragment(R.id.rl_container, ShoppingCartFragment.newInstance());
	  }

	  /** 改变界面的单个fragment */
	  protected void changeFragment(int resView, RdpBaseFragment targetFragment) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(ShoppingCartFragment.INP_BACK, false);
		targetFragment.setArguments(bundle);
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    transaction.replace(resView, targetFragment, targetFragment.getClass().getName());
	    transaction.commit();
	  }
}
