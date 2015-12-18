package com.huika.cloud.control.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.CustomViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.cart.activity.ConfirmOrderActivity;
import com.huika.cloud.control.home.fragment.AllProductEnvaluteFragment;
import com.huika.cloud.control.home.fragment.HasImgsProductEnvaluteFragment;
import com.huika.cloud.control.home.help.BuyNowClickListener;
import com.huika.cloud.control.home.help.BuyNowClickListener.IDetailShopingCar;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.event.CartChangeEvent;
import com.huika.cloud.support.model.OrderProduct;
import com.huika.cloud.support.model.ProductDetailBean;
import com.huika.cloud.support.model.ProductImageArray;
import com.huika.cloud.support.model.SkuPropertyUnit;
import com.huika.cloud.support.model.SkuStock;
import com.huika.cloud.util.ShareUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author shicm
 * @date：2015年7月8日,上午11:02:15
 * @describe:商品评价列表
 */
public class ProductCommentsActivity extends RdpBaseActivity implements IDetailShopingCar {
	public static final String INP_PRODUCTDETAILS = "productdetails";
	/**
	 * 设置标题
	 */
	@ViewInject(R.id.all_envalute_tv)
	public TextView all_envalute;
	@ViewInject(R.id.comment_has_imgs_tv)
	public TextView has_imgs_envalute;
	public List<SkuStock> skuStocks;
	public List<SkuPropertyUnit> skuItems;
	protected List<ProductImageArray> imgUrls;
	private View mView;
	/** viewPage模块 */
	@ViewInject(R.id.comment_product_viewpager)
	private CustomViewPager pager;// viewpage
	@ViewInject(R.id.comment_all_rl)
	private RelativeLayout comment_all_rl;
	/** 是否有sku，通过skuItems是否为空来判断，默认情况下商品是有sku */
	@ViewInject(R.id.comment_has_image_rl)
	private RelativeLayout comment_has_image_rl;
	@ViewInject(R.id.root_parent_fl)
	private ViewGroup root_parent_fl;
	@ViewInject(R.id.comment_bottom_product_ll)
	private LinearLayout comment_bottom_product_ll;
	@ViewInject(R.id.comment_add_product_btn)
	private Button add_to_list_btn;
	@ViewInject(R.id.comment_buy_now_btn)
	private Button mmediate_participation_btn;
	@ViewInject(R.id.comment_all_img)
	private View comment_all_img;
	@ViewInject(R.id.comment_has_imgs_img)
	private View comment_has_imgs_img;
	private String[] titleStrs;// 全部标题
	/** 底部相关 */
	private ProductDetailBean productDetailBean;
	private String productId;
	private SkuStock skuStock;

	@Override
	protected void initActivity() {
		// TODO Auto-generated method stub
		super.initActivity();
		mView = addMasterView(R.layout.product_comment_list);
		RdpAnnotationUtil.inject(this);
		initData();
		initAll();
	}

	private void initData() {
		productDetailBean = (ProductDetailBean) getIntent().getSerializableExtra(INP_PRODUCTDETAILS);
		productId = productDetailBean.productId;
		imgUrls = productDetailBean.productImageArray;
		skuStocks = productDetailBean.skuStock;
		skuItems = productDetailBean.skuItems;
	}

	private void initAll() {
		initViews();
		initPage();
	}

	private void initViews() {
		setFuncTitle(R.string.evaluate_list_title);
		pager.setCanDragState(true);
		pager.setOffscreenPageLimit(2);
		titleStrs = getResources().getStringArray(R.array.product_envalute_page_indicator);
		comment_all_rl.setOnClickListener(new MyOnClickListener(0));
		comment_has_image_rl.setOnClickListener(new MyOnClickListener(1));
		initOperationListener();
	}

	/**
	 * @description：操作购物车监听处理
	 * @author samy
	 * @date 2015年6月15日 下午2:09:17
	 */
	private void initOperationListener() {
		if (productDetailBean.stock.equals("0")) {// 没有下架或者库存为0的情况 productDetailBean.statusType.equals("1") ||
			/*
			 * if (productDetailBean.statusType.equals("1")) {
			 * showToastMsg("亲，当前商品已下架!");
			 * } else
			 */
			if (productDetailBean.stock.equals("0")) {
				showToastMsg("亲，当前商品已缺货!");
			}
			mmediate_participation_btn.setOnClickListener(null);
			add_to_list_btn.setOnClickListener(null);
			mmediate_participation_btn.setTextColor(Color.parseColor("#3Affffff"));
			add_to_list_btn.setTextColor(Color.parseColor("#3Affffff"));
		} else {
			// showToastMsg("亲，当前商品已缺货!"dsfsd updateDt);
			if (productDetailBean.skuItems != null) {// 有SKU情况下
				BuyNowClickListener buyNowClickListener = new BuyNowClickListener(this, productDetailBean, root_parent_fl);
				buyNowClickListener.setIDetailShopingCar(this);
				mmediate_participation_btn.setOnClickListener(buyNowClickListener);
				add_to_list_btn.setOnClickListener(buyNowClickListener);
			} else {// 如果是没有SKU商品的情况下；
				skuStock = productDetailBean.skuStock.get(0);// 如果没有SKU时，一定会有一个skuStock对象；
				mmediate_participation_btn.setOnClickListener(this);
				add_to_list_btn.setOnClickListener(this);
			}
		}
	}

	private void initPage() {
		if (pager.getAdapter() == null) {
			int initPage = 0;
			WantStudyPagerAdapter adapter = new WantStudyPagerAdapter(this.getSupportFragmentManager());
			pager.setAdapter(adapter);
			pager.setOnPageChangeListener(new MyOnPageChangeListener());
			pager.setCurrentItem(initPage);
			setTabSelect(true, false);
		}
	}

	private void setTabSelect(boolean isSelectTeach, boolean isSelectClassify) {
		comment_all_rl.setSelected(isSelectTeach);
		comment_has_image_rl.setSelected(isSelectClassify);
		comment_all_img.setVisibility(isSelectTeach ? View.VISIBLE : View.GONE);
		comment_has_imgs_img.setVisibility(isSelectClassify ? View.VISIBLE : View.GONE);
	}

	@Override
	public void operationShopCat(boolean isSkip, String buyNum, String lastSelectSkuId, String skuText, int stock) {
		addShopCatList(isSkip, buyNum, lastSelectSkuId, skuText, stock);
	}

	/**
	 * @description：加入购物车
	 * @author samy
	 * @date 2015年6月11日 下午2:40:57
	 */
	private void addShopCatList(final boolean isSkip, final String buyNum, final String lastSelectSkuId, final String skuText, final int stock) {
		showLoadingDialog(getString(R.string.common_loading));
		Type shopCartType = new TypeToken<HashMap<String, String>>() {
		}.getType();
		RdpNetCommand shopCartCommand = new RdpNetCommand(this, shopCartType);
		shopCartCommand.setServerApiUrl(isSkip ? UrlConstant.ORDER_GETPRODUCTPRICEFORORDER : UrlConstant.ORDER_INTOCART);
		shopCartCommand.clearConditions();
		shopCartCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().memberId); // 用户id
		shopCartCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID); // 商家id
		if (!isSkip) {
			shopCartCommand.setCondition("productId", productId);
			shopCartCommand.setCondition("skuNumber", lastSelectSkuId);
			shopCartCommand.setCondition("count", buyNum);
		} else {
			shopCartCommand.setCondition("productDetail", getProductDetailInfo(buyNum, lastSelectSkuId, skuText));
			shopCartCommand.setCondition("addressId", "");
		}
		shopCartCommand.execute();
		shopCartCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				dismissLoadingDialog();
				if (isSkip) {
					OrderProduct orderProduct = (OrderProduct) data;
					if (orderProduct.getProductRs().get(0).type == 1) {
						Intent mIntent = new Intent(ProductCommentsActivity.this, ConfirmOrderActivity.class);
						mIntent.putExtra(ConfirmOrderActivity.INP_PRODUCT_LIST, orderProduct);
						startActivity(mIntent);
					} else {
						showToastMsg("库存不足,请修改购买数量");
					}

				} else {
					@SuppressWarnings("unchecked")
					HashMap<String, String> map = (HashMap<String, String>) data;
					if (null != map && map.size() > 0) {
						EventBus.getDefault().post(new CartChangeEvent());
						showToastMsg("成功添加到购物车");
					}
				}
			}
		});
		shopCartCommand.setOnCommandFailedListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (!LoginHelper.getInstance(this).isAutoLogin()) {// 登录
			if ((v.getId() == R.id.comment_add_product_btn || v.getId() == R.id.comment_buy_now_btn)) {
				Intent intentlogin = getIntent();
				intentlogin.putExtra(LoginActivity.INP_WHERE, ProductDetailsAct.class.getName());
				intentlogin.setClass(this, LoginActivity.class);
				startActivity(intentlogin);
				return;
			}
		}
		switch (v.getId()) {
			case R.id.comment_add_product_btn: // 商品无SKU信息是监听
				addShopCatList(false, "1", skuStock.sku, "", skuStock.stock);
				break;
			case R.id.comment_buy_now_btn:// 商品无SKU信息是监听
				addShopCatList(true, "1", skuStock.sku, "", skuStock.stock);
				break;
			case R.id.notrans_detail_head_rigth_tv:// 进入分享
				ShareMall();
				break;
			default:
				break;
		}
	}

	/** 分享 */
	private void ShareMall() {
		if (productDetailBean == null) {
			showToastMsg("没有分享的数据");
			return;
		}
		if (productDetailBean.productImageArray.size() > 0) {
			ShareUtils.getInstantiation().ShareMall(this, mView, productDetailBean.productId, productDetailBean.productName, "我在惠卡发现一个不错的商品，赶快来看看吧...",
					productDetailBean.productImageArray.get(0).getBigImageUrl());
		} else {
			ShareUtils.getInstantiation().ShareMall(this, mView, productDetailBean.productId, productDetailBean.productName, "我在惠卡发现一个不错的商品，赶快来看看吧...", "");
		}
	}

	private String getProductDetailInfo(String buyCount, String skuID, String skuText) {
		JSONArray array = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("productId", productId);
			jsonObject.put("skuAttribute", skuText);
			jsonObject.put("skuId", skuID);
			jsonObject.put("productCount", buyCount);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		array.put(jsonObject);
		return array.toString();
	}

	private class WantStudyPagerAdapter extends FragmentStatePagerAdapter {

		public WantStudyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position == 0) {
				fragment = new AllProductEnvaluteFragment();
				Bundle bundle = new Bundle();
				bundle.putString("productId", productId);
				fragment.setArguments(bundle);
			} else {
				fragment = new HasImgsProductEnvaluteFragment();
				Bundle bundle = new Bundle();
				bundle.putString("productId", productId);
				fragment.setArguments(bundle);
			}
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

	/***
	 * tab点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			pager.setCurrentItem(index);
			switch (v.getId()) {
				case R.id.comment_all_rl:
					setTabSelect(true, false);
					break;
				case R.id.comment_has_image_rl:
					setTabSelect(false, true);
					break;

				default:
					break;
			}
		}
	}

	/**
	 * 底部动画监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int position) {
			if (position == 0) {
				setTabSelect(true, false);
			} else {
				setTabSelect(false, true);
			}
		}
	}
}
