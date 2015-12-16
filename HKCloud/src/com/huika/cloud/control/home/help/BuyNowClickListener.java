package com.huika.cloud.control.home.help;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.config.ManufacturerType;
import com.huika.cloud.control.home.activity.ProductDetailsAct;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.model.ProductDetailBean;
import com.huika.cloud.support.model.SkuPropertyUnit;
import com.huika.cloud.support.model.SkuPropertyValueUnit;
import com.huika.cloud.support.model.SkuSelect;
import com.huika.cloud.support.model.SkuStock;
import com.huika.cloud.util.MoneyShowTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.NetUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @desc：SKU选择购买商品
 * @author: samy (hongfeiliuxing@gmail.com)
 * @date: 2015年5月27日 下午4:41:49
 */
public class BuyNowClickListener implements View.OnClickListener {

	// SKU 浮层
	private PopupWindow popupWindow;
	private View popUpView;
	private ViewGroup itemDetailPanel;
	// SKU弹出浮层后的半透明背景
	private View skuPropertiesBackgroudShadowView;
	// 图文详情
	private ImageView dish_add_order_btn;
	private ImageView sub_order_bt;
	// private ImageLoader imageLoader;

	private SkuSelect skuSelect;
	// 商品总库存
	private String itemQuantity;
	// 商品基本信息
	private ProductDetailBean productDetailBean;
	/**
	 * sku库存价格
	 */
	public List<SkuStock> skuStocks;
	/**
	 * sku属性
	 * 如果商品没有SKU码时，skuItems就为null,skuStock数组将只有一组数据
	 */
	public List<SkuPropertyUnit> skuItems;
	/**
	 * sku属性中的黑白色
	 */
	public List<SkuPropertyValueUnit> skuValueItems;
	
	private RdpBaseActivity mBaseAct;
	protected DisplayImageOptions options;

	private IDetailShopingCar detailShopingCar;
	private Boolean isSkip = false;

	private String lastSelectSkuId;
	private SkuStock lastSelectSkuIdStock = null;
	private boolean isFirstLoadView = true;

	public BuyNowClickListener(RdpBaseActivity baseAct, ProductDetailBean productDetailBean, ViewGroup root_merge) {
		super();
		this.mBaseAct = baseAct;
		this.productDetailBean = productDetailBean;
		this.itemDetailPanel = (ViewGroup) root_merge;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.circle_shoping_cat_default_ic).showImageForEmptyUri(R.drawable.circle_shoping_cat_default_ic)
				.showImageOnFail(R.drawable.circle_shoping_cat_default_ic).cacheInMemory(true)// 开启内存缓存
				.cacheOnDisk(true) // 开启硬盘缓存
				.resetViewBeforeLoading(false).displayer(new RoundedBitmapDisplayer(15)).build();
	}

	public void onClick(View v) {
		if (!NetUtil.isNetworkAvailable(mBaseAct)) {
			ToastMsg.showToastMsg(mBaseAct, "亲，你的网络不给力哟");
			return;
		}
		if (!LoginHelper.getInstance(mBaseAct).isAutoLogin()) {// 登录

			Intent intentlogin = mBaseAct.getIntent();
			intentlogin.putExtra(LoginActivity.INP_WHERE, ProductDetailsAct.class.getName());
			intentlogin.setClass(mBaseAct, LoginActivity.class);
			mBaseAct.startActivity(intentlogin);
			return;
		}
		// 用这种方式去判断是否跳转
		switch (v.getId()) {
			case R.id.comment_buy_now_btn:
				isSkip = true;
				break;
			case R.id.add_product_detail_btn:
				isSkip = false;
				break;
		}

		initSkuData();
		setBackgroudShadowView();
		popUpView = mBaseAct.inflater.inflate(R.layout.fragment_sku_select, null);
		// 针对魅族手机的底部smartbar做兼容处理
		if (android.os.Build.MANUFACTURER.equalsIgnoreCase(ManufacturerType.MEIZU)) {
			TextView autoHigtView = (TextView) popUpView.findViewById(R.id.item_detail_popup_auto_hight);
			autoHigtView.getLayoutParams().height = 100;
		}
		popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setAnimationStyle(R.style.item_sku_animation);// 设置淡入淡出动画效果
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		initItemThumbnail();
		initSkuProperties(popUpView);
		listenConfirmButton();
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				isFirstLoadView = true;// 还原一下属性；
				itemDetailPanel.removeView(skuPropertiesBackgroudShadowView);
			}
		});
		popUpView.findViewById(R.id.close_sku_select_ll).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 商品SKU属性弹出时，设置除SKU视图部分为半透明
	 */
	private void setBackgroudShadowView() {
		skuPropertiesBackgroudShadowView = new View(mBaseAct);
		skuPropertiesBackgroudShadowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		skuPropertiesBackgroudShadowView.setBackgroundColor(mBaseAct.getResources().getColor(R.color.detail_sku_out_bg_shadow));
		itemDetailPanel.addView(skuPropertiesBackgroudShadowView);
	}

	public void initSkuData() {
		skuStocks = productDetailBean.skuStock;
		skuItems = productDetailBean.skuItems;
		// 商品无sku情况下可能用到；
		itemQuantity = productDetailBean.stock;
	}

	/**
	 * 初始化 商品SKU 浮层
	 */
	public void initSkuProperties(View view) {
		dish_add_order_btn = (ImageView) popUpView.findViewById(R.id.item_buy_count_add_btn);
		sub_order_bt = (ImageView) popUpView.findViewById(R.id.item_buy_count_reduce_btn);
		Display display = mBaseAct.getWindowManager().getDefaultDisplay();
		int maxWidth = display.getWidth() - 2 * mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_panel_dynamic_fill_margin_left_right);
		int buttonHeight = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_property_btn_height);
		int skuTitleMarginTop = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_title_margin_top);
		int skuLineMarginTop = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_line_margin_top);
		int skuBtnRightMargin = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_property_btn_margin_right);
		int skuBtnTopMargin = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_property_btn_margin_top);
		int skuLineHeight = mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_line_height);
		LinearLayout.LayoutParams skuTitleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		skuTitleLayoutParams.setMargins(0, skuTitleMarginTop, 0, 0);
		// 如果商品存在SKU属性
		if (skuStocks != null) {
			skuSelect = new SkuSelect();
			// 动态添加属性LinearLayout
			LinearLayout skuPropertiesLayout = (LinearLayout) popUpView.findViewById(R.id.item_detail_dynamic_sku_properties);
			// 购买的数量
			final TextView itemCountTextView = (TextView) popUpView.findViewById(R.id.item_buy_count);
			// 库存
			final TextView quantityView = (TextView) popUpView.findViewById(R.id.item_deatil_sku_quantity_txt);

			for (SkuPropertyUnit skuPropertyUnit : skuItems) {
				final SkuPropertyUnit skuProperty = skuPropertyUnit;
				// 添加尺码和颜色分类
				TextView textView = new TextView(mBaseAct);
				textView.setId(Integer.valueOf(skuProperty.attributeId));
				textView.setText(skuProperty.attributeName);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_title_size));
				textView.setTextColor(mBaseAct.getResources().getColor(R.color.common_text_color));
				textView.setLayoutParams(skuTitleLayoutParams);
				skuPropertiesLayout.addView(textView);
				// 默认加载控件填充，没有选中
				skuSelect.put(skuProperty.attributeId, skuProperty.attributeName, false);

				final List<Button> skuViewList = new ArrayList<Button>();
				if (skuProperty.values != null && skuProperty.values.size() > 0) {
					// button按钮容器LinearLayout
					LinearLayout linearLayout = new LinearLayout(mBaseAct);
					linearLayout.setOrientation(LinearLayout.VERTICAL);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					linearLayout.setLayoutParams(layoutParams);

					LinearLayout newlinearLayout = new LinearLayout(mBaseAct);
					newlinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					newlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
					newlinearLayout.setLayoutParams(layoutParams);
					// 统计Button一排的总宽度
					int widthSoFar = 0;
					// 当前属性下的各种尺码，和颜色分类的各种属性
					for (final SkuPropertyValueUnit skuPropertyValueUnit : skuProperty.values) {
						// 添加各个尺码和颜色分类按钮
						final Button skuButton = new Button(mBaseAct);
						LinearLayout.LayoutParams skuLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, buttonHeight);
						skuLayoutParams.setMargins(0, skuBtnTopMargin, skuBtnRightMargin, 0);
						skuButton.setText(skuPropertyValueUnit.valueStr);
						skuButton.measure(0, 0);
						skuButton.setId(Integer.valueOf(skuPropertyValueUnit.valueId));
						skuButton.setClickable(true);
						skuButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBaseAct.getResources().getDimensionPixelSize(R.dimen.detail_sku_property_size));
						skuButton.setTextColor(mBaseAct.getResources().getColor(R.color.common_text_color));
						skuButton.setLayoutParams(skuLayoutParams);
						skuButton.setSingleLine();
						skuButton.setBackgroundResource(R.drawable.sku_button_unselect_bg);
						if (skuItems.size() >= 1 && skuProperty.values.size() == 1) { // 如有两个及以上SKU时属性时且规格为一个时，默认选中处理
							selectSkuBtnChange(itemCountTextView, quantityView, skuProperty, skuViewList, skuPropertyValueUnit, skuButton);
						}
						skuButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								isFirstLoadView = false;// 点过一次默认的就变成false；
								selectSkuBtnChange(itemCountTextView, quantityView, skuProperty, skuViewList, skuPropertyValueUnit, skuButton);
							}
						});

						dish_add_order_btn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Integer itemCount = Integer.valueOf(itemCountTextView.getText().toString());
								if (lastSelectSkuIdStock == null) {
									return;
								} else {
									if (itemCount == lastSelectSkuIdStock.stock) { return; }
								}
								itemCount = itemCount + 1;
								itemCountTextView.setText(itemCount.toString());
								if (itemCount > 1) {
									sub_order_bt.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_sub_order_bg));
								}
							}
						});
						sub_order_bt.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Integer itemCount = Integer.valueOf(itemCountTextView.getText().toString());
								if (itemCount == 1) { return; }
								itemCount = itemCount - 1;
								itemCountTextView.setText(itemCount.toString());
								if (itemCount == 1) {
									sub_order_bt.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_sub_order_bg_disable));
								}
							}
						});

						skuViewList.add(skuButton);
						// 测量该measure的大小
						skuButton.measure(0, 0);
						widthSoFar += skuButton.getMeasuredWidth();
						widthSoFar += skuBtnRightMargin;
						if (widthSoFar >= maxWidth) {// 如果大于现在要去的总宽度，要求再次添加一个容器用来包容button；
							linearLayout.addView(newlinearLayout);
							newlinearLayout = new LinearLayout(mBaseAct);
							newlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
							newlinearLayout.setLayoutParams(layoutParams);
							newlinearLayout.addView(skuButton);
							widthSoFar = skuButton.getMeasuredWidth();
						} else {
							newlinearLayout.addView(skuButton);
						}
					}
					linearLayout.addView(newlinearLayout);
					skuPropertiesLayout.addView(linearLayout);
				}
				// 添加一根线，跟购买数量操作，分割；
				View lineView = new View(mBaseAct);
				lineView.setBackgroundColor(mBaseAct.getResources().getColor(R.color.common_single_line));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, skuLineHeight);
				layoutParams.setMargins(0, skuLineMarginTop, 0, 0);
				lineView.setLayoutParams(layoutParams);
				skuPropertiesLayout.addView(lineView);
			}
		} else {
			showThumbnailItemInfoOnNoSkuInfo();
		}
	}

	/**
	 * 商品无SKU信息时，只展示总库存
	 */
	public void showThumbnailItemInfoOnNoSkuInfo() {
		// 不存在商品SKU属性，直接展示商品库存量等信息
		TextView quantityView = (TextView) popUpView.findViewById(R.id.item_deatil_sku_quantity_txt);
		quantityView.setText("(库存".concat(itemQuantity).concat("件)"));
		if (TextUtils.isEmpty(itemQuantity) && Integer.valueOf(itemQuantity) > 1) {
			dish_add_order_btn.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_add_order_bg));
			dish_add_order_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView itemCountTextView = (TextView) popUpView.findViewById(R.id.item_buy_count);
					Integer itemCount = Integer.valueOf(itemCountTextView.getText().toString());
					if (itemCount == Integer.valueOf(itemQuantity)) { return; }
					itemCount = itemCount + 1;
					itemCountTextView.setText(itemCount.toString());
					if (itemCount > 1) {
						sub_order_bt.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_add_order_bg));
					}
				}
			});

			sub_order_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					TextView itemCountTextView = (TextView) popUpView.findViewById(R.id.item_buy_count);
					Integer itemCount = Integer.valueOf(itemCountTextView.getText().toString());
					if (itemCount == 1) { return; }
					itemCount = itemCount - 1;
					itemCountTextView.setText(itemCount.toString());
					if (itemCount == 1) {
						sub_order_bt.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_sub_order_bg_disable));
					}
				}
			});
		} else {
			ToastMsg.showToastMsg(mBaseAct, "商品库存不足");
		}
	}

	/**
	 * 初始化商品SKU选择时顶部的商品缩略信息
	 */
	public void initItemThumbnail() {
		String pic = productDetailBean.productSmallImage;
		String title = productDetailBean.productName;
		TextView titleTextView = (TextView) popUpView.findViewById(R.id.item_deatil_sku_title_txt);
		titleTextView.setText(title);
		TextView priceTextView = (TextView) popUpView.findViewById(R.id.item_deatil_sku_price_txt);
		priceTextView.setText("¥".concat(MoneyShowTool.twolastDF(productDetailBean.productPrice)));
		ImageView imageThumbnailView = (ImageView) popUpView.findViewById(R.id.item_detail_sku_sm_img);
		ImageLoader.getInstance().displayImage(pic, imageThumbnailView, options);
	}

	/**
	 * 监听 商品SKU浮层中的确定按钮
	 * 首先判断用户是否选择了所有的SKU属性，如果没有选择，则提示选择。
	 * 如果用户选择了所有必选的SKU属性后，则判断是否登录，已登录，创建订单；未登录 ，跳转登录页面。
	 */
	public void listenConfirmButton() {
		Button confirmButton = (Button) popUpView.findViewById(R.id.item_detail_buy_confirm_btn);
		final TextView buynum_tv = (TextView) popUpView.findViewById(R.id.item_buy_count);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (skuItems != null && skuSelect != null) {
					if (!skuSelect.isSelectedAllSkus()) {
						ToastMsg.showToastMsg(mBaseAct, skuSelect.getPropNameString());
						return;
					} else {
						String lastSelectSkuId = skuSelect.getSeclectSkuId();
						if (TextUtils.isEmpty(lastSelectSkuId)) {
							ToastMsg.showToastMsg(mBaseAct, "此商品属性组合下已无商品".concat(skuSelect.getPropNameString()));
							return;
						}
						if (lastSelectSkuIdStock.stock == 0) {
							ToastMsg.showToastMsg(mBaseAct, "当前选择商品库存不足，请您重新选择!");
							return;
						}
					}
				}
				if (!LoginHelper.getInstance(mBaseAct).isAutoLogin()) {// 登录
					  Intent intentlogin = mBaseAct.getIntent();
					  intentlogin.putExtra(LoginActivity.INP_WHERE, ProductDetailsAct.class.getName());
					  intentlogin.setClass(mBaseAct, LoginActivity.class);
					  mBaseAct.startActivity(intentlogin);
					return;
				} else {
					// 构建属性名称和属性数目；
					String skuText = skuSelect.getUserSelectSkuPropNameString();
					popupWindow.dismiss();
					detailShopingCar.operationShopCat(isSkip, buynum_tv.getText().toString(), lastSelectSkuId, skuText, lastSelectSkuIdStock.stock);
				}
			}
		});
	}

	/**
	 * @description：Btn选择
	 * @author samy
	 * @date 2015年6月26日 下午3:19:47
	 */
	@SuppressWarnings("deprecation")
	private void selectSkuBtnChange(final TextView itemCountTextView, final TextView quantityView, final SkuPropertyUnit skuProperty, final List<Button> skuViewList,
			final SkuPropertyValueUnit skuPropertyValueUnit, final Button skuButton) {
		int skuId = skuPropertyValueUnit.valueId;
		String skuName = skuPropertyValueUnit.valueStr;
		String sku = skuPropertyValueUnit.sku;
		String imgUrl = skuPropertyValueUnit.imgUrl;
		for (Button allButton : skuViewList) {
			// 默认统一设置成黑色字体
			allButton.setTextColor(Color.parseColor("#ff333333"));
			allButton.setBackgroundResource(R.drawable.sku_button_unselect_bg);
		}
		skuSelect.setSelectedSkuId(skuProperty.attributeId, skuId, skuName, sku, imgUrl, true);

		skuButton.setTextColor(mBaseAct.getResources().getColor(R.color.white));
		skuButton.setBackgroundResource(R.drawable.sku_button_select_bg);
		if ((skuItems.size() >= 1 && skuProperty.values.size() == 1) || (!isFirstLoadView && skuSelect.isSelectedAllSkus())) {
			lastSelectSkuId = skuSelect.getSeclectSkuId();
			lastSelectSkuIdStock = null;
			for (SkuStock tempStok : skuStocks) {
				if (tempStok.sku.equals(lastSelectSkuId)) {
					lastSelectSkuIdStock = tempStok;
					break;
				}
			}
			if (lastSelectSkuIdStock == null) {   // 当不能遍历到交集情况，如秒杀情况下,商品库存为0;
				lastSelectSkuIdStock = new SkuStock();
				lastSelectSkuIdStock.sku = "";
				lastSelectSkuIdStock.stock = 0;
				lastSelectSkuIdStock.price = 0;
			}

			final int tempStock = lastSelectSkuIdStock.stock;
			if (tempStock == 0) {
				quantityView.setText("(库存0件)");
				ToastMsg.showToastMsg(mBaseAct, "商品库存不足");
				// 再次缓存起来，方便点击时不能触发；
				return;
			}
			quantityView.setText("(库存".concat(tempStock + "").concat("件)"));
			// 更新 商品图片
			if (skuProperty.isRelationImage) {
				ImageView imageView = (ImageView) popUpView.findViewById(R.id.item_detail_sku_sm_img);
				ImageLoader.getInstance().displayImage(skuPropertyValueUnit.imgUrl, imageView, options);
			}
			// 更新商品价格
			if (lastSelectSkuIdStock != null) {
				TextView priceView = (TextView) popUpView.findViewById(R.id.item_deatil_sku_price_txt);
				priceView.setText("¥".concat(MoneyShowTool.twolastDF(lastSelectSkuIdStock.price)));
			}

			if (tempStock > 1) {
				dish_add_order_btn.setBackgroundDrawable(mBaseAct.getResources().getDrawable(R.drawable.cart_add_order_bg));
			}
		} else {// 处理没有全选情况
				// skuSelect.getAdvanceSeclectSkuId();
		}
	}

	public void setIDetailShopingCar(IDetailShopingCar detailShopingCar) {
		this.detailShopingCar = detailShopingCar;
	}

	public interface IDetailShopingCar {
		void operationShopCat(boolean isSkip, String buyNum, String lastSelectSkuId, String skuText, int stock);
	}
}