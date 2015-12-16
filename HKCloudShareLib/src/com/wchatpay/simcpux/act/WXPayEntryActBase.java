package com.wchatpay.simcpux.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wchatpay.simcpux.Constants;

public abstract class WXPayEntryActBase extends Activity implements IWXAPIEventHandler {

  private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

  private IWXAPI api;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
    api.handleIntent(getIntent(), this);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override public void onReq(BaseReq req) {
  }

  public abstract void paySuccess(BaseResp resp);

  public abstract void payFail(BaseResp resp);

  public abstract void payCancle(BaseResp resp);

  /**
   * ERR_AUTH_DENIED
   *
   * 认证被否决
   * static int	ERR_COMM
   *
   * 一般错误
   * static int	ERR_OK
   *
   * 正确返回
   * static int	ERR_SENT_FAILED
   *
   * 发送失败
   * static int	ERR_UNSUPPORT
   *
   * 不支持错误
   * static int	ERR_USER_CANCEL
   *
   * 用户取消
   */
  @Override public void onResp(
      BaseResp resp) {// 网络不稳定的时候，微信返回界面提示支付失败，但是收到微信通知其实已经支付成功了，必须要去自己的服务器查询支付状态
    Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
    if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {// 微信支付
      switch (resp.errCode) {
        case BaseResp.ErrCode.ERR_OK:// 成功
          paySuccess(resp);

          break;
        case BaseResp.ErrCode.ERR_COMM:// 普通错误类型
          payFail(resp);
          break;
        case BaseResp.ErrCode.ERR_SENT_FAILED:// 发送失败
          Toast.makeText(this, "发送支付失败", Toast.LENGTH_LONG).show();
          break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:// 授权失败, 被拒绝
          Toast.makeText(this, "授权失败", Toast.LENGTH_LONG).show();
          break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:// 取消并返回
          payCancle(resp);
          // payFail();
          break;
        case BaseResp.ErrCode.ERR_UNSUPPORT:// 微信不支持
          Toast.makeText(this, "微信不支持", Toast.LENGTH_LONG).show();
          break;
        default:// 返回
          break;
      }
    }
    finish();
  }
}