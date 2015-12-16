package com.huika.cloud.control.home.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.cart.activity.ConfirmOrderActivity;
import com.huika.cloud.control.home.adapter.EvaluteListImgsAdapter;
import com.huika.cloud.control.home.adapter.ProductEvaluteAdapter;
import com.huika.cloud.control.home.help.BuyNowClickListener;
import com.huika.cloud.control.home.help.BuyNowClickListener.IDetailShopingCar;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.event.CartChangeEvent;
import com.huika.cloud.support.model.CartProduct;
import com.huika.cloud.support.model.OrderProduct;
import com.huika.cloud.support.model.ProductComment;
import com.huika.cloud.support.model.ProductDetailBean;
import com.huika.cloud.support.model.ProductImageArray;
import com.huika.cloud.support.model.SkuPropertyUnit;
import com.huika.cloud.support.model.SkuStock;
import com.huika.cloud.util.MoneyShowTool;
import com.huika.cloud.util.ShareUtils;
import com.huika.cloud.views.CheckedTextView;
import com.huika.cloud.views.CustomWebView;
import com.huika.cloud.views.GridViewForScrollView;
import com.huika.cloud.views.SampleListLinearLayout;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;

import de.greenrobot.event.EventBus;

/**
 * @dec:4.9.商品详情
 * @datetime: 2015/5/26 17:27
 */
public class ProductDetailsAct extends ADetailDataLayerActivity implements IDetailShopingCar, OnRefreshItemViewsListener {
	private TextView product_name_tv; // 商品名称
	private TextView discount_price_tv; // 折扣价格
	private TextView trade_price_tv; // 价格
	private TextView commission_money_tv; // 佣金
	private TextView discount_tv; // 折扣
	private TextView welfare_content_tv; // 专享福利内容
	private TextView stock_tv; // 库存
	private TextView sales_tv; // 销量
	private TextView postage_detail_show_tv; // 快递方式
	private TextView comment_count_tv; // 评价数
	private RelativeLayout goto_average_comment_rl; // 评价的跳转
	private LinearLayout comment_lv_split_ll; // 评价的布局
	private SampleListLinearLayout shop_evaluation_slv; // 评价的list

	// 网页内容
	private WebView wv_html1;
	private WebView wv_html2;
	private WebView wv_html3;
	// 详情bar的3个分类
	private CheckedTextView[] pViews = new CheckedTextView[3];
	private CheckedTextView[] floatViews = new CheckedTextView[3];

	private String product_id;
	private ProductDetailBean productDetailBean;
	private ProductEvaluteAdapter evaluteAdapter;

	// / 底部
	private TextView under_the_plane_tips_tv; // 下架商品
	private Button add_product_detail_btn; // 加入购物车
	private Button product_buy_now_btn; // 马上购买

	private LinearLayout operation_buy_product_ll;
	private boolean isFistLoad;

	public List<SkuStock> skuStocks;
	public List<SkuPropertyUnit> skuItems;
	/** 是否有sku，通过skuItems是否为空来判断，默认情况下商品是有sku */
	private SkuStock skuStock;
	private ViewGroup root_parent_fl;


	private HashMap<String, String> product_detail_map;

	@Override
	protected void initData() {
		super.initData();
		// product_id = getIntent().getStringExtra(INP_PRODUCT_ID);
		product_id = "4028b284518041370151819939010060";
		isFistLoad = true;
	}

	protected void initActivity() {
		super.initActivity();
		initViews();
		initViewListener();
	}

	private void initViews() {
		// initTitleView();
		addRightFuncView(R.drawable.icon_share_title, new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareMall();
			}
		}, 1);
		root_parent_fl = (ViewGroup) mView.findViewById(R.id.root_parent_fl);
		product_name_tv = (TextView) mView.findViewById(R.id.product_name_tv);
		discount_price_tv = (TextView) mView.findViewById(R.id.discount_price_tv);
		trade_price_tv = (TextView) mView.findViewById(R.id.trade_price_tv);
		commission_money_tv = (TextView) mView.findViewById(R.id.commission_money_tv);
		discount_tv = (TextView) mView.findViewById(R.id.discount_tv);
		stock_tv = (TextView) mView.findViewById(R.id.stock_tv);
		sales_tv = (TextView) mView.findViewById(R.id.sales_tv);

		postage_detail_show_tv = (TextView) mView.findViewById(R.id.postage_detail_show_tv);
		comment_count_tv = (TextView) mView.findViewById(R.id.comment_count_tv);
		goto_average_comment_rl = (RelativeLayout) mView.findViewById(R.id.goto_average_comment_rl);
		comment_lv_split_ll = (LinearLayout) mView.findViewById(R.id.comment_lv_split_ll);
		shop_evaluation_slv = (SampleListLinearLayout) mView.findViewById(R.id.shop_evaluation_slv);

		// 底部
		under_the_plane_tips_tv = (TextView) mView.findViewById(R.id.under_the_plane_tips_tv);
		operation_buy_product_ll = (LinearLayout) mView.findViewById(R.id.buy_product_bottom_ll);
		add_product_detail_btn = (Button) mView.findViewById(R.id.add_product_detail_btn);
		product_buy_now_btn = (Button) mView.findViewById(R.id.product_buy_now_btn);
		showOverLay();
		evaluteAdapter = new ProductEvaluteAdapter(this);
		shop_evaluation_slv.setAdapter(evaluteAdapter);
		evaluteAdapter.setItemLayoutID(R.layout.item_dish_evaluate_lv);
		evaluteAdapter.setListener(this);
		welfare_content_tv = (TextView) mView.findViewById(R.id.welfare_content_tv);

	}

	/**
	 * @description：
	 * @date 2015年6月15日 下午2:09:17
	 */
	private void initOperationListener() {
		if (productDetailBean.statusType.equals("1") || productDetailBean.stock.equals("0")) {// 没有下架或者库存为0的情况
			if (productDetailBean.statusType.equals("1")) {
				showToastMsg("亲，当前商品已下架!");
			} else if (productDetailBean.stock.equals("0")) {
				showToastMsg("亲，当前商品已缺货!");
			}
			product_buy_now_btn.setOnClickListener(null);
			add_product_detail_btn.setOnClickListener(null);
			product_buy_now_btn.setTextColor(Color.parseColor("#3Affffff"));
			add_product_detail_btn.setTextColor(Color.parseColor("#3Affffff"));
		} else {
			if (productDetailBean.skuItems != null) {// 有SKU情况下
				BuyNowClickListener buyNowClickListener = new BuyNowClickListener(this, productDetailBean, root_parent_fl);
				buyNowClickListener.setIDetailShopingCar(this);
				product_buy_now_btn.setOnClickListener(buyNowClickListener);
				add_product_detail_btn.setOnClickListener(buyNowClickListener);
			} else {// 如果是没有SKU商品的情况下；
				skuStock = productDetailBean.skuStock.get(0);// 如果没有SKU时，一定会有一个skuStock对象；
				product_buy_now_btn.setOnClickListener(this);
				add_product_detail_btn.setOnClickListener(this);
			}
		}
		goto_average_comment_rl.setOnClickListener(this);
	}

	private void initViewListener() {
		initDetailBaseListener();
		refreshData(); // 请求详情的数据shicm2015.08.03
	}

	@Override
	public void onResume() {
		super.onResume();
		isFistLoad = false;
	}
  
	@Override
	protected void refreshData() {
		if (!isFistLoad) {
			showLoadingDialog(false);
		}
		Type detailType = new TypeToken<ProductDetailBean>() {
		}.getType();
		RdpNetCommand detailCommand = new RdpNetCommand(this, detailType);
		detailCommand.setServerApiUrl(UrlConstant.PRODUCT_GETPRODUCTDETAILS); // 商品详情
		detailCommand.clearConditions();
		detailCommand.setCondition("productId", product_id);
		detailCommand.execute();
		detailCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {
			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				refreshEnvaluteData();// 获取评论，如果是最新的放在onresume里面不是区最新的就不用shicm
				if (data != null) {
					productDetailBean = (ProductDetailBean) data;
					fillViewFromNetwork(productDetailBean);
				} else {
					showOverLay();
				}
			}
		});
		detailCommand.setOnCommandFailedListener(this);
	}

	/**
	 * 获取评论
	 */
	protected void refreshEnvaluteData() {
		Type envaluteType = new TypeToken<ArrayList<ProductComment>>() {
		}.getType();
		RdpNetDataSet envaluteCommand = new RdpNetDataSet(this, envaluteType);
		envaluteCommand.setServerApiUrl(UrlConstant.PRODUCT_GETPRODUCTCOMMENTLIST);
		envaluteCommand.clearConditions();
		envaluteCommand.setCondition("type", 0 + "");
		envaluteCommand.setCondition("productId", product_id);
		envaluteCommand.open();
		envaluteCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				dismissLoadingDialog();
				hideOverLay();
				refreshSkuData();
				// 默认的返回前两条
				int iCount = 0;
				@SuppressWarnings("unchecked")
				ArrayList<ProductComment> listComment = (ArrayList<ProductComment>) data;
				if (null != listComment && listComment.size() > 0) {
					ArrayList<ProductComment> datas = new ArrayList<ProductComment>();
					for (int i = 0; i < listComment.size(); i++) {
						ProductComment bean = listComment.get(i);
						datas.add(bean);
						iCount++;
						if (iCount > 1) {
							break;
						}
					}
					evaluteAdapter.setData(datas);
				}
			}
		});
		envaluteCommand.setOnCommandFailedListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (!LoginHelper.getInstance(this).isAutoLogin()) {// 登录
			if ((v.getId() == R.id.add_product_detail_btn || v.getId() == R.id.product_buy_now_btn)) {
				Intent intentlogin = getIntent();
				intentlogin.putExtra(LoginActivity.INP_WHERE, ProductDetailsAct.class.getName());
				intentlogin.setClass(this, LoginActivity.class);
				startActivity(intentlogin);
				return;
			}
		}
		switch (v.getId()) {
			case R.id.goto_average_comment_rl: // 评论的跳转
				Intent mIntent = new Intent(this, ProductCommentsActivity.class);
				mIntent.putExtra(ProductCommentsActivity.INP_PRODUCTDETAILS, productDetailBean);
				startActivity(mIntent);
				break;
			case R.id.add_product_detail_btn: // 商品无SKU信息是监听
				addShopCatList(false, "1", skuStock.sku, "", skuStock.stock);
				break;
			case R.id.product_buy_now_btn: // 商品无SKU信息是监听
				addShopCatList(true, "1", skuStock.sku, "", skuStock.stock);
				break;
			case R.id.index_gotoTop_iv:// 回到顶部
				float_bar.setVisibility(View.GONE);
				root_sve.setScollPageToTop();
				scroll_view_up.scrollTo(0, 0);
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

	protected void hideOverLay() {
		super.hideOverLay();
		operation_buy_product_ll.setVisibility(View.VISIBLE);
	}

	@Override
	protected void showOverLay() {
		super.showOverLay();
		operation_buy_product_ll.setVisibility(View.GONE);
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		if (result.getUrl().equals(UrlConstant.PRODUCT_GETPRODUCTDETAILS)) { // 详情页
			operation_buy_product_ll.setVisibility(View.GONE);
			// notrans_rl_titlebar.setVisibility(View.VISIBLE);// 头部显示非透明
			showErrorLayView(root_sve, ProductDetailsAct.this); // 网络错误的覆盖页 监听R.id.click_reload 重新加载数据
		} else if (result.getUrl().equals(UrlConstant.ORDER_GETPRODUCTPRICEFORORDER) || result.getUrl().equals(UrlConstant.ORDER_INTOCART)) {
		}
		dismissLoadingDialog();
	}

	/**
	 * @description：填充数据
	 * @author shicm
	 * @date 2015-11-23 下午3:40:21
	 */
	protected void fillViewFromNetwork(ProductDetailBean bean) {
		productDetailBean = bean;
		imgUrls = bean.productImageArray;
		skuStocks = bean.skuStock;
		skuItems = bean.skuItems;
		if (imgUrls != null && imgUrls.size() > 0) {
			advertisementVp.setDatas(imgUrls, (LinearLayout) advertisementContainerRl.findViewById(R.id.advertisement_dotLl), false, false);
			advertisementVp.start(5000);
		}
		product_name_tv.setText(bean.productName);
		double showPrice = bean.skuStock.get(0).price;

		for (int i = 0; i < bean.skuStock.size(); i++) {
			if (showPrice > bean.skuStock.get(i).price) {
				showPrice = bean.skuStock.get(i).price;
			}
		}
		trade_price_tv.setText(Html.fromHtml("¥<big>" + MoneyShowTool.twolastDF(bean.productPrice) + "</big>"));
		commission_money_tv.setText(bean.commissionPrice + "");
		discount_tv.setText(bean.discount + "起");
		sales_tv.setText(Html.fromHtml("当前销量<font color=#333333>" + "　" + bean.salesVolume + "</font>件"));
		stock_tv.setText(Html.fromHtml("当前库存<font color=#333333>" + "　" + bean.stock + "</font>件"));
		postage_detail_show_tv.setText(bean.logistics > 0 ? "邮费:  ".concat("¥" + bean.logistics) : "配送方式:  包邮");
		postage_detail_show_tv.setText(" ".concat(bean.postage));
		comment_count_tv.setText("评价  (".concat(bean.commentCount + ")"));
		// comment_count_tv.setText(String.format(baseAct.getString(R.string.post_evaluation_change), bean.commentCount));
		// 处理评论为零的情况
		comment_lv_split_ll.setVisibility(0 == bean.commentCount ? View.GONE : View.VISIBLE);

		under_the_plane_tips_tv.setVisibility(bean.statusType.equals("1") ? View.VISIBLE : View.GONE);
		welfare_content_tv.setText(bean.favourableTip);

		initOperationListener();
	}

	@Override
	public void operationShopCat(boolean isSkip, String buyNum, String lastSelectSkuId, String skuText, int stock) {
		addShopCatList(isSkip, buyNum, lastSelectSkuId, skuText, stock);
	}

	/**
	 * @description：加入购物车
	 * @author isSkip 这个参数是否马上购买
	 * @date 2015年6月11日 下午2:40:57
	 */
	private void addShopCatList(final boolean isSkip, final String buyNum, final String lastSelectSkuId, final String skuText, final int stock) {
		showLoadingDialog(getString(R.string.common_loading));

		Type shopCartType;
		if (!isSkip) {
			shopCartType = new TypeToken<HashMap<String, String>>() {
			}.getType();
		} else {
			shopCartType = new TypeToken<OrderProduct>() {
			}.getType();
		}
		RdpNetCommand shopCartCommand = new RdpNetCommand(this, shopCartType);
		shopCartCommand.setServerApiUrl(isSkip ? UrlConstant.ORDER_GETPRODUCTPRICEFORORDER : UrlConstant.ORDER_INTOCART);
		shopCartCommand.clearConditions();
		String memberId = HKCloudApplication.getInstance().getUserModel().getMemberId();
		if (TextUtils.isEmpty(memberId)) {
			memberId = "0";
		}
		shopCartCommand.setCondition("memberId", memberId); // 用户id
		shopCartCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID); // 用户id
		if (!isSkip) {
			shopCartCommand.setCondition("productId", product_id);
			shopCartCommand.setCondition("sku", lastSelectSkuId);
			shopCartCommand.setCondition("buyNum", buyNum);
		} else {
			shopCartCommand.setCondition("productDetail", getProductDetailInfo(buyNum, lastSelectSkuId, skuText));
			shopCartCommand.setCondition("addressId","");
		}
		shopCartCommand.execute();
		shopCartCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				dismissLoadingDialog();
				if (isSkip) {
					OrderProduct orderProduct = (OrderProduct) data;
					if (orderProduct.getProductRs().get(0).type == 1) {
						Intent mIntent = new Intent(ProductDetailsAct.this, ConfirmOrderActivity.class);
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
	public void onScrollPageChangeListener(int pageNum) {
		super.onScrollPageChangeListener(pageNum);
		if (pageNum == 1) {
			initbottomView();
			product_introduce();
		}
	}

	/**
	 * 商品详情下部分，介绍
	 */
	private void product_introduce() {
		showLoadingDialog(getString(R.string.common_loading));
		Type intrType = new TypeToken<HashMap<String, String>>() {
		}.getType();
		RdpNetCommand introCommand = new RdpNetCommand(this, intrType);
		introCommand.clearConditions();
		introCommand.setCondition("productId", product_id);
		introCommand.execute();
		introCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				dismissLoadingDialog();
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) data;
				if (null != map && map.size() > 0) {
					product_detail_map = map;
					checkedListener.onClick(pViews[0]);
				}
			}
		});
		introCommand.setOnCommandFailedListener(this);
	}

	/**
	 * @description：底部视图
	 * @author shicm
	 * @date 2015-11-13 上午10:23:01
	 */
	private void initbottomView() {
		wv_html1 = (CustomWebView) this.findViewById(R.id.wv_html1);
		wv_html2 = (CustomWebView) this.findViewById(R.id.wv_html2);
		wv_html3 = (CustomWebView) this.findViewById(R.id.wv_html3);
		// 商品详情bar部分
		View in_tab = this.findViewById(R.id.ll_tab);
		pViews[0] = (CheckedTextView) in_tab.findViewById(R.id.tv_p_detail);
		pViews[1] = (CheckedTextView) in_tab.findViewById(R.id.tv_p_param);
		pViews[2] = (CheckedTextView) in_tab.findViewById(R.id.tv_p_service);
		for (int i = 0; i < pViews.length; i++) {
			pViews[i].setToggable(false);
			pViews[i].setTag(i);
			pViews[i].setOnClickListener(checkedListener);
		}
		floatViews[0] = (CheckedTextView) float_bar.findViewById(R.id.tv_p_detail);
		floatViews[1] = (CheckedTextView) float_bar.findViewById(R.id.tv_p_param);
		floatViews[2] = (CheckedTextView) float_bar.findViewById(R.id.tv_p_service);
		for (int i = 0; i < floatViews.length; i++) {
			floatViews[i].setToggable(false);
			floatViews[i].setTag(i);
			floatViews[i].setOnClickListener(checkedListener);
		}
	}

	/**
	 * 商品详情bar点击事件监听
	 */
	private View.OnClickListener checkedListener = new View.OnClickListener() {
		public void onClick(View v) {
			CheckedTextView temp = (CheckedTextView) v;
			if (temp.isChecked()) return;
			int position = (Integer) temp.getTag();
			for (int i = 0; i < pViews.length; i++) {
				CheckedTextView ctv = pViews[i];
				CheckedTextView ctvFloat = floatViews[i];
				if (position == i) {
					ctv.setChecked(true);
					ctv.setTextColor(0xFFff4400);

					ctvFloat.setChecked(true);
					ctvFloat.setTextColor(0xFFff4400);
				} else {
					ctv.setTextColor(0xFF434343);
					ctv.setChecked(false);
					ctvFloat.setTextColor(0xFF434343);
					ctvFloat.setChecked(false);
				}
			}
			String html = "";
			switch (position) {
				case 0:
					html = product_detail_map.get("introduction");
					wv_html1.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
					break;
				case 1:
					html = product_detail_map.get("productparameter");
					wv_html2.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
					break;
				case 2:
					html = product_detail_map.get("salesservice");
					wv_html3.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
					break;
			}
			wv_html1.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
			wv_html2.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
			wv_html3.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
		}
	};
	private EvaluteListImgsAdapter evaluteListImgsAdapter;

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		// 两条评价
		holder.getTextView(R.id.comment_user_tv);
		holder.getRatingBar(R.id.comment_level_rb);
		holder.getTextView(R.id.comment_dt_tv).setVisibility(View.GONE);
		holder.getTextView(R.id.comment_content_tv);
		holder.getTextView(R.id.machent_res_tv).setVisibility(View.GONE);
		GridViewForScrollView grideView = (GridViewForScrollView) holder.getView(R.id.shop_evaluate_gridview);
		evaluteListImgsAdapter = new EvaluteListImgsAdapter(this);
		grideView.setAdapter(evaluteListImgsAdapter);
		evaluteListImgsAdapter.setItemLayoutID(R.layout.comment_image_item);
		evaluteListImgsAdapter.setListener(new OnRefreshItemViewsListener() {

			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
				ProductImageArray mproductImage = (ProductImageArray) adapter.getItem(position);
				RdpImageLoader.displayImage(mproductImage.getImageUrl(), holder.getImageView(R.id.seller_main_item_iv));
				return false;
			}
		});
		return false;
	}

	private String getProductDetailInfo(String buyCount, String skuId, String skuText) {
		JSONArray array = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("productId", product_id);
			jsonObject.put("skuAttribute", skuText);
			jsonObject.put("skuId", skuId);
			jsonObject.put("productCount", buyCount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		array.put(jsonObject);
		return array.toString();
	}

	@Override
	protected void refreshSkuData() {
		Type skuType = new TypeToken<ProductDetailBean>() {
		}.getType();
		RdpNetCommand detailCommand = new RdpNetCommand(this, skuType);
		detailCommand.setServerApiUrl(UrlConstant.PRODUCT_GETPRODUCTSKUDETAILS); // 商品详情
		detailCommand.clearConditions();
		detailCommand.setCondition("productId", product_id);
		detailCommand.execute();
		detailCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {
			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
				if (data != null) {
					ProductDetailBean product = (ProductDetailBean) data;
					productDetailBean.skuItems = product.skuItems;
					productDetailBean.skuStock = product.skuStock;
				} else {
					showToastMsg("sku获取不到");
				}
			}
		});
		detailCommand.setOnCommandFailedListener(this);
	}
	
	
}
