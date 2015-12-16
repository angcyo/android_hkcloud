package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @author shicm
 * @description：
 * @date 2015年6月8日 下午4:40:06
 */
public class WeixinPay implements Serializable {

  private static final long serialVersionUID = -211018191629342101L;
  private String orderNo;
  private String appid; // :微信分配的公众账号ID
  private String partnerid; //:微信支付分配的商户号
  private String prepayid; // :微信返回的支付交易会话ID
  private String wxPackage;//:暂填写固定值Sign=WXPay
  private String noncestr; // :随机字符串，不长于32位。推荐随机数生成算法
  private String timestamp; //:时间戳，请见接口规则-参数规定
  private String sign; //:签名，详见签名生成算法

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getPartnerid() {
    return partnerid;
  }

  public void setPartnerid(String partnerid) {
    this.partnerid = partnerid;
  }

  public String getPrepayid() {
    return prepayid;
  }

  public void setPrepayid(String prepayid) {
    this.prepayid = prepayid;
  }

  public String getWxpackage() {
    return wxPackage;
  }

  public void setWxpackage(String wxPackage) {
    this.wxPackage = wxPackage;
  }

  public String getNoncestr() {
    return noncestr;
  }

  public void setNoncestr(String noncestr) {
    this.noncestr = noncestr;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  @Override public String toString() {
    return "WeixinPay [orderNo=" + orderNo + ", appid=" + appid + ", partnerid=" + partnerid
        + ", prepayid=" + prepayid + ", wxpackage=" + wxPackage + ", noncestr=" + noncestr
        + ", timestamp=" + timestamp + ", sign=" + sign + "]";
  }
}
