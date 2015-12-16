package com.huika.cloud.support.model;

import com.zhoukl.androidRDP.RdpModel.BaseUserBean;

/**
 * @description：用户
 * @author shicm
 * @date 2015-11-20 下午2:04:26
 */
public class UserModel extends BaseUserBean {
	private static final long serialVersionUID = 2304038177368198123L;
	private String imageUrl; // :头像地址
	private String nickName;// :昵称
	private double balance;// : 个人账户余额
	private int couponNum;// :优惠券数量
	private int paymentNum;// :待付款订单数量
	private int goodsNum; // ：已发货订单数量
	private int returnNum; // ：退换货数量
	private long inviteNum;// :我邀请的会员数量
	private int transPassword; // 是否设置交易密码(返回0或1)0:未设置交易密码 //1:已设置交易密码(返回0或1)
	private int realNameAuthentication; // 是否实名认证 0:未完善， 1已认证
	private String realName;
	/** 身份证号 **/
	private String idNumber;

	private String failureRemark; // 实名验证失败提示语(当是否实名认证状
	private int isBindBank;// 是否绑定银行卡0未绑定1:绑定
	private String levelName;// :会员等级称号 (普通会员/三级分销)
	private String levelRemark;// :等级介绍信息
	private String levelIcon; // :等级图标
	private String saleLevel;// 分销等级
	private String saleNeedNum; //分销需要邀请人数
	private String discount; // 会员折扣


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}

	public int getPaymentNum() {
		return paymentNum;
	}

	public void setPaymentNum(int paymentNum) {
		this.paymentNum = paymentNum;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}

	public long getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(long inviteNum) {
		this.inviteNum = inviteNum;
	}

	public int getTransPassword() {
		return transPassword;
	}

	public void setTransPassword(int transPassword) {
		this.transPassword = transPassword;
	}

	public int getRealNameAuthentication() {
		return realNameAuthentication;
	}

	public void setRealNameAuthentication(int realNameAuthentication) {
		this.realNameAuthentication = realNameAuthentication;
	}

	public String getFailureRemark() {
		return failureRemark;
	}

	public void setFailureRemark(String failureRemark) {
		this.failureRemark = failureRemark;
	}

	public int getIsBindBank() {
		return isBindBank;
	}

	public void setIsBindBank(int isBindBank) {
		this.isBindBank = isBindBank;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelRemark() {
		return levelRemark;
	}

	public void setLevelRemark(String levelRemark) {
		this.levelRemark = levelRemark;
	}

	public String getLevelIcon() {
		return levelIcon;
	}

	public void setLevelIcon(String levelIcon) {
		this.levelIcon = levelIcon;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	
	public String getSaleLevel() {
		return saleLevel;
	}

	public void setSaleLevel(String saleLevel) {
		this.saleLevel = saleLevel;
	}

	public String getSaleNeedNum() {
		return saleNeedNum;
	}

	public void setSaleNeedNum(String saleNeedNum) {
		this.saleNeedNum = saleNeedNum;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "UserModel [imageUrl=" + imageUrl + ", nickName=" + nickName + ", balance=" + balance + ", couponNum=" + couponNum + ", paymentNum=" + paymentNum + ", goodsNum=" + goodsNum
				+ ", returnNum=" + returnNum + ", inviteNum=" + inviteNum + ", transPassword=" + transPassword + ", realNameAuthentication=" + realNameAuthentication + ", realName=" + realName
				+ ", idNumber=" + idNumber + ", failureRemark=" + failureRemark + ", isBindBank=" + isBindBank + ", levelName=" + levelName + ", levelRemark=" + levelRemark + ", levelIcon="
				+ levelIcon + ", saleLevel=" + saleLevel + ", saleNeedNum=" + saleNeedNum + ", discount=" + discount + "]";
	}

}
