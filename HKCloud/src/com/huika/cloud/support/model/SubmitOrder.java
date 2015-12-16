package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @description：确认的订单号
 * @author shicm
 * @date 2015-12-4 上午11:26:34
 */
@SuppressWarnings("serial")
public class SubmitOrder implements Serializable{
    public String orderNumber; // 订单编号
    public double orderPrice; // 
}
