package com.huika.cloud.control.eshop.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.support.model.MyOrderBean;
import com.huika.cloud.support.model.MyOrderDetailBean;
import com.huika.cloud.support.model.OrderGoodsBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;

public class OrderGoodsAdapter extends RdpDataListAdapter<OrderGoodsBean> {
	private DecimalFormat df = new DecimalFormat("0.00");
    private boolean mShowComment = false;
//    private BaseAct mActivity;
    private String mOrderNumber;
    private MyOrderBean orderBean;
    private int type;//type 1 购物订单列表  2购物订单详情
    private MyOrderDetailBean detail;
//    private MyShopOrderBusiOper busi;
    //获取订单状态4 及 商品状态 也是 4 及物流号是否为多个
    private boolean isOrderShopState = false;
	private DisplayImageOptions options;
    
    public OrderGoodsAdapter(Context context, MyOrderBean data) {
		super(context);
		setItemLayoutID(R.layout.my_order_list_item);
		orderBean = data;
	}

//    public OrderGoodsAdapter(Context context, BaseAct activity,MyOrderBean orderBean,int layoutType,int type,MyOrderDetailBean detail) {
//        super(context,layoutType);
//        mActivity = activity;
//        this.orderBean = orderBean;
//        this.type = type;
//        this.detail = detail;
//        busi = new MyShopOrderBusiOper(mActivity);
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.product_list_default) // resource or drawable
//				.showImageForEmptyUri(R.drawable.product_list_default) // resource or drawable
//				.showImageOnFail(R.drawable.product_list_default) // resource or drawable
//				.cacheInMemory(true)// 开启内存缓存
//				.cacheOnDisk(true) // 开启硬盘缓存
//				.resetViewBeforeLoading(false).build();
//       
//    }
    public void setDetailData(MyOrderDetailBean detail){
    	 this.detail = detail;
    }
//   
//    public void setMyShopCallBack(MyShopOrderBusiInterface mCallBack){
//         busi.setmCallBack(mCallBack);
//    }

	public void setIsOrderShopState(boolean isOrderShopState){
		this.isOrderShopState = isOrderShopState;
	}
    
    protected void refreshItemViews(int position, AdapterViewHolder viewHolder) {
        final OrderGoodsBean data = (OrderGoodsBean) getItem(position);
        viewHolder.getTextView(R.id.tvGoodsTitle).setText(data.productName);
        viewHolder.getTextView(R.id.tvPrice).setText("¥" + df.format(data.shopPrice));
        viewHolder.getTextView(R.id.tvGoodsNum).setText("x" + data.buyCount);
        viewHolder.getTextView(R.id.tvSKU).setText(data.skuRs);
        TextView orderShowState  =  viewHolder.getTextView(R.id.shoporder_tv_backstate);
        RdpImageLoader.displayImage(data.imageUrl, viewHolder.getImageView(R.id.imgGoods));
    }

    //returnType 操作类型，productReturnStatus 操作动作类型
    private String getBackShopState(int returnType,int productReturnStatus){
    	String state = "";
    	if(returnType < 1)
    		return "";
       	switch(productReturnStatus){
    		case 1:
    			//申请状态
    			state = getReturnType(returnType)+"中";
    			break;
    		case 2:
    			//同意申请状态
    			state = getReturnType(returnType)+"中";
    			break;
    		case 3:
    			//进行中状态
    			state = getReturnType(returnType)+"中";
    			break;
    		case 4:
    			//成功状态
    			state = getReturnType(returnType)+"成功";
    			break;
    		case 5:
    			//失败状态
    			state = getReturnType(returnType)+"失败";
    			break;
    		case 6:
    			//关闭状态
    			state = getReturnType(returnType)+"关闭";
    			break;
    	}
    	return state;
    }
    
	// 返回操作类型
	private String getReturnType(int returnType) {
		if (returnType == 1) {
			// 退货
			return "退货";
		} else if (returnType == 2) {
			// 退款
			return "退款";
		}
		else if(returnType == 3) {
			// 换货
			return "换货";
		}
		else{
			return "";
		}
	}
    public boolean isShowComment() {
        return mShowComment;
    }

    public void setShowComment(boolean mShowComment) {
        this.mShowComment = mShowComment;
    }

    public void setOrderNumber(String orderNumber) {
        mOrderNumber = orderNumber;
    }

	
}
