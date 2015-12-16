package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.Html;
import android.text.Spanned;

/** 
 * @description：
 * @author zhoukl
 * @date 2015-5-26 下午5:25:19
 */
public class MyOrderBean implements Serializable {
    private static final long serialVersionUID = -6233611514535555547L;

    public static final long DAY = 24 * 60 * 60;
	public static final long HOUR = 60 * 60;
	public static final long MINUTE = 60;

//	/**订单本地状态（0：全部，1：拼购进行中，2：待付尾款，3：待收货，4：已完成，5：待评价）**/
//	public static final int STATU = 0;// 显示所有状态，并不代表实际状态
//	public static final int STATU_L_PIN = 1;// 显示所有状态，并不代表实际状态
//	public static final int STATU_L_LAST = 2;
//	public static final int STATU_L_RECEIVE = 3;
//	public static final int STATU_L_COMMENT = 5;

    public static final int STATUS_ALL      = 0;
    public static final int STATUS_WAIT_PAY = 1;
	public static final int STATUS_WAIT_SEND = 2;
	public static final int STATUS_WAIT_RECEIVE = 3;
	public static final int STATUS_BARTER_APPLY = 4;
	public static final int STATUS_BARTER_WAIT_RECEIVE = 5;
	public static final int STATUS_REJECT_APPLY = 6;
	public static final int STATUS_CORP_WAIT_RECEIVE = 7;
    public static final int STATUS_COMPLETE = 8;
    public static final int STATUS_COMMENTTED = 9;
    public static final int STATUS_DELETED = 10;
	public static final int STATUS_TIMEOUT_RECEIVE = 4;
//    public static final int STATUS_CANCEL = 9;

	/**orderStatus:订单状态   1:待付款;2:待发货;3:待收货;4:换货待审核;5:换货待收货;6:退货待审核;7:企业待收货;8:交易完成;9:已评论;10:用户删除*/
    public int orderStatus;
	/**订单编号*/
	public String orderNumber;
    public int merchantID;
	/**店铺名称*/
	public String shopName;
	/**店铺图片*/
	public String imageUrl;
	/**邮费*/
	public double freightPrice;
	/**商品数量*/
	public String productNumber;
	/**订单总金额*/
	public double totalPrice;
	/** 是否已评价，1已评价，2未评价*/
	public int isComment;
	/**是否有物流信息 1有 0没有*/
	public int expressStatus;
	
	public List<OrderGoodsBean> Product = new ArrayList<OrderGoodsBean>();
	public AddressBean ReceiverRs;
	
	public long serverDt;  // 服务器当前时间（时间戳）
	public long endDt;     // 订单支付截止时间 (时间戳)
	
	/** 是否已延迟收货，1已延迟，0未延迟*/
	public int isDelay;
	/** 0：整单发货 1：单品发货*/
	public int isHaveSignDelivery;

	public boolean hadComment() {
		return isComment == 1;
	}


	/**
	 * 通过剩余时间戳的值，获取用于显示的值
	 * 如 剩余8天5时4分30秒
	 * @param time
	 * @return
	 * @author FAN 创建于Dec 4, 2014
	 */
	public static String getLastShowDt(long time) {
		StringBuilder builder = new StringBuilder();
		if (time > 0) {
			builder.append("剩余");
			// 秒
			if (time > DAY) {// 天
				long d = time / DAY;
				builder.append(d).append("天");
				time = time % DAY;
			}
			if (time > HOUR) {// 时
				long h = time / HOUR;
				builder.append(h).append("时");
				time = time % HOUR;
			}
			if (time > MINUTE) {// 分
				long m = time / MINUTE;
				builder.append(m).append("分");
				time = time % MINUTE;
			}
			if (time > 0) {
				builder.append(time).append("秒");
			}
		}
		else {
			builder.append("拼购结束");
		}
		return builder.toString();
	}

	public Spanned getShowStatu() {
		String s = "交易完成";
		String c = "#ff7815";
		switch (orderStatus) {
			case STATUS_WAIT_PAY:
				s = "待付款 ";
				break;
			case STATUS_WAIT_SEND:
				s = "待发货";
				break;
			case STATUS_WAIT_RECEIVE:
				s = "待收货";
				break;
			case STATUS_BARTER_APPLY:
				s = "换货待审核";
				break;
			case STATUS_BARTER_WAIT_RECEIVE:
				s = "换货待收货";
				break;
			case STATUS_REJECT_APPLY:
				s = "退货待审核";
				break;
			case STATUS_CORP_WAIT_RECEIVE:
				s = "企业待收货";
				break;
			case STATUS_COMPLETE:
				s = "已完成";
				break;
//			case STATUS_CANCEL:
//				s = "已取消";
//				break;
		}

		String fontStr = "<font color=%s>%s</font>";
		return Html.fromHtml(String.format(fontStr, c, s));
	}

	public boolean isDelay() {
	    return isDelay == 1 ? true : false;
	}
	
	public boolean isComment() {
	    return isComment == 1 ? true : false;
	}
	
	/**
	 * 判断请求状态和数据库真实状态是否对应
	 * @param requestStatu
	 * @param realStatu
	 * @return
	 * @author FAN 创建于Dec 10, 2014
	 */
//	public static boolean isSameStatu(int requestStatu, int realStatu) {
//		if (requestStatu == 0) { return true; }
//		int tempStatu = -1;
//		switch (realStatu) {
//			case STATU_PIN:
//				tempStatu = 1;
//				break;
//			case STATU_LAST:
//				tempStatu = 2;
//				break;
//			case STATU_SEND:
//			case STATU_RECEIVE:
//				tempStatu = 3;
//				break;
//			case STATU_TIMEOUT:
//				tempStatu = 7;
//				break;
//			case STATU_RECEIVE_TIMEOUT:
//			case STATU_OVER:
//				// tempStatu = 4;//已完成
//				tempStatu = 5;// 待评价
//				break;
//		}
//		return tempStatu == requestStatu;
//	}

	public static ArrayList<Object> getDemoData(){
		ArrayList<Object> arrayList = new ArrayList();
		for (int i = 0; i < 19; i++) {
			MyOrderBean bean = new MyOrderBean();
			bean.orderStatus = (i + 1) % 10;
			bean.shopName =""+i;
			bean.totalPrice =100+i;
			bean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			bean.Product = OrderGoodsBean.getDemoData(); 
			arrayList.add(bean);
		}
		return arrayList;
	}
}
