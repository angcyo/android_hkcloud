package com.huika.cloud.control.eshop.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.eshop.actvity.MyOrderActivity;
import com.huika.cloud.control.eshop.adapter.MyOrderListAdapter;
import com.huika.cloud.support.model.MyOrderBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataSetAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpNetListBaseFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：订单Fragment
 * @author zhoukl
 * @date 2015-5-26 上午8:58:28
 */
public class MyOrderFragment extends RdpNetListBaseFragment {//implements OnInitShowListener, Listener<RequestResult<List<MyOrderBean>>>, IOrderCallBack, IMyOrderBusiOper{
	public static final String IPN_ORDER_TYPE = "IPN_ORDER_TYPE";
	public static final String LOAD_INIT = "load_init";
	private static final String BUNDLE_DATAS = "BUNDLE_DATAS";
	private static final String BUNDLE_PAGE = "BUNDLE_PAGE";
	private static final String BUNDLE_LOADINIT = "BUNDLE_LOADINIT";
	private List<MyOrderBean> mOrderDataList = new ArrayList<MyOrderBean>();
	private int mOrderType = MyOrderActivity.ORDER_TYPE_ALL;
	private boolean loadOnInit = false;
	private View emptyView;
	

	@Override
	protected RdpDataSetAdapter getMasterAdapter() {
		mMasterAdapter = new MyOrderListAdapter(mActivity, mActivity, null);
		mMasterAdapter.setListener(this);
		return mMasterAdapter;
		// return super.getMasterAdapter();
	}
	
	@Override
	protected void initFragment() {
		super.initFragment();

		// initLoadingDialog(true);

		if (getArguments() != null) {
			mOrderType = getArguments().getInt(IPN_ORDER_TYPE, MyOrderActivity.ORDER_TYPE_ALL);
			loadOnInit = getArguments().getBoolean(LOAD_INIT, false);
		}
		//setFuncTitle("title: " + mOrderType);
		mTvTitle.setText("title: " + mOrderType);
		mTitleBar.setVisibility(View.GONE);

//		if (savedInstanceState != null) {
//			loadOnInit = savedInstanceState.getBoolean(BUNDLE_LOADINIT);
//			CURRENT_PAGE = savedInstanceState.getInt(BUNDLE_PAGE);
//		}

		
		//mLvMaster.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {
		mLvMaster.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int index = position - mLvMaster.getRefreshableView().getHeaderViewsCount();
				MyOrderBean data = (MyOrderBean) mMasterAdapter.getItem(index);
				Bundle bundle = new Bundle();
//				bundle.putString(MyOrderDetailActivity.IPN_ORDER_NO, data.orderNumber);
//				showActivity(getActivity(), MyOrderDetailActivity.class, bundle);
			}

		});

//		adapter = new OrderListAdapter(getActivity(), this, baseAct,this);

//		if (loadOnInit && adapter.getCount() == 0) {
//			onInitShow();
//		}
	}

	@Override
	protected void openDataSet() {
		super.openDataSet();
		if (!(loadOnInit && mDataSet.getRecordCount() == 0)) return;

		mDataSet.setServerApiUrl(UrlConstant.API_ORDER_LIST);
        mDataSet.clearConditions();
        mDataSet.setCondition("orderStatus", getOrderStatus(mOrderType));
//        mDataSet.setCondition("memberId", mActivity.getMemberId());
        mDataSet.setCondition("memberId", "402894e1511f1b6d01511f1bf30d0000");
        mDataSet.setCondition("merchantId", "402881e8461795c201461795c2e90000");
        
        
        Type typeOfResult = new TypeToken<ArrayList<MyOrderBean>>() {}.getType();
        mDataSet.setTypeOfResult(typeOfResult);

        mDataSet.open();
        
//        mDataSet.setData(MyOrderBean.getDemoData());
	}
	
	public void show() {
		loadOnInit = true;
		openDataSet();
	}
	
	private int getOrderStatus(int orderType) {
		switch (orderType) {
			case MyOrderActivity.ORDER_TYPE_ALL:
				return 0;
			case MyOrderActivity.ORDER_TYPE_WAIT_PAY:
				return 2;
			case MyOrderActivity.ORDER_TYPE_WAIT_SEND:
				return 3;
			case MyOrderActivity.ORDER_TYPE_WAIT_RECEIVE:
				return 4;
			case MyOrderActivity.ORDER_TYPE_WAIT_COMMENT:
				return 8;
			default:
				return 0;
		}
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View root = super.onCreateView(inflater, container, savedInstanceState);
//
//		if (savedInstanceState != null) {
//			ArrayList<MyOrderBean> group = (ArrayList<MyOrderBean>) savedInstanceState.getSerializable(BUNDLE_DATAS);
//			if (group == null || group.size() <= 0) {
//				onInitShow();
//				ajaxParams.putCommonTypeParam("pageNo", CURRENT_PAGE);
//				executeRequest(resultRequest);
//			}
//			else {
//				adapter.setGroup(group);
//			}
//		}
//
//		return root;
//	}

	/**
	 * 外部用刷新
	 */
	public void outReload() {
		// if (adapter == null || adapter.isEmpty()) {
		if (isAdded()) {
			hideOverLayView();
			showLoadingDialog(getString(R.string.common_loading));
//			reloadData();
		}
		// }
	}

	/**
	 * 外部用停止
	 */
	public boolean outStop() {
		dismissLoadingDialog();
//		cancelRequest(this);
//		if (mPullRefreshListView != null) {
//			mPullRefreshListView.onRefreshComplete();
//		}
		return mMasterAdapter == null || mMasterAdapter.isEmpty();
	}

//	@Override
//	public void onInitShow() {
//		if (baseAct == null) return;
//
//		hideOverLayView();
//		showLoadingDialog(getString(R.string.common_loading));
//		reloadData();
//		// if (adapter != null) adapter.clearGroup(true);
//
//	}

	
//	@Override
//	public void onMerchantClick(MyOrderBean data, View v) {
//		Bundle bundle = new Bundle();
//		bundle.putString(SellerMainAct.IPN_MERCHANT_ID, "" + data.merchantID);
//		showActivity(this.getActivity(), SellerMainAct.class, bundle);
//	}

//	@Override
//	public void onBusiOperResult(int busiOperType, MyOrderBean data) {
//
//		if (busiOperType == OrderListAdapter.BOT_DELETE) {
//			mOrderDataList.remove(data);
//		}
//		else if (mOrderType != MyOrderActivity.ORDER_TYPE_ALL) {
//			switch (busiOperType) {
//				case OrderListAdapter.BOT_PAY:
//				case OrderListAdapter.BOT_CANCEL:
//				case OrderListAdapter.BOT_VALIDAY:
//				case OrderListAdapter.BOT_DELETE:
//					mOrderDataList.remove(data);
//					break;
//				default:
//					break;
//			}
//		}
//		adapter.notifyDataSetChanged();
//	}
}
