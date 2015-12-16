package com.huika.cloud.control.eshop.actvity;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.eshop.adapter.OrderGoodsAdapter;
import com.huika.cloud.control.eshop.common.MyOrderBusiOper;
import com.huika.cloud.support.model.MyOrderDetailBean;
import com.huika.cloud.support.model.OrderGoodsBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpNetListBaseActivity;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerListView;

import de.greenrobot.event.EventBus;

/**
 * @description：我的订单详情
 * @author zhoukl
 * @date 2015-5-25 下午4:58:36
 */
public class MyOrderDetailActivity extends RdpNetListBaseActivity { 
	public static final String IPN_ORDER_NO = "IPN_ORDER_NO";
	// private OrderGoodsAdapter mAdapter;
	private MyOrderDetailBean mData;

	private Button btnBusiOne, btnBusiTwo, btnBusiExpress;
	private String mOrderNumber;

	 private MyOrderBusiOper mOrderBusiOper;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("订单详情");

		// TODO: ???
		// EventBus.getDefault().register(this);
		mOrderNumber = getIntent().getStringExtra(IPN_ORDER_NO);
		mOrderBusiOper = new MyOrderBusiOper(this, null);

		mMasterAdapter.setItemLayoutID(R.layout.my_order_detail_item);

		 btnBusiOne = (Button) findViewById(R.id.btnBusiOne);
		 btnBusiTwo = (Button) findViewById(R.id.btnBusiTwo);
		 btnBusiExpress = (Button) findViewById(R.id.btnBusiExpress);
		// hxInfo = (TextView) findViewById(R.id.tv_more_hx_info);
		//
		// hxInfo.setOnClickListener(this);
		// showLoadingOverLay(findViewById(R.id.rltUI));
	}

	@Override
	protected void openDataSet() {
		super.openDataSet();

		mDataSet.setServerApiUrl(UrlConstant.API_GET_OERDER_DETAIL);
		mDataSet.clearConditions();
		mDataSet.setCondition("orderNumber", mOrderNumber);
		mDataSet.setCondition("memberId", getMemberId());

		mDataSet.setObjectToList(true);
		Type typeOfResult = new TypeToken<ArrayList<MyOrderDetailBean>>() { }.getType();
		mDataSet.setTypeOfResult(typeOfResult);

		mDataSet.open();
		
//		mDataSet.setData(MyOrderDetailBean.getDemoData());
	}

	@Override
	protected int getMasterBaseLayoutID() {
//		return  super.getMasterBaseLayoutID();
		return R.layout.my_order_detail_master_activity;
	}
	// 获取订单状态4 及 商品状态 也是 4 及物流号是否为多个
	private boolean getOrderShopState(MyOrderDetailBean detail) {
		boolean flag = false;
		String isHaveNumer = "";
		List<String> orderGoods = new ArrayList<String>();
		for (int i = 0; i < detail.Product.size(); i++) {
			if (detail.Product.get(i).productStatus == 4) {
				orderGoods.add(detail.Product.get(i).logisticsNumber);
			}
		}
		if (orderGoods.size() > 1) {
			if (null != orderGoods.get(0)) {
				isHaveNumer = orderGoods.get(0);
			}
			for (int i = 1; i < orderGoods.size(); i++) {
				if (null != orderGoods.get(i)) {
					if (!isHaveNumer.equals(orderGoods.get(i))) {
						flag = true;
						break;
					} else {
						isHaveNumer = orderGoods.get(i);
					}
				}

			}
		} else {
			flag = false;
		}
		return flag;
	}

	// 获取订单状态4 及 商品状态 也是 4 及物流号是否为多个
	private boolean getOrderShopIsComment(MyOrderDetailBean detail) {
		boolean flag = false;
		String isHaveNumer = "";
		int j = 0;
		List<String> orderGoods = new ArrayList<String>();
		for (int i = 0; i < detail.Product.size(); i++) {
			if (detail.Product.get(i).productStatus == 6) {
				orderGoods.add(detail.Product.get(i).isComment + "");
			}
		}
		if (orderGoods.size() > 1) {
			for (int i = 0; i < orderGoods.size(); i++) {
				if (orderGoods.get(i).equals("2")) {
					j++;
					if (j > 1) {
						flag = true;
						break;
					}
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}

	// public void onEventMainThread(OrderDetailsEvent event) {
	// // 退款成功
	// // getRemoteData();
	// if (event.goodsBean != null) {
	// List<OrderGoodsBean> list = mData.productArray;
	// for (int i = 0; i < list.size(); i++) {
	// if (list.get(i).orderDetailsId.equals(event.goodsBean.orderDetailsId)) {
	// list.remove(i);
	// list.add(i, event.goodsBean);
	// break;
	// }
	// }
	//
	// if(getBackSuccess(list)){
	// if(mData.orderStatus != 5 || mData.orderStatus != 6 || mData.orderStatus
	// != 8){
	// mData.orderStatus = 9;
	// mData.productArray = list;
	// refreshUI(mData);
	// }
	// }
	// else{
	// mAdapter.setGroup(list);
	// mAdapter.notifyDataSetChanged();
	// }
	// }
	// else {
	// mOrderNumber = mData.orderNumber;
	// showLoadingOverLay(findViewById(R.id.rltUI));
	// getRemoteData();
	// }
	//
	// }

	// 是否商品合部为退款、退货、换货成功
	private boolean getBackSuccess(List<OrderGoodsBean> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).productReturnStatus != 4) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position,
			View convertView, AdapterViewHolder holder) {
		MyOrderDetailBean data = (MyOrderDetailBean) adapter.getItem(position);
		holder.getTextView(R.id.tvOrderType).setText(data.getShowStatu());
		holder.getTextView(R.id.tvOrderType).setTextColor(Color.parseColor("#FFFFFF"));
		holder.getTextView(R.id.tvTotalAmount).setText("" + df.format(data.totalPrice) + "元");
		holder.getTextView(R.id.tvReceiver).setText(data.receiverName);
		holder.getTextView(R.id.tvMobile).setText(data.receiverPhone);
		holder.getTextView(R.id.tvAddress).setText(data.receiverAddress);
		holder.getTextView(R.id.tvLogisticsNo).setText(data.expressOrder);

		// mAdapter.setOrderNumber(data.orderNumber);
		// mAdapter.setShowComment(data.orderStatus ==
		// MyOrderBean.STATUS_WAIT_COMMENT);
		// mAdapter.setData(data.productArray);

		// holder.getTextView(R.id.tvMerchant).setText(data.shopName);

		holder.getTextView(R.id.tvGoodsNum).setText("" + data.Product.size());
		holder.getTextView(R.id.tvOrderFreight).setText("¥" + df.format(data.freightPrice));
		holder.getTextView(R.id.tvOrderMoney).setText("¥" + df.format(data.totalPrice));

		holder.getTextView(R.id.tvOrderNo).setText(data.orderNumber);

		
		mOrderBusiOper.refreshOperButtons(data, btnBusiOne, btnBusiExpress, btnBusiTwo);

//		TextView tvOrderTimeLabel = (TextView) findViewById(R.id.tvOrderTimeLabel);
//		tvOrderTimeLabel.setText("成交时间：");

		final RdpInnerListView lvOrderGoods = (RdpInnerListView) holder.getView(R.id.lvOrderGoods);
		lvOrderGoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = position - lvOrderGoods.getHeaderViewsCount();
				OrderGoodsBean data = (OrderGoodsBean) mMasterAdapter
						.getItem(index);
				Bundle bundle = new Bundle();
				// bundle.putString(Constant.PRODUCT_ID, data.productId);
				// showActivity(MyOrderDetailActivity.this,
				// ProductDetailsAct.class, bundle);
			}
		});
		OrderGoodsAdapter goodsAdapter = new OrderGoodsAdapter(this, data);
		goodsAdapter.setData(data.Product);
		lvOrderGoods.setAdapter(goodsAdapter);

		// tvOrderTime.setText(DateTimeTool.ymdhmDate(data.paidDt));

		// if (MyOrderBean.STATUS_WAIT_PAY == data.orderStatus) {
		// // tvOrderTimeLabel.setText();
		// tvOrderTime.setText(DateTimeTool.ymdhmDate(data.createDt));
		// }
		// else if (MyOrderBean.STATUS_CANCEL == data.orderStatus) {
		// tvOrderTimeLabel.setText("取消时间：");
		// tvOrderTime.setText(DateTimeTool.ymdhmDate(data.updateDt));
		// }
		// else if (MyOrderBean.STATUS_COMPLETE == data.orderStatus) {
		//
		// }
		// else {
		// tvOrderTimeLabel.setText("成交时间：");
		// tvOrderTime.setText(DateTimeTool.ymdhmDate(data.paidDt));
		// }

		if (data.orderStatus > 2) {
			holder.getView(R.id.rltLogisticsInfo).setVisibility(View.GONE);
		} else {
			holder.getTextView(R.id.tvLogisticsCorp).setText("XXX物流");
			holder.getTextView(R.id.tvLogisticsNo).setText("XXX物流单号");
		}
		return true;
	}

	private DecimalFormat df = new DecimalFormat("0.00");


	// 判断此订单有没有已发货的商品
	private boolean getProductStatusInfo(List<OrderGoodsBean> lists) {
		boolean flags = false;
		if (lists.size() > 1) {
			for (int i = 0; i < lists.size(); i++) {
				/** 商品状态2待收货，3已完成 */
				if (lists.get(i).productStatus == 2 || lists.get(i).productStatus == 3) {
					flags = true;
				}
			}
		}
		return flags;
	}

//	private void refreshViewVisible(MyOrderDetailBean data) {
//		switch (data.orderStatus) {
//		case MyOrderBean.STATUS_WAIT_PAY:
//		case MyOrderBean.STATUS_CANCEL:
//			findViewById(R.id.tvLogisticsNoLabel).setVisibility(View.GONE);
//			tvLogisticsNo.setVisibility(View.GONE);
//
//			findViewById(R.id.tvPayTypeLabel).setVisibility(View.GONE);
//			break;
//		case MyOrderBean.STATUS_WAIT_SEND:
//			List<OrderGoodsBean> lists = data.productArray;
//			if (data.isHaveSignDelivery == 0) {
//				// 整单
//				findViewById(R.id.tvLogisticsNoLabel).setVisibility(View.GONE);
//				tvLogisticsNo.setVisibility(View.GONE);
//
//			} else if (data.isHaveSignDelivery == 1) {
//				// 单品
//				if (getProductStatusInfo(lists)) {
//					findViewById(R.id.tvLogisticsNoLabel).setVisibility(
//							View.VISIBLE);
//					tvLogisticsNo.setVisibility(View.VISIBLE);
//				} else {
//					findViewById(R.id.tvLogisticsNoLabel).setVisibility(
//							View.GONE);
//					tvLogisticsNo.setVisibility(View.GONE);
//				}
//			}
//			findViewById(R.id.tvPayTypeLabel).setVisibility(View.VISIBLE);
//			tvPayType.setVisibility(View.VISIBLE);
//			break;
//		default:
//			findViewById(R.id.tvLogisticsNoLabel).setVisibility(View.VISIBLE);
//			tvLogisticsNo.setVisibility(View.VISIBLE);
//
//			findViewById(R.id.tvPayTypeLabel).setVisibility(View.VISIBLE);
//			tvPayType.setVisibility(View.VISIBLE);
//		}
//
//	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		// if (R.id.left == v.getId()) {
		// onBackPressed();
		// return;
		// }
		// else if (R.id.lltMerchant == v.getId()) {
		// bundle.putString(SellerMainAct.IPN_MERCHANT_ID, "" +
		// mData.merchantID);
		// showActivity(this, SellerMainAct.class, bundle);
		// }
		// else if (R.id.tvLogisticsDetail == v.getId()) {
		// bundle.putString(ExpressLogDetailActivity.IPN_ORDER_NO,
		// mOrderNumber);
		// showActivity(this, ExpressLogDetailActivity.class, bundle);
		// }
		// else if (R.id.tv_getdetails == v.getId()) {
		// // TODO 跳转查看详情
		// bundle.putString(BaseWebViewAct.PAGE_URL, UrlConstants.getQBURL() +
		// appendUrl(UrlConstants.getQBRSAKEY()));
		// bundle.putString(BaseWebViewAct.PAGE_TITLE, "惠信钱包");
		// showActivity(this, BaseWebViewAct.class, bundle);
		// }
		// else if (R.id.tv_more_hx_info == v.getId()) {
		// // 跳转查看惠信体验金列表
		// bundle.putSerializable("hxPurser", mData);
		// showActivity(this, MyHXExperienceActivity.class, bundle);
		// }
		super.onClick(v);
	}

	private String appendUrl(String rsaKey) {
		// String phone =
		// PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PHONE);
		// String pwd =
		// PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
		// String appUrl = "accName=" + phone + "&pwd=" + pwd;
		// String data = RsaHelper.encryptDataFromStr(appUrl,
		// RsaHelper.decodePublicKeyFromXml(rsaKey));
		// return data;
		return "";
	}

	// @Override
	// public void onBusiOperResult(int busiOperType, MyOrderBean data) {
	// if (busiOperType == OrderListAdapter.BOT_DELETE) {
	// finish();
	// }
	// else if (busiOperType == OrderListAdapter.BOT_VALIDAY) {
	//
	// refreshUI((MyOrderDetailBean) data);
	// }
	// else {
	// refreshUI((MyOrderDetailBean) data);
	// }
	// }


	// @Override
	// public void onShopDetailResult(int busiOperType, OrderGoodsBean data) {
	// getRemoteData();
	// // if (busiOperType == 1) {
	// // //延时
	// // List<OrderGoodsBean> orders = mData.productArray;
	// // for(int i=0;i<orders.size();i++){
	// // if(orders.get(i).productId.equals(data.productId)){
	// // orders.add(i,data);
	// // }
	// // }
	// // }
	// // else if (busiOperType == 2) {
	// // //收货
	// //
	// // }
	// // mAdapter.notifyDataSetChanged();
	// }

	// @Override
	// public void onBackOperater(MyOrderDetailBean detailData, OrderGoodsBean
	// data) {
	// /**orderStatus:订单状态2：待付款，3：待发货，4：待收货，5：超时收货，6：已完成，8：待评价，9：已取消 */
	// Intent intent = null;
	// if (data.productStatus == 3 && data.returnType <= 0) {
	// intent = new Intent(this, ApplyRefundActivity.class);
	// }
	// else {
	// if (data.returnType > 0) {
	// intent = new Intent(this, ExchangeDetailAct.class);
	// }
	// else {
	// intent = new Intent(this, ApplyASServiceActivity.class);
	// }
	// }
	// intent.putExtra(ExchangeDetailAct.HK_MERCHANT_ID, detailData.merchantID);
	// intent.putExtra(ExchangeDetailAct.HK_ORDER_NUMBER,
	// detailData.orderNumber);
	// intent.putExtra(ExchangeDetailAct.HK_MY_ORDER_GOODS_BEAN, data);
	// startActivity(intent);
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
