package com.huika.cloud.control.me.activity;

import java.io.Serializable;

import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;

import com.huika.cloud.R;
import com.huika.cloud.control.me.fragment.MyDisOrderFragment;
import com.huika.cloud.support.model.DisOrderBean;
import com.huika.cloud.support.model.DisOrderDetailBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;
/**
 * @description：分销订单详情
 * @author ht
 * @date 2015-12-4 上午10:26:04
 */
public class DisOrderDetailActivity extends RdpBaseActivity implements OnRefreshItemViewsListener {
	private View mMasterView;
	private RdpListView orderLv;
	private RdpDataListAdapter<DisOrderDetailBean> orderItemAdapter;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("分销订单详情");
		DisOrderBean disOrderBean = (DisOrderBean) getIntent().getSerializableExtra(MyDisOrderFragment.SELECTED_ORDER);
		mMasterView = addMasterView(R.layout.dis_order_detail);
		orderLv = (RdpListView) mMasterView.findViewById(R.id.order_lv);
		orderItemAdapter = new RdpDataListAdapter<DisOrderDetailBean>(mApplication, R.layout.order_detail_item);
		orderItemAdapter.setListener(this);
		orderLv.setAdapter(orderItemAdapter);
		orderItemAdapter.setData(disOrderBean.orderArray);
	}
	
	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
			DisOrderDetailBean disOrderDetailBean = orderItemAdapter.getItem(position);
			RdpImageLoader.displayImage(disOrderDetailBean.productImage, holder.getImageView(R.id.iv_merchant));
			holder.getTextView(R.id.tv_merchant_name).setText(disOrderDetailBean.productName);
			holder.getTextView(R.id.tv_merchant_money).setText("¥"+disOrderDetailBean.productAppPrice);
			holder.getTextView(R.id.tv_merchant_num).setText("x"+disOrderDetailBean.count);
			holder.getTextView(R.id.tv_merchant_attr).setText(disOrderDetailBean.skuAttr);
		return false;
	}
}
