package com.wchatpay.simcpux.act;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.hkcloud.share.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wchatpay.simcpux.Constants;
import com.wchatpay.simcpux.MD5;
import com.wchatpay.simcpux.Util;

public abstract class WXPayTokenActBase extends Activity {

  private static final String TAG = "MicroMsg.SDKSample.PayActivity";
  public static final String ORDERINFOKEY = "orderinfokey";

  PayReq req;
  final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
  Map<String, String> resultunifiedorder;// 微信生成的订单信息
  StringBuffer sb;
  private OrderInfobean mOrderInfobean;

  @Override protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    req = new PayReq();
    sb = new StringBuffer();
    msgApi.registerApp(Constants.APP_ID);
    mOrderInfobean = (OrderInfobean) getIntent().getSerializableExtra(ORDERINFOKEY);
    if (mOrderInfobean != null) jumpTopay();
  }

  /**
   * 支付三步走
   * // 生成prepay_id
   * GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
   * getPrepayId.execute();
   *
   * // 生成签名参数
   * genPayReq();
   *
   * // 支付请求
   * sendPayReq();
   */

  public void jumpTopay() {
    // 生成prepay_id
    GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
    getPrepayId.execute();
  }

  /** 生成prepay_id */
  private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

    private ProgressDialog dialog;

    @Override protected void onPreExecute() {
      dialog = ProgressDialog.show(WXPayTokenActBase.this, getString(R.string.app_tip),
          getString(R.string.getting_prepayid));
    }

    @Override protected void onPostExecute(Map<String, String> result) {
      if (dialog != null) {
        dialog.dismiss();
      }
      sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

      resultunifiedorder = result;
      // 生成签名参数
      genPayReq();
    }

    @Override protected void onCancelled() {
      super.onCancelled();
    }

    @Override protected Map<String, String> doInBackground(Void... params) {

      String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
      String entity = genProductArgs();

      Log.e("orion", entity);

      byte[] buf = Util.httpPost(url, entity);

      String content = new String(buf);
      Log.e("orion", content);
      Map<String, String> xml = decodeXml(content);

      return xml;
    }
  }

  // 获取订单相关的参数
  private String genProductArgs() {

    try {
      String nonceStr = genNonceStr();

      List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
      packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
      packageParams.add(new BasicNameValuePair("body", "huifen"));// 商品描述
      packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
      packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));// 随机串，防重发
      packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));// 服务器回调ip
      packageParams.add(new BasicNameValuePair("out_trade_no",
          genOutTradNo()));// 商家订单号 可以用时间、加随机数来，微信会自动返回订单号(不应管支付宝的)
      packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));// 订单浏览器id
      packageParams.add(new BasicNameValuePair("total_fee", mOrderInfobean.getTotal_fee()));// 总共多少钱
      packageParams.add(new BasicNameValuePair("trade_type", "APP"));// 取值如下：JSAPI，NATIVE，APP，WAP

      String sign = genPackageSign(packageParams);
      packageParams.add(new BasicNameValuePair("sign", sign));

      String xmlstring = toXml(packageParams);

      return xmlstring;
    } catch (Exception e) {
      Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
      return null;
    }
  }

  /** 解析成xml */
  public Map<String, String> decodeXml(String content) {

    try {
      Map<String, String> xml = new HashMap<String, String>();
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(content));
      int event = parser.getEventType();
      while (event != XmlPullParser.END_DOCUMENT) {

        String nodeName = parser.getName();
        switch (event) {
          case XmlPullParser.START_DOCUMENT:

            break;
          case XmlPullParser.START_TAG:

            if ("xml".equals(nodeName) == false) {
              // 实例化student对象
              xml.put(nodeName, parser.nextText());
            }
            break;
          case XmlPullParser.END_TAG:
            break;
        }
        event = parser.next();
      }

      return xml;
    } catch (Exception e) {
      Log.e("orion", e.toString());
    }
    return null;
  }

  /**
   * 应用包 生成签名
   */

  private String genPackageSign(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append("key=");
    sb.append(Constants.API_KEY);

    String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    Log.e("orion", packageSign);
    return packageSign;
  }

  /** 防重复 */
  private String genNonceStr() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  private String genOutTradNo() {
    Random random = new Random();
    return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
  }

  /** 转换到xml */
  private String toXml(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();
    sb.append("<xml>");
    for (int i = 0; i < params.size(); i++) {
      sb.append("<" + params.get(i).getName() + ">");

      sb.append(params.get(i).getValue());
      sb.append("</" + params.get(i).getName() + ">");
    }
    sb.append("</xml>");

    Log.e("orion", sb.toString());
    return sb.toString();
  }

  // 生成签名参数
  private void genPayReq() {

    req.appId = Constants.APP_ID;// 商户在微信开放平台申请的应用id
    req.partnerId = Constants.MCH_ID;// 商户id

    /**
     * 这里的package参数值必须是Sign=WXPay,否则IOS端调不起微信支付，
     * (参数值是"prepay_id="+resultunifiedorder.get("prepay_id")的时候Android可以，IOS不可以)
     */
    req.prepayId = resultunifiedorder.get("prepay_id");// 预支付订单
    req.packageValue = "Sign=WXPay";// 商家根据文档填写的数据和签名
    req.nonceStr = genNonceStr();// 随机串，防重发
    req.timeStamp = String.valueOf(genTimeStamp());// 时间戳，防重发

    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
    signParams.add(new BasicNameValuePair("appid", req.appId));
    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));// 随机串，防重发
    signParams.add(new BasicNameValuePair("package",
        req.packageValue));// 这里的package参数值必须是Sign=WXPay,否则IOS端调不起微信支付
    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));// 商户id
    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));// 预支付订单
    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));// 时间戳，防重发

    req.sign = genAppSign(signParams);// 商家根据微信开放平台文档对数据做的签名

    sb.append("sign\n" + req.sign + "\n\n");

    Log.e("orion", signParams.toString());
    // 支付请求
    sendPayReq();
  }

  // 时间戳，防重发
  private long genTimeStamp() {
    return System.currentTimeMillis() / 1000;
  }

  // 商家根据微信开放平台文档对数据做的签名
  private String genAppSign(List<NameValuePair> params) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < params.size(); i++) {
      sb.append(params.get(i).getName());
      sb.append('=');
      sb.append(params.get(i).getValue());
      sb.append('&');
    }
    sb.append("key=");
    sb.append(Constants.API_KEY);

    this.sb.append("sign str\n" + sb.toString() + "\n\n");
    String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    Log.e("orion", appSign);
    return appSign;
  }

  // 支付请求
  private void sendPayReq() {

    msgApi.registerApp(Constants.APP_ID);
    msgApi.sendReq(req);
  }
}
