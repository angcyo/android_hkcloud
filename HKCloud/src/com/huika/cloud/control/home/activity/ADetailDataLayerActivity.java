package com.huika.cloud.control.home.activity;

import java.util.List;
import android.support.v4.view.AutoScrollViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import com.huika.cloud.R;
import com.huika.cloud.support.model.ProductImageArray;
import com.huika.cloud.util.DensityUtils;
import com.huika.cloud.views.MyScrollView;
import com.huika.cloud.views.ScrollViewContainer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

import de.greenrobot.event.EventBus;

public abstract class ADetailDataLayerActivity extends RdpBaseActivity implements AutoScrollViewPager.OnPageItemClickListener<ProductImageArray>, ScrollViewContainer.OnScrollPageChangeListener, MyScrollView.OnScrollListener {
    public static final String INP_PRODUCT_ID = "PRODUCT_ID";
    protected View mView;
	/**
	 * 商品详情虚浮条
	 */
	protected View float_bar;
	/**
	 * 翻页框架相关
	 */
	protected ScrollViewContainer root_sve;
	protected MyScrollView scroll_view_up;
	protected ViewGroup scroll_view_down;

	/**
	 * 广告部分
	 */
	protected AutoScrollViewPager<ProductImageArray> advertisementVp;
	protected RelativeLayout advertisementContainerRl;
	protected List<ProductImageArray> imgUrls;
	protected DisplayImageOptions options;


	/** 回到顶部 */
	protected View index_gotoTop_iv;

	protected void initData() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.index_advert_default).showImageForEmptyUri(R.drawable.index_advert_default) // resource
				.showImageOnFail(R.drawable.index_advert_default) // resource or
				.cacheInMemory(true)// 开启内存缓存
				.cacheOnDisk(true) // 开启硬盘缓存
				.resetViewBeforeLoading(false).build();
	}


	protected void initActivity() {
		super.initActivity();
		initData();
		setFuncTitle(R.string.product_detail);
		mView = addMasterView(R.layout.product_details_fragment);
		initDetailBaseView();
		initDetailBaseListener();
	}

	@SuppressWarnings("unchecked")
	protected void initDetailBaseView() {
		// 广告部分
		advertisementVp = (AutoScrollViewPager<ProductImageArray>) mView.findViewById(R.id.advertisementVp);
		advertisementVp.setImageOptions(options);

		advertisementContainerRl = (RelativeLayout) mView.findViewById(R.id.advertisementContainerRl);
		ViewGroup.LayoutParams params = advertisementContainerRl.getLayoutParams();
		params.height = getResources().getDisplayMetrics().widthPixels;
		advertisementContainerRl.setLayoutParams(params);
		// 分页部分
		root_sve = (ScrollViewContainer) mView.findViewById(R.id.root_sve);
		scroll_view_up = (MyScrollView) root_sve.findViewById(R.id.scroll_view_up);
		scroll_view_down = (ViewGroup) root_sve.findViewById(R.id.scroll_view_down);
		// 底部部分
		float_bar = mView.findViewById(R.id.float_bar);
		index_gotoTop_iv = mView.findViewById(R.id.index_gotoTop_iv);
	}

	protected void initDetailBaseListener() {
		root_sve.setOnScrollPageChangeListener(this);
		scroll_view_up.setOnScrollListener(this);
		advertisementVp.setOnPageItemClickListener(this);
		index_gotoTop_iv.setOnClickListener(this);
	}

	protected void hideOverLay() {
		// 现在是自己写的错误加载界面；
		hideOverLayView();// 隐藏
	}

	protected void showOverLay() {
		showLoadingOverLay(root_sve);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
			case R.id.click_reload:
				refreshData();
				break;
		}
	}

	@Override
	public void onPageItemClickListener(ProductImageArray pd) {
	}

	@Override
	public void onScrollPageChangeListener(int pageNum) {
		index_gotoTop_iv.setVisibility(pageNum == 0 ? View.GONE : View.VISIBLE);
		if (pageNum == 0) {
			scroll_view_down.setPadding(0, 0, 0, 0);
		}
		else {
			scroll_view_down.setPadding(0, DensityUtils.dip2px(this, 48), 0, DensityUtils.dip2px(this, 48));
		}
	}

	@Override
	public void onScrollPage() {
	}

	@Override
	public void onScrollChanged(int x, int y) {
		if (y > 100) {
		} else {
		}
	}

	protected abstract void refreshData();
	protected abstract void refreshSkuData();

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
