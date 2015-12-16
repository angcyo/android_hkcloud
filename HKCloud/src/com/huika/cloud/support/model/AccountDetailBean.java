package com.huika.cloud.support.model;

public class AccountDetailBean {
//	来源订单编号【分佣、返现、消费】、【提现（银行卡）状态返回银行卡号】、其他状态为空
	public String orderNumber;
//	资金异动类型:  【1:充值,2:转账,3:消费,4提现(银行卡),5提现（微信）5:退款,6:佣金,7买单返现】
	public int addOrCutType;
//	异动金额
	public String addOrCutAmount;
//	交易方【包含：买家、开户人、微信号】
	public String trader;
//	增或减 1 增 2减
	public int isAddOrCuT;
//	下单时间
	public String createDt;
//	交易时余额
	public String newAmount;
	
}
