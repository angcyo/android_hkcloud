package com.huika.cloud.control.cart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.me.activity.MyRecipientActivity;
import com.huika.cloud.control.pay.activity.HKCloudPayActivity;
import com.huika.cloud.support.event.CartChangeEvent;
import com.huika.cloud.support.model.AddressBean;
import com.huika.cloud.support.model.CartProduct;
import com.huika.cloud.support.model.OrderProduct;
import com.huika.cloud.support.model.SubmitOrder;
import com.huika.cloud.util.MoneyShowTool;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @description：确认订单的页面
 * @author shicm
 * @date 2015-11-10 下午4:37:20
 */
public class ConfirmOrderActivity extends RdpBaseActivity implements OnRefreshItemViewsListener{
	public static final String INP_PRODUCT_LIST = "PRODUCTLIST";
	public int INP_requestCode = 1001;
	private OrderProduct orderProduct;
	private View mLayoutView;
	private RdpDataListAdapter<CartProduct> listAdapter;
	
	private RdpListView mLvProduct;
	private View mLvHeadView;
	private RelativeLayout goto_add_address_chird_rl;
	private TextView tv_no_address;
	private TextView receiver_name_tv;
	private ImageView img_adress_icon;
	private TextView receiver_phone_tv;
	private TextView receiver_address_tv;
	
	private View mLvBottomView;
	private EditText edt_merchant_note; // 企业留言
	private TextView tv_order_shop_money;  // 商品多少钱
	private TextView tv_order_save_money;  // 节省
	private TextView tv_order_express_money; // 运费
	private TextView tv_order_welfare; // 福利
	private TextView tv_pay_product_num; // 商品数目
	private TextView tv_order_all_money;  // 付多少钱
	
	
	private Button goto_confirm_btn; 
	private TextView num_money_tv; 
	private AddressBean address;
	private double allMoney = 0;
//	private RdpDataListAdapter<CartProduct> cartAdapter;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		initDatas();
		mLayoutView = addMasterView(R.layout.order_comfirm_bottom_sure);
		initView();
	}
	
	private void initDatas() {
		orderProduct = (OrderProduct) getIntent().getSerializableExtra(INP_PRODUCT_LIST);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initView() {
		setFuncTitle("确认订单");
		mLvProduct = (RdpListView) mLayoutView.findViewById(R.id.lv_product_comfirm);
		mLvProduct.setMode(Mode.DISABLED);
		goto_confirm_btn = (Button)mLayoutView.findViewById(R.id.goto_confirm_btn);
		num_money_tv = (TextView)mLayoutView.findViewById(R.id.num_money_tv);
		
		listAdapter = new RdpDataListAdapter<CartProduct>(this, R.layout.order_product_confirm_item);
		mLvProduct.setAdapter(listAdapter);
		listAdapter.setListener(this);
		
		// listView的头部
		mLvHeadView = View.inflate(this, R.layout.order_comfirm_head, null);
		goto_add_address_chird_rl = (RelativeLayout) mLvHeadView.findViewById(R.id.goto_add_address_chird_rl);
	    mLvProduct.getRefreshableView().addHeaderView(mLvHeadView);
	    tv_no_address = (TextView)mLvHeadView.findViewById(R.id.tv_no_address);
	    receiver_name_tv = (TextView)mLvHeadView.findViewById(R.id.receiver_name_tv);
	    receiver_phone_tv = (TextView)mLvHeadView.findViewById(R.id.receiver_phone_tv);
		img_adress_icon = (ImageView) mLvHeadView.findViewById(R.id.img_adress_icon);
		receiver_address_tv = (TextView)mLvHeadView.findViewById(R.id.receiver_address_tv);
	    
	    // listView 的底部
	    mLvBottomView = View.inflate(this, R.layout.order_comfirm_bottom, null);
	    tv_order_shop_money = (TextView)mLvBottomView.findViewById(R.id.tv_order_shop_money);
	    tv_order_save_money = (TextView)mLvBottomView.findViewById(R.id.tv_order_save_money);
	    tv_order_express_money = (TextView)mLvBottomView.findViewById(R.id.tv_order_all_express_money);
	    tv_order_welfare = (TextView) mLvBottomView.findViewById(R.id.tv_order_welfare);
	    edt_merchant_note = (EditText)mLvBottomView.findViewById(R.id.edt_merchant_note);
	    tv_pay_product_num = (TextView)mLvBottomView.findViewById(R.id.tv_pay_product_num);
	    tv_order_all_money = (TextView)mLvBottomView.findViewById(R.id.tv_order_all_money);
	    
	    initLister();
	    setAdapterData();
	}

	/**
	 * @description：设置适配器的
	 * @author shicm
	 * @date 2015-12-9 下午3:32:43
	 */
	private void setAdapterData() {
		address = orderProduct.getReceiverRs();
		initAddressUI(address);
		listAdapter.setData(orderProduct.getProductRs());
		mLvProduct.getRefreshableView().addFooterView(mLvBottomView);
		initBottomUI(orderProduct.getLogistics(),orderProduct.getProductRs());
	}

	private void initLister() {
		goto_confirm_btn.setOnClickListener(this);
		goto_add_address_chird_rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.goto_confirm_btn:
				if (address == null) {
					showToastMsg("请先填写收货地址");
					return;
				}
                // 提交订单
				requestOrder();
				break;
			case R.id.goto_add_address_chird_rl:
				// 地址信息
				Intent intent = new Intent(this,MyRecipientActivity.class);
				intent.putExtra(MyRecipientActivity.IPN_ITEM_CLICK_TYPE, 1);
				startActivityForResult(intent, INP_requestCode);
				break;
			default:
				break;
		}
		super.onClick(v);
	}
	 @Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == INP_requestCode && arg1 == MyRecipientActivity.RESULTCODE) {
			address = (AddressBean) arg2.getSerializableExtra(Constant.IPN_SELECTED_ITEM);
			getAddressData();
		}
	}
	 
	private void getAddressData() {
		showLoadingDialog("");
		Type adressType = new TypeToken<OrderProduct>() {
		}.getType();
		RdpNetCommand addressCommand = new RdpNetCommand(this, adressType);
		addressCommand.setOnCommandSuccessedListener(this);
		addressCommand.setOnCommandFailedListener(this);
		addressCommand.setServerApiUrl(UrlConstant.ORDER_GETPRODUCTPRICEFORORDER); // 默认地址的接口
		addressCommand.clearConditions();
		addressCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		addressCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().memberId);
		addressCommand.setCondition("productDetail", getProductDetailInfo());
		addressCommand.setCondition("addressId", address.addressId);
		addressCommand.execute();
	}

	private void requestOrder() {
		showLoadingDialog("");
		Type orderType = new TypeToken<SubmitOrder>() {
		}.getType();
		RdpNetCommand orderCommand = new RdpNetCommand(this, orderType);
		orderCommand.setOnCommandSuccessedListener(this);
		orderCommand.setOnCommandFailedListener(this);
		orderCommand.setServerApiUrl(UrlConstant.ORDER_SUBMITORDER); // 下订单的接口
		orderCommand.clearConditions();
		orderCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().memberId);
		orderCommand.setCondition("addressId", address.addressId);
		orderCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		orderCommand.setCondition("remark", edt_merchant_note.getText().toString());
		orderCommand.setCondition("couponId", ""); // 现金卷id
		orderCommand.setCondition("integralId", ""); // 积分id
		orderCommand.setCondition("shopDetail", getProductInfo()); // 积分id
		orderCommand.execute();
	}

	private String getProductInfo(){
		JSONArray array = new JSONArray();
		for (int i = 0; i < orderProduct.getProductRs().size(); i++) {
			CartProduct cartProduct = orderProduct.getProductRs().get(i);
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("productId", cartProduct.getProductId());
				jsonObject.put("skuId", cartProduct.getSku());
				jsonObject.put("productCount", cartProduct.getCount() + "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(jsonObject);
		}
		return array.toString();
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
//		// 默认地址的接口
		if (result.getUrl().equals(UrlConstant.ORDER_GETPRODUCTPRICEFORORDER)) {
			orderProduct = (OrderProduct) data;
			setAdapterData();
		} else 
			if (result.getUrl().equals(UrlConstant.ORDER_SUBMITORDER)) {
			EventBus.getDefault().post(new CartChangeEvent());
			SubmitOrder order = (SubmitOrder) data;
			Bundle bundle = new Bundle();
			bundle.putDouble(HKCloudPayActivity.INP_HKCLOUD_PAY, order.orderPrice);
			bundle.putString(HKCloudPayActivity.INP_HKCLOUD_ORDER_NUM, order.orderNumber);
			bundle.putSerializable(HKCloudPayActivity.INP_HKCLOUD_ORDER_ADDRESS, orderProduct.getReceiverRs());
			skipActivity(this, HKCloudPayActivity.class);
		}
	}

	/**
	 * @description：listView底部的UI
	 * @author shicm
	 * @date 2015-11-26 下午3:17:44
	 */
	private void initBottomUI(double postPrice,List<CartProduct> cartList) {
		double mMoney = 0;
		int productNum = 0;
		for (CartProduct mCart: cartList) {
			mMoney += mCart.getCount() * mCart.getProductPrice();
			productNum += mCart.getCount();
		}
		tv_order_shop_money.setText(MoneyShowTool.twolastDF(mMoney));
		tv_order_save_money.setText("0元");
		tv_pay_product_num.setText("共有" + productNum + "件商品");
		tv_order_welfare.setText("无");
		tv_order_all_money.setText(Html.fromHtml("<font color='#D8D8D8'>合计：</font>"
				+"<font color='#FDFDFD'>"+ MoneyShowTool.twolastDF(mMoney)+"</font>")); // 合计的金额
		tv_order_express_money.setText(MoneyShowTool.twolastDF(postPrice)); // 邮费的处理
		allMoney += mMoney;
		num_money_tv.setText(Html.fromHtml("<font color='#D8D8D8'>合计：</font>" + "<font color='#FDFDFD'>" +
				MoneyShowTool.twolastDF(allMoney)+"</font>")); // 最后的钱
	}

	/**
	 * @description：地址的UI设计即listView头部的UI
	 * @author shicm
	 * @date 2015-11-19 下午12:08:22
	 */
	private void initAddressUI(AddressBean mAddress) {
		if (mAddress == null) {
			showToastMsg("地址没有");
			receiver_name_tv.setVisibility(View.GONE);
			receiver_phone_tv.setVisibility(View.GONE);
			receiver_address_tv.setVisibility(View.GONE);
			img_adress_icon.setVisibility(View.GONE);
			tv_no_address.setVisibility(View.VISIBLE);
		} else {
    		receiver_name_tv.setVisibility(View.VISIBLE);
    		receiver_phone_tv.setVisibility(View.VISIBLE);
    		receiver_address_tv.setVisibility(View.VISIBLE);
			img_adress_icon.setVisibility(View.VISIBLE);
			tv_no_address.setVisibility(View.GONE);
    		receiver_name_tv.setText("收货人:" + mAddress.receiverName);
    		receiver_phone_tv.setText(mAddress.receiverPhone);
    		receiver_address_tv.setText("收货地址：" + mAddress.receiverAddress);
		}
	}
	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		CartProduct cartProduct = (CartProduct) adapter.getItem(position);
		RdpImageLoader.displayImage(cartProduct.getProductImageUrl(),
				holder.getImageView(R.id.product_ic_iv), R.drawable.index_advert_default);
		holder.getTextView(R.id.tv_product_name).setText(cartProduct.getProductName());
		holder.getTextView(R.id.tv_quantity_order).setText(MoneyShowTool.twolastDF(cartProduct.getProductPrice()));
		holder.getTextView(R.id.tv_sku_property).setText(cartProduct.getSkuRs());
		holder.getTextView(R.id.tv_quantity_order_gg).setText(cartProduct.getCount() + "");
		return  false;
	}
	private String getProductDetailInfo() {
		JSONArray array = new JSONArray();
		for (int i = 0; i < orderProduct.getProductRs().size(); i++) {
			CartProduct cartProduct = orderProduct.getProductRs().get(i);
			if (cartProduct.isSelect()) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("productId", cartProduct.getProductId());
					jsonObject.put("skuAttribute", cartProduct.getSkuRs());
					jsonObject.put("skuAttribute", cartProduct.getSkuRs());
					jsonObject.put("skuId", cartProduct.getSku());
					jsonObject.put("productCount", cartProduct.getCount());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(jsonObject);
			}
		}
		return array.toString();
	}
}
