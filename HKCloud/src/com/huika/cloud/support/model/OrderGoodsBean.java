package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * @description：
 * @author zhoukl
 * @date 2015-5-26 下午5:23:56
 */
public class OrderGoodsBean implements Serializable {
    private static final long serialVersionUID = -5590570326345338429L;
    
    /**商品ID*/
	public String productId;
	/**商品名称*/
	public String productName;
	/**商品图片*/
	public String imageUrl;
	/**惠卡价格*/
//	public double tradePrice;
	/**购买数量*/
	public long buyCount;
	/**商品单价*/
	public long shopPrice;
    /**sku参数值拼接*/
    public String skuRs;
    public String colour;
    /**尺寸*/
    public String size;
    
    /** 是否已评价，1已评价，2未评价*/
    public int isComment;
	/** 是否已延迟收货，1已延迟，0未延迟*/
	public int isDelay;
    /**商品状态（订单状态（-1：无效订单；2：待付款，3：待发货，4：待收货，5：超时收货，6：已完成，8：待评价，9：已取消））*/
    public int productStatus;
    /**退换货状态 1.退换货申请中 2.商家同意退货 3.退换货中 4退换货完成 5退换货失败 6退换货关闭*/
    public int productReturnStatus;
    /**退款类型1.退货  2.退款  3.换货 0.没有退货款货*/
    public int returnType;
    /**物流单号*/
    public String logisticsNumber;
    /**退换货退款物流单号*/
    public String returnNumber;
    /**订单详情Id*/
    public String orderDetailsId;
    /**退款金额*/
    public String returnAmount;
    /**确定收货 0小于等于7 申请售后  大于7小于等于15 只能换货*/
    public int skipDay;
    public boolean getIsComment() {
        return isComment == 1 ? true : false;
    }

    public static List<OrderGoodsBean> getDemoData(){
    	ArrayList<OrderGoodsBean> arrayList = new ArrayList<OrderGoodsBean>();
		for (int i = 0; i < 2; i++) {
			OrderGoodsBean bean = new OrderGoodsBean();
			bean.productName ="商品名称"+i;
			bean.shopPrice = 50+i;
			bean.imageUrl="http://img0.bdstatic.com/img/image/shouye/mingxing0921.jpg";
			arrayList.add(bean);
		}
		return arrayList;
    }
}
