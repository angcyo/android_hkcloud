package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * @description：
 * @author zhoukl
 * @date 2015-5-26 下午5:25:19
 */
public class MyOrderDetailBean extends MyOrderBean implements Serializable {

 
	/** 收货人 */
    public String receiverName;
    /** 收货地址 */
    public String receiverAddress;
    /** 收货电话(手机) */
    public String receiverPhone;
    /** 收货电话(固话) */
    public String receiverTelephone;
    /** 快递单号 */
    public String expressOrder;
    /** 订单时间  */
    public long createDt;
    /** 支付时间 (时间戳)  */
    public long paidDt;
    /** 订单更新时间（取消时间）(时间戳)  */
    public long updateDt;

    /** 支付方式   1：余额支付；2：第三方支付；3：余额+第三方支付 */
    public int payWay;
    /**惠粉类型 1惠粉 2准惠粉*/
    public int hfType;
    /**钱包体验金信息*/
//    public List<qbExperienceGoldBean> qbExperienceGold = new ArrayList<qbExperienceGoldBean>();
//    public ExpressLogDetailBean expressLog;
    
    public String getPayWay() {
        switch (payWay) {
        case 2:
            return "第三方支付";
        case 3:
            return "余额+第三方支付";
        default:
            return "余额支付";
        }
    }
    
    public static ArrayList<Object> getDemoData(){
		ArrayList<Object> arrayList = new ArrayList();
		MyOrderDetailBean bean = new MyOrderDetailBean();
		bean.shopName ="11";
		bean.totalPrice =100;
		bean.orderStatus = 1;
		bean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
		bean.Product = OrderGoodsBean.getDemoData(); 
		arrayList.add(bean);
		return arrayList;
	}
}
