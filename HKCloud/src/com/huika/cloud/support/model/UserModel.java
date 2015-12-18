package com.huika.cloud.support.model;

import com.zhoukl.androidRDP.RdpModel.BaseUserBean;

/**
 * @description：用户
 * @author shicm
 * @date 2015-11-20 下午2:04:26
 */
public class UserModel extends BaseUserBean {
	private static final long serialVersionUID = 2304038177368198123L;
	public String imageUrl;//:头像地址
	public String nickName;//:昵称
	public double balance;//: 个人账户余额
	public int couponNum;//:优惠券数量
	public int paymentNum;//:待付款订单数量
	public int goodsNum;//：已发货订单数量
	public int returnNum;//：退换货数量
	public int inviteNum;//:我邀请的会员数量
	public int transPassword;//  是否设置交易密码(返回0或1)0:未设置交易密码 1:已设置交易密码(返回0或1)
	public int realNameAuthentication;// 是否实名认证0:未完善， 1:已认证
	public int isBindBank;//是否绑定银行卡0未绑定1:绑定
	public String levelName;// :会员等级称号 (普通会员/三级分销)
	public String levelRemark;//:等级介绍信息
	public String levelIcon;//:等级图标
	public int saleLevel;// 分销等级
	public int saleNeedNum;// 分销需要邀请人数
	public String realName;// 真实姓名
	public String idNumber;// 身份证号
	public double discount; //会员折扣
	public String phone;//:手机号码
	public int bankNum;//:银行卡数量

	@Override
	public String toString() {
		return "UserModel [imageUrl=" + imageUrl + ", nickName=" + nickName + ", balance=" + balance + ", couponNum=" + couponNum + ", paymentNum=" + paymentNum + ", goodsNum=" + goodsNum + ", returnNum=" + returnNum + ", inviteNum=" + inviteNum + ", transPassword=" + transPassword + ", realNameAuthentication=" + realNameAuthentication + ", realName=" + realName + ", idNumber=" + idNumber + ", isBindBank=" + isBindBank + ", levelName=" + levelName + ", levelRemark=" + levelRemark + ", levelIcon=" + levelIcon + ", saleLevel=" + saleLevel + ", saleNeedNum=" + saleNeedNum + ", discount=" + discount + ", phone=" + phone + ", bankNum=" + bankNum + "]";
	}

}
