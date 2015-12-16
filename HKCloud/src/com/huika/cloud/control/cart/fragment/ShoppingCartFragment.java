package com.huika.cloud.control.cart.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.cart.activity.ConfirmOrderActivity;
import com.huika.cloud.control.cart.adapter.CartAdapter;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.event.CartChangeEvent;
import com.huika.cloud.support.event.MainPagerChangeEvent;
import com.huika.cloud.support.model.CartProduct;
import com.huika.cloud.support.model.OrderProduct;
import com.huika.cloud.util.MMAlertDialog;
import com.huika.cloud.util.MoneyShowTool;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

import de.greenrobot.event.EventBus;

/**
 * @description：购物车
 * @date 2015-11-9 上午10:16:50
 */
public class ShoppingCartFragment extends RdpBaseFragment implements OnRefreshItemViewsListener {
	public static final String INP_BACK = "COMMON_BACK";
	private View mView;
	@ViewInject(R.id.cart_empty_rl)
	private RelativeLayout cart_empty_rl; // 空布局
	@ViewInject(R.id.go_shopping_tv)
	private TextView go_shopping_tv;

	@ViewInject(R.id.no_login_rl)
	private LinearLayout no_login_rl;
	@ViewInject(R.id.login_tv)
	private TextView login_tv;

	@ViewInject(R.id.cover_view_rl)
	private RelativeLayout cover_view_rl; // 内容布局
	@ViewInject(R.id.lv_cart_list)
	private RdpListView mListView; // 列表
	@ViewInject(R.id.all_checked_cb)
	private Button all_checked_cb; // 全选按钮
	@ViewInject(R.id.all_delete)
	private TextView all_delete; // 删除按钮
	@ViewInject(R.id.tv_all_money)
	private TextView tv_all_money; // 金额
	@ViewInject(R.id.tv_type_shipment)
	private TextView tv_type_shipment; // 是否含运费
	@ViewInject(R.id.btn_calculate)
	private Button btn_calculate; // 结算按钮

	private boolean isBackShow = false;
	private List<CartProduct> cartList = new ArrayList<CartProduct>();
	private CartAdapter mCartAdapter;
	private CartProduct mCartProduct;
	private long buyCount;
	private int mType = 0;

	/**
	 * 内部提供，尽量少让外部直接去new一个Fragment
	 */
	public static ShoppingCartFragment newInstance() {
		return new ShoppingCartFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		RdpAnnotationUtil.inject(this, mRootView);
		EventBus.getDefault().register(this);
		initView();
		if (HKCloudApplication.getInstance().getUserModel().getMemberId() != null) {
			showLoadingOverLay(mLltMasterArea);
			excuteCartData();
		} else {
			mType = 1;
			isOnShow();
		}
		return mRootView;
	}

	@Override
	protected void initFragment() {
		initDatas();
		super.initFragment();
		if (isBackShow) {
			removeLeftFuncView(TBAR_FUNC_BACK);
		}
		setFuncTitle("购物车");
		mView = addMasterView(R.layout.shop_cart);
	}

	public void onEventMainThread(CartChangeEvent event) {
		excuteCartData();
	}

	private void initDatas() {
		isBackShow = getArguments().getBoolean(INP_BACK); // 获取传过来的参数
	}

	private void initView() {
		all_checked_cb.setSelected(true);
		mCartAdapter = new CartAdapter(getActivity(), R.layout.order_cart_product_item);
		mListView.setAdapter(mCartAdapter);
		mCartAdapter.setListener(this);
		mListView.setScrollingWhileRefreshingEnabled(true);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
				excuteCartData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
			}
		});
	}

	@OnClick({ R.id.go_shopping_tv, R.id.login_tv, R.id.all_checked_cb, R.id.all_delete, R.id.btn_calculate })
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.go_shopping_tv: // 回首页
				EventBus.getDefault().post(new MainPagerChangeEvent(0));
				break;
			case R.id.login_tv: // 登录按钮
				showActivity(getActivity(), LoginActivity.class);
				// showActivity(getActivity(), ProductDetailsAct.class);
				break;
			case R.id.all_checked_cb: // 全选按钮
				boolean flagAll = all_checked_cb.isSelected();
				all_checked_cb.setSelected(!flagAll);
				mCartAdapter.setAllSelect(!flagAll);
				refreshUI();
				break;
			case R.id.all_delete: // 全部删除按钮
				Dialog dialog = MMAlertDialog.createSoftwareUpdate(getActivity(), "确认将购物车删除", new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						showLoadingDialog("删除中...");
						excuteEditeCart(null, 0, true);
					}
				});
				dialog.show();
				break;
			case R.id.btn_calculate: // 计算按钮
				requestOrderProduct();
				break;
			default:
				break;
		}
	}

	/**
	 * @description：请求地址
	 * @author shicm
	 * @date 2015-11-18 下午5:26:52
	 */
	private void requestOrderProduct() {
		Type adressType = new TypeToken<OrderProduct>() {
		}.getType();
		RdpNetCommand addressCommand = new RdpNetCommand(getActivity(), adressType);
		addressCommand.setOnCommandSuccessedListener(this);
		addressCommand.setOnCommandFailedListener(this);
		addressCommand.setServerApiUrl(UrlConstant.ORDER_GETPRODUCTPRICEFORORDER); // 默认地址的接口
		addressCommand.clearConditions();
		addressCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		addressCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().getMemberId());
		addressCommand.setCondition("productDetail", getProductDetailInfo());
		addressCommand.setCondition("addressId", "");
		addressCommand.execute();
	}

	/**
	 * @description：编辑
	 * @author shicm
	 * @date 2015-11-25 上午10:06:25
	 */
	private void excuteEditeCart(CartProduct cartProduct, long count, boolean isAllDelete) {
		mCartProduct = cartProduct;
		buyCount = count;
		Type editType = new TypeToken<String>() {
		}.getType();
		RdpNetCommand editCommand = new RdpNetCommand(getActivity(), editType);
		editCommand.setOnCommandSuccessedListener(this);
		editCommand.setOnCommandFailedListener(this);
		editCommand.setServerApiUrl(UrlConstant.ORDER_EDITCART); //
		editCommand.clearConditions();
		editCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().getMemberId()); // HKCloudApplication.getInstance().getUserModel().getMemberId()
		editCommand.setCondition("type", isAllDelete ? 1 : 0);
		editCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID); // HKCloudApplication.getInstance().getUserModel().getMerchantId()
		editCommand.setCondition("productId", isAllDelete ? getSelectProductID(0) : cartProduct.getProductId()); //
		editCommand.setCondition("count", count); // count
		editCommand.execute();
	}

	/**
	 * 除了适配器的UI其他的ui更新
	 * @description：
	 * @author shicm
	 * @date 2015-11-23 下午4:49:27
	 */
	private void refreshUI() {
		double allMoney = 0;
		double allFreightPrice = 0;
		for (int i = 0; i < cartList.size(); i++) {
			CartProduct cartProduct = cartList.get(i);
			if (cartProduct.isSelect()) {
				allMoney += cartProduct.getCount() * cartProduct.getProductPrice();
				allFreightPrice += cartProduct.getLogistics();
			}
		}
		tv_type_shipment.setText(allFreightPrice == 0 ? "运费 ：包邮" : "运费：" + MoneyShowTool.twolastDF(allFreightPrice));
		tv_all_money.setText(MoneyShowTool.twolastDF(allMoney));
	}

	/**
	 * @description：得到选择的商品ID
	 * @author shicm
	 * @date 2015-12-2 上午10:59:18
	 */
	private String getSelectProductID(int type) {
		String productData = "";
		for (int i = 0; i < cartList.size(); i++) {
			CartProduct cartProduct = cartList.get(i);
			if (cartProduct.isSelect()) {
				if (type == 0) {
					productData += cartProduct.getProductId() + ",";
				} else if (type == 1) {
					productData += cartProduct.getSku() + ",";
				}

			}
		}
		return productData.substring(0, productData.length() - 1);
	}

	// 其他空的购物车1未登录 0展现购物车 另外一种是网络错误
	private void isOnShow() {
		if (mType == 0) {
			cart_empty_rl.setVisibility(View.GONE);
			no_login_rl.setVisibility(View.GONE);
			cover_view_rl.setVisibility(View.VISIBLE);
		} else if (mType == 1) {
			cart_empty_rl.setVisibility(View.GONE);
			no_login_rl.setVisibility(View.VISIBLE);
			cover_view_rl.setVisibility(View.GONE);
		} else {
			cart_empty_rl.setVisibility(View.VISIBLE);
			no_login_rl.setVisibility(View.GONE);
			cover_view_rl.setVisibility(View.GONE);
		}
	}

	private void excuteCartData() {
		Type cartType = new TypeToken<ArrayList<CartProduct>>() {
		}.getType();
		RdpNetDataSet cartCommand = new RdpNetDataSet(getActivity(), cartType);
		cartCommand.setOnCommandSuccessedListener(this);
		cartCommand.setOnCommandFailedListener(this);
		cartCommand.setServerApiUrl(UrlConstant.ORDER_GETCARTLIST); //
		cartCommand.clearConditions();
		cartCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().getMemberId()); // HKCloudApplication.getInstance().getUserModel().getMemberId()
		cartCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		cartCommand.open();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		dismissLoadingDialog();
		hideOverLayView();
		if (result.getUrl().equals(UrlConstant.ORDER_GETCARTLIST)) {
			cartList.clear();
			cartList = (List<CartProduct>) data;
			mListView.onRefreshComplete();
			if (cartList != null && cartList.size() > 0) {
				mType = 0;
				mCartAdapter.setData(cartList);
				refreshUI();
			} else {
				mType = 2;
			}
			isOnShow();
		} else if (result.getUrl().equals(UrlConstant.ORDER_EDITCART)) {
			if (buyCount == 0) {
				showLoadingDialog("");
				excuteCartData(); // 删除的从新走一遍请求
			} else {
				mCartProduct.setEditor(!mCartProduct.isEditor());
				mCartProduct.setCount(buyCount);
				mCartAdapter.notifyDataSetChanged();
				refreshUI();
			}
		} else if (result.getUrl().equals(UrlConstant.ORDER_GETPRODUCTPRICEFORORDER)) {
			OrderProduct orderProduct = (OrderProduct) data;
			checkOrder(orderProduct);
		}

	}

	private void checkOrder(OrderProduct orderProduct) {
		String msg = "";
		for (int i = 0; i < orderProduct.getProductRs().size(); i++) {
			CartProduct checkProduct = orderProduct.getProductRs().get(i);
			if (checkProduct.type != 1) {
				msg = msg + checkProduct.getProductName() + ",";
			}
		}
		if (TextUtils.isEmpty(msg)) {
			Intent mIntent = new Intent(getActivity(), ConfirmOrderActivity.class);
			mIntent.putExtra(ConfirmOrderActivity.INP_PRODUCT_LIST, orderProduct);
			showActivity(getActivity(), mIntent);
		} else {
			showToastMsg(msg.substring(0, msg.length() - 1) + "等商品库存不足，请修改购买数量");
		}
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		dismissLoadingDialog();
		if (result.getUrl().endsWith(UrlConstant.ORDER_GETCARTLIST)) {
			showErrorLayView(mLltMasterArea, new OnClickListener() {
				@Override
				public void onClick(View v) {
					showLoadingOverLay(mLltMasterArea);
					excuteCartData();
				}
			});
		} else {
			showToastMsg(result.getMsg());
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public boolean onRefreshItemViews(final RdpAdapter adapter, int position, View convertView, final AdapterViewHolder holder) {
		final CartProduct cartProduct = (CartProduct) adapter.getItem(position);
		initHodlerData(adapter, position, holder);
		OnClickListener itemViewLister = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.goods_delete_tv:
						Dialog dialog = MMAlertDialog.createSoftwareUpdate(getActivity(), "确认将这个宝贝删除", new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}, new OnClickListener() {
							@Override
							public void onClick(View v) {
								excuteEditeCart(cartProduct, 0, false);
							}
						});
						dialog.show();
						break;
					case R.id.goods_add_btn:
						holder.getEditText(R.id.goods_num_show_tv).setText((Long.parseLong(holder.getEditText(R.id.goods_num_show_tv).getText().toString()) + 1) + "");
						break;
					case R.id.goods_sub_btn:
						if (cartProduct.getCount() <= 1) {
							showToastMsg(R.string.cart_stock_low_one);
						} else {
							holder.getEditText(R.id.goods_num_show_tv).setText((Long.parseLong(holder.getEditText(R.id.goods_num_show_tv).getText().toString()) - 1) + "");
						}
						break;
					case R.id.product_edit_tv:
						if (cartProduct.isEditor()) {
							showLoadingDialog("");
							excuteEditeCart(cartProduct, Long.parseLong(holder.getEditText(R.id.goods_num_show_tv).getText().toString()), false);
						} else {
							cartProduct.setEditor(!cartProduct.isEditor());
							adapter.notifyDataSetChanged();
						}
						break;
					default:
						break;
				}

			}
		};
		initHodlerLister(adapter, position, holder, itemViewLister);
		holder.getTextView(R.id.goods_num_tv).setText(holder.getEditText(R.id.goods_num_show_tv).getText().toString());
		return false;
	}

	private void initHodlerData(RdpAdapter adapter, int position, AdapterViewHolder holder) {
		CartProduct mCartProduct = (CartProduct) adapter.getItem(position);
		holder.getCheckBox(R.id.cart_goods_cb).setChecked(mCartProduct.isSelect());
		holder.getCheckBox(R.id.cart_goods_cb).setOnCheckedChangeListener(null);
		RdpImageLoader.displayImage(mCartProduct.getProductImageUrl(), holder.getImageView(R.id.goods_img_iv), R.drawable.product_list_default);

		holder.getEditText(R.id.goods_num_show_tv).setText(mCartProduct.getCount() + "");
		holder.getTextView(R.id.product_edit_tv).setText(mCartProduct.isEditor() ? "完成" : "编辑");

		// 设置展示
		holder.getTextView(R.id.goods_name_tv).setVisibility(!mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getTextView(R.id.goods_sku_tv).setVisibility(!mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getTextView(R.id.goods_price_tv).setVisibility(!mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getTextView(R.id.goods_num_tv).setVisibility(!mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getTextView(R.id.goods_delete_tv).setVisibility(mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getLinearLayout(R.id.goods_editor_ll).setVisibility(mCartProduct.isEditor() ? View.VISIBLE : View.GONE);
		holder.getTextView(R.id.goods_name_tv).setText(mCartProduct.getProductName());
		holder.getTextView(R.id.goods_sku_tv).setText(mCartProduct.getSkuRs());
		holder.getTextView(R.id.goods_price_tv).setText(MoneyShowTool.twolastDF(mCartProduct.getProductPrice()));

	}

	/**
	 * @description：监听
	 * @author shicm
	 * @date 2015-12-10 下午2:05:41
	 */
	private void initHodlerLister(final RdpAdapter adapter, final int position, final AdapterViewHolder holder, OnClickListener itemViewLister) {
		holder.getButton(R.id.goods_add_btn).setOnClickListener(itemViewLister);
		holder.getTextView(R.id.product_edit_tv).setOnClickListener(itemViewLister);
		holder.getButton(R.id.goods_sub_btn).setOnClickListener(itemViewLister);
		holder.getTextView(R.id.goods_delete_tv).setOnClickListener(itemViewLister);
		holder.getEditText(R.id.goods_num_show_tv).addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (Long.parseLong(s.toString()) < 1) {
					showToastMsg(getString(R.string.cart_stock_low_one));
					holder.getEditText(R.id.goods_num_show_tv).setText(1);
				}
			}
		});
		holder.getCheckBox(R.id.cart_goods_cb).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				((CartProduct) adapter.getItem(position)).setSelect(isChecked);
				int num = 0;
				for (int i = 0; i < adapter.getCount(); i++) {
					CartProduct mCProduct = (CartProduct) adapter.getItem(i);
					if (mCProduct.isSelect()) {
						num++;
					}
				}
				all_checked_cb.setSelected(num == adapter.getCount());
				refreshUI();
			}

		});
	}

	private String getProductDetailInfo() {
		JSONArray array = new JSONArray();
		for (int i = 0; i < cartList.size(); i++) {
			CartProduct cartProduct = cartList.get(i);
			if (cartProduct.isSelect()) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("productId", cartProduct.getProductId());
					jsonObject.put("skuAttribute", cartProduct.getSkuRs());
					jsonObject.put("skuAttribute", cartProduct.getSkuRs());
					jsonObject.put("skuId", cartProduct.getSku());
					jsonObject.put("productCount", cartProduct.getCount());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				array.put(jsonObject);
			}
		}
		return array.toString();
	}

}
