package com.huika.cloud.control.pay.activity;

import android.view.View;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.main.activity.MainActivity;
import com.huika.cloud.support.event.MainPagerChangeEvent;
import com.huika.cloud.support.model.AddressBean;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import de.greenrobot.event.EventBus;

/**
 * @description：支付结果页面
 * @author shicm
 * @date 2015-11-19 下午5:19:44
 */
public class PayResultActivty extends RdpBaseActivity {
	public static final String INP_SUCCESSMONEY = "SUCCESS_MONEY";
	public static final String INP_SUCCESSADRESS = "SUCCESS_ADRESS";
//	private View mViews;
	@ViewInject(R.id.pay_name_product_tv)
	private TextView pay_name_product_tv;
	@ViewInject(R.id.pay_adress_product_tv)
	private TextView pay_adress_product_tv;
	@ViewInject(R.id.pay_money_product_tv)
	private TextView pay_money_product_tv;
	@ViewInject(R.id.tv_goto_home_buy)
	private TextView tv_goto_home_buy;
	@ViewInject(R.id.tv_goto_order_buy)
	private TextView tv_goto_order_buy;
	
	private double  money;
	private AddressBean address;

	@Override
	protected void initActivity() {
		super.initActivity();
		initData();
		addMasterView(R.layout.order_pay_result);
		RdpAnnotationUtil.inject(this);
		initViews();
	}

	private void initData() {
		money = getIntent().getDoubleExtra(INP_SUCCESSMONEY, 0.0); //   
		address = (AddressBean) getIntent().getSerializableExtra(INP_SUCCESSADRESS); //   默认支付失败
	}

	private void initViews() {
		setFuncTitle("支付成功");
		pay_money_product_tv.setText("" +money);
		pay_name_product_tv.setText(address.receiverName + "  " + address.receiverPhone);
		pay_adress_product_tv.setText(address.receiverAddress);
	}

	 @OnClick({R.id.tv_goto_home_buy,R.id.tv_goto_order_buy})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
			case R.id.tv_goto_home_buy: // 返回首页
				EventBus.getDefault().post(new MainPagerChangeEvent(0));
				skipActivity(this, MainActivity.class);
				break;
			case R.id.tv_goto_order_buy: // 跳订单
				showToastMsg("订单详情的页面");
				break;
			default:
				break;
		}
	}
}
