package com.huika.cloud.support.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @description：支付渠道
 * @author shicm
 * @date 2015-11-25 下午3:25:23
 */
public class PayChannelBean {
	/** 渠道名称 */
	public String channelName;
	public boolean isSelect;
	/** :图标 */
	public String icon;
	/** 1微信 2银联 */
	public int type;

	@Override
	public String toString() {
		return "PayChannelItemBean [channelName=" + channelName + ", icon=" + icon + ", type=" + type + "]";
	}
	
	public static List<PayChannelBean> getData(){
		List<PayChannelBean> list = new ArrayList<PayChannelBean>();
		PayChannelBean alipay = new PayChannelBean();
		alipay.channelName = "支付宝支付";
		alipay.type = 1;
		list.add(alipay);
		PayChannelBean wxPay = new PayChannelBean();
		wxPay.channelName = "微信支付";
		wxPay.type = 2;
		list.add(wxPay);
		return list;
		
	}
}
