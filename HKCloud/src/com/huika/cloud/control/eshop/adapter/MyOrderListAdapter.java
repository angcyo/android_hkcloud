package com.huika.cloud.control.eshop.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huika.cloud.R;
import com.huika.cloud.control.eshop.actvity.MyOrderDetailActivity;
import com.huika.cloud.control.eshop.common.MyOrderBusiOper;
import com.huika.cloud.control.eshop.common.MyOrderBusiOper.IMyOrderBusiOper;
import com.huika.cloud.support.model.MyOrderBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataSetAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015-5-27 下午6:02:47
 */
public class MyOrderListAdapter extends RdpDataSetAdapter {
	public static final int BOT_PAY     = 1;
    public static final int BOT_CANCEL  = 2;
    public static final int BOT_AFTER_SERVICE   = 3;
    public static final int BOT_VALIDAY = 4;
    public static final int BOT_COMMENT = 5;
    public static final int BOT_DELETE  = 6;
    
    private RdpBaseActivity mActivity;
    private DecimalFormat df = new DecimalFormat("0.00");
    
	private MyOrderBusiOper mOrderBusiOper;
	private IMyOrderBusiOper mCallBack;
    public MyOrderListAdapter(Context context, RdpBaseActivity activity, IMyOrderBusiOper busiOperCallBack) {
	    super(context, R.layout.my_order_item);
	    mActivity = activity;
		mCallBack = busiOperCallBack;
		mOrderBusiOper = new MyOrderBusiOper(activity, busiOperCallBack);
	}

	@Override
    protected void refreshItemViews(final int position, AdapterViewHolder viewHolder) {
        final MyOrderBean data = (MyOrderBean) getItem(position);
        viewHolder.getTextView(R.id.tvOrderType).setText(data.getShowStatu());
        viewHolder.getTextView(R.id.tvOrderNumber).setText(data.orderNumber);
        viewHolder.getTextView(R.id.tvGoodsNum).setText("" + data.Product.size());
        viewHolder.getTextView(R.id.tvOrderFreightL).setText("合计：" + data.totalPrice);
        
        OrderGoodsAdapter adapter = new OrderGoodsAdapter(mContext, data);
        adapter.setOrderNumber(data.orderNumber);
//        adapter.setShowComment(data.orderStatus == MyOrderBean.STATUS_WAIT_COMMENT);
        viewHolder.getAbsListView(R.id.lvOrderGoods).setAdapter(adapter);
        viewHolder.getAbsListView(R.id.lvOrderGoods).setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posi, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(MyOrderDetailActivity.IPN_ORDER_NO, data.orderNumber);
                mActivity.showActivity(mActivity, MyOrderDetailActivity.class, bundle);
            }
        });
        adapter.setData(data.Product);
        
        viewHolder.getTextView(R.id.tvGoodsNum).setText("" + data.Product.size());
        
        
        Button btnBusiOne = viewHolder.getButton(R.id.btnBusiOne);
        Button btnBusiTwo = viewHolder.getButton(R.id.btnBusiTwo);
        Button btnBusiExpress = viewHolder.getButton(R.id.btnBusiExpress);
        View viewLine = (View) viewHolder.get(R.id.viewLine);
        mOrderBusiOper.refreshOperButtons(data, btnBusiOne, btnBusiExpress,btnBusiTwo, viewLine);
    }

    public interface IOrderCallBack {
        
        public void onMerchantClick(MyOrderBean data, View v);
    }

	

}
