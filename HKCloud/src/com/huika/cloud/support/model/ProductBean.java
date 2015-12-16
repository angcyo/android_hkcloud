package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerData;


public class ProductBean implements Serializable ,PagerData{
	private static final long serialVersionUID = 2507674943561555543L;

	private String productId;// 商品ID
	private String productName;// 商品名称
	private String imageUrl;// 商品图片(取第一张)
	private String saleCount;// 当前销售数量
	private double productPrice;// 价格(商家主页中指惠卡价格)
	/**商家主页面中用到字段*/
	private double shopPrice;// 市场价格
    private double discount;//折扣
    private double commissionAmount;//分佣金额
	/**我的浏览记录中用到字段*/
	private String browseId;// 浏览ID
	private boolean isSelected;//是否选中

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

	public String getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(String saleCount) {
		this.saleCount = saleCount;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public double getShopPrice() {
		return shopPrice;
	}

	public void setShopPrice(double shopPrice) {
		this.shopPrice = shopPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public String getBrowseId() {
		return browseId;
	}

	public void setBrowseId(String browseId) {
		this.browseId = browseId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static List<ProductBean> getDemoData(){
		ArrayList<ProductBean> arrayList = new ArrayList();
		for (int i = 0; i < 19; i++) {
			ProductBean productBean = new ProductBean();
			productBean.productName="列表一商品"+i;
			productBean.shopPrice=100+i;
			productBean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			arrayList.add(productBean);
		}
		return arrayList;
	}
	
	public static List<ProductBean> getDemoData2(){
		ArrayList<ProductBean> arrayList = new ArrayList();
		for (int i = 0; i < 15; i++) {
			ProductBean productBean = new ProductBean();
			productBean.productName="列表二商品"+i;
			productBean.shopPrice=100+i;
			productBean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			arrayList.add(productBean);
		}
		return arrayList;
	}
	
	public static List<ProductBean> getDemoData3(){
		ArrayList<ProductBean> arrayList = new ArrayList();
		for (int i = 0; i < 5; i++) {
			ProductBean productBean = new ProductBean();
			productBean.productName="轮播图商品"+i;
			productBean.shopPrice=100+i;
			productBean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			arrayList.add(productBean);
		}
		return arrayList;
	}
	
	public static List<ProductBean> getDemoData4(){
		ArrayList<ProductBean> arrayList = new ArrayList();
		for (int i = 0; i < 3; i++) {
			ProductBean productBean = new ProductBean();
			productBean.productName="轮播图一商品"+i;
			productBean.shopPrice=100+i;
			productBean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			arrayList.add(productBean);
		}
		return arrayList;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
