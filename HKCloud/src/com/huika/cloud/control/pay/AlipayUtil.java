package com.huika.cloud.control.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.SignUtils;

public class AlipayUtil implements Runnable {

	private Activity mActivity;
	// 商户PID//合作身份者id，以2088开头的16位纯数字，这个你申请支付宝签约成功后就会看见
	public static final String PARTNER = "2088021510729153";
	// 商户收款账号 //这里填写收款支付宝账号，即你付款后到账的支付宝账号
	public static final String SELLER = "1685736211@qq.com"; // u39qszpseuff1jdpjey0y0hi5u6u2frl
	// 商户私钥，pkcs8格式 //商户私钥，自助生成，即rsa_private_key.pem中去掉首行，最后一行，
	// 空格和换行最后拼成一行的字符串，rsa_private_key.pem这个文件等你申请支付宝签约成功后，
	// 按照文档说明你会生成的.........................如果android版本太高，这里要用PKCS8格式用户私钥，
	// 不然调用不会成功的，那个格式你到时候会生成的，表急。
	public static final String RSA_PRIVATE = "u39qszpseuff1jdpjey0y0hi5u6u2frl";
	// /支付宝（RSA）公钥 ,demo自带不用改，或者用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取；或者文档上也有。
	public static final String RSA_PUBLIC = "";
	public static final int SDK_PAY_FLAG = 1;
	
	private double money = 0;
	private String subject; // 商品名称
	private String body;   // 商品描素
	private String orderNo;   // 商品描素
	private Handler mHandler; // 传送
	
	public AlipayUtil(Activity mContext, Handler mHandler,String subject, String body, String orderNo,double Money) {
		super();
		this.mActivity = mContext;
		this.mHandler = mHandler;
		this.subject = subject;
		this.body = body;
		this.money =  Money;
		this.orderNo = orderNo;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body,String orderNo, double price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
//	public String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	@Override
	public void run() {
		// 订单
		String orderInfo = getOrderInfo(subject, body,orderNo, money);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		// 构造PayTask 对象
		PayTask alipay = new PayTask(mActivity);
		// 调用支付接口，获取支付结果
		String result = alipay.pay(payInfo);
		Message msg = new Message();
		msg.what = SDK_PAY_FLAG;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}

}
