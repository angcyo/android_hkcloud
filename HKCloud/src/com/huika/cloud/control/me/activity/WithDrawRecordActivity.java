package com.huika.cloud.control.me.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.huika.cloud.R;
import com.huika.cloud.support.model.AccountDetailBean;
import com.huika.cloud.support.model.CardBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：账单明细
 * @author ht
 * @date 2015-12-4 上午10:24:34
 */
public class WithDrawRecordActivity extends RdpBaseActivity implements OnRefreshItemViewsListener {
	boolean selectedAll = true;
	boolean selectedIncome = false;
	boolean selectedExpanse = false;
	String[] moneyType = new String[]{"充值", "转账", "消费", "提现", "提现", "佣金", "买单返现"};
	private View mMasterView;
	private RdpListView rlv;
	private TextView tv_my_banlance;
	private RelativeLayout rl_all;
	private RelativeLayout rl_income;
	private RelativeLayout rl_expense;
	private View view_all;
	private View view_income;
	private View view_expense;
	private RdpListView rlv2;
	private RdpNetDataSet mDataSet;
	private RdpDataListAdapter<AccountDetailBean> adapter;
	private CardBean selectedCard;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("账单明细");
		mMasterView = addMasterView(R.layout.with_draw_record);
		initView();
		initListener();
		if(selectedCard!=null){

		}
		adapter = new RdpDataListAdapter<AccountDetailBean>(mApplication, R.layout.account_detail_item);
		adapter.setListener(this);
		rlv.setAdapter(adapter);
		mDataSet = new RdpNetDataSet(mApplication);
		mDataSet.setOnCommandFailedListener(this);
		mDataSet.setOnCommandSuccessedListener(this);
		//获取账户名字数据
		getAccountDetailData(0);
	}

	private void getAccountDetailData(int type) {
		showLoadingOverLay(rlv);
		mDataSet.setServerApiUrl("http://192.168.49.28:8081/hxlmpro-api/User/GetCapitalRecordList");
		mDataSet.clearConditions();
		mDataSet.setCondition("memberId", "402880e447d7243d0147d72ea3eb0002");
		mDataSet.setCondition("type",type);
		Type typeOfResult = new TypeToken<ArrayList<AccountDetailBean>>() {
		}.getType();
		mDataSet.setTypeOfResult(typeOfResult);
		mDataSet.open();
	}

	private void initListener() {
		rl_all.setOnClickListener(this);
		rl_income.setOnClickListener(this);
		rl_expense.setOnClickListener(this);
		rlv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mDataSet.open();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				mDataSet.getNextPageDatas();
			}
		});
	}

	private void initView() {
		rlv = (RdpListView) mMasterView.findViewById(R.id.rlv);
		tv_my_banlance = (TextView) mMasterView.findViewById(R.id.tv_my_banlance);
		rl_all = (RelativeLayout) mMasterView.findViewById(R.id.rl_all);
		rl_income = (RelativeLayout) mMasterView.findViewById(R.id.rl_income);
		rl_expense = (RelativeLayout) mMasterView.findViewById(R.id.rl_expense);
		view_all = mMasterView.findViewById(R.id.view_all);
		view_income = mMasterView.findViewById(R.id.view_income);
		view_expense = mMasterView.findViewById(R.id.view_expense);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_all:
			if(!selectedAll){
				selectedAll=true;
				selectedExpanse=false;
				selectedIncome=false;
				view_all.setVisibility(View.VISIBLE);
				view_income.setVisibility(View.GONE);
				view_expense.setVisibility(View.GONE);
				getAccountDetailData(0);
			}else{
				return;
			}
			break;
		case R.id.rl_income:
			if(!selectedIncome){
				selectedAll=false;
				selectedIncome=true;
				selectedExpanse=false;
				view_all.setVisibility(View.GONE);
				view_income.setVisibility(View.VISIBLE);
				view_expense.setVisibility(View.GONE);
				getAccountDetailData(1);
			}else{
				return;
			}
			break;
		case R.id.rl_expense:
			if(!selectedExpanse){
				selectedAll=false;
				selectedIncome=false;
				selectedExpanse=true;
				view_all.setVisibility(View.GONE);
				view_income.setVisibility(View.GONE);
				view_expense.setVisibility(View.VISIBLE);
				getAccountDetailData(2);
			}else{
				return;
			}
			break;

		default:
			super.onClick(v);
			break;
		}
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result,
			Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		hideOverLayView();
		adapter.setData((List<AccountDetailBean>)data);
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		AccountDetailBean accountDetailBean = (AccountDetailBean) adapter.getItem(position);
		if(accountDetailBean.isAddOrCuT==1){
			holder.getTextView(R.id.add_or_sub).setText("+");
		}else if(accountDetailBean.isAddOrCuT==2){
			holder.getTextView(R.id.add_or_sub).setText("-");
		}
		holder.getTextView(R.id.tv_money_type).setText(moneyType[accountDetailBean.addOrCutType-1]);
		holder.getTextView(R.id.tv_money).setText("¥"+accountDetailBean.addOrCutAmount);
//		orderNumber:来源订单编号【分佣、返现、消费】、【提现（银行卡）状态返回银行卡号】、其他状态为空
//		addOrCutType:资金异动类型:  【1:充值,2:转账,3:消费,4提现(银行卡),5提现（微信）5:退款,6:佣金,7买单返现】
		if(accountDetailBean.addOrCutType==6||accountDetailBean.addOrCutType==7||accountDetailBean.addOrCutType==3){
			holder.getTextView(R.id.tv_source).setText("来源订单编号：");
		}else if(accountDetailBean.addOrCutType==4){
			holder.getTextView(R.id.tv_source).setText("银行卡号");
		}else{
			holder.getTextView(R.id.tv_source).setText("");
		}
		
		holder.getTextView(R.id.tv_money_source).setText(accountDetailBean.orderNumber+"");
		holder.getTextView(R.id.tv_account_money).setText("¥"+accountDetailBean.newAmount);
		holder.getTextView(R.id.tv_trader).setText(accountDetailBean.trader);
		holder.getTextView(R.id.tv_createDt).setText(accountDetailBean.createDt);
		return false;
	}
}
