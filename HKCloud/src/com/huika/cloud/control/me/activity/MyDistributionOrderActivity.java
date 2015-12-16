//package com.huika.cloud.control.me.activity;
//
//import java.util.List;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.huika.cloud.R;
//import com.huika.cloud.support.model.DisOrderBean;
//import com.huika.cloud.support.model.DisOrderDetailBean;
//import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
//import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
//import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
//import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
//import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
//import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
//import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
//import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
//import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
//import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerListView;
//import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;
//
///**
// * @description：我的分销订单
// * @author ht
// * @date 2015-12-4 上午10:22:33
// */
//public class MyDistributionOrderActivity extends RdpBaseActivity implements OnItemClickListener, OnRefreshItemViewsListener {
//	private View mMasterView;
//	private RdpListView disOrderLv;
//	private TextView tv_team_consume_total;
//	private ImageView iv_goto_team_consume;
//	private TextView tv_my_dis_order_count;
//	private RelativeLayout rl_all,rl_wait,rl_after_sale,rl_finished;
//	private View view_all;
//	private View view_wait;
//	private View view_after_sale;
//	private View view_finished;
//	private TextView order_wait;
//	private TextView order_after_sale;
//	private TextView finished_order;
//	boolean isSelectedAll=true;
//	boolean isSelectedWait=false;
//	boolean isSelectedSaled=false;
//	boolean isSelectedFinished=false;
//	private RdpDataListAdapter<DisOrderBean> disOrderAdapter;
//	private RdpDataListAdapter<DisOrderDetailBean> disOrderDetailAdapter;
//	
//	@Override
//	protected void initActivity() {
//		super.initActivity();
//		setFuncTitle("我的分销订单");
//		mMasterView = addMasterView(R.layout.my_distri_order);
//		initView();
//		initListener();
//		disOrderAdapter = new RdpDataListAdapter<DisOrderBean>(mApplication, R.layout.my_dis_order_item);
//		disOrderAdapter.setListener(this);
//		disOrderLv.setAdapter(disOrderAdapter);
//		
//		//获取分销订单数据
//		RdpNetDataSet disOrderData = new RdpNetDataSet(mApplication);
//		disOrderData.setServerApiUrl("http://192.168.50.3:8083/hxlmpro-api/User/GetMyDistributionOrder");
//		disOrderData.setOnCommandSuccessedListener(this);
//		disOrderData.setOnCommandFailedListener(this);
//		disOrderData.clearConditions();
//		disOrderData.setCondition("memberId", "402894e1511f1b6d01511f1bf30d0000");
//		disOrderData.setCondition("merchantId","402881e8461795c201461795c2e90000");
//		disOrderData.open();
//	}
//
//	private void initListener() {
//		iv_goto_team_consume.setOnClickListener(this);
//		rl_all.setOnClickListener(this);
//		rl_wait.setOnClickListener(this);
//		rl_after_sale.setOnClickListener(this);
//		rl_finished.setOnClickListener(this);
//		disOrderLv.setOnItemClickListener(this);
//	}
//
//	private void initView() {
//		disOrderLv = (RdpListView) mMasterView.findViewById(R.id.rlv);
//		tv_team_consume_total = (TextView) mMasterView.findViewById(R.id.tv_team_consume_total);
//		iv_goto_team_consume = (ImageView) mMasterView.findViewById(R.id.iv_goto_team_consume);
//		tv_my_dis_order_count = (TextView) mMasterView.findViewById(R.id.tv_my_dis_order_count);
//		rl_all = (RelativeLayout) mMasterView.findViewById(R.id.rl_all);
//		rl_wait = (RelativeLayout) mMasterView.findViewById(R.id.rl_wait);
//		rl_after_sale = (RelativeLayout) mMasterView.findViewById(R.id.rl_after_sale);
//		rl_finished = (RelativeLayout) mMasterView.findViewById(R.id.rl_finished);
//		view_all = mMasterView.findViewById(R.id.view_all);
//		view_wait = mMasterView.findViewById(R.id.view_wait);
//		view_after_sale = mMasterView.findViewById(R.id.view_after_sale);
//		view_finished = mMasterView.findViewById(R.id.view_finished);
//		order_wait = (TextView) mMasterView.findViewById(R.id.order_wait);
//		order_after_sale = (TextView) mMasterView.findViewById(R.id.order_after_sale);
//		finished_order = (TextView) mMasterView.findViewById(R.id.finished_order);
//	}
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.rl_all:
//			if(!isSelectedAll){
//				isSelectedAll=true;
//				isSelectedFinished=false;
//				isSelectedSaled=false;
//				isSelectedWait=false;
//				view_all.setVisibility(View.VISIBLE);
//				view_finished.setVisibility(View.GONE);
//				view_wait.setVisibility(View.GONE);
//				view_after_sale.setVisibility(View.GONE);
//			}
//			break;
//		case R.id.rl_wait:
//			if(!isSelectedWait){
//				isSelectedAll=false;
//				isSelectedFinished=false;
//				isSelectedSaled=false;
//				isSelectedWait=true;
//				view_all.setVisibility(View.GONE);
//				view_finished.setVisibility(View.GONE);
//				view_wait.setVisibility(View.VISIBLE);
//				view_after_sale.setVisibility(View.GONE);
//			}
//			break;
//		case R.id.rl_after_sale:
//			if(!isSelectedSaled){
//				isSelectedAll=false;
//				isSelectedFinished=false;
//				isSelectedSaled=true;
//				isSelectedWait=false;
//				view_all.setVisibility(View.GONE);
//				view_finished.setVisibility(View.GONE);
//				view_wait.setVisibility(View.GONE);
//				view_after_sale.setVisibility(View.VISIBLE);
//			}
//			break;
//		case R.id.rl_finished:
//			if(!isSelectedFinished){
//				isSelectedAll=false;
//				isSelectedFinished=true;
//				isSelectedSaled=false;
//				isSelectedWait=false;
//				view_all.setVisibility(View.GONE);
//				view_finished.setVisibility(View.VISIBLE);
//				view_wait.setVisibility(View.GONE);
//				view_after_sale.setVisibility(View.GONE);
//			}
//			break;
//
//		default:
//			super.onClick(v);
//			break;
//		}
//	}
//
//	@Override
//	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
//		super.onCommandSuccessed(reqKey, result, data);
//		disOrderAdapter.setData((List<DisOrderBean>) data);
//	}
//	
//	@Override
//	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
//		super.onCommandFailed(reqKey, result);
//	}
//	
//	@Override
//	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
//		if(adapter.equals(disOrderAdapter)){
//			DisOrderBean disOrderBean = disOrderAdapter.getItem(position);
//			holder.getTextView(R.id.order_num).setText(disOrderBean.orderNo);
//			holder.getTextView(R.id.merchant_num).setText("共"+disOrderBean.orderArray.size()+"件商品");
//			holder.getTextView(R.id.account_money).setText("合计¥ "+disOrderBean.totalAmount+"(含运费¥+"+disOrderBean.express_money+")");
//			RdpInnerListView orderItemLv = (RdpInnerListView) holder.getView(R.id.lv_order_item);
//			disOrderDetailAdapter = new RdpDataListAdapter<DisOrderDetailBean>(mApplication, R.layout.order_detail_item); 
//			disOrderDetailAdapter.setListener(MyDistributionOrderActivity.this);
//			orderItemLv.setAdapter(adapter);
//			disOrderDetailAdapter.setData(disOrderBean.orderArray);
//		}
//		if(adapter.equals(disOrderDetailAdapter)){
//			DisOrderDetailBean disOrderDetailBean = disOrderDetailAdapter.getItem(position);
//			RdpImageLoader.displayImage(disOrderDetailBean.productImage, holder.getImageView(R.id.iv_merchant));
//			holder.getTextView(R.id.tv_merchant_name).setText(disOrderDetailBean.productName);
//			holder.getTextView(R.id.tv_merchant_money).setText("¥"+disOrderDetailBean.productAppPrice);
//			holder.getTextView(R.id.tv_merchant_num).setText("x"+disOrderDetailBean.count);
//			holder.getTextView(R.id.tv_merchant_attr).setText(disOrderDetailBean.skuAttr);
//		}
//		return false;
//	}
//	
//	
//	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		//跳转到分销订单详情界面
//		
//	}
//
//	
//}
