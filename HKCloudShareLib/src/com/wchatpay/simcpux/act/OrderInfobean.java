package com.wchatpay.simcpux.act;

import java.io.Serializable;

/**
 * @author zhangjianlin (990996641)
 * @description：订单实体类
 * @date 2015年6月6日 上午11:54:07
 */
public class OrderInfobean implements Serializable {

  private static final long serialVersionUID = 2666389784608169768L;
  private String orderDes;// 商品描述
  private String total_fee;// 支付的总价格
  private String out_trade_no;// 订单号
  private String notify_url;// 异步调用的地址

  public String getOrderDes() {
    return orderDes;
  }

  public void setOrderDes(String orderDes) {
    this.orderDes = orderDes;
  }

  public String getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(String total_fee) {
    this.total_fee = total_fee;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }
}
