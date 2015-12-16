package com.huika.cloud.wxapi;

import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseResp;

public class WXPayEntryActivity extends WXPayEntryActBase {

	@Override
	public void paySuccess(BaseResp resp) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "付款成功", Toast.LENGTH_SHORT).show();
		// 支付成功
	}

	@Override
	public void payFail(BaseResp resp) {
		// TODO Auto-generated method stub
//		putValueIntoGlobal(false, resp);
		// 支付失败
		Toast.makeText(this, "付款失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void payCancle(BaseResp resp) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "您取消了支付操作！", Toast.LENGTH_LONG).show();
		// GlobalApp.getInstance().globalMap.remove(wxpaytypeKey);
//		removePayType(WXPayType.wxpaytypeKey);
	}
}
