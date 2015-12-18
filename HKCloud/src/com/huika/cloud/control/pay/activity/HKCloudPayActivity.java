package com.huika.cloud.control.pay.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.util.PayResult;
import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.me.activity.BindBankActivity;
import com.huika.cloud.control.pay.AlipayUtil;
import com.huika.cloud.control.pay.adapter.PayTypeAdapter;
import com.huika.cloud.support.model.AddressBean;
import com.huika.cloud.support.model.PayChannelBean;
import com.huika.cloud.support.model.WeixinPay;
import com.huika.cloud.util.MMAlertDialog;
import com.huika.cloud.util.MMAlertDialog.DialogOnItemClickListener;
import com.huika.cloud.util.MoneyShowTool;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @description：惠云支付界面
 * @author shicm
 * @date 2015-11-18 下午5:49:37
 */
public class HKCloudPayActivity extends RdpBaseActivity implements OnCheckedChangeListener, OnRefreshItemViewsListener {
	public static final String INP_HKCLOUD_PAY = "HKCLOUDPAY";
	public static final String INP_HKCLOUD_ORDER_NUM = "HKCLOUDORDERNUM";
	public static final String INP_HKCLOUD_ORDER_ADDRESS = "HKCLOUDORDERADDRESS";

	private int mPayType = 0; // 支付方式0：余额支付；1：支付宝支付；2：微信支付; 3：余额+支付宝 4：余额+微信支付

	@ViewInject(R.id.payamount_tv)
	private TextView payamount_tv;
	@ViewInject(R.id.hkcloud_amount_tv)
	private TextView hkcloud_amount_tv;
	@ViewInject(R.id.hkcloud_checkout_rb)
	private CheckBox hkcloud_checkout_cb;
	@ViewInject(R.id.protocol_checkout_cb)
	private CheckBox protocol_checkout_cb;
	@ViewInject(R.id.load_mod_of_payment_lv)
	private ListView load_mod_of_payment_lv;
	@ViewInject(R.id.payprotocol_tv)
	private TextView payprotocol_tv;
	@ViewInject(R.id.payconfirm_btn)
	private Button payconfirm_btn;

	private double amountMoney = 0;
	private double mBalance = 0;
	private String orderNum;
	private AddressBean mAddress;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case AlipayUtil.SDK_PAY_FLAG:
					PayResult payResult = new PayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						showToastMsg("支付成功");
						Intent intent = new Intent(HKCloudPayActivity.this, PayResultActivty.class);
						Bundle bundle = new Bundle();
						bundle.putDouble(PayResultActivty.INP_SUCCESSMONEY, amountMoney);
						bundle.putSerializable(PayResultActivty.INP_SUCCESSADRESS, mAddress);
						intent.putExtras(bundle);
						HKCloudPayActivity.this.startActivity(intent);
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							showToastMsg("支付结果确认中");
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							showToastMsg("支付失败");
						}
					}
					break;

				default:
					break;
			}
		}

	};
	private PayTypeAdapter payTypeAdapter;
	private List<PayChannelBean> payList = PayChannelBean.getData();
	private IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private PayReq req;

	@Override
	protected void initActivity() {
		super.initActivity();
		initDatas();
		initView();
	}

	private void initDatas() {
		amountMoney = getIntent().getDoubleExtra(INP_HKCLOUD_PAY, 0.0);
		orderNum = getIntent().getStringExtra(INP_HKCLOUD_ORDER_NUM);
		mAddress = (AddressBean) getIntent().getSerializableExtra(INP_HKCLOUD_ORDER_ADDRESS);
		mBalance = HKCloudApplication.getInstance().getUserModel().balance;
	}

	private void initView() {
		addMasterView(R.layout.order_pay_layout);
		RdpAnnotationUtil.inject(this);

		payamount_tv.setText(Html.fromHtml("<font color =#333333>" + "支付金额：" + "</font>" + "<font color =#FF6D00>" + "¥ " + MoneyShowTool.twolastDF(amountMoney) + "</font>"));
		payTypeAdapter = new PayTypeAdapter(this, R.layout.order_pay_type_item);
		payTypeAdapter.setListener(this);
		load_mod_of_payment_lv.setAdapter(payTypeAdapter);
		payTypeAdapter.setData(payList);

		hkcloud_checkout_cb.setOnCheckedChangeListener(this);
		protocol_checkout_cb.setOnCheckedChangeListener(this);
		initComfirmUI();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.hkcloud_checkout_rb:
				initAdapterUI(isChecked);
				break;
			case R.id.protocol_checkout_cb:
				if (mPayType == 0 && mBalance < amountMoney && isChecked) {
					payconfirm_btn.setEnabled(false);
				} else {
					payconfirm_btn.setEnabled(isChecked);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * @description：
	 * @author shicm
	 * @date 2015-11-25 下午4:35:07
	 */
	private void initComfirmUI() {
		if (mBalance < amountMoney) {
			if (mBalance == 0) {
				hkcloud_checkout_cb.setChecked(false);
				hkcloud_checkout_cb.setEnabled(false);
			}
			payconfirm_btn.setEnabled(false);
		} else {
			hkcloud_checkout_cb.setChecked(true);
			payconfirm_btn.setEnabled(true);
		}
	}

	private void initAdapterUI(boolean isChecked) {
		hkcloud_checkout_cb.setChecked(isChecked);
		if (isChecked) {
			// 余额大于登于要付的钱,点击余额是余额支付，其他的不处理
			if (mBalance >= amountMoney) {
				// 余额支付
				mPayType = 0;
				payTypeAdapter.setNoSelect(true);
				payTypeAdapter.notifyDataSetChanged();
				payconfirm_btn.setEnabled(true);
			}
		} else {
			if (mPayType > 0) {
				payconfirm_btn.setEnabled(true);
			} else {
				payconfirm_btn.setEnabled(false);
			}
		}

	}

	@OnClick({ R.id.payconfirm_btn, R.id.payprotocol_tv })
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.payconfirm_btn: // 支付确认键
				if (mPayType == 1) {
					aliPayReq(amountMoney); // 支付宝全额付款
				} else if (mPayType == 2) {
					requestPay("");
				} else {
					if (HKCloudApplication.getInstance().getUserModel().transPassword == 0) {
						Dialog dialog = MMAlertDialog.ClearcacheDate(this, "还没有设置支付密码,是否现在去设置支付密码？", this, new OnClickListener() {
							@Override
							public void onClick(View v) {
								showActivity(HKCloudPayActivity.this, BindBankActivity.class);
							}
						});
						dialog.show();
					} else {
						Dialog payDialog = MMAlertDialog.createPwdDialog(this, new DialogOnItemClickListener() {
							@Override
							public void onItemClickListener(View v, int position) {
							}

							@Override
							public void onDialogDismiss(String psw) {
								requestPay(psw);
							}
						}, true, null);
						payDialog.show();
					}
				}
				break;
			case R.id.payprotocol_tv: // 支付协议
				showToastMsg("支付协议");
				break;
			default:
				break;
		}
	}

	/**
	 * @description：支付
	 * @author shicm
	 * @date 2015-11-24 下午2:16:23
	 */
	private void requestPay(String psw) {
		showLoadingDialog("");
		Type payOfType = new TypeToken<String>() {
		}.getType();
		RdpNetCommand payCommand = new RdpNetCommand(this, payOfType);
		payCommand.clearConditions();
		payCommand.setServerApiUrl(UrlConstant.ORDER_PAYVALIDATE);
		payCommand.setCondition("memberId", HKCloudApplication.getInstance().getUserModel().memberId);
		payCommand.setCondition("orderNumber", orderNum);
		payCommand.setCondition("payPwd", psw);
		payCommand.setCondition("amount", amountMoney);
		payCommand.setCondition("payType", mPayType);
		payCommand.execute();
		payCommand.setOnCommandSuccessedListener(this);
		payCommand.setOnCommandFailedListener(this);
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		if (mPayType == 0) { // 余额支付
			Bundle bundle = new Bundle();
			bundle.putDouble(PayResultActivty.INP_SUCCESSMONEY, amountMoney);
			bundle.putSerializable(PayResultActivty.INP_SUCCESSADRESS, mAddress);
			showActivity(this, PayResultActivty.class, bundle);
		} else if (mPayType == 2) {// 微信
			wxPayReq((WeixinPay) data);
		} else if (mPayType == 3) {// 余额支付 + 支付宝
			double balance = mBalance;
			aliPayReq(amountMoney - balance);
		} else if (mPayType == 4) {// 余额支付 + 微信
			wxPayReq((WeixinPay) data);
		} else {
			// 支付失败，网络不行
			showToastMsg("支付失败");
		}

	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, final int position, View convertView, AdapterViewHolder holder) {
		PayChannelBean channelBean = (PayChannelBean) adapter.getItem(position);
		holder.getImageView(R.id.pay_type_img).setImageResource(position == 0 ? R.drawable.icon_pay_alipay : R.drawable.icon_pay_weixin);
		holder.getTextView(R.id.pay_type_name_tv).setText(channelBean.channelName);
		RadioButton rbSelect = holder.getRadioButton(R.id.pay_type_checkout_rb);
		rbSelect.setOnCheckedChangeListener(null);
		rbSelect.setChecked(channelBean.isSelect);
		rbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (protocol_checkout_cb.isChecked()) {
						payconfirm_btn.setEnabled(true); // 确定按钮的监听
					} else {
						payconfirm_btn.setEnabled(false);
					}
					refreshUI(position);
				} else {
					if (protocol_checkout_cb.isChecked() && mBalance > amountMoney) {
						payconfirm_btn.setEnabled(true); // 确定按钮的监听
					} else {
						payconfirm_btn.setEnabled(false);
					}
				}
			}
		});
		return false;
	}

	/**
	 * @description：选择点击
	 * @author shicm
	 * @date 2015-11-25 下午4:04:02
	 */
	private void refreshUI(int position) {
		payTypeAdapter.setNoSelect(false);
		for (int i = 0; i < payList.size(); i++) {
			payList.get(i).isSelect = i == position;
		}
		payTypeAdapter.notifyDataSetChanged();
		if (hkcloud_checkout_cb.isChecked()) {
			if (mBalance < amountMoney) {
				mPayType = payList.get(position).type;
				hkcloud_checkout_cb.setChecked(false);
			} else {
				if (payList.get(position).type == 1) {
					mPayType = 3;
				} else {
					mPayType = 4;
				}
			}
		} else {
			mPayType = payList.get(position).type;
		}
	}

	/**
	 * @description：微信支付
	 * @author shicm
	 * @date 2015-12-3 上午10:56:16
	 */
	private void wxPayReq(WeixinPay weixinPay) {
		req = new PayReq();
		req.appId = Constant.APP_ID;
		req.partnerId = weixinPay.getPartnerid();
		req.prepayId = weixinPay.getPrepayid();
		req.packageValue = weixinPay.getWxpackage();
		req.nonceStr = weixinPay.getNoncestr();
		req.timeStamp = weixinPay.getTimestamp();
		req.sign = weixinPay.getSign();
		msgApi.registerApp(Constant.APP_ID);
		msgApi.sendReq(req);
	}

	/**
	 * 支付宝支付
	 */
	private void aliPayReq(double money) {
		AlipayUtil alipayUtil = new AlipayUtil(this, mHandler, "惠云商品", "惠云商品详情", orderNum, money);
		new Thread(alipayUtil).start();
	}
}
