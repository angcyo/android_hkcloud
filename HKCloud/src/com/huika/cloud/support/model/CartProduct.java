package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @description：购物车商品
 * @author shicm
 * @date 2015-11-18 上午11:13:31
 */
public class CartProduct implements Serializable{

	private static final long serialVersionUID = -3489746155606253451L;
	private boolean isEditor; // 是否编辑
	private boolean isSelect = true; // 是否选择
	private String productId;// 商品ID
	private String productName;//:商品名称
	private String productImageUrl;//:商品图片（小图）
	private double productPrice;//:商品价格
	private String skuRs;//:  sku参数值拼接
	private String sku;//:  sku编码
	private long count;//:购买数量
	private double logistics;//:配送方式（0:包邮 其他:显示运费）【金额根据运费模板规则计算】
	public int type;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImageUrl() {
		return productImageUrl;
	}
	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public String getSkuRs() {
		return skuRs;
	}
	public void setSkuRs(String skuRs) {
		this.skuRs = skuRs;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getLogistics() {
		return logistics;
	}
	public void setLogistics(double logistics) {
		this.logistics = logistics;
	}
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	public boolean isEditor() {
		return isEditor;
	}
	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	@Override
	public String toString() {
		return "CartProduct [isEditor=" + isEditor + ", isSelect=" + isSelect + ", productId=" + productId + ", productName=" + productName + ", productImageUrl=" + productImageUrl
				+ ", productPrice=" + productPrice + ", skuRs=" + skuRs + ", sku=" + sku + ", count=" + count + ", logistics=" + logistics + "]";
	}

}
