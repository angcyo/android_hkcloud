package com.huika.cloud.control.eshop.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.eshop.adapter.MyOrderListAdapter;
import com.huika.cloud.control.pay.activity.HKCloudPayActivity;
import com.huika.cloud.support.model.CartProduct;
import com.huika.cloud.support.model.MyOrderBean;
import com.huika.cloud.support.model.OrderGoodsBean;
import com.huika.cloud.util.CommonAlertDialog;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

public class MyOrderBusiOper implements OnCommandSuccessedListener, OnCommandFailedListener {

	private RdpBaseActivity mActivity;
	private IMyOrderBusiOper mCallBack;
	private MyOrderBean mData;

	public MyOrderBusiOper(RdpBaseActivity activity, IMyOrderBusiOper callBack) {
		mActivity = activity;
		mCallBack = callBack;
	}

	public void refreshOperButtons(final MyOrderBean data, Button btnBusiOne, Button btnBusiExpress, Button btnBusiTwo) {
		refreshOperButtons(data, btnBusiOne, btnBusiExpress, btnBusiTwo, null);
	}

	public void refreshOperButtons(final MyOrderBean data, Button btnBusiOne, Button btnBusiExpress, Button btnBusiTwo,
			View viewLine) {
		final int status = data.orderStatus;
		if (viewLine != null)
			viewLine.setVisibility(View.VISIBLE);
		btnBusiOne.setVisibility(View.VISIBLE);
		btnBusiTwo.setVisibility(View.VISIBLE);
		btnBusiExpress.setVisibility(View.GONE);

		switch (status) {
		case MyOrderBean.STATUS_WAIT_PAY:
			btnBusiOne.setText("取消订单");
			btnBusiTwo.setText("付款");
			btnBusiExpress.setVisibility(View.GONE);
			break;
		case MyOrderBean.STATUS_WAIT_RECEIVE:
			// if (data.isDelay())
			// btnBusiOne.setVisibility(View.GONE);
			// else
			btnBusiOne.setText("退换货申请");
			btnBusiTwo.setText("确认收货");
			break;
		case MyOrderBean.STATUS_BARTER_WAIT_RECEIVE:
			btnBusiOne.setVisibility(View.GONE);
			btnBusiTwo.setText("确认收货");
			btnBusiExpress.setVisibility(View.GONE);
			break;
		case MyOrderBean.STATUS_COMPLETE:
			btnBusiOne.setText("删除订单");
			if (data.isComment())
				btnBusiTwo.setVisibility(View.GONE);
			else
				btnBusiTwo.setText("评价");
			break;
		// case MyOrderBean.STATUS_CANCEL:
		// btnBusiOne.setText("删除订单");
		// btnBusiTwo.setVisibility(View.GONE);
		// if(data.expressStatus == 1){
		// btnBusiExpress.setVisibility(View.VISIBLE);
		// }
		// else{
		// btnBusiExpress.setVisibility(View.GONE);
		// }
		// break;
		case MyOrderBean.STATUS_WAIT_SEND:
		case MyOrderBean.STATUS_BARTER_APPLY:
		case MyOrderBean.STATUS_REJECT_APPLY:
		case MyOrderBean.STATUS_CORP_WAIT_RECEIVE:
		default:
			if (viewLine != null)
				viewLine.setVisibility(View.GONE);
			btnBusiOne.setVisibility(View.GONE);
			btnBusiTwo.setVisibility(View.GONE);
			btnBusiExpress.setVisibility(View.GONE);
			break;

		}
		// viewHolder.getLinearLayout(R.id.lltBusi).
		btnBusiOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status) {
				case MyOrderBean.STATUS_WAIT_PAY:
					// 取消订单
					doExecBusiOper(MyOrderListAdapter.BOT_CANCEL, data, v);
					break;
				case MyOrderBean.STATUS_WAIT_RECEIVE:
					// 退换货申请
					doExecBusiOper(MyOrderListAdapter.BOT_AFTER_SERVICE, data, v);
					break;
				case MyOrderBean.STATUS_TIMEOUT_RECEIVE:
				case MyOrderBean.STATUS_COMPLETE:
					// case MyOrderBean.STATUS_WAIT_COMMENT:
					// case MyOrderBean.STATUS_CANCEL:
					// 删除订单
					doExecBusiOper(MyOrderListAdapter.BOT_DELETE, data, v);
					break;
				}
			}
		});

		btnBusiTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (status) {
				case MyOrderBean.STATUS_WAIT_PAY:
					// 付款回调
					doExecBusiOper(MyOrderListAdapter.BOT_PAY, data, v);
					break;
				case MyOrderBean.STATUS_WAIT_RECEIVE:
				case MyOrderBean.STATUS_BARTER_WAIT_RECEIVE:
					// 确认收货
					doExecBusiOper(MyOrderListAdapter.BOT_VALIDAY, data, v);
					break;
				case MyOrderBean.STATUS_TIMEOUT_RECEIVE:
				case MyOrderBean.STATUS_COMPLETE:
					// case MyOrderBean.STATUS_WAIT_COMMENT:
					// // 评价
					// Bundle bundle = new Bundle();
					// if(data.isHaveSignDelivery == 0){
					// //整单发货
					// //
					// bundle.putSerializable(CopyPublishEvaluat.IPN_ORDER_DATA,
					// data);
					// // bundle.putBoolean("ISJUMPDETAIL",true);
					// // mActivity.showActivity(mActivity,
					// CopyPublishEvaluat.class, bundle);
					// }
					// else if(data.isHaveSignDelivery == 1){
					// //单品发货
					// //
					// bundle.putBoolean("ISEVALUAT",getOrderEvaluatState(data));
					// //
					// bundle.putSerializable(CopyPublishEvaluat.IPN_ORDER_DATA,
					// getEvaluatList(data));
					// // mActivity.showActivity(mActivity,
					// CopyPublishEvaluat.class, bundle);
					// }
					// break;
				}
			}
		});
	}

	public void doExecBusiOper(int busiOperType, final MyOrderBean data, View v) {
		switch (busiOperType) {
		// 跳转到【支付】界面即可
		case MyOrderListAdapter.BOT_PAY:
			Bundle bundle = new Bundle();
			bundle.putDouble(HKCloudPayActivity.INP_HKCLOUD_PAY, data.totalPrice);
			bundle.putString(HKCloudPayActivity.INP_HKCLOUD_ORDER_NUM, data.orderNumber);
			bundle.putSerializable(HKCloudPayActivity.INP_HKCLOUD_ORDER_ADDRESS, data.ReceiverRs);
			mActivity.showActivity(mActivity, HKCloudPayActivity.class, bundle);
			break;
			// 取消订单
		case MyOrderListAdapter.BOT_CANCEL:
			comfirmOper(mActivity.getString(R.string.order_cancel_order), "", data, busiOperType);
			break;
		// case MyOrderListAdapter.BOT_DEALY:
		// comfirmOper(mActivity.getString(R.string.order_delay_receive_order),
		// mActivity.getString(R.string.order_delay_receive_msg), data,
		// busiOperType);
		// break;
		case MyOrderListAdapter.BOT_DELETE:
			comfirmOper(mActivity.getString(R.string.order_delete_order),
					mActivity.getString(R.string.order_delete_order_msg), data, busiOperType);
			break;
		case MyOrderListAdapter.BOT_AFTER_SERVICE:
			mData = data;
			final CommonAlertDialog tipDialog = CommonAlertDialog.getInstance(mActivity);
			tipDialog.setCustomView(R.layout.my_order_service_apply, mActivity);
			final Spinner spiServiceType = (Spinner) tipDialog.findViewById(R.id.spiServiceType);
			final EditText edtApply = (EditText) tipDialog.findViewById(R.id.edtApply);
			tipDialog.withTitle(mActivity.getString(R.string.order_after_service));
			tipDialog.setOnShowListener(new OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {
					// mActivity.showInput(edtPayPwd);
				}
			});
			// tipDialog.withMessage(mActivity.getString(R.string.order_after_service),
			// false);
			// tvForgetPwd.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Bundle bundle = new Bundle();
			// // bundle.putBoolean(Constant.IS_TRADERS_PASSWORD, true);
			// // mActivity.showActivity(mActivity, FindPwdActivity.class,
			// bundle);
			// }
			// });
			tipDialog.setRightButtonClick(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mActivity.showLoadingDialog("提交中");
					returnOrChangeApply(data, spiServiceType.getSelectedItemId(), edtApply.getText().toString());
					tipDialog.dismiss();
				}
			}).show();
			break;
		case MyOrderListAdapter.BOT_VALIDAY:
			mData = data;
			final CommonAlertDialog tipDialog1 = CommonAlertDialog.getInstance(mActivity);
			// tipDialog.setCustomView(R.layout.my_order_comfirm_receive,
			// mActivity);
			// final EditText edtPayPwd = (EditText)
			// tipDialog.findViewById(R.id.edtPayPwd);
			// final TextView tvForgetPwd = (TextView)
			// tipDialog.findViewById(R.id.tvForgetPwd);
			tipDialog1.withTitle("");
			tipDialog1.setOnShowListener(new OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {
					// mActivity.showInput(edtPayPwd);
				}
			});
			tipDialog1.withMessage(mActivity.getString(R.string.order_comfirm_receive_order), false);
			// tvForgetPwd.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Bundle bundle = new Bundle();
			// // bundle.putBoolean(Constant.IS_TRADERS_PASSWORD, true);
			// // mActivity.showActivity(mActivity, FindPwdActivity.class,
			// bundle);
			// }
			// });
			tipDialog1.setRightButtonClick(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mActivity.showLoadingDialog("提交中");
					// affirmReceiving(data, edtPayPwd.getText().toString());
					affirmReceiving(data, "");
					tipDialog1.dismiss();
				}
			}).show();
			break;
		}

	}

	private void comfirmOper(String title, String msg, final MyOrderBean data, final int busiOperType) {
		final CommonAlertDialog tipDialog = CommonAlertDialog.getInstance(mActivity);
		tipDialog.removeCustomView();
		tipDialog.withTitle(title);
		tipDialog.withMessage(msg, true);
		tipDialog.setRightButtonClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.showLoadingDialog("提交中");
				updateOrderStatus(data, busiOperType);
				tipDialog.dismiss();
			}
		}).show();
	}

	private void updateOrderStatus(final MyOrderBean data, final int busiOperType) {
		Type resultType = new TypeToken<String>() {
		}.getType();

		RdpNetCommand netCommand = new RdpNetCommand(mActivity, resultType);
		netCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object resultData) {
				mActivity.dismissLoadingDialog();
				data.orderStatus = MyOrderBean.STATUS_COMPLETE;
				if (mCallBack != null)
					mCallBack.onBusiOperResult(MyOrderListAdapter.BOT_VALIDAY, data);
				switch (busiOperType) {
				// case MyOrderListAdapter.BOT_CANCEL:
				// data.orderStatus = MyOrderBean.STATUS_CANCEL;
				// break;
				// case MyOrderListAdapter.BOT_DEALY:
				// data.isDelay = 1;// 改为已延时收货
				// EventBus.getDefault().post(new OrderDetailsEvent(null));
				// break;
				case MyOrderListAdapter.BOT_DELETE:
					if (!data.isComment())
						// adapter.removeItem(data);
						break;
				}
				// adapter.notifyDataSetChanged();
				if (mCallBack != null)
					mCallBack.onBusiOperResult(busiOperType, data);
			}
		});
		netCommand.setOnCommandFailedListener(this);
		netCommand.setServerApiUrl(UrlConstant.API_AFFIRM_RECEIVING);
		netCommand.clearConditions();
		netCommand.setCondition("memberId", mActivity.getMemberId()); // HKCloudApplication.getInstance().getUserModel().getMemberId()
		netCommand.setCondition("orderNumber", data.orderNumber);
		// type: 1取消订单 2延长收货时间 3删除订单
		switch (busiOperType) {
		case MyOrderListAdapter.BOT_CANCEL:
			netCommand.setCondition("type", 1);
			break;
		// case MyOrderListAdapter.BOT_DEALY:
		// netCommand.setCondition("type", 2);
		// break;
		case MyOrderListAdapter.BOT_DELETE:
			netCommand.setCondition("type", 3);
			break;
		}
		netCommand.execute();

	}

	private void affirmReceiving(final MyOrderBean data, String payPwd) {
		Type resultType = new TypeToken<String>() {}.getType();

		RdpNetCommand netCommand = new RdpNetCommand(mActivity, resultType);
		netCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object resultData) {
				mActivity.dismissLoadingDialog();
				data.orderStatus = MyOrderBean.STATUS_COMPLETE;
				if (mCallBack != null)
					mCallBack.onBusiOperResult(MyOrderListAdapter.BOT_VALIDAY, data);

			}
		});
		netCommand.setOnCommandFailedListener(this);
		netCommand.setServerApiUrl(UrlConstant.API_AFFIRM_RECEIVING);
		netCommand.clearConditions();
		netCommand.setCondition("memberId", mActivity.getMemberId()); // HKCloudApplication.getInstance().getUserModel().getMemberId()
		netCommand.setCondition("orderNumber", data.orderNumber);
		netCommand.execute();
	}

	private void returnOrChangeApply(final MyOrderBean data, final long applyType, String remark) {
		Type resultType = new TypeToken<String>() {}.getType();

		RdpNetCommand netCommand = new RdpNetCommand(mActivity, resultType);
		netCommand.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {

			@Override
			public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object resultData) {
				mActivity.dismissLoadingDialog();
				if (applyType == 1) {
					data.orderStatus = MyOrderBean.STATUS_BARTER_APPLY;
				} else {
					data.orderStatus = MyOrderBean.STATUS_REJECT_APPLY;
				}
				if (mCallBack != null)
					mCallBack.onBusiOperResult(MyOrderListAdapter.BOT_AFTER_SERVICE, data);
			}
		});
		netCommand.setOnCommandFailedListener(this);
		netCommand.setServerApiUrl(UrlConstant.API_RETURN_OR_CHANGE_APPLY);
		netCommand.clearConditions();
		netCommand.setCondition("memberId", mActivity.getMemberId()); 
		netCommand.setCondition("orderNumber", data.orderNumber);
		if (applyType == 1) {
			netCommand.setCondition("type", 1);
		} else {
			netCommand.setCondition("type", 2);
		}
		netCommand.setCondition("remark", remark);
		netCommand.execute();
	}

	// 判断订单是否已评价过的商品
	private boolean getOrderEvaluatState(MyOrderBean data) {
		for (int i = 0; i < data.Product.size(); i++) {
			if (data.Product.get(i).isComment == 1) {
				return true;
			}
		}
		return false;
	}

	// 筛选订单中可评价的商品
	private MyOrderBean getEvaluatList(MyOrderBean order) {
		MyOrderBean orderEvalute = new MyOrderBean();
		orderEvalute.orderNumber = order.orderNumber;
		List<OrderGoodsBean> orderBeans = new ArrayList<OrderGoodsBean>();
		for (int i = 0; i < order.Product.size(); i++) {
			OrderGoodsBean bean = order.Product.get(i);
			if (bean.isComment != 1) {
				orderBeans.add(bean);
			}
		}
		orderEvalute.Product = orderBeans;
		return orderEvalute;
	}

	// private ErrorListener delayReceiveError=new ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// mActivity.dismissLoadingDialog();
	// String msg = "网络异常";
	// if (error instanceof ServerFlagError) {
	// msg = ((ServerFlagError) error).result.msg;
	// } else if (error instanceof ServerJsonUnParseError) {
	// try {
	// String res = ((ServerJsonUnParseError) error).result;
	// JSONObject jsonObject = new JSONObject(res);
	// msg = jsonObject.getString("msg");
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// } else {
	// try {
	// if (error.networkResponse != null && error.networkResponse.data != null)
	// msg = new String(error.networkResponse.data, "utf-8");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }
	// mActivity.showToastMsg(msg);
	// }
	// };

	public interface IMyOrderBusiOper {

		public void onBusiOperResult(int busiOperType, MyOrderBean data);
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		mActivity.dismissLoadingDialog();
		mActivity.showToastMsg(result.getMsg());
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {

	}

}
