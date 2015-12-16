package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.List;

public class DisOrderBean implements Serializable {
	public String express_money;//邮费 
	public String buyersUP;//买家上级
	public String orderNo;//订单编号
	public String buyers;//买家
	public String totalAmount;//订单总金额,
	public String createDt;//下单时间
	public String orderStatus;//订单状态
	public List<DisOrderDetailBean> orderArray;

}
