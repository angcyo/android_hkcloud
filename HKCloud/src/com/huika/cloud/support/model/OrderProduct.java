package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：确认订单的准确性
 * @author shicm
 * @date 2015-12-3 下午4:29:58
 */
public class OrderProduct implements Serializable{

	private static final long serialVersionUID = 8596401265847430450L;
	private  AddressBean receiverRs;
	private List<CartProduct> productRs = new ArrayList<CartProduct>();
	private double logistics;//:配送方式（0:包邮 其他:显示运费）【金额根据运费模板规则计算】
	public AddressBean getReceiverRs() {
		return receiverRs;
	}
	public void setReceiverRs(AddressBean receiverRs) {
		this.receiverRs = receiverRs;
	}
	public List<CartProduct> getProductRs() {
		return productRs;
	}
	public void setProductRs(List<CartProduct> productRs) {
		this.productRs = productRs;
	}
	public double getLogistics() {
		return logistics;
	}
	public void setLogistics(double logistics) {
		this.logistics = logistics;
	}
	
}
